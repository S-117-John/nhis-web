package com.zebone.nhis.ma.pub.arch.vo;

public class ArchInputParam {
	private String funcId;
	private String pkOrg;
	private ArchUploadContent content;
	
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
	public ArchUploadContent getContent() {
		return content;
	}
	public void setContent(ArchUploadContent content) {
		this.content = content;
	}


}
