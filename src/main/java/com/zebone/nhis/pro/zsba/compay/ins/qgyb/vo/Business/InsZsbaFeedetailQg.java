package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: ins_diseinfo_qg
 *
 * @since 2020-11-10 10:42:10
 */
@Table(value="INS_FEEDETAIL_QG")
public class InsZsbaFeedetailQg extends BaseModule{

	@PK
	@Field(value="PK_INSFEEDETAILQG",id=KeyId.UUID)
    private String pkInsdiseinfoqg;
	
	@Field(value="PK_INSPVQG")
    private String pkInspvqg;
	
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	@Field(value="PSN_NO")
	private String	psnNo;//		人员编码
	@Field(value="MDTRT_ID")
	private String	mdtrtId;//		就诊ID
	@Field(value="SETL_ID")
	private String	setlId;//		结算ID
	@Field(value="FEEDETL_SN")
	private String	feedetlSn;//		费用明细流水号
	@Field(value="RX_DRORD_NO")
	private String	rxDrordNo;//		处方/医嘱号
	@Field(value="MED_TYPE")
	private String	medType;//		医疗类别
	@Field(value="FEE_OCUR_TIME")
	private Date	feeOcurTime;//		费用发生时间
	@Field(value="CNT")
	private BigDecimal	cnt;//		数量
	@Field(value="PRIC")
	private BigDecimal	pric;//		单价
	@Field(value="SIN_DOS_DSCR")
	private String	sinDosDscr;//		单次剂量描述
	@Field(value="USED_FRQU_DSCR")
	private String	usedFrquDscr;//		使用频次描述
	@Field(value="PRD_DAYS")
	private BigDecimal	prdDays;//		周期天数
	@Field(value="MEDC_WAY_DSCR")
	private String	medcWayDscr;//		用药途径描述
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
	@Field(value="HILIST_CODE")
	private String	hilistCode;//		医保目录编码
	@Field(value="HILIST_NAME")
	private String	hilistName;//		医保目录名称
	@Field(value="LIST_TYPE")
	private String	listType;//		目录类别
	@Field(value="MED_LIST_CODG")
	private String	medListCodg;//		医疗目录编码
	@Field(value="MEDINS_LIST_CODG")
	private String	medinsListCodg;//		医药机构目录编码
	@Field(value="MEDINS_LIST_NAME")
	private String	medinsListName;//		医药机构目录名称
	@Field(value="MED_CHRGITM_TYPE")
	private String	medChrgitmType;//		医疗收费项目类别
	@Field(value="PRODNAME")
	private String	prodname;//		商品名
	@Field(value="SPEC")
	private String	spec;//		规格
	@Field(value="DOSFORM_NAME")
	private String	dosformName;//		剂型名称
	@Field(value="BILG_DEPT_CODG")
	private String	bilgDeptCodg;//		开单科室编码
	@Field(value="BILG_DEPT_NAME")
	private String	bilgDeptName;//		开单科室名称
	@Field(value="BILG_DR_CODG")
	private String	bilgDrCodg;//		开单医生编码
	@Field(value="BILG_DR_NAME")
	private String	bilgDrName;//		开单医师姓名
	@Field(value="ACORD_DEPT_CODG")
	private String	acordDeptCodg;//		受单科室编码
	@Field(value="ACORD_DEPT_NAME")
	private String	acordDeptName;//		受单科室名称
	@Field(value="ORDERS_DR_CODE")
	private String	ordersDrCode;//		受单医生编码
	@Field(value="ORDERS_DR_NAME")
	private String	ordersDrName;//		受单医生姓名
	@Field(value="LMT_USED_FLAG")
	private String	lmtUsedFlag;//		限制使用标志
	@Field(value="HOSP_PREP_FLAG")
	private String	hospPrepFlag;//		医院制剂标志
	@Field(value="HOSP_APPR_FLAG")
	private String	hospApprFlag;//		医院审批标志
	@Field(value="TCMDRUG_USED_WAY")
	private String	tcmdrugUsedWay;//		中药使用方式
	@Field(value="PRODPLAC_TYPE")
	private String	prodplacType;//		生产地类别
	@Field(value="BAS_MEDN_FLAG")
	private String	basMednFlag;//		基本药物标志
	@Field(value="HI_NEGO_DRUG_FLAG")
	private String	hiNegoDrugFlag;//		医保谈判药品标志
	@Field(value="CHLD_MEDC_FLAG")
	private String	chldMedcFlag;//		儿童用药标志
	@Field(value="ETIP_FLAG")
	private String	etipFlag;//		外检标志
	@Field(value="ETIP_HOSP_CODE")
	private String	etipHospCode;//		外检医院编码
	@Field(value="DSCG_TKDRUG_FLAG")
	private String	dscgTkdrugFlag;//		出院带药标志
	@Field(value="LIST_SP_ITEM_FLAG")
	private String	listSpItemFlag;//		目录特项标志
	@Field(value="MATN_FEE_FLAG")
	private String	matnFeeFlag;//		生育费用标志
	@Field(value="DRT_REIM_FLAG")
	private String	drtReimFlag;//		直报标志
	@Field(value="MEMO")
	private String	memo;//		备注
	@Field(value="OPTER_ID")
	private String	opterId;//		经办人ID
	@Field(value="OPTER_NAME")
	private String	opterName;//		经办人姓名
	@Field(value="OPT_TIME")
	private Date	optTime;//		经办时间
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
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}
	public String getMdtrtId() {
		return mdtrtId;
	}
	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}
	public String getSetlId() {
		return setlId;
	}
	public void setSetlId(String setlId) {
		this.setlId = setlId;
	}
	public String getFeedetlSn() {
		return feedetlSn;
	}
	public void setFeedetlSn(String feedetlSn) {
		this.feedetlSn = feedetlSn;
	}
	public String getRxDrordNo() {
		return rxDrordNo;
	}
	public void setRxDrordNo(String rxDrordNo) {
		this.rxDrordNo = rxDrordNo;
	}
	public String getMedType() {
		return medType;
	}
	public void setMedType(String medType) {
		this.medType = medType;
	}
	public Date getFeeOcurTime() {
		return feeOcurTime;
	}
	public void setFeeOcurTime(Date feeOcurTime) {
		this.feeOcurTime = feeOcurTime;
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
	public String getSinDosDscr() {
		return sinDosDscr;
	}
	public void setSinDosDscr(String sinDosDscr) {
		this.sinDosDscr = sinDosDscr;
	}
	public String getUsedFrquDscr() {
		return usedFrquDscr;
	}
	public void setUsedFrquDscr(String usedFrquDscr) {
		this.usedFrquDscr = usedFrquDscr;
	}
	public BigDecimal getPrdDays() {
		return prdDays;
	}
	public void setPrdDays(BigDecimal prdDays) {
		this.prdDays = prdDays;
	}
	public String getMedcWayDscr() {
		return medcWayDscr;
	}
	public void setMedcWayDscr(String medcWayDscr) {
		this.medcWayDscr = medcWayDscr;
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
	public String getHilistCode() {
		return hilistCode;
	}
	public void setHilistCode(String hilistCode) {
		this.hilistCode = hilistCode;
	}
	public String getHilistName() {
		return hilistName;
	}
	public void setHilistName(String hilistName) {
		this.hilistName = hilistName;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	public String getMedListCodg() {
		return medListCodg;
	}
	public void setMedListCodg(String medListCodg) {
		this.medListCodg = medListCodg;
	}
	public String getMedinsListCodg() {
		return medinsListCodg;
	}
	public void setMedinsListCodg(String medinsListCodg) {
		this.medinsListCodg = medinsListCodg;
	}
	public String getMedinsListName() {
		return medinsListName;
	}
	public void setMedinsListName(String medinsListName) {
		this.medinsListName = medinsListName;
	}
	public String getMedChrgitmType() {
		return medChrgitmType;
	}
	public void setMedChrgitmType(String medChrgitmType) {
		this.medChrgitmType = medChrgitmType;
	}
	public String getProdname() {
		return prodname;
	}
	public void setProdname(String prodname) {
		this.prodname = prodname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getDosformName() {
		return dosformName;
	}
	public void setDosformName(String dosformName) {
		this.dosformName = dosformName;
	}
	public String getBilgDeptCodg() {
		return bilgDeptCodg;
	}
	public void setBilgDeptCodg(String bilgDeptCodg) {
		this.bilgDeptCodg = bilgDeptCodg;
	}
	public String getBilgDeptName() {
		return bilgDeptName;
	}
	public void setBilgDeptName(String bilgDeptName) {
		this.bilgDeptName = bilgDeptName;
	}
	public String getBilgDrCodg() {
		return bilgDrCodg;
	}
	public void setBilgDrCodg(String bilgDrCodg) {
		this.bilgDrCodg = bilgDrCodg;
	}
	public String getBilgDrName() {
		return bilgDrName;
	}
	public void setBilgDrName(String bilgDrName) {
		this.bilgDrName = bilgDrName;
	}
	public String getAcordDeptCodg() {
		return acordDeptCodg;
	}
	public void setAcordDeptCodg(String acordDeptCodg) {
		this.acordDeptCodg = acordDeptCodg;
	}
	public String getAcordDeptName() {
		return acordDeptName;
	}
	public void setAcordDeptName(String acordDeptName) {
		this.acordDeptName = acordDeptName;
	}
	public String getOrdersDrCode() {
		return ordersDrCode;
	}
	public void setOrdersDrCode(String ordersDrCode) {
		this.ordersDrCode = ordersDrCode;
	}
	public String getOrdersDrName() {
		return ordersDrName;
	}
	public void setOrdersDrName(String ordersDrName) {
		this.ordersDrName = ordersDrName;
	}
	public String getLmtUsedFlag() {
		return lmtUsedFlag;
	}
	public void setLmtUsedFlag(String lmtUsedFlag) {
		this.lmtUsedFlag = lmtUsedFlag;
	}
	public String getHospPrepFlag() {
		return hospPrepFlag;
	}
	public void setHospPrepFlag(String hospPrepFlag) {
		this.hospPrepFlag = hospPrepFlag;
	}
	public String getHospApprFlag() {
		return hospApprFlag;
	}
	public void setHospApprFlag(String hospApprFlag) {
		this.hospApprFlag = hospApprFlag;
	}
	public String getTcmdrugUsedWay() {
		return tcmdrugUsedWay;
	}
	public void setTcmdrugUsedWay(String tcmdrugUsedWay) {
		this.tcmdrugUsedWay = tcmdrugUsedWay;
	}
	public String getProdplacType() {
		return prodplacType;
	}
	public void setProdplacType(String prodplacType) {
		this.prodplacType = prodplacType;
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
	public String getEtipFlag() {
		return etipFlag;
	}
	public void setEtipFlag(String etipFlag) {
		this.etipFlag = etipFlag;
	}
	public String getEtipHospCode() {
		return etipHospCode;
	}
	public void setEtipHospCode(String etipHospCode) {
		this.etipHospCode = etipHospCode;
	}
	public String getDscgTkdrugFlag() {
		return dscgTkdrugFlag;
	}
	public void setDscgTkdrugFlag(String dscgTkdrugFlag) {
		this.dscgTkdrugFlag = dscgTkdrugFlag;
	}
	public String getListSpItemFlag() {
		return listSpItemFlag;
	}
	public void setListSpItemFlag(String listSpItemFlag) {
		this.listSpItemFlag = listSpItemFlag;
	}
	public String getMatnFeeFlag() {
		return matnFeeFlag;
	}
	public void setMatnFeeFlag(String matnFeeFlag) {
		this.matnFeeFlag = matnFeeFlag;
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
	public String getOpterId() {
		return opterId;
	}
	public void setOpterId(String opterId) {
		this.opterId = opterId;
	}
	public String getOpterName() {
		return opterName;
	}
	public void setOpterName(String opterName) {
		this.opterName = opterName;
	}
	public Date getOptTime() {
		return optTime;
	}
	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}


	
	
}
