package com.zebone.platform.quartz.modle;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 *
 * @author 
 */
@Table("qrtz_job_log")
public class QrtzJobLog implements java.io.Serializable{
    /**
     * 
     */
	@PK
	@Field(id= KeyId.UUID)
    private String id;
    /**
     * 
     */
	@Field
    private String jobname;
	  /**
     * 
     */
	@Field
    private String jobcname;
    /**
     * 
     */
	@Field
    private String jobmessage;
    /**
     * 0:任务执行成功  1：任务执行业务失败    2：任务执行异常  
     */
	@Field
    private Short jobtype;
    /**
     * 
     */
	@Field(value="create_time",date= FieldType.INSERT)
    private Date createTime;

    /**
     * 
     */
    public String getId(){
        return this.id;
    }

    /**
     * 
     */
    public void setId(String id){
        this.id = id;
    }    
    /**
     * 
     */
    public String getJobname(){
        return this.jobname;
    }

    /**
     * 
     */
    public void setJobname(String jobname){
        this.jobname = jobname;
    }    
    /**
     * 
     */
    public String getJobmessage(){
        return this.jobmessage;
    }

    public String getJobcname() {
		return jobcname;
	}

	public void setJobcname(String jobcname) {
		this.jobcname = jobcname;
	}

	/**
     * 
     */
    public void setJobmessage(String jobmessage){
        this.jobmessage = jobmessage;
    }    
    /**
     * 0:任务执行成功  1：任务执行业务失败    2：任务执行异常  
     */
    public Short getJobtype(){
        return this.jobtype;
    }

    /**
     * 0:任务执行成功  1：任务执行业务失败    2：任务执行异常  
     */
    public void setJobtype(Short jobtype){
        this.jobtype = jobtype;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

}