package com.zebone.platform.quartz.vo;

/**
 * 任务查询条件对象
 * @author Xulj
 *
 */
public class JobCfgQueryForm implements java.io.Serializable{

	/**
     * 任务编码
     */
    private String jobname;
    
    /**
     * 任务名称
     */
    private String jobcname;
	
    /**
     *  0：可用  1：不可用
     */
    private String isvisible;
    
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

	public String getIsvisible() {
		return isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}
}
