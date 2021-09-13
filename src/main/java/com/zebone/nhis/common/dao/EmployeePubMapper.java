package com.zebone.nhis.common.dao;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface EmployeePubMapper {
	/**
	 * 查询人员各类处方权标志
	 * @param paramMap{pkEmp(必传)}
	 * @return
	 * @throws BusException
	 */
	public Map<String,Object> queryEmpPresFlag(@Param("pkEmp")String pkEmp)throws BusException;
}
