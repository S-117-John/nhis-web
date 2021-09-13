package com.zebone.platform.quartz;

import com.zebone.platform.quartz.service.IJobHandler;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;


public class CommonJob implements StatefulJob {
	
	private static IJobHandler jobService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
			jobService.run(context);
	}

	public static void setJobService(IJobHandler Service){
		if(jobService == null){
			jobService = Service;
		}
	}
}
