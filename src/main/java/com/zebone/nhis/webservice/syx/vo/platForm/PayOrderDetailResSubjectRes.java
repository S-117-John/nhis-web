package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PayOrderDetailResSubjectRes {

	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
    @XmlElement(name="orderDetailInfo")
    private List<PayOrderDetailRes> orderDetailInfo;
    
    @XmlElement(name="userName")
    private String userName;
    
    @XmlElement(name="payAmout")
    private String payAmout;
    
    

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<PayOrderDetailRes> getOrderDetailInfo() {
		return orderDetailInfo;
	}

	public void setOrderDetailInfo(List<PayOrderDetailRes> orderDetailInfo) {
		this.orderDetailInfo = orderDetailInfo;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	
}
