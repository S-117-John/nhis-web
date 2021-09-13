package com.zebone.nhis.cn.ipdw.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CpQualityControlMapper;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CpQualityControlService {
	
	@Autowired  
	private CpQualityControlMapper CpQualityControl;
	
	/***
	 * 获得路径患者分析列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCPatient(String param , IUser user) {
		@SuppressWarnings("unchecked")
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNull(map.get("dept"))) map.remove("dept");
		if(CommonUtils.isNull(map.get("clinicPath"))) map.remove("clinicPath");
		return CpQualityControl.qryQCPatient(map);
	}
	
	/***
	 * 获得路径 使用状况列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCUseState(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		if(CommonUtils.isNull(map.get("dept"))) map.remove("dept");
		if(CommonUtils.isNull(map.get("clinicPath"))) map.remove("clinicPath");
		return CpQualityControl.qryQCUseState(map);
	}
	/***
	 * 获得变异分析列表汇总
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCVariationSum(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return CpQualityControl.qryQCVariationSum(map);
	}	
	/***
	 * 获得变异分析列表明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCVariationDetail(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return CpQualityControl.qryQCVariationDetail(map);
	}
	/***
	 * 获得退出分析列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCQuit(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return CpQualityControl.qryQCQuit(map);
	}	
	/***
	 * 获得路径外医嘱列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryQCOutCp(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		return CpQualityControl.qryQCOutCp(map);
	}		

}
