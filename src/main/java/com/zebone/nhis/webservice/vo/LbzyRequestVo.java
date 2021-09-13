package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbzyRequestVo {
    //业务请求码
    @XmlElement(name="TransCode")
    private String transCode;

    //患者信息
    //卡类型
    @XmlElement(name="CardTypeID")
    private String cardTypeID;
    //卡号
    @XmlElement(name="CardNO")
    private String cardNO;
    //患者ID
    @XmlElement(name="PatientID")
    private String patientID;
    //住院号
    @XmlElement(name="InPatientID")
    private String inPatientID;
    //患者姓名
    @XmlElement(name="PatientName")
    private String patientName;
    //患者性别	见标准字典-性别代码
    @XmlElement(name="PatientSexID")
    private String patientSexID;
    //出生日期	YYYY-MM-DD
    @XmlElement(name="Birthday")
    private String birthday;
    //身份证号	对已有诊疗卡号患者可能通过身份证号自动、关联旧的门诊号
    @XmlElement(name="IDCardNO")
    private String iDCardNO;
    //地址
    @XmlElement(name="Address")
    private String address;
    //手机号码
    @XmlElement(name="Mobile")
    private String mobile;
    //金额	预交金金额
    @XmlElement(name="Amount")
    private String amount;

    //挂号类别代码
    @XmlElement(name="RegTypeID")
    private String regTypeID;
    //日期	挂号日期YYYY-MM-DD
    @XmlElement(name="Date")
    private String date;
    //科室编号
    @XmlElement(name="DepartmentID")
    private String departmentID;
    //科室编号
    @XmlElement(name="DeptID")
    private String deptID;

    @XmlElementWrapper(name = "List")
    @XmlElement(name = "Item")
    private List<LbzyRequItemVo> itemVos;
    //自助设备编码
    @XmlElement(name="UserID")
    private String userID;

    @XmlElement(name="BillDate")
    private String billDate;

    //挂号单据号
    @XmlElement(name="TotalCost")
    private String regFlow;
    //排班编码	HIS系统的排班主键
    @XmlElement(name="ScheduleCode")
    private String scheduleCode;

    //开始日期	YYYY-MM-DD
    @XmlElement(name="StartDate")
    private String startDate;
    //结束日期	YYYY-MM-DD
    @XmlElement(name="EndDate")
    private String endDate;

    //时段编码
    @XmlElement(name="ScheduleItemCode")
    private String scheduleItemCode;
    
    
    public String getScheduleItemCode() {
		return scheduleItemCode;
	}

	public void setScheduleItemCode(String scheduleItemCode) {
		this.scheduleItemCode = scheduleItemCode;
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

    public String getCardTypeID() {
        return cardTypeID;
    }

    public void setCardTypeID(String cardTypeID) {
        this.cardTypeID = cardTypeID;
    }

    public String getCardNO() {
        return cardNO;
    }

    public void setCardNO(String cardNO) {
        this.cardNO = cardNO;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getInPatientID() {
        return inPatientID;
    }

    public void setInPatientID(String inPatientID) {
        this.inPatientID = inPatientID;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getiDCardNO() {
        return iDCardNO;
    }

    public void setiDCardNO(String iDCardNO) {
        this.iDCardNO = iDCardNO;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRegTypeID() {
        return regTypeID;
    }

    public void setRegTypeID(String regTypeID) {
        this.regTypeID = regTypeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public List<LbzyRequItemVo> getItemVos() {
        return itemVos;
    }

    public void setItemVos(List<LbzyRequItemVo> itemVos) {
        this.itemVos = itemVos;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getRegFlow() {
        return regFlow;
    }

    public void setRegFlow(String regFlow) {
        this.regFlow = regFlow;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
