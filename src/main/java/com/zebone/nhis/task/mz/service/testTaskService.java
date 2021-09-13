package com.zebone.nhis.task.mz.service;

import java.util.HashMap;
import org.springframework.stereotype.Service;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.quartz.modle.QrtzJobCfg;

@Service
public class testTaskService {

	@Resource
	private  testrHandel testrHandel;
	
	@SuppressWarnings("rawtypes")
	public Map testTask(QrtzJobCfg cfg){
		Map a = new HashMap();
		
		a.put("name", "完成183条业务处理");
		return a;
		
	}
	
	
	@SuppressWarnings("rawtypes")
	public Map testTask1(QrtzJobCfg cfg){
		Map a = new HashMap();
		a.put("name", "完成1ewewe条业务处理");
		return a;
		
	}
	
	
	////////////////测试事务/////////////////////
	@SuppressWarnings("rawtypes")
	public Map test(QrtzJobCfg cfg){
		Map a = new HashMap();
	
		DataBaseHelper.update("insert into test(a,b)values('11','22')");
		try{
			testrHandel.testaa();
		}catch(Exception e){
			
		}
		return a;
		
	}
	
	
	
}
