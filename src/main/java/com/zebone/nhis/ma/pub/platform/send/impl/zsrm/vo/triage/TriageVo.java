package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.triage;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

public class TriageVo extends PhResource {

    private String idNo;
    /** 就诊卡号*/
    private String cardNo;
    /** 病人ID*/
    private String patientId;
    private String patientName;
    private String sex;
    /**就诊卡类型 医保卡,自费卡等*/
    private String cardType;
    private String phoneNumber;
    /**就诊ID	HIS门诊就诊唯一号*/
    private String registerId;
    private String registerTime;
    private String deptCode;
    private String deptName;
    private String doctorCode;
    private String doctorName;
    /**号别名称*/
    private String markDesc;
    /**门诊类型	普通,专家*/
    private String clinicType;
    /**操作时间	时间触发时间*/
    private String operationTime;
    /**操作类型	分诊签到*/
    private String operationType;
    //对方没有给 队列号码字段，暂时站位
    private String queNo;

    //叫号，过号，重呼，签到
    /**叫号号码*/
    private String currentCallNum;
    /**医生电脑IP地址*/
    private String ipAdress;

    public String getQueNo() {
        return queNo;
    }

    public void setQueNo(String queNo) {
        this.queNo = queNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMarkDesc() {
        return markDesc;
    }

    public void setMarkDesc(String markDesc) {
        this.markDesc = markDesc;
    }

    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getCurrentCallNum() {
        return currentCallNum;
    }

    public void setCurrentCallNum(String currentCallNum) {
        this.currentCallNum = currentCallNum;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }
}
