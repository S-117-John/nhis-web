package com.zebone.nhis.ex.oi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.module.ex.oi.ExInfusionOccDt;
import com.zebone.nhis.common.module.ex.oi.ExInfusionReaction;
import com.zebone.nhis.common.module.ex.oi.ExInfusionRegDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.oi.dao.OinfuStatisticMapper;
import com.zebone.nhis.ex.oi.vo.OinfuSearchResultVO;
import com.zebone.nhis.ex.oi.vo.OinfuStatisticVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OinfuStatisticService {
	@Resource
	private OinfuStatisticMapper oinfuStatisticMapper;
	
	/**
	 * trans code 005004004001
	 * 查询输液患者信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<OinfuSearchResultVO> seatchOinfuInfo(String param, IUser user){
		OinfuStatisticVO orv = JsonUtil.readValue(param, OinfuStatisticVO.class);
		User u = (User)user;
		
		StringBuilder  sql = new StringBuilder();
		sql.append("select distinct pv.code_pv as pv_code,pi.name_pi,d.name as dt_sex,pi.birth_date,r.register_no,");
		sql.append("case r.eu_status  when '0' then '待治疗' when '1' then '治疗中' when '2' then '完成' when '9' then '终止' end as eu_state,'详情' as act, ");
		sql.append("emp.name_emp as employee,r.date_reg date_register,r.pk_infureg ");
		sql.append("from ex_infusion_register r inner join pv_encounter pv on r.pk_pv = pv.pk_pv ");
		sql.append("inner join pi_master pi on pi.pk_pi = r.pk_pi ");
		sql.append("inner join bd_defdoc d on d.code = pi.dt_sex  and d.code_defdoclist = '000000'");
		sql.append("inner join ex_infusion_reg_dt  dt on r.pk_infureg=dt.pk_infureg ");
		sql.append("inner join bd_pd pd on pd.pk_pd=dt.pk_ord ");
		sql.append("inner join bd_ou_employee emp on emp.pk_emp=r.emp_reg ");
		sql.append("where r.pk_org='"+u.getPkOrg()+"' ");
		
		if(null == orv){
			sql.append(" order by pv.code_pv");
			List<OinfuSearchResultVO> ret = DataBaseHelper.queryForList(sql.toString(),OinfuSearchResultVO.class);			
			return ret;			
		}
		
		
		List l = new ArrayList();
		
		if(!StringUtils.isBlank(orv.getDateEnd()) && !"null".equalsIgnoreCase(orv.getDateEnd())){
			String dd = orv.getDateEnd()+" 23:59:59"; 
			Date ddd = DateUtils.strToDate(dd,"yyyy-MM-dd HH:mm:ss");
			ddd = new Date(ddd.getTime()+1000);
			sql.append(" and r.date_reg <=? ");
			l.add(ddd);
		}
		
		if(!StringUtils.isBlank(orv.getDateStart()) && !"null".equalsIgnoreCase(orv.getDateStart())){
			String dd = orv.getDateStart()+" 00:00:00";
			sql.append(" and r.date_reg >=? ");
			l.add(DateUtils.strToDate(dd,"yyyy-MM-dd HH:mm:ss"));
		}
		
		if(!StringUtils.isBlank(orv.getDrugName()) && !"null".equalsIgnoreCase(orv.getDrugName())) sql.append(" and pd.pk_pd='"+orv.getDrugName()+"' ");
		if(!StringUtils.isBlank(orv.getEmployee()) && !"null".equalsIgnoreCase(orv.getEmployee())) sql.append(" and r.emp_reg='"+orv.getEmployee()+"' ");
		if(!StringUtils.isBlank(orv.getEuStatus()) && !"null".equalsIgnoreCase(orv.getEuStatus())) sql.append(" and r.eu_status='"+orv.getEuStatus()+"' ");
		if(!StringUtils.isBlank(orv.getEuType()) && !"null".equalsIgnoreCase(orv.getEuType())) sql.append(" and dt.eu_type='"+orv.getEuType()+"' ");
		if(!StringUtils.isBlank(orv.getNamePi()) && !"null".equalsIgnoreCase(orv.getNamePi())) sql.append(" and pi.name_pi like '%"+orv.getNamePi()+"%' ");
		if(!StringUtils.isBlank(orv.getPvCode()) && !"null".equalsIgnoreCase(orv.getPvCode())) sql.append(" and pv.code_pv='"+orv.getPvCode()+"' ");
		if(!StringUtils.isBlank(orv.getRegisterNo()) && !"null".equalsIgnoreCase(orv.getRegisterNo())) sql.append(" and r.register_no='"+orv.getRegisterNo()+"' ");
		
		sql.append(" order by pv.code_pv");
		
		List<OinfuSearchResultVO> ret = DataBaseHelper.queryForList(sql.toString(),OinfuSearchResultVO.class,l.toArray());
		return ret;
	}
	
	/**
	 * trans code 005004004003
	 * 查询输液患者处方信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionRegDt> seatchPressInfo(String param, IUser user){
		String pkInfureg = JsonUtil.getFieldValue(param, "pk");
		if(StringUtils.isEmpty(pkInfureg)) throw new BusException("查询失败,没有获取到前台对象!");
		
		String sql = "select pd.name as pk_ord,pd.spec,unit.name as pk_unit_dos,dt.dosage, "
				+ "	case dt.eu_type when '0' then '输液' when '1' then '注射' when '2' then '皮试' when '3' then '雾化' end as eu_type, "
				+ " dt.days,dt.note_supply,dt.exec_times,dt.remain_times,dt.ordsn_parent,freq.name as code_freq,dt.reg_dt_no  "
				+ " from EX_INFUSION_REG_DT dt inner join bd_pd pd on pd.pk_pd=dt.pk_ord "
				+ "	left join bd_unit unit on unit.pk_unit = dt.pk_unit_dos "
				+ "	left join bd_term_freq freq on freq.code=dt.code_freq "
				+ "	where dt.pk_infureg=? order by dt.ordsn_parent"; 
		
		List<ExInfusionRegDt> ret = DataBaseHelper.queryForList(sql,ExInfusionRegDt.class,pkInfureg);
				
		for(ExInfusionRegDt dt : ret){
			if(!StringUtils.isEmpty(dt.getPkUnitDos()))dt.setPkUnitDos(dt.getDosage()+dt.getPkUnitDos());
			else dt.setPkUnitDos(""+dt.getDosage());
			
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
	 * trans code 005004004004
	 * 查询输液患者治疗信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionOcc> seatchDealInfo(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class); 
		if(paramMap == null ||CommonUtils.isNull(paramMap.get("pkInfureg")) ||CommonUtils.isNull(paramMap.get("regDtNo"))) throw new BusException("查询失败,没有获取到前台对象!");
		List<ExInfusionOcc>  list = oinfuStatisticMapper.getDealInfo(paramMap);		
		return list;
	}
	
	/**
	 * trans code 005004004005
	 * 查询输液患者的接瓶人等信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionOccDt> seatchOccDt(String param, IUser user){
		String pkInfureg = JsonUtil.getFieldValue(param, "pkInfuocc"); 
		if(StringUtils.isEmpty(pkInfureg)) throw new BusException("查询失败,没有获取到前台对象!");
		
		String sql = "select emp.name_emp as emp_opera,dt.date_opera from EX_INFUSION_OCC_DT dt inner join bd_ou_employee emp on emp.pk_emp= dt.emp_opera where dt.pk_infuocc=?";		
		List<ExInfusionOccDt> ret = DataBaseHelper.queryForList(sql,ExInfusionOccDt.class,pkInfureg);
		
		return ret;		
	}
	
	/**
	 * trans code 005004004006
	 * 查询输液患者的不良反应
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionReaction> seatchReaction(String param, IUser user){
		String pkInfureg = JsonUtil.getFieldValue(param, "pkInfuocc"); 
		if(StringUtils.isEmpty(pkInfureg)) throw new BusException("查询失败,没有获取到前台对象!");
		
		String sql = "select case re.eu_status when '0' then '治愈'  when '1' then '好转'  when '2' then '有后遗症' when '3' then '死亡' end as eu_status,"
				   + "note from EX_INFUSION_REACTION re where re.pk_infuocc=?";		
		List<ExInfusionReaction> ret = DataBaseHelper.queryForList(sql,ExInfusionReaction.class,pkInfureg);
		
		return ret;		
	}

}
