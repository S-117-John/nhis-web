package com.zebone.platform.quartz.service;


import com.zebone.platform.quartz.modle.QrtzJobCfg;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;


//写接口
@Service
public interface IJobHandler{

	public void run(JobExecutionContext context);
	
	//手工执行算法
	public void autoRunJob(QrtzJobCfg jobcfg);
	

}
