package com.zebone.nhis.sch.hd.vo;

import java.util.Date;

public class SchBedVo {
	//床位PK
	private String pkHdbed;
	
	//床位编码
	private String codeBed;
	
	//床位名称
	private String nameBed;
	
	//关联设备
	private String msp;
		
	//治疗类型
	private String dtHdtype;
	
	//日期分组名称
	private String nameDateslot;
	
	private String pkDateslot;
	
	//排班PK
	private String pkSchhd;
	
	//排班日期
	private Date dateHd;
	
	//患者PK
	private String pkPi;
	
	//患者姓名
	private String namePi;
	
	//患者编码
	public String codePi;
	
	//透析号码
	public String codeHd;
	
	//周透析次数
	public String cntWeek;

	public String getPkHdbed() {
		return pkHdbed;
	}

	public void setPkHdbed(String pkHdbed) {
		this.pkHdbed = pkHdbed;
	}

	public String getCodeBed() {
		return codeBed;
	}

	public void setCodeBed(String codeBed) {
		this.codeBed = codeBed;
	}

	public String getNameBed() {
		return nameBed;
	}

	public void setNameBed(String nameBed) {
		this.nameBed = nameBed;
	}

	public String getMsp() {
		return msp;
	}

	public void setMsp(String msp) {
		this.msp = msp;
	}

	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public String getNameDateslot() {
		return nameDateslot;
	}

	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}

	public String getPkSchhd() {
		return pkSchhd;
	}

	public void setPkSchhd(String pkSchhd) {
		this.pkSchhd = pkSchhd;
	}

	public Date getDateHd() {
		return dateHd;
	}

	public void setDateHd(Date dateHd) {
		this.dateHd = dateHd;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	
	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodeHd() {
		return codeHd;
	}

	public void setCodeHd(String codeHd) {
		this.codeHd = codeHd;
	}

	public String getCntWeek() {
		return cntWeek;
	}

	public void setCntWeek(String cntWeek) {
		this.cntWeek = cntWeek;
	}
	
}
