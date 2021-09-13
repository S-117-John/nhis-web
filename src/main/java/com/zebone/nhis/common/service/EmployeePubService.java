package com.zebone.nhis.common.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.dao.EmployeePubMapper;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.exception.BusException;
/**
 * 人员公共服务
 * @author yangxue
 *
 */
@Service
public class EmployeePubService {
	@Resource
	private EmployeePubMapper employeePubMapper;
	/**
	 * 获取人员各类处方权标志
	 * @param pkEmp
	 * @param user
	 * @return
	 */
	public Map<String,Object> getEmpPresAuths(String pkEmp){
		if(CommonUtils.isEmptyString(pkEmp))
			throw new BusException("未获取到人员主键，无法获取处方权!");
		return employeePubMapper.queryEmpPresFlag(pkEmp);
	}
}
