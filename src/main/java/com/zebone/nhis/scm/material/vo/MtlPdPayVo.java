package com.zebone.nhis.scm.material.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdPay;

public class MtlPdPayVo extends PdPay {
	private String supplyerName;
	private String bankName;
	private String paymodeName;
	private List<MtlPdStDtVo> dtList;
	
	public String getSupplyerName() {
		return supplyerName;
	}
	public void setSupplyerName(String supplyerName) {
		this.supplyerName = supplyerName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPaymodeName() {
		return paymodeName;
	}
	public void setPaymodeName(String paymodeName) {
		this.paymodeName = paymodeName;
	}
	public List<MtlPdStDtVo> getDtList() {
		return dtList;
	}
	public void setDtList(List<MtlPdStDtVo> dtList) {
		this.dtList = dtList;
	}


}
