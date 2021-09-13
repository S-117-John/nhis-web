package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="res")
public class QryPiDeposResVo {
	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
	@XmlElement(name="departmentName")
	private String departmentName;
	
	@XmlElement(name="sickBedNo")
	private String sickBedNo;
	
	@XmlElement(name="foregiftAmount")
	private String foregiftAmount;
	
	@XmlElement(name="todayAmout")
	private String todayAmout;
	
	private String pkPv;
	
	@XmlElement(name="orderDetailInfo")
	private List<QryPiDeposOrderInfo> orderInfos;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getSickBedNo() {
		return sickBedNo;
	}
	public void setSickBedNo(String sickBedNo) {
		this.sickBedNo = sickBedNo;
	}
	public String getForegiftAmount() {
		return foregiftAmount;
	}
	public void setForegiftAmount(String foregiftAmount) {
		this.foregiftAmount = foregiftAmount;
	}
	public String getTodayAmout() {
		return todayAmout;
	}
	public void setTodayAmout(String todayAmout) {
		this.todayAmout = todayAmout;
	}
	public List<QryPiDeposOrderInfo> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(List<QryPiDeposOrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
	
}
