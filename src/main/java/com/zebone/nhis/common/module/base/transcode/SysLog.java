package com.zebone.nhis.common.module.base.transcode;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="sys_log")
public class SysLog {

	@PK
	@Field(value="pk_log",id=KeyId.UUID)
	private String pklog;
	
	@Field(value="trans_code")
	private String transcode;
	
	@Field(value="type")
	private String type;
	
	private String log;
	
	@Field(value="in_param")
	private String inparam;
	
	@Field(value="out_param")
	private String outparam;
	
	private String creator;
	
	@Field(value="create_time",date=FieldType.INSERT)
	private Date createtime;
	
	@Field(value="exec_time")
	private Integer exectime;

	public String getPklog() {
		return pklog;
	}

	public void setPklog(String pklog) {
		this.pklog = pklog;
	}

	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getInparam() {
		return inparam;
	}

	public void setInparam(String inparam) {
		this.inparam = inparam;
	}

	public String getOutparam() {
		return outparam;
	}

	public void setOutparam(String outparam) {
		this.outparam = outparam;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getExectime() {
		return exectime;
	}

	public void setExectime(Integer exectime) {
		this.exectime = exectime;
	} 
}
