package com.zebone.nhis.base.bd.vo;

public class DisRefPdVo {
	private String pkDiseaseord;
	private String code;
	private String name;
	private String pkPd;
	private long days;
	private String note;
	public String getPkDiseaseord() {
		return pkDiseaseord;
	}
	public void setPkDiseaseord(String pkDiseaseord) {
		this.pkDiseaseord = pkDiseaseord;
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
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public long getDays() {
		return days;
	}
	public void setDays(long days) {
		this.days = days;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
