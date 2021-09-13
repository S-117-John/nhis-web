package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: 
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_ST_SETLDETAIL_QG")
public class InsZsbaStSetldetailQg extends BaseModule{

	@PK
	@Field(value="PK_INSITEMCATEQG",id=KeyId.UUID)
    private String pkInsitemcateqg;
	
	@Field(value="PK_INSSTQG")
    private String pkInsstqg;
	
	@Field(value="PK_SETTLE")
    private String pkSettle;
	
	@Field(value="FUND_PAY_TYPE")
    private String fundPayType;
	
	@Field(value="INSCP_SCP_AMT")
    private String inscpScpAmt;
	
	@Field(value="CRT_PAYB_LMT_AMT")
    private String crtPaybLmtAmt;
	
	@Field(value="FUND_PAYAMT")
    private String fundPayAmt;
	
	@Field(value="FUND_PAY_TYPE_NAME")
    private String fundPayTypeName;
	
	@Field(value="SETL_PROC_INFO")
    private String setlProcInfo;

	public String getPkInsitemcateqg() {
		return pkInsitemcateqg;
	}

	public void setPkInsitemcateqg(String pkInsitemcateqg) {
		this.pkInsitemcateqg = pkInsitemcateqg;
	}

	public String getPkInsstqg() {
		return pkInsstqg;
	}

	public void setPkInsstqg(String pkInsstqg) {
		this.pkInsstqg = pkInsstqg;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getFundPayType() {
		return fundPayType;
	}

	public void setFundPayType(String fundPayType) {
		this.fundPayType = fundPayType;
	}

	public String getInscpScpAmt() {
		return inscpScpAmt;
	}

	public void setInscpScpAmt(String inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}

	public String getCrtPaybLmtAmt() {
		return crtPaybLmtAmt;
	}

	public void setCrtPaybLmtAmt(String crtPaybLmtAmt) {
		this.crtPaybLmtAmt = crtPaybLmtAmt;
	}

	public String getFundPayAmt() {
		return fundPayAmt;
	}

	public void setFundPayAmt(String fundPayAmt) {
		this.fundPayAmt = fundPayAmt;
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
	
}
