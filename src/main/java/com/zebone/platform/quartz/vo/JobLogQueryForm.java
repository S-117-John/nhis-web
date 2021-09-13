package com.zebone.platform.quartz.vo;

public class JobLogQueryForm implements java.io.Serializable{

	/**
     * 任务日志编码
     */
    private String jobname;
    
    /**
     * 任务日志名称
     */
    private String jobcname;
	
    /**
     *  0:任务执行成功  1：任务执行业务失败    2：任务执行异常  
     */
    private int jobtype;

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobcname() {
		return jobcname;
	}

	public void setJobcname(String jobcname) {
		this.jobcname = jobcname;
	}

	public int getJobtype() {
		return jobtype;
	}

	public void setJobtype(int jobtype) {
		this.jobtype = jobtype;
	}
}
