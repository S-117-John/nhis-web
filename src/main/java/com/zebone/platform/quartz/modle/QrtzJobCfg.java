package com.zebone.platform.quartz.modle;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 *
 * @author 
 */
@Table(value="qrtz_job_cfg")
public class QrtzJobCfg implements java.io.Serializable{
    /**
     * 任务code
     */
	@PK
	@Field(value="jobname")
    private String jobname;
    /**
     * 任务组
     */
	@Field(value="job_group")
    private String jobgroup;
    /**
     * 任务名称
     */
	@Field(value="jobcname")
    private String jobcname;
    /**
     * 任务执行时间规则
     */
	@Field(value="cron_expression")
    private String cronexpression;
    /**
     * 任务执行对象
     */
	@Field(value="jobobject")
    private String jobobject;
    /**
     * 任务备注
     */
	@Field(value="jobnote")
    private String jobnote;
    /**
     *  0：可用  1：不可用
     */
	@Field(value="isvisible")
    private Short isvisible;
    /**
     *  0：不记录日志  2：记录日志
     */
	@Field(value="islog")
    private Short islog;
    /**
     * 日志格式
     */
	@Field(value="logformat")
    private String logformat;
	
	@Field(value="jobparam")
	private String jobparam;
	
	@Field(value="jgs")
	private String jgs;

    /**
     * 任务code
     */
    public String getJobname(){
        return this.jobname;
    }

    /**
     * 任务code
     */
    public void setJobname(String jobname){
        this.jobname = jobname;
    }    
    /**
     * 任务组
     */
    public String getJobgroup(){
        return this.jobgroup;
    }

    public String getJgs() {
		return jgs;
	}

	public void setJgs(String jgs) {
		this.jgs = jgs;
	}

	/**
     * 任务组
     */
    public void setJobgroup(String jobgroup){
        this.jobgroup = jobgroup;
    }    
    public String getJobparam() {
		return jobparam;
	}

	public void setJobparam(String jobparam) {
		this.jobparam = jobparam;
	}

	/**
     * 任务名称
     */
    public String getJobcname(){
        return this.jobcname;
    }

    /**
     * 任务名称
     */
    public void setJobcname(String jobcname){
        this.jobcname = jobcname;
    }    
    /**
     * 任务执行时间规则
     */
    public String getCronexpression(){
        return this.cronexpression;
    }

    /**
     * 任务执行时间规则
     */
    public void setCronexpression(String cronexpression){
        this.cronexpression = cronexpression;
    }    
    /**
     * 任务执行对象
     */
    public String getJobobject(){
        return this.jobobject;
    }

    /**
     * 任务执行对象
     */
    public void setJobobject(String jobobject){
        this.jobobject = jobobject;
    }    
    /**
     * 任务备注
     */
    public String getJobnote(){
        return this.jobnote;
    }

    /**
     * 任务备注
     */
    public void setJobnote(String jobnote){
        this.jobnote = jobnote;
    }    
    /**
     *  0：可用  1：不可用
     */
    public Short getIsvisible(){
        return this.isvisible;
    }

    /**
     *  0：可用  1：不可用
     */
    public void setIsvisible(Short isvisible){
        this.isvisible = isvisible;
    }    
    /**
     *  0：不记录日志  2：记录日志
     */
    public Short getIslog(){
        return this.islog;
    }

    /**
     *  0：不记录日志  2：记录日志
     */
    public void setIslog(Short islog){
        this.islog = islog;
    }    
    /**
     * 日志格式
     */
    public String getLogformat(){
        return this.logformat;
    }

    /**
     * 日志格式
     */
    public void setLogformat(String logformat){
        this.logformat = logformat;
    }
    
    public String[] getJgList(){
    	if(jgs != null){
    		return jgs.split(",");
    	}
    	
    	return null;
    }

   
}