package com.zebone.nhis.emr.scires.vo;

import java.util.List;

import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;

/**
 * 科研分析结果VO
 * @author chengjia
 *
 */
public class RptRsltRec {
	private String pkPv;
	private String pkPi;
	private String name;
	private String sexName;
	private String ageTxt;
	private int  times;
	private String pkPatrec;
	private String userName;
	private String value;
	private String patNo;
	private String beginDate;
	
	//private RptRsltItem[] arrayItems;
	private List<RptRsltItem> items;
	
	
	public List<RptRsltItem> getItems() {
		return items;
	}

	public void setItems(List<RptRsltItem> items) {
		this.items = items;
	}
	
	
	/*
	public RptRsltItem[] getArrayItems() {
		return arrayItems;
	}

	public void setArrayItems(RptRsltItem[] arrayItems) {
		this.arrayItems = arrayItems;
	}*/

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public String getAgeTxt() {
		return ageTxt;
	}

	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}



	public String getPkPatrec() {
		return pkPatrec;
	}

	public void setPkPatrec(String pkPatrec) {
		this.pkPatrec = pkPatrec;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPatNo() {
		return patNo;
	}

	public void setPatNo(String patNo) {
		this.patNo = patNo;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}




	
	
}
