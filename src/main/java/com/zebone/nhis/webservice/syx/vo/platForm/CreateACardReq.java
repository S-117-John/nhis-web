package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateACardReq {

	/**
     * 预约来源
     */
    @XmlElement(name = "orderType")
    private String orderType;
    
    /**
     * 患者身份证件号码
     */
    @XmlElement(name = "userCardId")
    private String userCardId;
    
    /**
     * 患者姓名
     */
    @XmlElement(name = "userName")
    private String userName;
    
    /**
     * 患者性别：M-男性 F-女性
     */
    @XmlElement(name = "userGender")
    private String userGender;
    
    /**
     * 患者电话
     */
    @XmlElement(name = "userMobile")
    private String userMobile;
    
    /**
     * 患者出生日期：YYYY-MM-DD
     */
    @XmlElement(name = "userBirthday")
    private String userBirthday;
    
    /**
     * 患者住址
     */
    @XmlElement(name = "userAddress")
    private String userAddress;
    
    /**
     * 医院没有分院，则值为空字符串；医院有分院，则值不允许为空字符串
     */
    @XmlElement(name = "branchCode")
    private String branchCode;
    
    /**
     * 有效期开始时间
     */
    @XmlElement(name = "beginDate")
    private String beginDate;
    
    /**
     * 有效期结束时间
     */
    @XmlElement(name = "endDate")
    private String endDate;

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	

	
}
