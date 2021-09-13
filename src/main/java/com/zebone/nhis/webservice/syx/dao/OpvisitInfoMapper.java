package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.webservice.syx.vo.platForm.OpdiagInfoVo;
import com.zebone.nhis.webservice.syx.vo.platForm.OpvisitInfoVo;

import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface OpvisitInfoMapper {
	
	/**
	 * 获取门诊就诊记录信息
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> opvisitinfoQuery(Map<String, Object> param);
	
	/**
	 * 门诊患者诊断信息
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> opdiaginfoQueryDao(Map<String, Object> param);
 
}
