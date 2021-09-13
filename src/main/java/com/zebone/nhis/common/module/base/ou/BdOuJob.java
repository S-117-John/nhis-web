package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 岗位信息
 * @author Xulj
 *
 */
@Table(value="bd_ou_job")
public class BdOuJob extends BaseModule {

	/**
	 * 岗位主键
	 */
	@PK
	@Field(value="pk_job",id=KeyId.UUID)
	private String pkJob;
	
	/**
	 * 岗位编码
	 */
	@Field(value="code_job")
	private String codeJob;
	
	/**
	 * 岗位名称
	 */
	@Field(value="name_job")
	private String nameJob;
	
	/**
	 * 拼音码
	 */
	@Field(value="py_code")
	private String pyCode;
	
	/**
	 * 自定义码
	 */
	@Field(value="d_code")
	private String dCode;
	
	/**
	 * 上级岗位
	 */
	@Field(value="pk_father")
	private String pkFather;
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;

	public String getPkJob() {
		return pkJob;
	}

	public void setPkJob(String pkJob) {
		this.pkJob = pkJob;
	}

	public String getCodeJob() {
		return codeJob;
	}

	public void setCodeJob(String codeJob) {
		this.codeJob = codeJob;
	}

	public String getNameJob() {
		return nameJob;
	}

	public void setNameJob(String nameJob) {
		this.nameJob = nameJob;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

}

