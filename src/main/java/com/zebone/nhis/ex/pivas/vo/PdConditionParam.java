package com.zebone.nhis.ex.pivas.vo;

import java.util.ArrayList;
import java.util.List;

public class PdConditionParam {
	/*
	 * 药理分类
	 */
	String dtPharm;
	/*
	 * 毒麻分类
	 */
	String dtPois;
	/*
	 * 抗菌药
	 */
	String dtAnti;
	/*
	 * TPN标志
	 */
	String flagTpn;
	/*
	 * 已被bd_pivas_pd表应用的药品主键数组
	 */
	List<String> pkPdList = new ArrayList<String>();
	public String getDtPharm() {
		return dtPharm;
	}
	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}
	public String getDtPois() {
		return dtPois;
	}
	public void setDtPois(String dtPois) {
		this.dtPois = dtPois;
	}
	public String getDtAnti() {
		return dtAnti;
	}
	public void setDtAnti(String dtAnti) {
		this.dtAnti = dtAnti;
	}
	public String getFlagTpn() {
		return flagTpn;
	}
	public void setFlagTpn(String flagTpn) {
		this.flagTpn = flagTpn;
	}
	public List<String> getPkPdList() {
		return pkPdList;
	}
	public void setPkPdList(List<String> pkPdList) {
		this.pkPdList = pkPdList;
	}
}
