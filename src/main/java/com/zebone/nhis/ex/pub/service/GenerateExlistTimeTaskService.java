package com.zebone.nhis.ex.pub.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.platform.modules.exception.BusException;

/**
 * 生成执行单定时任务 --入口方法使用父类的exec
 * @author yangxue
 * 
 *
 */
@Service
public class GenerateExlistTimeTaskService extends TimeTaskService{
	@Resource
	private CreateExecListService service ;
	@Override
	protected String execute(String pk_org,String pk_dept) throws BusException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkDeptNsPv",pk_dept);
		paramMap.put("pkOrg",pk_org);
		return service.createExecList(paramMap);
	}

}
