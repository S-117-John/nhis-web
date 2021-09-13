package com.zebone.nhis.emr.mgr.vo;

import java.util.List;

import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiagsIcd;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOpsIcd;

public class SaveEmrHomePageParam {

	private List<EmrHomePageDiagsIcd> diagsList;

	private List<EmrHomePageOpsIcd> opsList;

	private List<EmrHomePageFeedBackIcd> feedList;
	private String pkPv;

	/**
	 * 诊断符合-门诊与住院
	 */
	private String diagFitCodeOi;

	/**
	 * 诊断符合-临床与病理
	 */
	private String diagFitCodeCp;

	/**
	 * 门急诊诊断编码-病案编码
	 */
	private String diagCodeClinicIcd;

	/**
	 * 门急诊诊断名称-病案编码
	 */
	private String diagNameClinicIcd;

	/**
	 * 门急诊诊断编码-病案编码
	 */
	private String diagCodeExtcIpIcd;

	/**
	 * 门急诊诊断名称-病案编码
	 */
	private String diagNameExtcIpIcd;

	/**
	 * 病理诊断编码-病案编码
	 */
	private String diagCodePathoIcd;

	/**
	 * 病理诊断名称-病案编码
	 */
	private String diagNamePathoIcd;

	/**
	 * 病理号-病案编码
	 */
	private String pathoNoIcd;
	
	 /**
     * 药物过敏标志-病案编码
     */
    private String flagDrugAllergyIcd;
    /**
     * 过敏药物-病案编码
     */
    private String allergicDrugIcd;
    /**
     * 部分病种
     */
    private String partDisease;
    /**
     * 病人来源
     */
    private String patSource;
    /**
     * 现住址
     */
    private String currAddrIcd;
    /**
     * 超时标志
     */
    private String flagTimeoutCode;
    
    
	public String getFlagTimeoutCode() {
		return flagTimeoutCode;
	}

	public void setFlagTimeoutCode(String flagTimeoutCode) {
		this.flagTimeoutCode = flagTimeoutCode;
	}

	public String getCurrAddrIcd() {
		return currAddrIcd;
	}

	public void setCurrAddrIcd(String currAddrIcd) {
		this.currAddrIcd = currAddrIcd;
	}

	public String getFlagDrugAllergyIcd() {
		return flagDrugAllergyIcd;
	}

	public void setFlagDrugAllergyIcd(String flagDrugAllergyIcd) {
		this.flagDrugAllergyIcd = flagDrugAllergyIcd;
	}

	public String getAllergicDrugIcd() {
		return allergicDrugIcd;
	}

	public void setAllergicDrugIcd(String allergicDrugIcd) {
		this.allergicDrugIcd = allergicDrugIcd;
	}

	public String getPartDisease() {
		return partDisease;
	}

	public void setPartDisease(String partDisease) {
		this.partDisease = partDisease;
	}

	public String getPatSource() {
		return patSource;
	}

	public void setPatSource(String patSource) {
		this.patSource = patSource;
	}

	public List<EmrHomePageDiagsIcd> getDiagsList() {
		return diagsList;
	}

	public void setDiagsList(List<EmrHomePageDiagsIcd> diagsList) {
		this.diagsList = diagsList;
	}

	public List<EmrHomePageOpsIcd> getOpsList() {
		return opsList;
	}

	public void setOpsList(List<EmrHomePageOpsIcd> opsList) {
		this.opsList = opsList;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getDiagFitCodeOi() {
		return diagFitCodeOi;
	}

	public void setDiagFitCodeOi(String diagFitCodeOi) {
		this.diagFitCodeOi = diagFitCodeOi;
	}

	public String getDiagFitCodeCp() {
		return diagFitCodeCp;
	}

	public void setDiagFitCodeCp(String diagFitCodeCp) {
		this.diagFitCodeCp = diagFitCodeCp;
	}

	public String getDiagCodeClinicIcd() {
		return diagCodeClinicIcd;
	}

	public void setDiagCodeClinicIcd(String diagCodeClinicIcd) {
		this.diagCodeClinicIcd = diagCodeClinicIcd;
	}

	public String getDiagNameClinicIcd() {
		return diagNameClinicIcd;
	}

	public void setDiagNameClinicIcd(String diagNameClinicIcd) {
		this.diagNameClinicIcd = diagNameClinicIcd;
	}

	public String getDiagCodeExtcIpIcd() {
		return diagCodeExtcIpIcd;
	}

	public void setDiagCodeExtcIpIcd(String diagCodeExtcIpIcd) {
		this.diagCodeExtcIpIcd = diagCodeExtcIpIcd;
	}

	public String getDiagNameExtcIpIcd() {
		return diagNameExtcIpIcd;
	}

	public void setDiagNameExtcIpIcd(String diagNameExtcIpIcd) {
		this.diagNameExtcIpIcd = diagNameExtcIpIcd;
	}

	public String getDiagCodePathoIcd() {
		return diagCodePathoIcd;
	}

	public void setDiagCodePathoIcd(String diagCodePathoIcd) {
		this.diagCodePathoIcd = diagCodePathoIcd;
	}

	public String getDiagNamePathoIcd() {
		return diagNamePathoIcd;
	}

	public void setDiagNamePathoIcd(String diagNamePathoIcd) {
		this.diagNamePathoIcd = diagNamePathoIcd;
	}

	public String getPathoNoIcd() {
		return pathoNoIcd;
	}

	public void setPathoNoIcd(String pathoNoIcd) {
		this.pathoNoIcd = pathoNoIcd;
	}

	public List<EmrHomePageFeedBackIcd> getFeedList() {
		return feedList;
	}

	public void setFeedList(List<EmrHomePageFeedBackIcd> feedList) {
		this.feedList = feedList;
	}
	
	
}
