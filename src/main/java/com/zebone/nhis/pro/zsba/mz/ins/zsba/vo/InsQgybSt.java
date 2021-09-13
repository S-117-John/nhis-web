package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "INS_QGYB_ST")
public class InsQgybSt extends BaseModule {

	@PK
	@Field(value = "pk_Insst", id = KeyId.UUID)
	private String pkInsst;
	/** 医保登记主键 */
	@Field("pk_visit")
	private String pkVisit;
	@JsonProperty("setl_id")
	@Field("setl_id")
	private String setlId;
	@Field("yb_pksettle")
	private String ybPksettle;

	@Field("setl_id_cancel")
	private String setlIdCancel;
	@Field("pk_Insst_Cancel")
	private String pkInsstCancel;
	
	@Field("pk_hp")
	private String pkHp;
	@Field("pk_pv")
	private String pkPv;
	@Field("pk_pi")
	private String pkPi;
	@Field("med_type")
	@JsonProperty("med_type")
	private String medType;
	@Field("pk_settle")
	private String pkSettle;
	@Field("mdtrt_id")
	@JsonProperty("mdtrt_id")
	private String mdtrtId;
	@Field("insutype")
	@JsonProperty("insutype")
	private String insutype;
	@Field("psn_type")
	private String psnType;
	@Field("psn_no")
	private String psnNo;
	@Field("mdtrt_cert_type")
	@JsonProperty("mdtrt_cert_type")
	private String mdtrtCertType;
	@Field("date_st")
	private Date dateSt;
	@Field("amount")
	private Double amount;
	@Field("bill_no")
	private String billNo;
	@Field("amt_grzhzf")
	private Double amtGrzhzf;
	@Field("amt_grzf")
	private Double amtGrzf;
	@Field("amt_grzh")
	private Double amtGrzh;
	@Field("amt_jjzf")
	private Double amtJjzf;
	
	@Field("grzhzf_trade_no")
	private String grzhzfTradeNo;

	/** 费用总额 */
	@Field("medfee_sumamt")
	@JsonProperty("medfee_sumamt")
	private Double medfeeSumamt;
	/** 个人账户支付 */
	@Field("acct_pay")
	@JsonProperty("acct_pay")
	private Double acctPay;
	/** 基金支付 */
	@Field("fund_pay_sumamt")
	@JsonProperty("fund_pay_sumamt")
	private Double fundPaySumamt;
	/** 个人现金支付 */
	@Field("psn_cash_pay")
	@JsonProperty("psn_cash_pay")
	private Double psnCashPay;

	// 全自费金额
	@Field("fulamt_ownpay_amt")
	@JsonProperty("fulamt_ownpay_amt")
	private Double fulamtOwnpayAmt;
	// 超限价自费费用
	@Field("overlmt_selfpay")
	@JsonProperty("overlmt_selfpay")
	private Double overlmtSelfpay;
	// 先行自付金额
	@Field("preselfpay_amt")
	@JsonProperty("preselfpay_amt")
	private Double preselfpayAmt;
	// 符合政策范围金额
	@Field("inscp_scp_amt")
	@JsonProperty("inscp_scp_amt")
	private Double inscpScpAmt;
	// 实际支付起付线
	@Field("act_pay_dedc")
	@JsonProperty("act_pay_dedc")
	private Double actPayDedc;
	// 基本医疗保险统筹基金支出
	@Field("hifp_pay")
	@JsonProperty("hifp_pay")
	private Double hifpPay;
	// 基本医疗保险统筹基金支付比例
	@Field("pool_prop_selfpay")
	@JsonProperty("pool_prop_selfpay")
	private Double poolPropSelfpay;
	// 公务员医疗补助资金支出
	@Field("cvlserv_pay")
	@JsonProperty("cvlserv_pay")
	private Double cvlservPay;
	// 企业补充医疗保险基金支出
	@Field("hifes_pay")
	@JsonProperty("hifes_pay")
	private Double hifesPay;
	// 居民大病保险资金支出
	@Field("hifmi_pay")
	@JsonProperty("hifmi_pay")
	private Double hifmiPay;
	// 职工大额医疗费用补助基金支出
	@Field("hifob_pay")
	@JsonProperty("hifob_pay")
	private Double hifobPay;
	// 医疗救助基金支出
	@Field("maf_pay")
	@JsonProperty("maf_pay")
	private Double mafPay;
	// 其他支出
	@Field("oth_pay")
	@JsonProperty("oth_pay")
	private Double othPay;

