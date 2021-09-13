package com.zebone.nhis.ex.oi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.oi.ExInfusionRegDt;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.oi.vo.ExInfusionReactionVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 输液不良反应查询和登记模块
 * @author gejianwen
 *
 */
@Service
public class ExInfusionReactionService {
	
	/**
	 * trans code 005004003001
	 * 查询不良反应
	 */
	public List<ExInfusionReactionVO> searchByParam(String param, IUser user){
		ExInfusionReactionVO efr = JsonUtil.readValue(param, ExInfusionReactionVO.class);
		User u = (User)user;
		
		String sql = "select distinct ex.*,pd.name as medicin_name,bd.name as dt_sex_name,pi.name_pi,pi.dt_sex,pv.code_pv,"
                   +" case ex.eu_status "
                   +" when '0' then '治愈' "
                   +" when '1' then '好转' "
                   +" when '2' then '有后遗症' "
                   +" when '3' then '死亡' "
                   +" end as eu_state_name "
                   +" from EX_INFUSION_REACTION ex "
                   + " inner join ex_infusion_occ occ on occ.pk_infuocc = ex.pk_infuocc "
                   + " inner join EX_INFUSION_REG_DT dt "
                   + " on dt.reg_dt_no = occ.reg_dt_no and dt.pk_org='"+u.getPkOrg()+"'"
				   /*+" from EX_INFUSION_REACTION ex  "
				   +" inner join EX_INFUSION_REGISTER reg "
				   + " on ex.pk_infureg = reg.pk_infureg "
				   + " inner join EX_INFUSION_REG_DT dt "
				   + " on dt.pk_infureg = reg.pk_infureg "*/
				   + " inner join  bd_pd pd on pd.pk_pd=dt.pk_ord "
				   +" inner join pi_master pi on pi.pk_pi = ex.pk_pi "
				   +" inner join pv_encounter pv on pv.pk_pv = ex.pk_pv "
				   +" left join bd_defdoc bd on bd.code = pi.dt_sex and bd.code_defdoclist = '000000' "
				   + " where ex.pk_org='"+u.getPkOrg()+"' and dt.ordsn = dt.ordsn_parent ";
		
		if(null == efr){
			List<ExInfusionReactionVO> ret = DataBaseHelper.queryForList(sql,ExInfusionReactionVO.class);			
			return ret;
		}
		
		List l = new ArrayList();
		
		if(null != efr.getStartd()){
			String dd = DateUtils.dateToStr("yyyy-MM-dd",efr.getStartd())+" 00:00:00";
			sql += " and ex.date_record >=? ";
			l.add(DateUtils.strToDate(dd,"yyyy-MM-dd HH:mm:ss"));
		}
		if(null != efr.getEndd()){
			String dd = DateUtils.dateToStr("yyyy-MM-dd", efr.getEndd())+" 23:59:59";  
			Date d = DateUtils.strToDate(dd,"yyyy-MM-dd HH:mm:ss");
			d = new Date(d.getTime()+1000);
			sql += " and ex.date_record <=?";
			l.add(d);
		}
		
		if(!StringUtils.isEmpty(efr.getEuStatus()) && !"null".equalsIgnoreCase(efr.getEuStatus())) sql += " and ex.eu_status='"+efr.getEuStatus()+"' ";
		if(!StringUtils.isEmpty(efr.getNamePi()) && !"null".equalsIgnoreCase(efr.getNamePi())) sql += " and pi.name_pi like '%"+efr.getNamePi()+"%'";
		if(!StringUtils.isEmpty(efr.getPkOrd()) && !"null".equalsIgnoreCase(efr.getPkOrd())) sql += " and pd.pk_pd='"+efr.getPkOrd()+"'";
		
		sql += " order by ex.date_record desc";
		
		List<ExInfusionReactionVO> ret = null; 
		
		if(l.size() <= 0) ret = DataBaseHelper.queryForList(sql,ExInfusionReactionVO.class);
		else ret = DataBaseHelper.queryForList(sql,ExInfusionReactionVO.class,l.toArray());
		
				
		return ret;		
	}
	
