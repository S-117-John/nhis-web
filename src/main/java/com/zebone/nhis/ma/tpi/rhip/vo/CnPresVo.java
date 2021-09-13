package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;

public class CnPresVo extends CnPrescription{
    
	private List<TOrderVo> orders;
	
	private String codeEmp;
	
	

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public List<TOrderVo> getOrders() {
		return orders;
	}

	public void setOrders(List<TOrderVo> orders) {
		this.orders = orders;
	}



}
