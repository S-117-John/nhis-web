package com.zebone.nhis.cn.opdw.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.dao.OpPatientListMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class OpPatientListService {
	
	@Autowired   
    private OpPatientListMapper OpPatientlist;
	
	/***
	 * 获得门诊医生站列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPatientList(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Calendar instance = Calendar.getInstance();
		Date time = instance.getTime();

		
		map.put("date", time);
		String sMode = map.get("mode").toString();
		String sPatientType = map.get("patientType").toString();
		if ("0".equals(sMode) && "op_unuse".equals(sPatientType))
		    return OpPatientlist.getPatientListNormalNoTriage(map);
		else if ("1".equals(sMode) && "op_unuse".equals(sPatientType))
			return OpPatientlist.getPatientListNormalTriage(map);
		else if ("op_used".equals(sPatientType))
			return OpPatientlist.getPatientListNormalUsed(map);
		else if ("op_dept".equals(sPatientType))
			return OpPatientlist.getPatientListNormalDept(map);
		else if ("op_history".equals(sPatientType)){
				if (Application.isSqlServer()) {
					return OpPatientlist.getPatientListHistory(map);
				} else{
					return	OpPatientlist.getPatientListHistoryOracle(map);
				}
			}
		else
			return null;
	}
	/***
	 * 获得门诊患者资源列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getPatientListSource(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return OpPatientlist.getPatientListSource(map);
	}	
	
	/***
	 * 设置病人就诊状态为1 （已接诊状态）
	 * @param param
	 * @param user
	 * @return
	 */
	public void savePatientStatus(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Map<String, Object>  map1 = new HashMap<>();
		User user2 = (User) user;
		String pkemp = user2.getPkEmp();
		String nameEmp = user2.getUserName();
		String sql_update = "update pv_encounter set eu_status = '1', pk_emp_phy =:pkemp, name_emp_phy =:nameEmp  where pk_pv =:pkPv " ;
		map1.put("pkemp", pkemp);
		map1.put("nameEmp", nameEmp);
		map1.put("pkPv", map.get("pkPv").toString());
		
		if (map.get("euStatus").toString() != "1")
		{
			DataBaseHelper.update(sql_update, map1 );	
		}
	}	
    
    
}
