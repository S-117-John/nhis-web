package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

public class PdstDetail extends BaseModule{
	
	@PK
	@Field(value="PK_PDST",id=KeyId.UUID)
    private String pkPdst;

	@Field(value="PK_DEPT_ST")
    private String pkDeptSt;

	@Field(value="PK_STORE_ST")
    private String pkStoreSt;

    /** DT_STTYPE - 码表080008 */
	@Field(value="DT_STTYPE")
    private String dtSttype;

	@Field(value="CODE_ST")
    private String codeSt;

	@Field(value="NAME_ST")
    private String nameSt;

    /** EU_DIRECT - 1入库，-1出库 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="DATE_ST")
    private Date dateSt;

    /** EU_STATUS - 0 制单 1 审核 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_ORG_LK")
    private String pkOrgLk;

	@Field(value="PK_DEPT_LK")
    private String pkDeptLk;

	@Field(value="PK_STORE_LK")
    private String pkStoreLk;

	@Field(value="PK_PDPU")
    private String pkPdpu;

	@Field(value="PK_PDPLAN")
    private String pkPdplan;

	@Field(value="PK_EMP_OP")
    private String pkEmpOp;

	@Field(value="NAME_EMP_OP")
    private String nameEmpOp;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_PDST_SR")
    private String pkPdstSr;

	@Field(value="FLAG_PU")
    private String flagPu;
	
	private String pkPdstdt;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_UNIT_PACK")
    private String pkUnitPack;

	@Field(value="PACK_SIZE")
    private Integer packSize;

	@Field(value="QUAN_PACK")
    private Double quanPack;

	@Field(value="QUAN_MIN")
    private Double quanMin;

	@Field(value="QUAN_OUTSTORE")
    private Double quanOutstore;

	@Field(value="PRICE_COST")
    private Double priceCost;

	@Field(value="AMOUNT_COST")
    private Double amountCost;

	@Field(value="PRICE")
    private Double price;

	@Field(value="AMOUNT")
    private Double amount;

	@Field(value="DISC")
    private Double disc;

	@Field(value="BATCH_NO")
    private String batchNo;

	@Field(value="DATE_FAC")
    private Date dateFac;

	@Field(value="DATE_EXPIRE")
    private Date dateExpire;

	@Field(value="INV_NO")
    private String invNo;

	@Field(value="RECEIPT_NO")
    private String receiptNo;

	@Field(value="FLAG_CHK_RPT")
    private String flagChkRpt;

	@Field(value="DATE_CHK_RPT")
    private Date dateChkRpt;

	@Field(value="PK_EMP_CHK_RPT")
    private String pkEmpChkRpt;

	@Field(value="NAME_EMP_CHK_RPT")
    private String nameEmpChkRpt;

	@Field(value="PK_PDPAY")
    private String pkPdpay;

	@Field(value="AMOUNT_PAY")
    private Double amountPay;

	@Field(value="FLAG_PAY")
    private String flagPay;

	@Field(value="PK_PDPUDT")
    private String pkPdpudt;

	@Field(value="FLAG_FINISH")
    private String flagFinish;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="PK_SUPPLYER")
	private String pkSupplyer;

	@Field(value = "PK_PDSTDT_RL")
	private String pkPdstdtRl;

	public String getPkPdst() {
		return pkPdst;
	}

	public void setPkPdst(String pkPdst) {
		this.pkPdst = pkPdst;
	}

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getPkStoreSt() {
		return pkStoreSt;
	}

	public void setPkStoreSt(String pkStoreSt) {
		this.pkStoreSt = pkStoreSt;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getNameSt() {
		return nameSt;
	}

	public void setNameSt(String nameSt) {
		this.nameSt = nameSt;
	}

	public String getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkOrgLk() {
		return pkOrgLk;
	}

	public void setPkOrgLk(String pkOrgLk) {
		this.pkOrgLk = pkOrgLk;
	}

	public String getPkDeptLk() {
		return pkDeptLk;
	}

	public void setPkDeptLk(String pkDeptLk) {
		this.pkDeptLk = pkDeptLk;
	}

	public String getPkStoreLk() {
		return pkStoreLk;
	}

	public void setPkStoreLk(String pkStoreLk) {
		this.pkStoreLk = pkStoreLk;
	}

	public String getPkPdpu() {
		return pkPdpu;
	}

	public void setPkPdpu(String pkPdpu) {
		this.pkPdpu = pkPdpu;
	}

	public String getPkPdplan() {
		return pkPdplan;
	}

	public void setPkPdplan(String pkPdplan) {
		this.pkPdplan = pkPdplan;
	}

	public String getPkEmpOp() {
		return pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {
		this.pkEmpOp = pkEmpOp;
	}

	public String getNameEmpOp() {
		return nameEmpOp;
	}

	public void setNameEmpOp(String nameEmpOp) {
		this.nameEmpOp = nameEmpOp;
	}

	public String getFlagChk() {
		return flagChk;
	}

	public void setFlagChk(String flagChk) {
		this.flagChk = flagChk;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getPkPdstSr() {
		return pkPdstSr;
	}

	public void setPkPdstSr(String pkPdstSr) {
		this.pkPdstSr = pkPdstSr;
	}

	public String getFlagPu() {
		return flagPu;
	}

	public void setFlagPu(String flagPu) {
		this.flagPu = flagPu;
	}

	public String getPkPdstdt() {
		return pkPdstdt;
	}

	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public Double getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}

	public Double getQuanMin() {
		return quanMin;
	}

	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}

	public Double getQuanOutstore() {
		return quanOutstore;
	}

	public void setQuanOutstore(Double quanOutstore) {
		this.quanOutstore = quanOutstore;
	}

	public Double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}

	public Double getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(Double amountCost) {
		this.amountCost = amountCost;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDisc() {
		return disc;
	}

	public void setDisc(Double disc) {
		this.disc = disc;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getDateFac() {
		return dateFac;
	}

	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getFlagChkRpt() {
		return flagChkRpt;
	}

	public void setFlagChkRpt(String flagChkRpt) {
		this.flagChkRpt = flagChkRpt;
	}

	public Date getDateChkRpt() {
		return dateChkRpt;
	}

	public void setDateChkRpt(Date dateChkRpt) {
		this.dateChkRpt = dateChkRpt;
	}

	public String getPkEmpChkRpt() {
		return pkEmpChkRpt;
	}

	public void setPkEmpChkRpt(String pkEmpChkRpt) {
		this.pkEmpChkRpt = pkEmpChkRpt;
	}

	public String getNameEmpChkRpt() {
		return nameEmpChkRpt;
	}

	public void setNameEmpChkRpt(String nameEmpChkRpt) {
		this.nameEmpChkRpt = nameEmpChkRpt;
	}

	public String getPkPdpay() {
		return pkPdpay;
	}

	public void setPkPdpay(String pkPdpay) {
		this.pkPdpay = pkPdpay;
	}

	public Double getAmountPay() {
		return amountPay;
	}

	public void setAmountPay(Double amountPay) {
		this.amountPay = amountPay;
	}

	public String getFlagPay() {
		return flagPay;
	}

	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}

	public String getPkPdpudt() {
		return pkPdpudt;
	}

	public void setPkPdpudt(String pkPdpudt) {
		this.pkPdpudt = pkPdpudt;
	}

	public String getFlagFinish() {
		return flagFinish;
	}

	public void setFlagFinish(String flagFinish) {
		this.flagFinish = flagFinish;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getPkSupplyer() {
		return pkSupplyer;
	}

	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}

	public String getPkPdstdtRl() {
		return pkPdstdtRl;
	}

	public void setPkPdstdtRl(String pkPdstdtRl) {
		this.pkPdstdtRl = pkPdstdtRl;
	}
	
	
}
