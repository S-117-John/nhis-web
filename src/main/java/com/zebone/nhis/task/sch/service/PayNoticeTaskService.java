package com.zebone.nhis.task.sch.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 定时任务推送预约挂号待缴费
 *
 */
@Service
public class PayNoticeTaskService {
	
	@SuppressWarnings("unused")
	public void executeTask(QrtzJobCfg cfg){
		ApplicationUtils apputil = new ApplicationUtils();
		Map<String,Object> paramMap=new HashMap<String, Object>();
		User u  = new User();
		ResponseJson  rs =  apputil.execService("MA", "WePayService", "sendPayNotice",paramMap,u);
	}
}