	// 个人负担总金额
	@Field("psn_part_amt")
	@JsonProperty("psn_part_amt")
	private Double psnPartAmt;

	// 医院负担金额
	@Field("hosp_part_amt")
	@JsonProperty("hosp_part_amt")
	private Double hospPartAmt;
	// 余额
	@Field("balc")
	@JsonProperty("balc")
	private Double balc;
	
	// 个人账户共济支付金额
	@Field("acct_mulaid_pay")
	@JsonProperty("acct_mulaid_pay")
	private Double acctMulaidPay;
	
	// 医药机构结算ID
	@Field("medins_setl_id")
	@JsonProperty("medins_setl_id")
	private String medinsSetlId;
	
	// 清算经办机构
	@Field("clr_optins")
	@JsonProperty("clr_optins")
	private String clrOptins;
	// 清算方式
	@Field("clr_way")
	@JsonProperty("clr_way")
	private String clrWay;
	// 清算类别
	@Field("clr_type")
	@JsonProperty("clr_type")
	private String clrType;
	
	@JsonProperty("yb_cancel_pksettle")
	private String ybCancelPksettle;

	@Field("PK_INSPV")
	@JsonProperty("pk_inspv")
	private String pkInspv;

	public String getPkInspv() {
		return pkInspv;
	}

	public void setPkInspv(String pkInspv) {
		this.pkInspv = pkInspv;
	}

	public String getPkInsst() {
		return pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getPkVisit() {
		return pkVisit;
	}

	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
	}

	public String getSetlId() {
		return setlId;
	}

	public void setSetlId(String setlId) {
		this.setlId = setlId;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}

	public String getPsnType() {
		return psnType;
	}

	public void setPsnType(String psnType) {
		this.psnType = psnType;
	}



	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Double getAmtGrzhzf() {
		return amtGrzhzf;
	}

	public void setAmtGrzhzf(Double amtGrzhzf) {
		this.amtGrzhzf = amtGrzhzf;
	}



	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmtGrzf() {
		return amtGrzf;
	}

	public void setAmtGrzf(Double amtGrzf) {
		this.amtGrzf = amtGrzf;
	}

	public Double getAmtJjzf() {
		return amtJjzf;
	}

	public void setAmtJjzf(Double amtJjzf) {
		this.amtJjzf = amtJjzf;
	}

	public String getMdtrtCertType() {
		return mdtrtCertType;
	}

	public void setMdtrtCertType(String mdtrtCertType) {
		this.mdtrtCertType = mdtrtCertType;
	}

	public Double getMedfeeSumamt() {
		return medfeeSumamt;
	}

	public void setMedfeeSumamt(Double medfeeSumamt) {
		this.medfeeSumamt = medfeeSumamt;
	}

	public Double getAcctPay() {
		return acctPay;
	}

	public void setAcctPay(Double acctPay) {
		this.acctPay = acctPay;
	}

	public Double getFundPaySumamt() {
		return fundPaySumamt;
	}

	public void setFundPaySumamt(Double fundPaySumamt) {
		this.fundPaySumamt = fundPaySumamt;
	}

	public Double getPsnCashPay() {
		return psnCashPay;
	}

	public void setPsnCashPay(Double psnCashPay) {
		this.psnCashPay = psnCashPay;
	}

	public Double getAmtGrzh() {
		return amtGrzh;
	}

	public void setAmtGrzh(Double amtGrzh) {
		this.amtGrzh = amtGrzh;
	}

	public Double getFulamtOwnpayAmt() {
		return fulamtOwnpayAmt;
	}

	public void setFulamtOwnpayAmt(Double fulamtOwnpayAmt) {
		this.fulamtOwnpayAmt = fulamtOwnpayAmt;
	}

	public Double getOverlmtSelfpay() {
		return overlmtSelfpay;
	}

	public void setOverlmtSelfpay(Double overlmtSelfpay) {
		this.overlmtSelfpay = overlmtSelfpay;
	}

