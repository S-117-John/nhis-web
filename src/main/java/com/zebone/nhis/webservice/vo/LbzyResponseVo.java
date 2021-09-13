package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbzyResponseVo {
    //业务请求码
    @XmlElement(name="TransCode")
    private String transCode;
    //交易结果
    @XmlElement(name="ResultCode")
    private String resultCode;
    //错误信息
    @XmlElement(name="ErrorMsg")
    private String errorMsg;
    //His系统时间
    @XmlElement(name="HISDateTime")
    private String hISDateTime;

    //患者基本信息
    //患者id
    @XmlElement(name="PatientID")
    private String PatientID;
    //患者姓名
    @XmlElement(name="PatientName")
    private String patientName;
    //患者性别
    @XmlElement(name="PatientSexID")
    private String patientSexID;
    //身份证号
    @XmlElement(name="IDCardNO")
    private String iDCardNO;
    //出生日期
    @XmlElement(name="Birthday")
    private String birthday;
    //患者年龄
    @XmlElement(name="PatientAge")
    private String patientAge;
    //卡状态	卡状态：0正常 ，1挂失 ，2注销 ，3未注册
    @XmlElement(name="CardStatus")
    private String cardStatus;
    //手机号码
    @XmlElement(name="Mobile")
    private String mobile;
    //诊疗卡号
    @XmlElement(name="CardNO")
    private String cardNO;

    //锁号响应
    //排班编码	HIS系统的排班主键
    @XmlElement(name="ScheduleCode")
    private String scheduleCode;
    //锁定的挂号序号
    @XmlElement(name="LockQueueNo")
    private String lockQueueNo;

    //挂号响应
    //交易流水号
    @XmlElement(name="TranSerialNO")
    private String tranSerialNO;
    //挂号单据号	挂号退号时使用
    @XmlElement(name="RegFlow")
    private String regFlow;
    //医生编码
    @XmlElement(name="DoctorID")
    private String doctorID;
    //就诊时间
    @XmlElement(name="SeeTime")
    private String seeTime;
    //就诊序号
    @XmlElement(name="SeeNO")
    private String seeNO;
    //挂号种类
    @XmlElement(name="RegType")
    private String regType;
    //患者姓名
    //@XmlElement(name="PatientName")
    //private String patientName;
    //患者ID
    //@XmlElement(name="PatientID")
    //private String PatientID;
    //科室名称	就诊科室
    @XmlElement(name="DepartmentName")
    private String departmentName;
    //科室位置	就诊位置
    @XmlElement(name="DepartmentAddress")
    private String departmentAddress;
    //医生姓名
    @XmlElement(name="DoctorName")
    private String doctorName;

    //预交金充值
    //医院交易流水号	充值成功与否的凭证
    //@XmlElement(name="TranSerialNO")
    //private String tranSerialNO;
    //住院余额
    @XmlElement(name="InBalance")
    private String inBalance;

    //对账数据
    //账单日期
    @XmlElement(name="BillDate")
    private String billDate;
    @XmlElementWrapper(name = "List")
    @XmlElement(name = "Item")
    private List<LbzyResponItemVo> itemVos;

    public String getLockQueueNo() {
        return lockQueueNo;
    }

    public void setLockQueueNo(String lockQueueNo) {
        this.lockQueueNo = lockQueueNo;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public List<LbzyResponItemVo> getItemVos() {
        return itemVos;
    }

    public void setItemVos(List<LbzyResponItemVo> itemVos) {
        this.itemVos = itemVos;
    }

    public String gethISDateTime() {
        return hISDateTime;
    }

    public void sethISDateTime(String hISDateTime) {
        this.hISDateTime = hISDateTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSexID() {
        return patientSexID;
    }

    public void setPatientSexID(String patientSexID) {
        this.patientSexID = patientSexID;
    }

    public String getiDCardNO() {
        return iDCardNO;
    }

    public void setiDCardNO(String iDCardNO) {
        this.iDCardNO = iDCardNO;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getTranSerialNO() {
        return tranSerialNO;
    }

    public void setTranSerialNO(String tranSerialNO) {
        this.tranSerialNO = tranSerialNO;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getSeeTime() {
        return seeTime;
    }

    public void setSeeTime(String seeTime) {
        this.seeTime = seeTime;
    }

    public String getSeeNO() {
        return seeNO;
    }

    public void setSeeNO(String seeNO) {
        this.seeNO = seeNO;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentAddress() {
        return departmentAddress;
    }

    public void setDepartmentAddress(String departmentAddress) {
        this.departmentAddress = departmentAddress;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getInBalance() {
        return inBalance;
    }

    public void setInBalance(String inBalance) {
        this.inBalance = inBalance;
    }
}
