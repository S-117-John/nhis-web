package com.zebone.nhis.common.module.compay.ins.qgyb;

import com.zebone.nhis.common.module.BaseModule;
import org.codehaus.jackson.annotate.JsonProperty;

//import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "Ins_Qgyb_Cg")
public class InsQgybCg extends BaseModule {

	/** PK_INSCG - 主键 */
	@PK
	@Field(value = "PK_INSCG", id = KeyId.UUID)
	private String pkInscg;

	@JsonProperty("pk_pv")
	@Field(value = "PK_PV")
	private String pkPv;

	/** PK_CGIP - 主键 */
	@Field(value = "PK_CGOP")
	private String pkCgop;

	@JsonProperty("feedetl_sn")
	@Field(value = "feedetl_sn")
	private String feedetlSn;

	@Field(value = "det_item_fee_sumamt")
	@JsonProperty("det_item_fee_sumamt")
	private String detItemFeeSumamt;

	@Field(value = "cnt")
	private String cnt;
	@Field(value = "pric")
	private String pric;

	@Field(value = "pric_uplmt_amt")
	@JsonProperty("pric_uplmt_amt")
	private String pricUplmtAmt;

	@JsonProperty("selfpay_prop")
	@Field(value = "selfpay_prop")
	private String selfpayProp;

	@JsonProperty("fulamt_ownpay_amt")
	@Field(value = "fulamt_ownpay_amt")
	private String fulamtOwnpayAmt;

	@JsonProperty("overlmt_amt")
	@Field(value = "overlmt_amt")
	private String overlmtAmt;

	@JsonProperty("preselfpay_amt")
	@Field(value = "preselfpay_amt")
	private String preselfpayAmt;

	@JsonProperty("inscp_scp_amt")
	@Field(value = "inscp_scp_amt")
	private String inscpScpAmt;

	@Field(value = "chrgitm_lv")
	@JsonProperty("chrgitm_lv")
	private String chrgitmLv;

	@JsonProperty("med_chrgitm_type")
	@Field(value = "med_chrgitm_type")
	private String medChrgitmType;

	@Field(value = "bas_medn_flag")
	@JsonProperty("bas_medn_flag")
	private String basMednFlag;

	@JsonProperty("hi_nego_drug_flag")
	@Field(value = "hi_nego_drug_flag")
	private String hiNegodrugFlag;

	@JsonProperty("chld_medc_flag")
	@Field(value = "chld_medc_flag")
	private String chld_medc_flag;

	@JsonProperty("list_sp_item_flag")
	@Field(value = "list_sp_item_flag")
	private String listSpItemFlag;

	@JsonProperty("lmt_used_flag")
	@Field(value = "lmt_used_flag")
	private String lmtUsedFlag;

	@JsonProperty("drt_reim_flag")
	@Field(value = "drt_reim_flag")
	private String drtReimFlag;

	@Field(value = "memo")
	private String memo;

	public String getPkInscg() {
		return pkInscg;
	}

	public void setPkInscg(String pkInscg) {
		this.pkInscg = pkInscg;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkCgop() {
		return pkCgop;
	}

	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}

	public String getFeedetlSn() {
		return feedetlSn;
	}

	public void setFeedetlSn(String feedetlSn) {
		this.feedetlSn = feedetlSn;
	}

	public String getDetItemFeeSumamt() {
		return detItemFeeSumamt;
	}

	public void setDetItemFeeSumamt(String detItemFeeSumamt) {
		this.detItemFeeSumamt = detItemFeeSumamt;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getPric() {
		return pric;
	}

	public void setPric(String pric) {
		this.pric = pric;
	}

	public String getPricUplmtAmt() {
		return pricUplmtAmt;
	}

	public void setPricUplmtAmt(String pricUplmtAmt) {
		this.pricUplmtAmt = pricUplmtAmt;
	}

	public String getSelfpayProp() {
		return selfpayProp;
	}

	public void setSelfpayProp(String selfpayProp) {
		this.selfpayProp = selfpayProp;
	}

	public String getFulamtOwnpayAmt() {
		return fulamtOwnpayAmt;
	}

	public void setFulamtOwnpayAmt(String fulamtOwnpayAmt) {
		this.fulamtOwnpayAmt = fulamtOwnpayAmt;
	}

	public String getOverlmtAmt() {
		return overlmtAmt;
	}

	public void setOverlmtAmt(String overlmtAmt) {
		this.overlmtAmt = overlmtAmt;
	}

	public String getPreselfpayAmt() {
		return preselfpayAmt;
	}

	public void setPreselfpayAmt(String preselfpayAmt) {
		this.preselfpayAmt = preselfpayAmt;
	}

	public String getInscpScpAmt() {
		return inscpScpAmt;
	}

	public void setInscpScpAmt(String inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}

	public String getChrgitmLv() {
		return chrgitmLv;
	}

	public void setChrgitmLv(String chrgitmLv) {
		this.chrgitmLv = chrgitmLv;
	}

	public String getMedChrgitmType() {
		return medChrgitmType;
	}

	public void setMedChrgitmType(String medChrgitmType) {
		this.medChrgitmType = medChrgitmType;
	}

	public String getBasMednFlag() {
		return basMednFlag;
	}

	public void setBasMednFlag(String basMednFlag) {
		this.basMednFlag = basMednFlag;
	}

	public String getHiNegodrugFlag() {
		return hiNegodrugFlag;
	}

	public void setHiNegodrugFlag(String hiNegodrugFlag) {
		this.hiNegodrugFlag = hiNegodrugFlag;
	}

	public String getChld_medc_flag() {
		return chld_medc_flag;
	}

	public void setChld_medc_flag(String chld_medc_flag) {
		this.chld_medc_flag = chld_medc_flag;
	}

	public String getListSpItemFlag() {
		return listSpItemFlag;
	}

	public void setListSpItemFlag(String listSpItemFlag) {
		this.listSpItemFlag = listSpItemFlag;
	}

	public String getLmtUsedFlag() {
		return lmtUsedFlag;
	}

	public void setLmtUsedFlag(String lmtUsedFlag) {
		this.lmtUsedFlag = lmtUsedFlag;
	}

	public String getDrtReimFlag() {
		return drtReimFlag;
	}

	public void setDrtReimFlag(String drtReimFlag) {
		this.drtReimFlag = drtReimFlag;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
