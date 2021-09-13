package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnIpPressAuthMapper {
	/*
	 * 获取医生的处方抗菌药等权限
	 */
	public List<Map<String,Object>> qryPressAuth(String empId);

}
