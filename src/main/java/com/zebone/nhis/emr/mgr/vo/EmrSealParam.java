package com.zebone.nhis.emr.mgr.vo;

import java.util.Date;

/**
 * 病历封存信息
 * @author yuanxinan
 *
 */
public class EmrSealParam {
	private String pkPv;//就诊ID
	private String pkPi;//病人ID
	private String id;//病历封存记录主ID
	private String pkEmpSeal;//封存人
	private String euStatus;//封存状态，默认1
	private String pkSealrec;//封存记录ID
	private String pkSealitem;//病历封存记录明细ID
	private Date dateApply;//申请，处理，归档时间
	private String pkRec;//文档ID
	private String subEustatus;//明细处理状态，直接默认为4
	private String contentSeal;//封存原因
	
	private String dateSeal;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPkEmpSeal() {
		return pkEmpSeal;
	}
	public void setPkEmpSeal(String pkEmpSeal) {
		this.pkEmpSeal = pkEmpSeal;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getPkSealrec() {
		return pkSealrec;
	}
	public void setPkSealrec(String pkSealrec) {
		this.pkSealrec = pkSealrec;
	}
	public String getPkSealitem() {
		return pkSealitem;
	}
	public void setPkSealitem(String pkSealitem) {
		this.pkSealitem = pkSealitem;
	}
	public Date getDateApply() {
		return dateApply;
	}
	public void setDateApply(Date dateApply) {
		this.dateApply = dateApply;
	}
	public String getPkRec() {
		return pkRec;
	}
	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}
	public String getSubEustatus() {
		return subEustatus;
	}
	public void setSubEustatus(String subEustatus) {
		this.subEustatus = subEustatus;
	}
	public String getContentSeal() {
		return contentSeal;
	}
	public void setContentSeal(String contentSeal) {
		this.contentSeal = contentSeal;
	}
	public String getDateSeal() {
		return dateSeal;
	}
	public void setDateSeal(String dateSeal) {
		this.dateSeal = dateSeal;
	}
	
}
