package com.zebone.nhis.webservice.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.platform.modules.dao.build.au.Field;
/*
 * 预约详情
 */
@XmlRootElement(name = "schAppt")
@XmlAccessorType(XmlAccessType.FIELD)
public class PskqSchApptVo {
	/**
	 * 预约编码
	 */
	@XmlElement(name = "code")
	private String code;
	
	/**
	 * 患者姓名
	 */
	@XmlElement(name = "namePi")
	private String namePi;
	/**
	 * 身份证号
	 */
	@XmlElement(name = "idNo")
	private String idNo;
	/**
	 * 手机号
	 */
	@XmlElement(name = "mobile")
	private String mobile;
	/**
	 * 门诊号
	 */
	@XmlElement(name = "codeOp")
	private String codeOp;
	/**
	 * 科室
	 */
	@XmlElement(name = "nameDept")
	private String nameDept;
	/**
	 * 医生名
	 */
	@XmlElement(name = "nameEmpPhy")
	private String nameEmpPhy;
	/**
	 * 预约时间
	 */
	@XmlElement(name = "dateAppt")
	private String dateAppt;
	/**
	 * 预约序号
	 */
	@XmlElement(name = "ticketNo")
	private String ticketNo;
	/**
	 * 状态
	 */
	@XmlElement(name = "euStatus")
	private String euStatus;
	/**
	 * 是否支付
	 */
	@XmlElement(name = "flagPay")
	private String flagPay;
	/**
	 * 预约渠道
	 */
	@XmlElement(name = "appttype")
	private String appttype;
	/**
	 * 登记人
	 */
	@XmlElement(name = "nameEmpReg")
	private String nameEmpReg;
	/**
	 * 登记时间
	 */
	@XmlElement(name = "dateReg")
	private String dateReg;
	/**
	 * 取消人
	 */
	@XmlElement(name = "nameEmpCancel")
	private String nameEmpCancel;
	/**
	 * 取消时间
	 */
	@XmlElement(name = "dateCancel")
	private String dateCancel;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getDateAppt() {
		return dateAppt;
	}
	public void setDateAppt(String dateAppt) {
		this.dateAppt = dateAppt;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getFlagPay() {
		return flagPay;
	}
	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}
	public String getAppttype() {
		return appttype;
	}
	public void setAppttype(String appttype) {
		this.appttype = appttype;
	}
	public String getNameEmpReg() {
		return nameEmpReg;
	}
	public void setNameEmpReg(String nameEmpReg) {
		this.nameEmpReg = nameEmpReg;
	}
	public String getDateReg() {
		return dateReg;
	}
	public void setDateReg(String dateReg) {
		this.dateReg = dateReg;
	}
	public String getNameEmpCancel() {
		return nameEmpCancel;
	}
	public void setNameEmpCancel(String nameEmpCancel) {
		this.nameEmpCancel = nameEmpCancel;
	}
	public String getDateCancel() {
		return dateCancel;
	}
	public void setDateCancel(String dateCancel) {
		this.dateCancel = dateCancel;
	}
	
	
}
