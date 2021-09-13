package com.zebone.nhis.emr.mgr.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="EMR_HOME_PAGE_FEEDBACK_ICD")
public class EmrHomePageFeedBackIcd extends BaseModule{

	@PK
	@Field(value="PK_FEEDBACK_ICD",id=KeyId.UUID)
	private String pkFeedbackIcd;
	
	@Field(value="PK_ORG")
	private String pkOrg;
	
	@Field(value="pk_page")
	private String pkPage;
	
	@Field(value="pk_pv")
	private String pkPv;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="PK_EMP")
	private String pkEmp;
	
	@Field(value="CODE")
	private String code;
	
	@Field(value="BACK_CONTENT")
	private String backContent;
	
	@Field(value="SEQ_NO")
	private String seqNo;

	@Field(value="TYPE")
	private String type;

	private String codeIp;
	
	private String name;
	
	private String times;
	
	private String content;
	public String getPkFeedbackIcd() {
		return pkFeedbackIcd;
	}

	public void setPkFeedbackIcd(String pkFeedbackIcd) {
		this.pkFeedbackIcd = pkFeedbackIcd;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPage() {
		return pkPage;
	}

	public void setPkPage(String pkPage) {
		this.pkPage = pkPage;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBackContent() {
		return backContent;
	}

	public void setBackContent(String backContent) {
		this.backContent = backContent;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
