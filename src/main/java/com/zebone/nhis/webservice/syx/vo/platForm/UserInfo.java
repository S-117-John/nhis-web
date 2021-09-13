package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfo {
	/**
	 * 用户证件类型
	 */
	@XmlElement(name = "userCardType")
	private String userCardType;
	/**
	 * 用户证件号码
	 */
	@XmlElement(name = "userCardId")
	private String userCardId;
	/**
	 * 患者身份证号码
	 */
	@XmlElement(name = "userIdCard")
	private String userIdCard;
	/**
	 * 患者姓名
	 */
	@XmlElement(name = "userName")
	private String userName;
	/**
	 * 用户对应HIS系统患者ID
	 */
	@XmlElement(name = "userHisPatientId")
	private String userHisPatientId;
	/**
	 * 患者诊疗卡号码
	 */
	@XmlElement(name = "userJKK")
	private String userJKK;
	/**
	 * 患者市民卡号码
	 */
	@XmlElement(name = "userSMK")
	private String userSMK;
	/**
	 * 患者医保卡号码
	 */
	@XmlElement(name = "userYBK")
	private String userYBK;
	/**
	 * 患者监护人身份证件号码
	 */
	@XmlElement(name = "userParentIdCard")
	private String userParentIdCard;
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
	 * 用户地址
	 */
	@XmlElement(name = "userAddress")
	private String userAddress;
	/**
	 * 用户的患者类型名称
	 */
	@XmlElement(name = "userPatienTypeListName")
	private String userPatienTypeListName;
	
	
	public String getUserCardType() {
		return userCardType;
	}
	public void setUserCardType(String userCardType) {
		this.userCardType = userCardType;
	}
	public String getUserCardId() {
		return userCardId;
	}
	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}
	public String getUserIdCard() {
		return userIdCard;
	}
	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserHisPatientId() {
		return userHisPatientId;
	}
	public void setUserHisPatientId(String userHisPatientId) {
		this.userHisPatientId = userHisPatientId;
	}
	public String getUserJKK() {
		return userJKK;
	}
	public void setUserJKK(String userJKK) {
		this.userJKK = userJKK;
	}
	public String getUserSMK() {
		return userSMK;
	}
	public void setUserSMK(String userSMK) {
		this.userSMK = userSMK;
	}
	public String getUserYBK() {
		return userYBK;
	}
	public void setUserYBK(String userYBK) {
		this.userYBK = userYBK;
	}
	public String getUserParentIdCard() {
		return userParentIdCard;
	}
	public void setUserParentIdCard(String userParentIdCard) {
		this.userParentIdCard = userParentIdCard;
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
	public String getUserPatienTypeListName() {
		return userPatienTypeListName;
	}
	public void setUserPatienTypeListName(String userPatienTypeListName) {
		this.userPatienTypeListName = userPatienTypeListName;
	}	
}
