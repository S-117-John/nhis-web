package com.zebone.nhis.compay.ins.lb.vo.nhyb;

import java.math.BigDecimal;



public class CnOrderAndBdItem {
	 //BlIpDt
    private String pkItem;
    private BigDecimal price ;

    private String quan ;
    
    private BigDecimal amount ;
    private String pkDeptApp;



    //CodeOrd
    private String codeOrd ;
   
    private String presNo;
    
    private String codeFreq ;

    private String codeSupply;

    private String nameEmpOrd ;

    private String pkEmpOrd;

    private String pkUnit;
    
    private String unitName;

    private String days ;
   
	private String orderQuan ;
	
	private String pkCgip;
	
	private String pkCgop;
	
	private String codeBill;
	
	private String codeItem;
	
	private String itemCode;
	
	private String ItemName;
	
	private String flagPd;
	private String pkCgipBack;
	private int ordsnParent;
	private int ordsn;
	
	private String doseTypeName;
	
	private String spec;
	
	
    public String getPkCgipBack() {
		return pkCgipBack;
	}

	public void setPkCgipBack(String pkCgipBack) {
		this.pkCgipBack = pkCgipBack;
	}

	public int getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(int ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public int getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(int ordsn) {
		this.ordsn = ordsn;
	}

	public String getPresNo() {
		return presNo;
	}

	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}

	public String getFlagPd() {
		return flagPd;
	}

	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}

	public String getCodeItem() {
		return codeItem;
	}

	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}

	public String getCodeBill() {
		return codeBill;
	}

	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	/// <summary>
    /// 处方医师编码
    /// </summary>
    private String cfysbm ;
    
    private String nameCg;
    
    private String codeCg;
    
    private String dateHap;

    public String getDateHap() {
		return dateHap;
	}

	public void setDateHap(String dateHap) {
		this.dateHap = dateHap;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getCodeCg() {
		return codeCg;
	}

	public void setCodeCg(String codeCg) {
		this.codeCg = codeCg;
	}

	public String getOrderQuan() {
		return orderQuan;
	}

	public void setOrderQuan(String orderQuan) {
		this.orderQuan = orderQuan;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPkDeptApp() {
		return pkDeptApp;
	}

	public void setPkDeptApp(String pkDeptApp) {
		this.pkDeptApp = pkDeptApp;
	}

	public String getCodeOrd() {
		return codeOrd;
	}

	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}

	public String getCodeFreq() {
		return codeFreq;
	}

	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}

	public String getCodeSupply() {
		return codeSupply;
	}

	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

	public String getPkEmpOrd() {
		return pkEmpOrd;
	}

	public void setPkEmpOrd(String pkEmpOrd) {
		this.pkEmpOrd = pkEmpOrd;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getCfysbm() {
		return cfysbm;
	}

	public void setCfysbm(String cfysbm) {
		this.cfysbm = cfysbm;
	}

	public String getDoseTypeName() {
		return doseTypeName;
	}

	public void setDoseTypeName(String doseTypeName) {
		this.doseTypeName = doseTypeName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkCgop() {
		return pkCgop;
	}

	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return ItemName;
	}

	public void setItemName(String itemName) {
		ItemName = itemName;
	}
}
