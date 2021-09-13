package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;
import java.util.List;

/**
 * 根据费用退药明细信息
 * @author yangxue
 *
 */
public class PdRtnDtVo {
	private String pkCgip;
    private String pkCnord;
    private String pkPv;
    private String pkPd;
    private String pkUnit;
    private String nameunit;
    private String batchNo;
    private Integer packSize;
    private String nameCg;
    private String spec;
    private Double price;
    private Double amount;//总金额
    private Double quan;//已计费可退数量
    private Double dclnum;//待处理数量
    private Double cgQuan;//计费数量
    private Double rtnum;//实际录入退药数量（不允许写小数）
    private Double priceCost;//购入价格
    private String pkPres;
    private Integer ords;
    private String flagBase;
    private String flagSelf;
    private String flagMedout;
    private Date dateCg;
    private Date dateExpire;
    private String pkOrgDe;
    private String pkDeptDe;
    private String codeOrdtype;
    private String ordsn;
    private String euAlways;
    private List<String> exlistPks ;
    
    public List<String> getExlistPks() {
		return exlistPks;
	}
	public void setExlistPks(List<String> exlistPks) {
		this.exlistPks = exlistPks;
	}
    
	public String getPkOrgDe() {
		return pkOrgDe;
	}
	public void setPkOrgDe(String pkOrgDe) {
		this.pkOrgDe = pkOrgDe;
	}
	public String getPkDeptDe() {
		return pkDeptDe;
	}
	public void setPkDeptDe(String pkDeptDe) {
		this.pkDeptDe = pkDeptDe;
	}
	public Date getDateExpire() {
		return dateExpire;
	}
	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public Integer getOrds() {
		return ords;
	}
	public void setOrds(Integer ords) {
		this.ords = ords;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getFlagMedout() {
		return flagMedout;
	}
	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}
	public Double getPriceCost() {
		return priceCost;
	}
	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}
	public Double getRtnum() {
		return rtnum;
	}
	public void setRtnum(Double rtnum) {
		this.rtnum = rtnum;
	}
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getNameunit() {
		return nameunit;
	}
	public void setNameunit(String nameunit) {
		this.nameunit = nameunit;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public String getNameCg() {
		return nameCg;
	}
	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
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
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public Double getDclnum() {
		return dclnum;
	}
	public void setDclnum(Double dclnum) {
		this.dclnum = dclnum;
	}
	public Double getCgQuan() {
		return cgQuan;
	}
	public void setCgQuan(Double cgQuan) {
		this.cgQuan = cgQuan;
	}
	public Date getDateCg() {
		return dateCg;
	}
	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
    
    
   
}
