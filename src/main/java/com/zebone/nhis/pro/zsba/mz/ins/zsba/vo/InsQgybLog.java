package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
@Table(value = "INS_QGYB_LOG")
public class InsQgybLog extends BaseModule{

	@PK
	@Field(value = "pk_inslog", id = KeyId.UUID)
	private String pk_Inslog;
	//业务类型/接口号
	@Field("bues_type")
	private String buesType;
	//日志id
	@Field("id")
	private String id;
	//日志内容
	@Field("content")
	private String content;
	@Field("pk_pi")
	private String pkPi;
	@Field("pk_pv")
	private String pkPv;
	
	@Field("code_emp")
	private String codeEmp;

	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getPk_Inslog() {
		return pk_Inslog;
	}
	public void setPk_Inslog(String pk_Inslog) {
		this.pk_Inslog = pk_Inslog;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getBuesType() {
		return buesType;
	}
	public void setBuesType(String buesType) {
		this.buesType = buesType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}
