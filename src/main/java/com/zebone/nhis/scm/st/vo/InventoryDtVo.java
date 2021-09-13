package com.zebone.nhis.scm.st.vo;

import java.util.Date;

import com.zebone.nhis.common.module.scm.st.PdInvDetail;

public class InventoryDtVo extends PdInvDetail{
    private String pdcode;
    private String pdname;
    private String spec;
    private String factory;
    private Date dateFac;
    private String unit;
    private Double amtAcc;//账面金额
    private Double amt;//实盘金额（零售金额）
    private Double amtCost;//成本金额
    private String spcode;
    private Integer packSizePd;//零售包装量
    private String oldCode;//老系统药品编码
    private String posiNo;//货位号
    private Double quanPackInp;
    private Double quanMinInp;
    private String nameMin;
    private Double pricePd;
    private String euDrugtype;
    
    
	public String getEuDrugtype() {
		return euDrugtype;
	}
	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}
	public String getOldCode() {
		return oldCode;
	}
	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}
	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public Date getDateFac() {
		return dateFac;
	}
	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}
	public Double getAmtCost() {
		return amtCost;
	}
	public void setAmtCost(Double amtCost) {
		this.amtCost = amtCost;
	}
	public Double getAmtAcc() {
		return amtAcc;
	}
	public void setAmtAcc(Double amtAcc) {
		this.amtAcc = amtAcc;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
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
	public String getPosiNo() {
		return posiNo;
	}
	public void setPosiNo(String posiNo) {
		this.posiNo = posiNo;
	}
	public Double getQuanPackInp() {
		return quanPackInp;
	}
	public void setQuanPackInp(Double quanPackInp) {
		this.quanPackInp = quanPackInp;
	}
	public Double getQuanMinInp() {
		return quanMinInp;
	}
	public void setQuanMinInp(Double quanMinInp) {
		this.quanMinInp = quanMinInp;
	}
	public String getNameMin() {
		return nameMin;
	}
	public void setNameMin(String nameMin) {
		this.nameMin = nameMin;
	}
	public Double getPricePd() {
		return pricePd;
	}
	public void setPricePd(Double pricePd) {
		this.pricePd = pricePd;
	}
    
    
}
