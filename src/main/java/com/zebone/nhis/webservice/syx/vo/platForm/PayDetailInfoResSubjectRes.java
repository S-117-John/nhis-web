package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PayDetailInfoResSubjectRes {

	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
    @XmlElement(name="orderDetailInfo")
    private List<PayDetailInfoRes> orderDetailInfo;
    
    @XmlElement(name="infoSeq")
    private String infoSeq;
    
    @XmlElement(name="payAmout")
    private String payAmout;
    
    @XmlElement(name="selfPayAmout")
    private String selfPayAmout;
    
    @XmlElement(name="accountAmount")
    private String accountAmount;
    
    @XmlElement(name="userName")
    private String userName;

    
    
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

	public List<PayDetailInfoRes> getOrderDetailInfo() {
		return orderDetailInfo;
	}

	public void setOrderDetailInfo(List<PayDetailInfoRes> orderDetailInfo) {
		this.orderDetailInfo = orderDetailInfo;
	}

	public String getInfoSeq() {
		return infoSeq;
	}

	public void setInfoSeq(String infoSeq) {
		this.infoSeq = infoSeq;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	public String getSelfPayAmout() {
		return selfPayAmout;
	}

	public void setSelfPayAmout(String selfPayAmout) {
		this.selfPayAmout = selfPayAmout;
	}

	public String getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}
    
   
}
