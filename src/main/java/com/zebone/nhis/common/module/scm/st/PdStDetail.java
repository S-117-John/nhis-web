package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_ST_DETAIL - pd_st_detail 
 *
 * @since 2016-11-03 10:03:41
 */
@Table(value="PD_ST_DETAIL")
public class PdStDetail extends BaseModule  {

	@PK
	@Field(value="PK_PDSTDT",id=KeyId.UUID)
    private String pkPdstdt;

	@Field(value="PK_PDST")
    private String pkPdst;

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


    public String getPkPdstdt(){
        return this.pkPdstdt;
    }
    public void setPkPdstdt(String pkPdstdt){
        this.pkPdstdt = pkPdstdt;
    }

    public String getPkPdst(){
        return this.pkPdst;
    }
    public void setPkPdst(String pkPdst){
        this.pkPdst = pkPdst;
    }

    public Integer getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Integer sortNo){
        this.sortNo = sortNo;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkUnitPack(){
        return this.pkUnitPack;
    }
    public void setPkUnitPack(String pkUnitPack){
        this.pkUnitPack = pkUnitPack;
    }

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
        this.packSize = packSize;
    }

    public Double getQuanPack(){
        return this.quanPack;
    }
    public void setQuanPack(Double quanPack){
        this.quanPack = quanPack;
    }

    public Double getQuanMin(){
        return this.quanMin;
    }
    public void setQuanMin(Double quanMin){
        this.quanMin = quanMin;
    }

    public Double getQuanOutstore(){
        return this.quanOutstore;
    }
    public void setQuanOutstore(Double quanOutstore){
        this.quanOutstore = quanOutstore;
    }

    public Double getPriceCost(){
        return this.priceCost;
    }
    public void setPriceCost(Double priceCost){
        this.priceCost = priceCost;
    }

    public Double getAmountCost(){
        return this.amountCost;
    }
    public void setAmountCost(Double amountCost){
        this.amountCost = amountCost;
    }

    public Double getPrice(){
        return this.price;
    }
    public void setPrice(Double price){
        this.price = price;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Double getDisc(){
        return this.disc;
    }
    public void setDisc(Double disc){
        this.disc = disc;
    }

    public String getBatchNo(){
        return this.batchNo;
    }
    public void setBatchNo(String batchNo){
        this.batchNo = batchNo;
    }

    public Date getDateFac(){
        return this.dateFac;
    }
    public void setDateFac(Date dateFac){
        this.dateFac = dateFac;
    }

    public Date getDateExpire(){
        return this.dateExpire;
    }
    public void setDateExpire(Date dateExpire){
        this.dateExpire = dateExpire;
    }

    public String getInvNo(){
        return this.invNo;
    }
    public void setInvNo(String invNo){
        this.invNo = invNo;
    }

    public String getReceiptNo(){
        return this.receiptNo;
    }
    public void setReceiptNo(String receiptNo){
        this.receiptNo = receiptNo;
    }

    public String getFlagChkRpt(){
        return this.flagChkRpt;
    }
    public void setFlagChkRpt(String flagChkRpt){
        this.flagChkRpt = flagChkRpt;
    }

    public Date getDateChkRpt(){
        return this.dateChkRpt;
    }
    public void setDateChkRpt(Date dateChkRpt){
        this.dateChkRpt = dateChkRpt;
    }

    public String getPkEmpChkRpt(){
        return this.pkEmpChkRpt;
    }
    public void setPkEmpChkRpt(String pkEmpChkRpt){
        this.pkEmpChkRpt = pkEmpChkRpt;
    }

    public String getNameEmpChkRpt(){
        return this.nameEmpChkRpt;
    }
    public void setNameEmpChkRpt(String nameEmpChkRpt){
        this.nameEmpChkRpt = nameEmpChkRpt;
    }

    public String getPkPdpay(){
        return this.pkPdpay;
    }
    public void setPkPdpay(String pkPdpay){
        this.pkPdpay = pkPdpay;
    }

    public Double getAmountPay(){
        return this.amountPay;
    }
    public void setAmountPay(Double amountPay){
        this.amountPay = amountPay;
    }

    public String getFlagPay(){
        return this.flagPay;
    }
    public void setFlagPay(String flagPay){
        this.flagPay = flagPay;
    }

    public String getPkPdpudt(){
        return this.pkPdpudt;
    }
    public void setPkPdpudt(String pkPdpudt){
        this.pkPdpudt = pkPdpudt;
    }

    public String getFlagFinish(){
        return this.flagFinish;
    }
    public void setFlagFinish(String flagFinish){
        this.flagFinish = flagFinish;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
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