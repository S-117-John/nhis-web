package com.zebone.nhis.ex.oi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.module.ex.oi.ExInfusionOccDt;
import com.zebone.nhis.common.module.ex.oi.ExInfusionRegDt;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.oi.vo.ExInfusionOccVO;
import com.zebone.nhis.ex.oi.vo.OiPatientInfoVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class EmpReceiveService {
	
	/**
	 * trans code 005004003004
	 * 配药和接瓶界面查询处方方法
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionRegDt> searchForPress(String param, IUser user){
		String pkInfureg = JsonUtil.getFieldValue(param, "pk");
		if(StringUtils.isEmpty(pkInfureg)) throw new BusException("查询失败,没有获取到前台对象!");
		
		StringBuilder  sql = new StringBuilder();
		sql.append("select pd.name as pk_ord,pd.spec,unit.name as pk_unit_dos,dt.dosage, ");
		sql.append( "	case dt.eu_type when '0' then '输液' when '1' then '注射' when '2' then '皮试' when '3' then '雾化' end as eu_type, ");
		sql.append( " dt.days,dt.note_supply,dt.exec_times,dt.remain_times,dt.ordsn_parent,freq.name as code_freq,dt.reg_dt_no  ");
		sql.append( " from EX_INFUSION_REG_DT dt inner join bd_pd pd on pd.pk_pd=dt.pk_ord ");
		sql.append( "	left join bd_unit unit on unit.pk_unit = dt.pk_unit_dos ");
		sql.append( "	left join bd_term_freq freq on freq.code=dt.code_freq ");
		sql.append(" inner join EX_INFUSION_OCC occ on dt.pk_infureg=occ.pk_infureg ");
		sql.append("	where occ.occ_no=? and occ.pk_org = '"+((User)user).getPkOrg()+"' order by dt.ordsn_parent ");		
		
		List<ExInfusionRegDt> ret = DataBaseHelper.queryForList(sql.toString(),ExInfusionRegDt.class,pkInfureg);
						
		for(ExInfusionRegDt dt : ret){
			dt.setPkUnitDos(dt.getDosage()+dt.getPkUnitDos());
			long zhixincishu = dt.getExecTimes() - dt.getRemainTimes(); //剩余执行次数
			dt.setNameOrd(""+zhixincishu+"/"+dt.getExecTimes());			
		}
		
		setGroup(ret);
		return ret;		
	}
	
	/**
	 * 医嘱成组方法
	 * @param ret
	 */
	private void setGroup(List<ExInfusionRegDt> ret){
		Map<String,List<ExInfusionRegDt>> map = new HashMap<String,List<ExInfusionRegDt>>();
		
		for(ExInfusionRegDt dt : ret){
			List<ExInfusionRegDt> ll = map.get(""+dt.getOrdsnParent());
			if(null == ll || ll.size() <= 0){
				ll = new ArrayList<ExInfusionRegDt>();
			    ll.add(dt);
			    map.put(""+dt.getOrdsnParent(), ll);
			    continue;
			}
			
			ll.add(dt);
		}
		
		Iterator it = map.keySet().iterator();
		
		while(it.hasNext()){
			List<ExInfusionRegDt> ll = map.get(it.next());
			if(ll.size() <= 1) continue;
			
			if(ll.size() == 2){
				ll.get(0).setPkCnord("┌");
				ll.get(1).setPkCnord("┕");				
				continue;
			}
			
			for(int i=0;i<ll.size();i++){
				if(i==0){
					ll.get(i).setPkCnord("┌");
					continue;
				}
				
				if(i != ll.size()-1){
					ll.get(i).setPkCnord("┃");
					continue;
				}
				
				ll.get(i).setPkCnord("┕");
			}			
		}
	}
	
	/**
	 * trans code 005004003005
	 * 查询输液患者治疗信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionOccVO> seatchDealInfo(String param, IUser user){
		String occNo = JsonUtil.getFieldValue(param, "pk"); 
		
		if(StringUtils.isEmpty(occNo)) return null;
		StringBuilder  sql = new StringBuilder();
		sql.append("select occ.exec_times, occ.date_exec, ");
		sql.append(" case occ.eu_status when '0' then '待治疗' when '1' then '治疗中' when '2' then '完成' when '9' then '终止' end as eu_status,occ.eu_status as eu_status_code,");
		sql.append(" emp.name_emp as emp_receive, occ.date_receive,assign.name_emp as emp_assign,occ.date_assign,finished.name_emp as emp_finish, ");
		sql.append(" occ.date_finish,watcher.name_emp as emp_watch,circle.name_emp as emp_eyre,occ.eyre_record,recheck.name_emp as emp_check,");
		sql.append(" occ.date_check,occ.skin_test_ret,occ.comment_str, occ.date_plan,exec_emp.name_emp as emp_exec,occ.date_exec, ");
		sql.append(" case iv.eu_seattype when '0' then '椅位' when '1' then '床位' end as pk_bed,occ.occ_no, ");
		sql.append(" occ.exec_times,occ.pk_infuocc ");
		sql.append("  from ex_infusion_occ occ left join bd_ou_employee emp on emp.pk_emp=occ.emp_receive ");
		sql.append(" left join bd_ou_employee assign on assign.pk_emp=occ.emp_assign ");
		sql.append(" left join bd_ou_employee finished on finished.pk_emp = occ.emp_finish ");
		sql.append(" left join bd_ou_employee watcher on watcher.pk_emp=occ.emp_watch ");
		sql.append(" left join bd_ou_employee circle on circle.pk_emp=occ.emp_eyre ");
		sql.append(" left join bd_ou_employee recheck on recheck.pk_emp = occ.emp_check ");
		sql.append(" left join bd_ou_employee exec_emp on exec_emp.pk_emp=occ.emp_exec ");
		sql.append(" left join bd_place_iv iv on occ.pk_bed=iv.pk_placeiv where occ.occ_no=? and occ.pk_org = '"+((User)user).getPkOrg()+"' ");
		
		
		List<ExInfusionOccVO> ret = DataBaseHelper.queryForList(sql.toString(),ExInfusionOccVO.class,occNo);
		
		return ret;		
	}
	

	/**
	 * trans code 005004004008
	 * 更新患者输液患者治疗信息 EX_INFUSION_OCC表的配药人
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateOcc(String param, IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		
		if(StringUtils.isEmpty(pk) || "null".equalsIgnoreCase(pk)) throw new BusException("查询失败,没有获取到前台对象!");
		
		User u = (User)user;
		
		String sql = "update  EX_INFUSION_OCC occ set occ.emp_assign=?,occ.date_assign=? "
				   + " where occ.pk_infuocc in ( select oc.pk_infuocc from  EX_INFUSION_OCC oc where oc.occ_no=? and occ.pk_org = '"+((User)user).getPkOrg()+"' )";
		DataBaseHelper.update(sql, new Object[]{u.getPkEmp(),new Date(),pk});		
	}
	
	/**
	 * trans code 005004004009
	 * 更新患者输液患者治疗信息 EX_INFUSION_OCC_DT表的接瓶人
	 * @param param
	 * @param user
	 * @return
	 */
	public void updateOccDt(String param, IUser user){
		ExInfusionOccDt dt = JsonUtil.readValue(param, ExInfusionOccDt.class);
		
		if(null == dt) throw new BusException("查询失败,没有获取到前台对象!");
		
		User u = (User)user;
		
		dt.setCreateTime(new Date());
		dt.setCreator(u.getPkEmp());
		dt.setDateOpera(new Date());
		dt.setDelFlag("0");
		dt.setEmpOpera(u.getPkEmp());
		dt.setPkOrg(u.getPkOrg());
		dt.setTs(new Date());
		
		DataBaseHelper.insertBean(dt);	
	}
	
	/**
	 * trans code 005004003006
	 * 查询患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OiPatientInfoVo> searchPi(String param, IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		
		if(StringUtils.isEmpty(pk) || "null".equalsIgnoreCase(pk)) return null;
		
		String sql = "select pi.name_pi,pi.birth_date,hp.name as insur_no,pv.age_pv,bd.name as sex,pi.code_op as code_pi,dept.name_dept,pv.name_emp_phy,occ.date_exec"
				+ " from EX_INFUSION_OCC occ inner join EX_INFUSION_REGISTER reg on reg.pk_infureg=occ.pk_infureg "
				+ " inner join pi_master pi on pi.pk_pi=reg.pk_pi inner join bd_defdoc bd on bd.code=pi.dt_sex and bd.code_defdoclist='000000' "
				+ " inner join pv_encounter pv on pv.pk_pv=reg.pk_pv inner join bd_ou_dept dept on pv.pk_dept=dept.pk_dept "
				+ " left join bd_hp hp on hp.pk_hp = pv.pk_insu "
				+ " where occ.occ_no=? and occ.pk_org = '"+((User)user).getPkOrg()+"' ";		
		
		List<OiPatientInfoVo> ret = DataBaseHelper.queryForList(sql,OiPatientInfoVo.class,pk);  
		return ret;		
	}

}
