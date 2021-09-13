package com.zebone.nhis.ma.pub.arch.vo;

public class ArchReqParam {
	private String funcId;
	private String pkOrg;
	private ArchReqContent content;
	
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public ArchReqContent getContent() {
		return content;
	}
	public void setContent(ArchReqContent content) {
		this.content = content;
	}



}
