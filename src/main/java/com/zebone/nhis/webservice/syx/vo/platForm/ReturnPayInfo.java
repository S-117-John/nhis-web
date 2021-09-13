package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ReturnPayInfo {
	/**
	 * 预约来源
	 */
	@XmlElement(name = "orderType")
	private String orderType;
	
	/**
	 * 外部预约系统的订单号
	 */
	@XmlElement(name = "orderId")
	private String orderId;
	
	/**
	 * 退费金额
	 */
	@XmlElement(name = "returnAmout")
	private String returnAmout;
	
	/**
	 * 处理结果代码
	 */
	@XmlElement(name = "resultCode")
	private String resultCode;
	
	/**
	 * 处理结果描述
	 */
	@XmlElement(name = "resultDesc")
	private String resultDesc;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReturnAmout() {
		return returnAmout;
	}

	public void setReturnAmout(String returnAmout) {
		this.returnAmout = returnAmout;
	}

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

}
