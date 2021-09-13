package com.zebone.nhis.compay.ins.qgyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.compay.ins.qgyb.vo.CancerFighting;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NationalInsuBusMapper {
	
	/**
	 * 根据条件查询患者抗癌药登记信息
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryNationalList(Map<String, Object> param);

}
