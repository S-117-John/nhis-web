package com.zebone.nhis.ex.oi.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.oi.ExInfusionOcc;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.oi.dao.OinPlaceViewMapper;
import com.zebone.nhis.ex.oi.vo.ExInfusionRegDtVO;
import com.zebone.nhis.ex.oi.vo.OinfuPlaceViewVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OinPlaceViewService {
	@Resource
	private OinPlaceViewMapper oinPlaceViewMapper;
	
	/**
	 * trans code 005004004002
	 * 查询输液厅椅位信息，及患者相关信息
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<OinfuPlaceViewVO> searchPlaceIv(String param, IUser user){
		String pk_dept = JsonUtil.getFieldValue(param, "pk_dept");
	    if(StringUtils.isEmpty(pk_dept)) throw new BusException("未获取到输液大厅信息!");
		User u = (User)user;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkDeptiv", pk_dept);
		paramMap.put("pkOrg", u.getPkOrg());
		//查询座位信息
		List<OinfuPlaceViewVO> ret = oinPlaceViewMapper.searchPlaceIv(paramMap);
		
		if(ret == null|| ret.size()<=0) return null;
		for(OinfuPlaceViewVO placevo:ret){
			if(CommonUtils.isEmptyString(placevo.getPkBed())) continue;
				List<OinfuPlaceViewVO> pdlist = oinPlaceViewMapper.searchPdInfo(placevo.getPkBed());
				if(pdlist==null||pdlist.size()<=0) continue;
				placevo.setEuType(pdlist.get(0).getEuType());
				int len = 0;
				if(pdlist.size()>3)
					len = 2;
				else
					len = pdlist.size() - 1;
				List<String> pds = new ArrayList<String>();
				for(int i = 0;i<len;i++){
					pds.add(pdlist.get(i).getPdName());
				}
				placevo.setPdNames(pds);
			}
		return ret;		
	}
	

	/**
	 * trans code 005004004007
	 * 查询输液患者occ表信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionOcc> seatchOccInfo(String param, IUser user){
		String pkInfuocc = JsonUtil.getFieldValue(param, "pkInfuocc"); 		
		if(StringUtils.isEmpty(pkInfuocc)) throw new BusException("查询失败,没有获取到前台对象!");
		
		String sql = "select occ.exec_times, occ.date_exec, "
				+ " case occ.eu_status when '0' then '待治疗' when '1' then '治疗中' when '2' then '完成' when '9' then '终止' end as eu_status,"
				+ " emp.name_emp as emp_receive, occ.date_receive,assign.name_emp as emp_assign,occ.date_assign,finished.name_emp as emp_finish, "
				+ " occ.date_finish,watcher.name_emp as emp_watch,circle.name_emp as emp_eyre,occ.eyre_record,recheck.name_emp as emp_check,"
				+ " occ.date_check,occ.skin_test_ret,occ.comment_str, occ.date_plan,exec_emp.name_emp as emp_exec,occ.date_exec, "
				+ " case iv.eu_seattype when '0' then '椅位' when '1' then '床位' end as pk_bed, "
				+ " occ.exec_times,occ.pk_infuocc "
				+ "  from ex_infusion_occ occ left join bd_ou_employee emp on emp.pk_emp=occ.emp_receive "
				+ " left join bd_ou_employee assign on assign.pk_emp=occ.emp_assign "
				+ " left join bd_ou_employee finished on finished.pk_emp = occ.emp_finish "
				+ " left join bd_ou_employee watcher on watcher.pk_emp=occ.emp_watch "
				+ " left join bd_ou_employee circle on circle.pk_emp=occ.emp_eyre "
				+ " left join bd_ou_employee recheck on recheck.pk_emp = occ.emp_check "
				+ " left join bd_ou_employee exec_emp on exec_emp.pk_emp=occ.emp_exec "
				+ " inner join bd_place_iv iv on occ.pk_bed=iv.pk_placeiv where occ.pk_infuocc=?";
		
		List<ExInfusionOcc> ret = DataBaseHelper.queryForList(sql,ExInfusionOcc.class,pkInfuocc);
		
		return ret;		
	}
	
	/**
	 * trans code 005004004010
	 * 查询输液患者处方信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<ExInfusionRegDtVO> searchPressInfo(String param, IUser user){
		String pkBed = JsonUtil.getFieldValue(param, "pk_bed");
		if(StringUtils.isEmpty(pkBed)) throw new BusException("查询失败,没有获取到前台对象!");
		
		String sql = "select pd.name as pk_ord,pd.spec,unit.name as pk_unit_dos,dt.dosage, "
				+ "	case dt.eu_type when '0' then '输液' when '1' then '注射' when '2' then '皮试' when '3' then '雾化' end as eu_type, "
				+ " dt.days,dt.note_supply,dt.exec_times,dt.remain_times,dt.ordsn_parent,freq.name as code_freq,dt.reg_dt_no,occ.pk_infuocc,occ.occ_no "
				+ " from EX_INFUSION_REG_DT dt inner join bd_pd pd on pd.pk_pd=dt.pk_ord "
				+ "	left join bd_unit unit on unit.pk_unit = dt.pk_unit_dos "
				+ "	left join bd_term_freq freq on freq.code=dt.code_freq "
				+" inner join ex_infusion_occ occ on dt.reg_dt_no = occ.reg_dt_no and dt.pk_infureg = occ.pk_infureg and occ.eu_status not in ('2', '9') "
				+ "	where occ.pk_infuocc in (select pk_infuocc from ex_infusion_occ oc where oc.pk_bed=?) order by dt.ordsn_parent"; 
		
		List<ExInfusionRegDtVO> ret = DataBaseHelper.queryForList(sql,ExInfusionRegDtVO.class,pkBed);
				
		for(ExInfusionRegDtVO dt : ret){
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
	private void setGroup(List<ExInfusionRegDtVO> ret){
		Map<String,List<ExInfusionRegDtVO>> map = new HashMap<String,List<ExInfusionRegDtVO>>();
		
		for(ExInfusionRegDtVO dt : ret){
			List<ExInfusionRegDtVO> ll = map.get(""+dt.getOrdsnParent());
			if(null == ll || ll.size() <= 0){
				ll = new ArrayList<ExInfusionRegDtVO>();
			    ll.add(dt);
			    map.put(""+dt.getOrdsnParent(), ll);
			    continue;
			}
			
			ll.add(dt);
		}
		
		Iterator it = map.keySet().iterator();
		
		while(it.hasNext()){
			List<ExInfusionRegDtVO> ll = map.get(it.next());
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
}
