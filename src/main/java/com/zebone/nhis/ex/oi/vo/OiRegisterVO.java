package com.zebone.nhis.ex.oi.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class OiRegisterVO {
    private OiPatientInfoVo registerInfo = new OiPatientInfoVo();
    private List<CnOrder> orderList = new ArrayList<CnOrder>();
    
	public OiPatientInfoVo getRegisterInfo() {
		return registerInfo;
	}
	public void setRegisterInfo(OiPatientInfoVo registerInfo) {
		this.registerInfo = registerInfo;
	}
	public List<CnOrder> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<CnOrder> orderList) {
		this.orderList = orderList;
	}
    

}
