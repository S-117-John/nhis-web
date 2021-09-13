package com.zebone.nhis.ma.pub.arch.vo;

public class ArchQryParamVo {

	private String funcId;
	private String pkOrg;
	
	private ArchQryParamBodyVo content;

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

	public ArchQryParamBodyVo getContent() {
		return content;
	}

	public void setContent(ArchQryParamBodyVo content) {
		this.content = content;
	}


}
