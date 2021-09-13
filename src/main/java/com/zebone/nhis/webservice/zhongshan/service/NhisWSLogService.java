package com.zebone.nhis.webservice.zhongshan.service;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.message.SysWebserviceLog;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class NhisWSLogService {
	
	public void InsertWsLogs(String funcId , String inputInfo , String outputInfo , String flagSus){
		SysWebserviceLog sysWbsLog = new SysWebserviceLog();
		sysWbsLog.setFuncId(funcId);
		sysWbsLog.setInputInfo(inputInfo.length() > 1024 ? inputInfo.substring(0,600) : inputInfo);
		sysWbsLog.setOutputInfo(outputInfo.length() > 1024 ?outputInfo.substring(0,600) : outputInfo);
		sysWbsLog.setFlagSuccess(flagSus);
		DataBaseHelper.insertBean(sysWbsLog);
	}
}
