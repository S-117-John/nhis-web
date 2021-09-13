package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class ChecklistInfoRes {

	/**
     * 外部预约系统的订单号
     */
    @XmlElement(name = "orderId")
    private String orderId;
    
    /**
     * 交易号
     */
    @XmlElement(name = "payNum")
    private String payNum;
    
    /**
     * 交易金额
     */
    @XmlElement(name = "payAmout")
    private String payAmout;
    
    /**
     * 交易时间，格式：YYYY-MM-DD HI24:MI:SS
     */
    @XmlElement(name = "payTime")
    private String payTime;
    
    /**
     * 交易描述
     */
    @XmlElement(name = "payDesc")
    private String payDesc;
    
    /**
     * 患者身份证件号码
     */
    @XmlElement(name = "userIdCard")
    private String userIdCard;
    
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
     * 科室代码
     */
    @XmlElement(name = "deptId")
    private String deptId;
    
    /**
     * 医生代码
     */
    @XmlElement(name = "doctorId")
    private String doctorId;
    
    /**
     * 预约日期
     */
    @XmlElement(name = "regDate")
    private String regDate;
    
    /**
     * 预约时段
     */
    @XmlElement(name = "timeID")
    private String timeID;
    
    /**
     * 类型：1-支付,2-退费
     */
    @XmlElement(name = "type")
    private String type;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayDesc() {
		return payDesc;
	}

	public void setPayDesc(String payDesc) {
		this.payDesc = payDesc;
	}

	public String getUserIdCard() {
		return userIdCard;
	}

	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getTimeID() {
		return timeID;
	}

	public void setTimeID(String timeID) {
		this.timeID = timeID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
