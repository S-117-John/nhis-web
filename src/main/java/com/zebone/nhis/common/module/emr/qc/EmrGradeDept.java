package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="emr_grade_dept")
public class EmrGradeDept {
	@PK
	@Field(value="pk_gradedept",id=KeyId.UUID)
	private String pkGradeDept;
	
	@Field(value="pk_org")
	private String pkOrg;
	
	@Field(value="cate_code")
	private String cateCode;
	
	@Field(value="pk_dept")
	private String pkDept;
	
	@Field(value="del_flag")
	private String delFlag;
	
	@Field(value="creator")
    private String creator;
	
	@Field(value="create_time")
    private Date createTime;
	
	@Field(value="ts")
    private Date ts;

	private String deptCode;
	
	private String deptName;
	
	private String status;
	
	public String getPkGradeDept() {
		return pkGradeDept;
	}

	public void setPkGradeDept(String pkGradeDept) {
		this.pkGradeDept = pkGradeDept;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCateCode() {
		return cateCode;
	}

	public void setCateCode(String cateCode) {
		this.cateCode = cateCode;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
