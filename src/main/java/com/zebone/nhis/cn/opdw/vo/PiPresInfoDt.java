package com.zebone.nhis.cn.opdw.vo;

public class PiPresInfoDt {

	
	private String pkOrd; //医嘱主键
    private String nameOrd;// 药品名称
    private String spec;// 规格
    private String pkUnitCg; // 包装单位
    private Double dosage; //  用量
    private String pkUnitDos; //  用量单位
    private String codeSupply; // 用法
    private String codeFreq; // 频次
    private Double days; // 天数
    private Double quanCg;// 数量 
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPkUnitCg() {
		return pkUnitCg;
	}
	public void setPkUnitCg(String pkUnitCg) {
		this.pkUnitCg = pkUnitCg;
	}
	public Double getDosage() {
		return dosage;
	}
	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}
	public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	public String getCodeSupply() {
		return codeSupply;
	}
	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}
	public String getCodeFreq() {
		return codeFreq;
	}
	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}
	public Double getDays() {
		return days;
	}
	public void setDays(Double days) {
		this.days = days;
	}
	public Double getQuanCg() {
		return quanCg;
	}
	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}
    
    

}
