package com.zebone.nhis.pv.pub.vo;

import com.zebone.nhis.common.module.pv.PvEncounter;

import java.util.Date;

/**
 * 办理出院参数类
 * 
 * @author wangpeng
 * @date 2016年10月15日
 *
 */
public class AdtOutParam {
	
	/** 就诊主键 */
	private String pkPv;
	
	/** 出院时间 */
	private Date dateEnd;
	
	/** 病情转归 */
	private String dtOutcomes;
	
	/** 出院方式 */
	private String dtOuttype;
	
	/** 床位 */
	private String bedNo;
	
	/** 病区 */
	private String pkDeptNs;
	
	/** 患者主键 */
	private String pkPi;
	
	/** 患者主键 */
	private String dtSttypeIns;
	
	/** 是否需要校验：0-不需要，1-需要；默认1 */
	private String flagCheck = "1";
	
	/** 项目调用：0-原版，1-孙逸仙；默认0 */
	private String itemIndex = "0";
	
	/** 婴儿标志：0/1 =1时，则不校验出院诊断 */
	private String flagInfant;

	/**床位主键*/
	private String pkBed;

	/**婴儿母亲信息*/
	private PvEncounter pvMomInfo;
	
	/**博爱出院做不符合医保规范的护理费用校验*/
	private String flagNsCgChk;

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDtOutcomes() {
		return dtOutcomes;
	}

	public void setDtOutcomes(String dtOutcomes) {
		this.dtOutcomes = dtOutcomes;
	}

	public String getDtOuttype() {
		return dtOuttype;
	}

	public void setDtOuttype(String dtOuttype) {
		this.dtOuttype = dtOuttype;
	}

	public String getBedNo() {
		return bedNo;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getFlagCheck() {
		return flagCheck;
	}

	public void setFlagCheck(String flagCheck) {
		this.flagCheck = flagCheck;
	}

	public String getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}

	public String getDtSttypeIns() {
		return dtSttypeIns;
	}

	public void setDtSttypeIns(String dtSttypeIns) {
		this.dtSttypeIns = dtSttypeIns;
	}

	public String getFlagInfant() {
		return flagInfant;
	}

	public void setFlagInfant(String flagInfant) {
		this.flagInfant = flagInfant;
	}

	public String getPkBed() {
		return pkBed;
	}

	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}

	public PvEncounter getPvMomInfo() {
		return pvMomInfo;
	}

	public void setPvMomInfo(PvEncounter pvMomInfo) {
		this.pvMomInfo = pvMomInfo;
	}
}
