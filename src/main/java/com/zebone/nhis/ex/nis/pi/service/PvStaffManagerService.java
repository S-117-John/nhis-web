package com.zebone.nhis.ex.nis.pi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.pv.PvClinicGroup;
import com.zebone.nhis.common.module.pv.PvStaff;
import com.zebone.nhis.common.service.EmrPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.pi.dao.PvStaffMagMapper;
import com.zebone.nhis.ex.nis.pi.vo.PvClinicGroupVo;
import com.zebone.nhis.ex.nis.pi.vo.PvStaffVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医护人员管理
 * @author yangxue
 *
 */
@Service
public class PvStaffManagerService {
	
	@Resource
	private EmrPubService emrPubService;
	
	@Resource
	private PvStaffMagMapper pvStaffMagMapper;

	/**
	 * 保存变更后的医护人员信息
	 * @param param{pkPv,pkDept,pkDeptNs ,List<Map<String,Object>> list}
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void saveChangedStaff(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = (List<Map<String,Object>>)paramMap.get("list");//待保存的医护人员
		List<Map<String,Object>> wgList = (List<Map<String,Object>>)paramMap.get("wgList");//待保存的医疗组
		
		String pk_pv = CommonUtils.getString(paramMap.get("pkPv"));
		String pk_dept = CommonUtils.getString(paramMap.get("pkDept"));
		String pk_dept_ns =CommonUtils.getString(paramMap.get("pkDeptNs"));
		User u = (User)user;
		Date now = new Date();
		saveChangeStaffList(paramMap, list, pk_pv, pk_dept, pk_dept_ns, u, now);//保存医护人员
		saveChangeWgList(wgList, pk_pv, pk_dept, pk_dept_ns, u, now);//保存医疗组
	}

	/**
	 * 2019-05-15 保存变更的医护人员信息
	 * @param paramMap 用于病历同步信息使用
	 * @param list 变更后的医嘱人员信息
	 * @param pk_pv 患者就诊主键
	 * @param pk_dept   就诊科室
	 * @param pk_dept_ns就诊病区
	 * @param u 当前操作人信息
	 * @param now 当前时间
	 */
	private void saveChangeStaffList(Map<String, Object> paramMap,
			List<Map<String, Object>> list, String pk_pv, String pk_dept,
			String pk_dept_ns, User u, Date now) {
		if(list==null||list.size()<=0) return;
		//boolean flagSendMsg = false;//添加是否发送修改主治医生标志
		//插入新的医护人员，并更新原来的医护人员
		List<PvStaff> stflist = new ArrayList<PvStaff>();
		Map<String,Object> emrUpdateMap = new HashMap<String,Object>();
		emrUpdateMap.put("pkPv", pk_pv);//同步病历医护人员：pkPv(必填)
		String newNameDoc = null;
		String newPkDoc = null;
		for(Map<String,Object> staffmap : list){
			//if(CommonUtils.isNull(staffmap.get("pkStaff"))){
			//	continue;
			//}
			staffmap.put("dateEnd",now);
			staffmap.put("ts",now);
			staffmap.put("modifier", u.getPkEmp());
			PvStaff stfvo = new PvStaff();
			String dt_role = CommonUtils.getString(staffmap.get("dtRole"));
			stfvo.setDtRole(dt_role);
			ApplicationUtils.setBeanComProperty(stfvo, true);
			stfvo.setDateBegin(now);
			stfvo.setNameEmp(CommonUtils.getString(staffmap.get("nameEmp")));
			stfvo.setPkDept(pk_dept);
			stfvo.setPkDeptNs(pk_dept_ns);
			stfvo.setPkEmp(CommonUtils.getString(staffmap.get("pkEmp")));
			stfvo.setPkOrg(u.getPkOrg());
			stfvo.setPkPv(pk_pv);
			stfvo.setPkStaff(NHISUUID.getKeyId());
			stflist.add(stfvo);
			//更新掉原来的医护人员
			DataBaseHelper.update("update pv_staff set date_end = :dateEnd,ts=:ts,MODIFIER=:modifier where pk_staff = :pkStaff ",staffmap);
		    //如果是主管医生或主管护士，更新PV_ENCOUNTER的责任医生和责任护士
			if("0001".equals(dt_role)){//主管医生
				DataBaseHelper.update("update pv_encounter set pk_emp_phy = :pkEmp,name_emp_phy = :nameEmp where pk_pv = '"+pk_pv+"'", staffmap);
				//flagSendMsg = true;
				newNameDoc = stfvo.getNameEmp();
				newPkDoc = stfvo.getPkEmp();
				emrUpdateMap.put("pkEmpRefer", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpRefer住院医师
			}
			else if("0100".equals(dt_role)){//主管护士
				DataBaseHelper.update("update pv_encounter set pk_emp_ns = :pkEmp,name_emp_ns = :nameEmp where pk_pv = '"+pk_pv+"'", staffmap);
				emrUpdateMap.put("pkEmpNsCharge", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpNsCharge主管护士
			}
			else if("0000".equals(dt_role)){//主任医师
				emrUpdateMap.put("pkEmpDirector", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpDirector主任医师
			}
			else if("0006".equals(dt_role)){//主治医师
				emrUpdateMap.put("pkEmpConsult", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpConsult主治医师
			}
			else if("0101".equals(dt_role)){//护士长
				emrUpdateMap.put("pkEmpNsHead", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpNsHead护士长
			}
			else if("0005".equals(dt_role)){//质控医师
				emrUpdateMap.put("pkEmpQcDis", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpQcDis质控医师
			}
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvStaff.class), stflist);
		emrPubService.updateEmrPatRec(emrUpdateMap);//同步病历医护人员
		//if(!flagSendMsg) return;
		//发送主治医师变更至平台
		paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		paramMap.put("pkNewDoc",newPkDoc);
		paramMap.put("nameNewDoc",newNameDoc);
		PlatFormSendUtils.sendDoctorChangeMsg(paramMap);
	}
	
	/**
	 * 2019-05-15 保存变更的医疗组信息
	 * @param paramMap
	 * @param list
	 * @param pk_pv
	 * @param pk_dept
	 * @param pk_dept_ns
	 * @param u
	 * @param now
	 */
	private void saveChangeWgList(List<Map<String, Object>> list, String pk_pv, String pk_dept,
			String pk_dept_ns, User u, Date now) {
		if(list==null||list.size()<=0) return;

		//插入新的医疗组，更新历史医疗组信息
		List<PvClinicGroup> wglist = new ArrayList<PvClinicGroup>();
		String pkClinicGroup = "";
		for(Map<String,Object> wgMap : list){
			if(null != wgMap.get("pkClinicgroup") 
					&& !CommonUtils.isEmptyString(wgMap.get("pkClinicgroup").toString()))
			{
				pkClinicGroup +="'"+ wgMap.get("pkClinicgroup")+"'," ;
				continue;
			}
			PvClinicGroup wg = new PvClinicGroup();
			ApplicationUtils.setBeanComProperty(wg, true);
			wg.setDateBegin(now);
			wg.setEuStatus("1");
			wg.setPkDept(pk_dept);
			wg.setPkDeptNs(pk_dept_ns);
			wg.setPkWg(CommonUtils.getString(wgMap.get("pkWg")));
			wg.setPkOrg(u.getPkOrg());
			wg.setPkPv(pk_pv);
			wg.setPkClinicgroup(NHISUUID.getKeyId());
			wglist.add(wg);
		}
		//更新掉原来的医疗组
		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("pkPv", pk_pv);//同步医疗组信息
		updateMap.put("ts",now);
		updateMap.put("modifier", u.getPkEmp());
		if(!CommonUtils.isEmptyString(pkClinicGroup)){
			pkClinicGroup = pkClinicGroup.substring(0,pkClinicGroup.length()-1);
			updateMap.put("dateEnd",now);
			DataBaseHelper.update("update pv_clinic_group set date_end = :dateEnd,ts=:ts,MODIFIER=:modifier where pk_pv=:pkPv and pk_clinicgroup in("+pkClinicGroup +") ",updateMap);
		}
		//批量更新新增医疗组
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvClinicGroup.class), wglist);
		
		if(null == wglist || wglist.size() < 1) 
			throw new BusException("未获取到待修改后待保存的医疗组信息！");
		else if(wglist.size() > 1)
			throw new BusException("修改后待待保存的医疗组信息存在多条，无法保存！");
		updateMap.put("pkWg", wglist.get(0).getPkWg());
		int cnt = DataBaseHelper.update("update pv_encounter set pk_wg = :pkWg,ts=:ts,MODIFIER=:modifier where pk_pv=:pkPv and eu_status = '1' ",updateMap);
		if(1 != cnt)
			throw new BusException("医疗组保存失败！");
	}
	
	/**
	 * 保存变更后的医护人员信息
	 * @param param{pkPv,pkDept,pkDeptNs ,List<Map<String,Object>> list}
	 * @param user
	 */
	public void saveChangedStaff(Map<String,Object> paramMap,User u){
		String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
		//1、更新掉原来的医护人员
		Map<String,Object> emrUpdateMap = new HashMap<String,Object>();
		Date now = new Date();
		paramMap.put("dateEnd",now);
		paramMap.put("ts",now);
		paramMap.put("modifier", u.getPkEmp());
		DataBaseHelper.update("update pv_staff set date_end=:dateEnd,ts=:ts,MODIFIER=:modifier where date_end is null and pk_pv=:pkPv and dt_role ='0100'",paramMap);
		
		//2、插入新的医护人员
		PvStaff stfvo = new PvStaff();
		stfvo.setDtRole("0100");
		stfvo.setDateBegin(now);
		stfvo.setPkOrg(u.getPkOrg());
		stfvo.setPkDeptNs(u.getPkDept());
		stfvo.setPkPv(CommonUtils.getString(paramMap.get("pkPv")));
		stfvo.setPkDept(CommonUtils.getString(paramMap.get("pkDept")));
		stfvo.setPkEmp(CommonUtils.getString(paramMap.get("pkEmp")));
		stfvo.setNameEmp(CommonUtils.getString(paramMap.get("nameEmp")));
		ApplicationUtils.setBeanComProperty(stfvo, true);
		DataBaseHelper.insertBean(stfvo);

		//3、更新PV_ENCOUNTER的责任护士
		DataBaseHelper.update("update pv_encounter set pk_emp_ns =:pkEmp,name_emp_ns =:nameEmp where pk_pv =:pkPv", paramMap);

		//4、更新 病历系统的责任护士
		emrUpdateMap.put("pkPv", pkPv);//同步病历医护人员：pkPv(必填)
		emrUpdateMap.put("pkEmpNsCharge", stfvo.getPkEmp());//组装同步病历医护人员：pkEmpNsCharge主管护士
		emrPubService.updateEmrPatRec(emrUpdateMap);//同步病历医护人员
	}

	/**
     * 查询患者医护人员 + 医疗组信息
     */
    @SuppressWarnings("unchecked")
	public Map<String,Object> queryStafAndWgByPv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	Map<String,Object> rs = new HashMap<String,Object>();
    	List<PvStaffVo> stafList = pvStaffMagMapper.queryPvStaffList(paramMap);
    	if(null != stafList && stafList.size() > 0) 
    		rs.put("stafList", stafList);
    	List<PvClinicGroupVo> wgList = pvStaffMagMapper.queryPvWgList(paramMap);
    	if(null != wgList && wgList.size() > 0) 
    		rs.put("wgList", wgList);
    	return rs;
    }


    /**
	 * 查询该包床医生在本病区的医疗小组
	 **/
	public Map<String,Object> queryDocWg(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,Object> rs = new HashMap<String,Object>();
		List<PvClinicGroupVo> wgList = pvStaffMagMapper.queryWgList(paramMap);
		if(null != wgList && wgList.size() > 0)
			rs.put("wgList", wgList);
		return rs;
	}
}
