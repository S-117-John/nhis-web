package com.zebone.nhis.scm.material.vo;

import java.util.Date;

import com.zebone.nhis.common.module.scm.purchase.PdPurchaseDt;

@SuppressWarnings("serial")
public class MtlPuOrdDtInfo  extends PdPurchaseDt  {

	private String codePd;
	private String namePd;
	private String spec;
	private String euStockmode;
	private Date dateValidReg;
	private String factory;
	private String unit;
	private String nameOrg;
	private String nameDept;
	private String nameStore;
	private String pkPdplan;//采购计划主键
	private Double price;//由采购订单生成采购入库明细时使用
	private Double priceCost;//由采购订单生成采购入库明细时使用
	private Double quanStk;//由采购订单生成采购入库明细时使用
	private String euPdprice;
    private String euPap;
    private Double amtPap;
    private Double papRate;
    private String pkPdstore;//仓库物品维护关系
    private String pkUnitCvt;//仓库物品使用的单位
    private Integer packSizeCvt;//仓库物品使用单位的包装量
    private String nameUnitCvt;//仓库使用的单位名称
    
	public String getCodePd() {
		return codePd;
	}
	public void setCodePd(String codePd) {
		this.codePd = codePd;
	}
	public String getNamePd() {
		return namePd;
	}
	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getEuStockmode() {
		return euStockmode;
	}
	public void setEuStockmode(String euStockmode) {
		this.euStockmode = euStockmode;
	}
	public Date getDateValidReg() {
		return dateValidReg;
	}
	public void setDateValidReg(Date dateValidReg) {
		this.dateValidReg = dateValidReg;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	public String getPkPdplan() {
		return pkPdplan;
	}
	public void setPkPdplan(String pkPdplan) {
		this.pkPdplan = pkPdplan;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPriceCost() {
		return priceCost;
	}
	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}
	public Double getQuanStk() {
		return quanStk;
	}
	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}
	public String getEuPdprice() {
		return euPdprice;
	}
	public void setEuPdprice(String euPdprice) {
		this.euPdprice = euPdprice;
	}
	public String getEuPap() {
		return euPap;
	}
	public void setEuPap(String euPap) {
		this.euPap = euPap;
	}
	public Double getAmtPap() {
		return amtPap;
	}
	public void setAmtPap(Double amtPap) {
		this.amtPap = amtPap;
	}
	public Double getPapRate() {
		return papRate;
	}
	public void setPapRate(Double papRate) {
		this.papRate = papRate;
	}
	public String getPkPdstore() {
		return pkPdstore;
	}
	public void setPkPdstore(String pkPdstore) {
		this.pkPdstore = pkPdstore;
	}
	public String getPkUnitCvt() {
		return pkUnitCvt;
	}
	public void setPkUnitCvt(String pkUnitCvt) {
		this.pkUnitCvt = pkUnitCvt;
	}
	public Integer getPackSizeCvt() {
		return packSizeCvt;
	}
	public void setPackSizeCvt(Integer packSizeCvt) {
		this.packSizeCvt = packSizeCvt;
	}
	public String getNameUnitCvt() {
		return nameUnitCvt;
	}
	public void setNameUnitCvt(String nameUnitCvt) {
		this.nameUnitCvt = nameUnitCvt;
	}
    
}
