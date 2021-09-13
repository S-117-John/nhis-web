package com.zebone.nhis.webservice.zsrm.vo.self;

public class RequestYbSettleDtVo {

	private String pkInsstDt;

	private String pkInsst;
	
	private String fundPayType;

	private Double inscpScpAmt;

	private Double crtPaybLmtAmt;
	
	private Double fundPayamt;

	private String fundPayTypeName;

	private String setlProcInfo;

	public String getPkInsst() {
		return pkInsst;
	}
	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}
	public String getFundPayType() {
		return fundPayType;
	}
	public void setFundPayType(String fundPayType) {
		this.fundPayType = fundPayType;
	}
	public Double getInscpScpAmt() {
		return inscpScpAmt;
	}
	public void setInscpScpAmt(Double inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}
	public Double getFundPayamt() {
		return fundPayamt;
	}
	
	public void setFundPayamt(Double fundPayamt) {
		this.fundPayamt = fundPayamt;
	}
	public String getFundPayTypeName() {
		return fundPayTypeName;
	}
	public void setFundPayTypeName(String fundPayTypeName) {
		this.fundPayTypeName = fundPayTypeName;
	}
	public String getSetlProcInfo() {
		return setlProcInfo;
	}
	public void setSetlProcInfo(String setlProcInfo) {
		this.setlProcInfo = setlProcInfo;
	}
	public String getPkInsstDt() {
		return pkInsstDt;
	}
	public void setPkInsstDt(String pkInsstDt) {
		this.pkInsstDt = pkInsstDt;
	}
	public Double getCrtPaybLmtAmt() {
		return crtPaybLmtAmt;
	}
	public void setCrtPaybLmtAmt(Double crtPaybLmtAmt) {
		this.crtPaybLmtAmt = crtPaybLmtAmt;
	}

}
