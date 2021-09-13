package com.zebone.nhis.webservice.zsrm.vo.self;

public class ResponseYbWillSettleVo {
	
	private String diagnostics;
	
	private String codePi;
	
	private String codePv;

    private ReqYbPreSettleInfovo  ybPreSettleInfo;
    
    private ReqYbPreSettleParamVo  ybPreSettleParam;

	public String getCodePi() {
		return codePi;
	}

	public String getCodePv() {
		return codePv;
	}

	public ReqYbPreSettleInfovo getYbPreSettleInfo() {
		return ybPreSettleInfo;
	}

	public ReqYbPreSettleParamVo getYbPreSettleParam() {
		return ybPreSettleParam;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public void setYbPreSettleInfo(ReqYbPreSettleInfovo ybPreSettleInfo) {
		this.ybPreSettleInfo = ybPreSettleInfo;
	}

	public void setYbPreSettleParam(ReqYbPreSettleParamVo ybPreSettleParam) {
		this.ybPreSettleParam = ybPreSettleParam;
	}

	public String getDiagnostics() {
		return diagnostics;
	}

	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}
    
}
