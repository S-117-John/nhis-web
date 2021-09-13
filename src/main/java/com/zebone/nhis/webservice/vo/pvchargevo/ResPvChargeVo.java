package com.zebone.nhis.webservice.vo.pvchargevo;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class ResPvChargeVo {
	private Double amount;
	private Double amountAdd;
	private Double amountHppi;
	private Double amountPi;
	private String codeFreq;
	private Date dateStart;
	private String days;
	private Double dosage;
	private String euAdditem;
	private String euDrugtype;
	private String flagInsu;
	private String flagPv;
	private String itemcate;
	private String nameCg;
	private String nameDeptOrd;
	private String nameEmpInput;
	private String nameEmpOrd;
	private String nameSupply;
	private String nameUnitDos;
	private String ords;
	private String pkCgop;
	private String pkCnord;
	private String pkDeptEx;
	private String nameDeptEx;
	private String pkDeptOrd;
	private String pkDisc;
	private String pkEmpInput;
	private String pkEmpOrd;
	private String pkPi;
	private String pkPv;
	private String pkUnitDos;
	private String presNo;
	private Double price;
	private Double priceOrg;
	private Integer quan;
	private String ratioAdd;
	private String ratioDisc;
	private String ratioSelf;
	private String spec;
	private String codeOrdType;
	private String ts;
	private String unit;
	private String code;
	
	@XmlElement(name = "code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@XmlElement(name = "nameDeptEx")
	public String getNameDeptEx() {
		return nameDeptEx;
	}
	public void setNameDeptEx(String nameDeptEx) {
		this.nameDeptEx = nameDeptEx;
	}
	
	@XmlElement(name = "codeOrdType")
	public String getCodeOrdType() {
		return codeOrdType;
	}
	public void setCodeOrdType(String codeOrdType) {
		this.codeOrdType = codeOrdType;
	}
	@XmlElement(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@XmlElement(name = "amountAdd")
	public Double getAmountAdd() {
		return amountAdd;
	}
	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}
	
	@XmlElement(name = "amountHppi")
	public Double getAmountHppi() {
		return amountHppi;
	}
	public void setAmountHppi(Double amountHppi) {
		this.amountHppi = amountHppi;
	}
	
	@XmlElement(name = "amountPi")
	public Double getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}
	
	@XmlElement(name = "codeFreq")
	public String getCodeFreq() {
		return codeFreq;
	}
	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}
	
	@XmlElement(name = "dateStart")
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	
	@XmlElement(name = "days")
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	
	@XmlElement(name = "dosage")
	public Double getDosage() {
		return dosage;
	}
	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}
	
	@XmlElement(name = "euAdditem")
	public String getEuAdditem() {
		return euAdditem;
	}
	public void setEuAdditem(String euAdditem) {
		this.euAdditem = euAdditem;
	}
	
	@XmlElement(name = "euDrugtype")
	public String getEuDrugtype() {
		return euDrugtype;
	}
	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}
	
	@XmlElement(name = "flagInsu")
	public String getFlagInsu() {
		return flagInsu;
	}
	public void setFlagInsu(String flagInsu) {
		this.flagInsu = flagInsu;
	}
	
	@XmlElement(name = "flagPv")
	public String getFlagPv() {
		return flagPv;
	}
	public void setFlagPv(String flagPv) {
		this.flagPv = flagPv;
	}
	
	@XmlElement(name = "itemcate")
	public String getItemcate() {
		return itemcate;
	}
	public void setItemcate(String itemcate) {
		this.itemcate = itemcate;
	}
	
	@XmlElement(name = "nameCg")
	public String getNameCg() {
		return nameCg;
	}
	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}
	
	@XmlElement(name = "nameDeptOrd")
	public String getNameDeptOrd() {
		return nameDeptOrd;
	}
	public void setNameDeptOrd(String nameDeptOrd) {
		this.nameDeptOrd = nameDeptOrd;
	}
	
	@XmlElement(name = "nameEmpInput")
	public String getNameEmpInput() {
		return nameEmpInput;
	}
	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
	}
	
	@XmlElement(name = "nameEmpOrd")
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	
	@XmlElement(name = "nameSupply")
	public String getNameSupply() {
		return nameSupply;
	}
	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}
	
	@XmlElement(name = "nameUnitDos")
	public String getNameUnitDos() {
		return nameUnitDos;
	}
	public void setNameUnitDos(String nameUnitDos) {
		this.nameUnitDos = nameUnitDos;
	}
	
	@XmlElement(name = "ords")
	public String getOrds() {
		return ords;
	}
	public void setOrds(String ords) {
		this.ords = ords;
	}
	
	@XmlElement(name = "pkCgop")
	public String getPkCgop() {
		return pkCgop;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}
	
	@XmlElement(name = "pkCnord")
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	
	@XmlElement(name = "pkDeptEx")
	public String getPkDeptEx() {
		return pkDeptEx;
	}
	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}
	
	@XmlElement(name = "pkDeptOrd")
	public String getPkDeptOrd() {
		return pkDeptOrd;
	}
	public void setPkDeptOrd(String pkDeptOrd) {
		this.pkDeptOrd = pkDeptOrd;
	}
	
	@XmlElement(name = "pkDisc")
	public String getPkDisc() {
		return pkDisc;
	}
	public void setPkDisc(String pkDisc) {
		this.pkDisc = pkDisc;
	}
	
	@XmlElement(name = "pkEmpInput")
	public String getPkEmpInput() {
		return pkEmpInput;
	}
	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}
	
	@XmlElement(name = "pkEmpOrd")
	public String getPkEmpOrd() {
		return pkEmpOrd;
	}
	public void setPkEmpOrd(String pkEmpOrd) {
		this.pkEmpOrd = pkEmpOrd;
	}
	
	@XmlElement(name = "pkPi")
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	
	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
	@XmlElement(name = "pkUnitDos")
	public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	
	@XmlElement(name = "presNo")
	public String getPresNo() {
		return presNo;
	}
	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}
	
	@XmlElement(name = "price")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@XmlElement(name = "priceOrg")
	public Double getPriceOrg() {
		return priceOrg;
	}
	public void setPriceOrg(Double priceOrg) {
		this.priceOrg = priceOrg;
	}
	
	@XmlElement(name = "quan")
	public Integer getQuan() {
		return quan;
	}
	public void setQuan(Integer quan) {
		this.quan = quan;
	}
	
	@XmlElement(name = "ratioAdd")
	public String getRatioAdd() {
		return ratioAdd;
	}
	public void setRatioAdd(String ratioAdd) {
		this.ratioAdd = ratioAdd;
	}
	
	@XmlElement(name = "ratioDisc")
	public String getRatioDisc() {
		return ratioDisc;
	}
	public void setRatioDisc(String ratioDisc) {
		this.ratioDisc = ratioDisc;
	}
	
	@XmlElement(name = "ratioSelf")
	public String getRatioSelf() {
		return ratioSelf;
	}
	public void setRatioSelf(String ratioSelf) {
		this.ratioSelf = ratioSelf;
	}
	
	@XmlElement(name = "spec")
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	@XmlElement(name = "ts")
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	
	@XmlElement(name = "unit")
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
