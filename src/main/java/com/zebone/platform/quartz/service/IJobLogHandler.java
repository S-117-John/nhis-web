package com.zebone.platform.quartz.service;

import com.zebone.platform.quartz.modle.QrtzJobCfg;

public interface IJobLogHandler {

	
	//手工执行算法
	public void autoRunJob(QrtzJobCfg jobcfg);
}
