package com.zebone.nhis.ex.nis.pd.vo;

public  class DetailedVo {
	//住院号
    private String codeIp ;
    //床号
    private String bedNo ;
    //姓名
    private String namePi ;
    //发药单号
    private String codeDe ;
    //药品名称
    private String pdName ;
    //规格
    private String spec ;
    //单位
    private String unit ;
    //数量
    private String quan ;
    //频次
    private String freq ;
    //用法
    private String supply ;
    //用量
    private String dosage ;
    //用量单位
    private String unitDos ;
    //状态
    private String euStatus ;
    //发药日期
    private String dateDe ;
	public String getBedNo() {
		return bedNo;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getCodeDe() {
		return codeDe;
	}
	public void setCodeDe(String codeDe) {
		this.codeDe = codeDe;
	}
	public String getPdName() {
		return pdName;
	}
	public void setPdName(String pdName) {
		this.pdName = pdName;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnti(String unit) {
		this.unit = unit;
	}
	public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getSupply() {
		return supply;
	}
	public void setSupply(String supply) {
		this.supply = supply;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getUnitDos() {
		return unitDos;
	}
	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getDateDe() {
		return dateDe;
	}
	public void setDateDe(String dateDe) {
		this.dateDe = dateDe;
	}
}