	public Double getPreselfpayAmt() {
		return preselfpayAmt;
	}

	public void setPreselfpayAmt(Double preselfpayAmt) {
		this.preselfpayAmt = preselfpayAmt;
	}

	public Double getInscpScpAmt() {
		return inscpScpAmt;
	}

	public void setInscpScpAmt(Double inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}

	public Double getActPayDedc() {
		return actPayDedc;
	}

	public Double getPoolPropSelfpay() {
		return poolPropSelfpay;
	}

	public void setPoolPropSelfpay(Double poolPropSelfpay) {
		this.poolPropSelfpay = poolPropSelfpay;
	}

	public Double getCvlservPay() {
		return cvlservPay;
	}

	public void setCvlservPay(Double cvlservPay) {
		this.cvlservPay = cvlservPay;
	}

	public Double getHifesPay() {
		return hifesPay;
	}

	public void setHifesPay(Double hifesPay) {
		this.hifesPay = hifesPay;
	}

	public Double getHifmiPay() {
		return hifmiPay;
	}

	public void setHifmiPay(Double hifmiPay) {
		this.hifmiPay = hifmiPay;
	}

	public Double getHifobPay() {
		return hifobPay;
	}

	public void setHifobPay(Double hifobPay) {
		this.hifobPay = hifobPay;
	}

	public Double getMafPay() {
		return mafPay;
	}

	public void setMafPay(Double mafPay) {
		this.mafPay = mafPay;
	}

	public Double getOthPay() {
		return othPay;
	}

	public void setOthPay(Double othPay) {
		this.othPay = othPay;
	}

	public Double getPsnPartAmt() {
		return psnPartAmt;
	}

	public void setPsnPartAmt(Double psnPartAmt) {
		this.psnPartAmt = psnPartAmt;
	}

	public Double getHospPartAmt() {
		return hospPartAmt;
	}

	public void setHospPartAmt(Double hospPartAmt) {
		this.hospPartAmt = hospPartAmt;
	}

	public Double getBalc() {
		return balc;
	}

	public void setBalc(Double balc) {
		this.balc = balc;
	}

	public void setActPayDedc(Double actPayDedc) {
		this.actPayDedc = actPayDedc;
	}

	public Double getHifpPay() {
		return hifpPay;
	}

	public void setHifpPay(Double hifpPay) {
		this.hifpPay = hifpPay;
	}

	public String getClrOptins() {
		return clrOptins;
	}

	public void setClrOptins(String clrOptins) {
		this.clrOptins = clrOptins;
	}

	public String getClrWay() {
		return clrWay;
	}

	public void setClrWay(String clrWay) {
		this.clrWay = clrWay;
	}

	public String getClrType() {
		return clrType;
	}

	public void setClrType(String clrType) {
		this.clrType = clrType;
	}

	public String getMedinsSetlId() {
		return medinsSetlId;
	}

	public void setMedinsSetlId(String medinsSetlId) {
		this.medinsSetlId = medinsSetlId;
	}

	public Double getAcctMulaidPay() {
		return acctMulaidPay;
	}

	public void setAcctMulaidPay(Double acctMulaidPay) {
		this.acctMulaidPay = acctMulaidPay;
	}

	public String getSetlIdCancel() {
		return setlIdCancel;
	}

	public void setSetlIdCancel(String setlIdCancel) {
		this.setlIdCancel = setlIdCancel;
	}

	public String getPkInsstCancel() {
		return pkInsstCancel;
	}

	public void setPkInsstCancel(String pkInsstCancel) {
		this.pkInsstCancel = pkInsstCancel;
	}

	public String getYbPksettle() {
		return ybPksettle;
	}

	public void setYbPksettle(String ybPksettle) {
		this.ybPksettle = ybPksettle;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getGrzhzfTradeNo() {
		return grzhzfTradeNo;
	}

	public void setGrzhzfTradeNo(String grzhzfTradeNo) {
		this.grzhzfTradeNo = grzhzfTradeNo;
	}

	public String getYbCancelPksettle() {
		return ybCancelPksettle;
	}

	public void setYbCancelPksettle(String ybCancelPksettle) {
		this.ybCancelPksettle = ybCancelPksettle;
	}
}
