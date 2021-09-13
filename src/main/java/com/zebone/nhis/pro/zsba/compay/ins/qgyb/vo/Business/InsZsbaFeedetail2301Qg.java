package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 
 *
 * @since 2020-11-10 10:42:10
 */
@Table(value="INS_FEEDETAIL2301_QG")
public class InsZsbaFeedetail2301Qg extends BaseModule{

	@PK
	@Field(value="PK_INSFEEDETAILQG",id=KeyId.UUID)
    private String pkInsdiseinfoqg;
	
	@Field(value="PK_INSPVQG")
    private String pkInspvqg;
	
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	@Field(value="MDTRT_ID")
	private String	mdtrtId;//		就诊ID
	@Field(value="FEEDETL_SN")
	private String	feedetlSn;//		费用明细流水号
	@Field(value="CNT")
	private BigDecimal	cnt;//		数量
	@Field(value="PRIC")
	private BigDecimal	pric;//		单价
	@Field(value="DET_ITEM_FEE_SUMAMT")
	private BigDecimal	detItemFeeSumamt;//		明细项目费用总额
	@Field(value="PRIC_UPLMT_AMT")
	private BigDecimal	pricUplmtAmt;//		定价上限金额
	@Field(value="SELFPAY_PROP")
	private BigDecimal	selfpayProp;//		自付比例
	@Field(value="FULAMT_OWNPAY_AMT")
	private BigDecimal	fulamtOwnpayAmt;//		全自费金额
	@Field(value="OVERLMT_AMT")
	private BigDecimal	overlmtAmt;//		超限价金额
	@Field(value="PRESELFPAY_AMT")
	private BigDecimal	preselfpayAmt;//		先行自付金额
	@Field(value="INSCP_SCP_AMT")
	private BigDecimal	inscpScpAmt;//		符合政策范围金额
	@Field(value="CHRGITM_LV")
	private String	chrgitmLv;//		收费项目等级
	@Field(value="MED_CHRGITM_TYPE")
	private String	medChrgitmType;//		医疗收费项目类别
	@Field(value="BAS_MEDN_FLAG")
	private String	basMednFlag;//		基本药物标志
	@Field(value="HI_NEGO_DRUG_FLAG")
	private String	hiNegoDrugFlag;//		医保谈判药品标志
	@Field(value="CHLD_MEDC_FLAG")
	private String	chldMedcFlag;//		儿童用药标志
	@Field(value="LIST_SP_ITEM_FLAG")
	private String	listSpItemFlag;//		目录特项标志
	@Field(value="LMT_USED_FLAG")
	private String	lmtUsedFlag;//		限制使用标志
	@Field(value="DRT_REIM_FLAG")
	private String	drtReimFlag;//		直报标志
	@Field(value="MEMO")
	private String	memo;//		备注
	public String getPkInsdiseinfoqg() {
		return pkInsdiseinfoqg;
	}
	public void setPkInsdiseinfoqg(String pkInsdiseinfoqg) {
		this.pkInsdiseinfoqg = pkInsdiseinfoqg;
	}
	public String getPkInspvqg() {
		return pkInspvqg;
	}
	public void setPkInspvqg(String pkInspvqg) {
		this.pkInspvqg = pkInspvqg;
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
	public String getMdtrtId() {
		return mdtrtId;
	}
	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}
	public String getFeedetlSn() {
		return feedetlSn;
	}
	public void setFeedetlSn(String feedetlSn) {
		this.feedetlSn = feedetlSn;
	}
	public BigDecimal getCnt() {
		return cnt;
	}
	public void setCnt(BigDecimal cnt) {
		this.cnt = cnt;
	}
	public BigDecimal getPric() {
		return pric;
	}
	public void setPric(BigDecimal pric) {
		this.pric = pric;
	}
	public BigDecimal getDetItemFeeSumamt() {
		return detItemFeeSumamt;
	}
	public void setDetItemFeeSumamt(BigDecimal detItemFeeSumamt) {
		this.detItemFeeSumamt = detItemFeeSumamt;
	}
	public BigDecimal getPricUplmtAmt() {
		return pricUplmtAmt;
	}
	public void setPricUplmtAmt(BigDecimal pricUplmtAmt) {
		this.pricUplmtAmt = pricUplmtAmt;
	}
	public BigDecimal getSelfpayProp() {
		return selfpayProp;
	}
	public void setSelfpayProp(BigDecimal selfpayProp) {
		this.selfpayProp = selfpayProp;
	}
	public BigDecimal getFulamtOwnpayAmt() {
		return fulamtOwnpayAmt;
	}
	public void setFulamtOwnpayAmt(BigDecimal fulamtOwnpayAmt) {
		this.fulamtOwnpayAmt = fulamtOwnpayAmt;
	}
	public BigDecimal getOverlmtAmt() {
		return overlmtAmt;
	}
	public void setOverlmtAmt(BigDecimal overlmtAmt) {
		this.overlmtAmt = overlmtAmt;
	}
	public BigDecimal getPreselfpayAmt() {
		return preselfpayAmt;
	}
	public void setPreselfpayAmt(BigDecimal preselfpayAmt) {
		this.preselfpayAmt = preselfpayAmt;
	}
	public BigDecimal getInscpScpAmt() {
		return inscpScpAmt;
	}
	public void setInscpScpAmt(BigDecimal inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}
	public String getChrgitmLv() {
		return chrgitmLv;
	}
	public void setChrgitmLv(String chrgitmLv) {
		this.chrgitmLv = chrgitmLv;
	}
	public String getBasMednFlag() {
		return basMednFlag;
	}
	public void setBasMednFlag(String basMednFlag) {
		this.basMednFlag = basMednFlag;
	}
	public String getHiNegoDrugFlag() {
		return hiNegoDrugFlag;
	}
	public void setHiNegoDrugFlag(String hiNegoDrugFlag) {
		this.hiNegoDrugFlag = hiNegoDrugFlag;
	}
	public String getChldMedcFlag() {
		return chldMedcFlag;
	}
	public void setChldMedcFlag(String chldMedcFlag) {
		this.chldMedcFlag = chldMedcFlag;
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
	public String getMedChrgitmType() {
		return medChrgitmType;
	}
	public void setMedChrgitmType(String medChrgitmType) {
		this.medChrgitmType = medChrgitmType;
	}
	
}
