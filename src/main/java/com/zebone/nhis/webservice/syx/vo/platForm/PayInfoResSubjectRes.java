package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PayInfoResSubjectRes {

	@XmlElement(name="resultCode")
	private String resultCode;
	
	@XmlElement(name="resultDesc")
	private String resultDesc;
	
    @XmlElement(name="payListInfo")
    private List<PayInfoRes> payListInfo;
    
    @XmlElement(name="userName")
    private String userName;
    
    @XmlElement(name="canPay")
    private String canPay;
    
    

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

	public String getCanPay() {
		return canPay;
	}

	public void setCanPay(String canPay) {
		this.canPay = canPay;
	}

	public List<PayInfoRes> getPayListInfo() {
		return payListInfo;
	}

	public void setPayListInfo(List<PayInfoRes> payListInfo) {
		this.payListInfo = payListInfo;
	}

	
    
}
