package com.zebone.nhis.ex.nis.pub.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.ex.nis.pub.dao.PatiListMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class PatiListService {
	
   @Resource
	public PatiListMapper patiListMapper;
	/**
	 * 根据病区获取在院患者列表
	 * @param param
	 * @param user
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryPatiListByDeptNs(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = patiListMapper.queryPatiListByDept(paramMap); 
		return list;
	}
	
	/**
	 * 根据病区获取转科患者列表
	 * @param param
	 * @param user
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryTransPiListByDeptNs(String param, IUser user) {
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list = patiListMapper.queryTransPiListByDeptNs(paramMap);
		return list;
	}
}
