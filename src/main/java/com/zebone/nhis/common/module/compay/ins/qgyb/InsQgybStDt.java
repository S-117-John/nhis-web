package com.zebone.nhis.common.module.compay.ins.qgyb;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "INS_QGYB_ST_DT")
public class InsQgybStDt extends BaseModule{

	@PK
	@Field(value = "pk_insst_dt", id = KeyId.UUID)
	private String pkInsstDt;
	@Field("pk_insst")
	private String pkInsst;
	
	//基金支付类型
	@Field("fund_pay_type")
	@JsonProperty("fund_pay_type")
	private String fundPayType;
	
	//符合政策范围金额
	@Field("inscp_scp_amt")
	@JsonProperty("inscp_scp_amt")
	private Double inscpScpAmt;
	
	//本次可支付限额金额
	@Field("crt_payb_lmt_amt")
	@JsonProperty("crt_payb_lmt_amt")
	private Double crtPaybLmtAmt;
	
	//基金支付金额
	@Field("fund_payamt")
	@JsonProperty("fund_payamt")
	private Double fundPayamt;
	
	//基金支付类型名称
	@Field("fund_pay_type_name")
	@JsonProperty("fund_pay_type_name")
	private String fundPayTypeName;
	
	//结算过程信息
	@Field("setl_proc_info")
	@JsonProperty("setl_proc_info")
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
