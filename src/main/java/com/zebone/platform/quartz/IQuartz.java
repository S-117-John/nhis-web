package com.zebone.platform.quartz;


import org.quartz.JobDataMap;

public interface IQuartz {
	public void run(JobDataMap map);
}
