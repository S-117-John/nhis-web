package com.zebone.nhis.ma.pub.zsrm.vo;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 **/
@Component
public class AtfYpxxDetailVo {
    private String pageNo;//摆药单号
    private String orderNo;//医嘱号
    private Date occTime;//服药时间
    private String groupNo;//药房代码
    private String wardSn;//病区编码
    private String wardName;//病区名称
    private String inpatientNo;//住院号
    private String patientId;//病人ID
    private String patientName;//患者姓名
    private String patientType;//患者类型
    private String bedNo;//床号
    private String doctor;//医生
    private String drugCode;//药品编码
    private String drugName;//药品名称
    private String specification;//规格
    private String manufactory;//厂家
    private String expDate;//效期
    private double amount;//服用数量
    private double total;//每日总量
    private String amountUnit;//数量单位
    private double dosage;//剂量
    private String dosUnit;//剂量单位
    private double dosPer;//单次剂量
    private String dosPerUnits;//单位
    private String includeFlag;//是否参加分包
    private String ferquency;//频次
    private String comment1;//服药方法
    private String comment2;//医嘱嘱托
    private String checkResult;//检查结果，超量、重复、违反配伍禁忌等
    private String lot;//批号
    private String procType;//处置方式，例如，已和大夫确认，未做任何处理
    private String printType;//1:正常 0:只打印不包药
    private Date startTime;//服药开始时间
    private Date endTime;//服药结束时间
    private String ParentOrder;//父医嘱order_no
    private String mzFlag;//医嘱类型
    private String atfNo;//分包机编号
    private String codeDept;//科室编码
    private String nameDept;//科室名称
    private String detailSn;//

    public String getDetailSn() {
        return detailSn;
    }

    public void setDetailSn(String detailSn) {
        this.detailSn = detailSn;
    }

    public String getCodeDept() {
        return codeDept;
    }

    public void setCodeDept(String codeDept) {
        this.codeDept = codeDept;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getOccTime() {
        return occTime;
    }

    public void setOccTime(Date occTime) {
        this.occTime = occTime;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getWardSn() {
        return wardSn;
    }

    public void setWardSn(String wardSn) {
        this.wardSn = wardSn;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getInpatientNo() {
        return inpatientNo;
    }

    public void setInpatientNo(String inpatientNo) {
        this.inpatientNo = inpatientNo;
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

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManufactory() {
        return manufactory;
    }

    public void setManufactory(String manufactory) {
        this.manufactory = manufactory;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(double dosage) {
        this.dosage = dosage;
    }

    public String getDosUnit() {
        return dosUnit;
    }

    public void setDosUnit(String dosUnit) {
        this.dosUnit = dosUnit;
    }

    public double getDosPer() {
        return dosPer;
    }

    public void setDosPer(double dosPer) {
        this.dosPer = dosPer;
    }

    public String getDosPerUnits() {
        return dosPerUnits;
    }

    public void setDosPerUnits(String dosPerUnits) {
        this.dosPerUnits = dosPerUnits;
    }

    public String getIncludeFlag() {
        return includeFlag;
    }

    public void setIncludeFlag(String includeFlag) {
        this.includeFlag = includeFlag;
    }

    public String getFerquency() {
        return ferquency;
    }

    public void setFerquency(String ferquency) {
        this.ferquency = ferquency;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getProcType() {
        return procType;
    }

    public void setProcType(String procType) {
        this.procType = procType;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getParentOrder() {
        return ParentOrder;
    }

    public void setParentOrder(String parentOrder) {
        ParentOrder = parentOrder;
    }

    public String getMzFlag() {
        return mzFlag;
    }

    public void setMzFlag(String mzFlag) {
        this.mzFlag = mzFlag;
    }

    public String getAtfNo() {
        return atfNo;
    }

    public void setAtfNo(String atfNo) {
        this.atfNo = atfNo;
    }
}
