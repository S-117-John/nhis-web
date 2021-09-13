package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

public class SyxCpTempFormPhaseVo {

   private String pkCpphase;
   private String codePhase;
   private String title;
   private String subTitle;
   private String flagExp;
   private String expReason;
   
   private String nameCp;
   private Integer daysMin;
   private Integer daysMax;
   private String codeIcd;
   private String nameIcd;
   private String namePhase;
   private String euTasktype;
   private String euAlways;
   private Integer sortno;
   private String nameForm;
   
   private List<SyxCpTempFormItem> tabOrds;
   
   private List<SyxCpTempFormItem> clinics ;
   private List<SyxCpTempFormItem> orders ;
   private List<SyxCpTempFormItem> nursings ;
   
   
   
	public String getPkCpphase() {
	   return pkCpphase;
    }
    public void setPkCpphase(String pkCpphase) {
	    this.pkCpphase = pkCpphase;
    }
	public String getNameCp() {
		return nameCp;
	}
	public void setNameCp(String nameCp) {
		this.nameCp = nameCp;
	}
	public Integer getDaysMin() {
		return daysMin;
	}
	public void setDaysMin(Integer daysMin) {
		this.daysMin = daysMin;
	}
	public Integer getDaysMax() {
		return daysMax;
	}
	public void setDaysMax(Integer daysMax) {
		this.daysMax = daysMax;
	}
	public String getCodeIcd() {
		return codeIcd;
	}
	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}
	public String getNameIcd() {
		return nameIcd;
	}
	public void setNameIcd(String nameIcd) {
		this.nameIcd = nameIcd;
	}
	public String getNamePhase() {
		return namePhase;
	}
	public void setNamePhase(String namePhase) {
		this.namePhase = namePhase;
	}
	public String getEuTasktype() {
		return euTasktype;
	}
	public void setEuTasktype(String euTasktype) {
		this.euTasktype = euTasktype;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public Integer getSortno() {
		return sortno;
	}
	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	public String getNameForm() {
		return nameForm;
	}
	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}
	public String getCodePhase() {
		return codePhase;
	}
	public void setCodePhase(String codePhase) {
		this.codePhase = codePhase;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getFlagExp() {
		return flagExp;
	}
	public void setFlagExp(String flagExp) {
		this.flagExp = flagExp;
	}
	public String getExpReason() {
		return expReason;
	}
	public void setExpReason(String expReason) {
		this.expReason = expReason;
	}
	public List<SyxCpTempFormItem> getClinics() {
		return clinics;
	}
	public void setClinics(List<SyxCpTempFormItem> clinics) {
		this.clinics = clinics;
	}
	public List<SyxCpTempFormItem> getOrders() {
		return orders;
	}
	public void setOrders(List<SyxCpTempFormItem> orders) {
		this.orders = orders;
	}
	public List<SyxCpTempFormItem> getNursings() {
		return nursings;
	}
	public void setNursings(List<SyxCpTempFormItem> nursings) {
		this.nursings = nursings;
	}
	public List<SyxCpTempFormItem> getTabOrds() {
		return tabOrds;
	}
	public void setTabOrds(List<SyxCpTempFormItem> tabOrds) {
		this.tabOrds = tabOrds;
	}	
}