	/**
	 * 根据传递过来的不良记录主键查询药品信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionReactionVO> searchByOinfu(String param, IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		String sql =  "select  pd.name    as medicin_name,regdt.spec,fac.name       as pk_factory, "
				+ " bd.name as dosage,unit.name as unit_name,ex.eu_status,ex.date_record,emp.name_emp   as emp_record,"
				+ " ex.pk_infurea,sup.name       as code_supply,occ.date_exec as startd, "
				+ " occ.date_finish as endd,ex.note,regdt.reg_dt_no,regdt.dosage  from ex_infusion_reaction ex "
				+ " inner join ex_infusion_occ occ on ex.pk_infuocc = occ.pk_infuocc "
				+ " inner join EX_INFUSION_REG_dt regdt on regdt.pk_infureg=ex.pk_infureg"
				+ " left join bd_pd pd on pd.pk_pd = regdt.pk_ord"
				+ " left join bd_factory fac on pd.pk_factory = fac.pk_factory "
				+ " left join bd_unit unit on regdt.pk_unit_dos = unit.pk_unit "
				+ " left join bd_supply sup on sup.code = regdt.code_supply "
				+ " inner join bd_ou_employee emp on emp.pk_emp = ex.emp_record"
				+ " left join bd_defdoc bd on bd.code = pd.dt_dosage  and bd.code_defdoclist = '030400'"
				+ " where ex.pk_infurea = ? ";
								
		List<ExInfusionReactionVO> ret = DataBaseHelper.queryForList(sql,ExInfusionReactionVO.class,new Object[]{pk});  
		
		for(ExInfusionReactionVO vo : ret){
			String str = "";
			if(null != vo.getStartd()) str += DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", vo.getStartd())+"--";
			else str += "--";
			
			if(null != vo.getEndd()) str += DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", vo.getEndd());
			else str += "";
			vo.setMedcinePeroid(str);
			
			if(!StringUtils.isEmpty(vo.getDosage()) && !StringUtils.isEmpty(vo.getUnitName())) vo.setDosageDef(vo.getDosage()+vo.getUnitName());
			else vo.setDosageDef("");
		}	
		
		
		
		setGroup(ret);
		
		return ret;
		
	}
	
	/**
	 * 医嘱成组方法
	 * @param ret
	 */
	private void setGroup(List<ExInfusionReactionVO> ret){
		Map<String,List<ExInfusionReactionVO>> map = new HashMap<String,List<ExInfusionReactionVO>>();
		
		for(ExInfusionReactionVO dt : ret){
			List<ExInfusionReactionVO> ll = map.get(""+dt.getRegDtNo());
			if(null == ll || ll.size() <= 0){
				ll = new ArrayList<ExInfusionReactionVO>();
			    ll.add(dt);
			    map.put(""+dt.getRegDtNo(), ll);
			    continue;
			}
			
			ll.add(dt);
		}
		
		Iterator it = map.keySet().iterator();
		
		while(it.hasNext()){
			List<ExInfusionReactionVO> ll = map.get(it.next());
			if(ll.size() <= 1) continue;
			
			if(ll.size() == 2){
				ll.get(0).setRegDtNo("┌");
				ll.get(1).setRegDtNo("┕");				
				continue;
			}
			
			for(int i=0;i<ll.size();i++){
				if(i==0){
					ll.get(i).setRegDtNo("┌");
					continue;
				}
				
				if(i != ll.size()-1){
					ll.get(i).setRegDtNo("┃");
					continue;
				}
				
				ll.get(i).setRegDtNo("┕");
			}			
		}
		
		for(ExInfusionReactionVO vo : ret){
			if("┌".equals(vo.getRegDtNo()) || "┃".equals(vo.getRegDtNo()) || "┕".equals(vo.getRegDtNo())) continue;
			vo.setRegDtNo("");
		}
	}
	
	/**
	 * transcode 005004003003
	 * 保存不良记录修改信息
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveOinfu(String param, IUser user){
		ExInfusionReactionVO efr = JsonUtil.readValue(param, ExInfusionReactionVO.class);
		if(null == efr || StringUtils.isEmpty(efr.getPkInfurea())) throw new BusException("保存失败,没有获取到前台对象!");
		
		String sql = "update ex_infusion_reaction e set e.eu_status='"+efr.getEuStatus()+"',e.note='"+efr.getNote()+"' where e.pk_infurea='"+efr.getPkInfurea()+"'";		
		DataBaseHelper.update(sql);
		
	}

}
