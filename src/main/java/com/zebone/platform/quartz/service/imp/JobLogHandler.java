package com.zebone.platform.quartz.service.imp;

import com.zebone.platform.quartz.modle.QrtzJobCfg;
import com.zebone.platform.quartz.service.IJobHandler;
import com.zebone.platform.quartz.service.IJobLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobLogHandler implements IJobLogHandler {
	
	@Autowired
	private IJobHandler jobHandler;

	@Override
	public void autoRunJob(QrtzJobCfg jobcfg) {
		jobHandler.autoRunJob(jobcfg);
		
	}

}
