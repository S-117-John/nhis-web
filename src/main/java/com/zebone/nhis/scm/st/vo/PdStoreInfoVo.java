package com.zebone.nhis.scm.st.vo;

import java.util.Date;

public class PdStoreInfoVo {
	private String pkPdstock;
    private String pkPd;
    private String code;
    private String name;
    private String spec;
    private Integer packSize;
    private Integer packSizePd;
    private String unit;
    private Double quan; //结存数量
    private Double quanPrep;// --预留数量
    private Double quanUse;//可用数量
    private String posiNo;// --存放位置
    private String batchNo;//--批号
    private Date dateExpire;// --失效日期
    private Double priceCost;
    private Double price;
    private Double amountCost;
    private Double amount;
    private String flagStop;
    private String flagStopOp;
    private String flagStopEr;
    private String factory ;
    private String spcode;
    private String nameChem;
    private String pdFlagStop;
	private String storeFlagStop;
	private String codeStd;

	public String getPdFlagStop() {
		return pdFlagStop;
	}

	public void setPdFlagStop(String pdFlagStop) {
		this.pdFlagStop = pdFlagStop;
	}

	public String getStoreFlagStop() {
		return storeFlagStop;
	}

	public void setStoreFlagStop(String storeFlagStop) {
		this.storeFlagStop = storeFlagStop;
	}

	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public String getFlagStopOp() {
		return flagStopOp;
	}
	public void setFlagStopOp(String flagStopOp) {
		this.flagStopOp = flagStopOp;
	}
	public String getFlagStopEr() {
		return flagStopEr;
	}
	public void setFlagStopEr(String flagStopEr) {
		this.flagStopEr = flagStopEr;
	}
	public String getPkPdstock() {
		return pkPdstock;
	}  
	public void setPkPdstock(String pkPdstock) {
		this.pkPdstock = pkPdstock;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public Double getQuanPrep() {
		return quanPrep;
	}
	public void setQuanPrep(Double quanPrep) {
		this.quanPrep = quanPrep;
	}
	public Double getQuanUse() {
		return quanUse;
	}
	public void setQuanUse(Double quanUse) {
		this.quanUse = quanUse;
	}
	public String getPosiNo() {
		return posiNo;
	}
	public void setPosiNo(String posiNo) {
		this.posiNo = posiNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Date getDateExpire() {
		return dateExpire;
	}
	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}
	public Double getPriceCost() {
		return priceCost;
	}
	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getAmountCost() {
		return amountCost;
	}
	public void setAmountCost(Double amountCost) {
		this.amountCost = amountCost;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getNameChem() {
		return nameChem;
	}
	public void setNameChem(String nameChem) {
		this.nameChem = nameChem;
	}

	public String getCodeStd() {
		return codeStd;
	}

	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}
	
}
