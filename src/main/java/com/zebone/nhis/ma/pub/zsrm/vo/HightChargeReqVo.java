package com.zebone.nhis.ma.pub.zsrm.vo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 高值耗材记退费接口请求
 */
public class HightChargeReqVo {

    @JsonProperty("ID")
    private String Id;

    @JsonProperty("RECEIVE_ID")
    private String receiveId;

    @JsonProperty("CHARGE_DATE")
    private String chargeDate;

    @JsonProperty("HOSPITAL_NO")
    private String hospitalNo;

    @JsonProperty("IN_HOSPITAL_NO")
    private String InHospitalNo;

    @JsonProperty("ADVICE_NO")
    private String adviceNo;

    @JsonProperty("CHARGE_ITEM_CODE")
    private String chargeItemCode;

    @JsonProperty("CHARGE_ITEM_NAME")
    private String chargeItemName;

    @JsonProperty("CHARGE_ITEM_UNIT")
    private String chargeItemUnit;

    @JsonProperty("CHARGE_ITEM_SPEC")
    private String chargeItemSpec;

    @JsonProperty("CHARGE_KIND_CODE")
    private String chargeKindCode;

    @JsonProperty("CHARGE_KIND_NAME")
    private String chargeKindName;

    @JsonProperty("BAR_CODE")
    private String barCode;

    @JsonProperty("ADMISS_DEPT_CODE")
    private String admissDeptCode;

    @JsonProperty("ADMISS_DEPT_NAME")
    private String admissDeptName;

    @JsonProperty("ORDER_DEPT_CODE")
    private String orderDeptCode;

    @JsonProperty("ORDER_DEPT_NAME")
    private String orderDeptName;

    @JsonProperty("EXEC_DEPT_CODE")
    private String execDeptCode;

    @JsonProperty("EXEC_DEPT_NAME")
    private String execDeptName;

    @JsonProperty("DOC_CODE")
    private String docCode;

    @JsonProperty("DOC_NAME")
    private String docName;

    @JsonProperty("ADMISS_DIAG")
    private String admissDiag;

    @JsonProperty("PATIENT_ID")
    private String patientId;

    @JsonProperty("PATIENT_NAME")
    private String patientName;

    @JsonProperty("PATIENT_DATE")
    private String patientDate;

    @JsonProperty("PATIENT_AGE")
    private String patientAge;

    @JsonProperty("PATIENT_TYPE")
    private String patientType;

    @JsonProperty("CHARGE_NUM")
    private String chargeNum;

    @JsonProperty("CHARGE_PRICE")
    private String chagePrice;

    @JsonProperty("CHARGE_MONEY")
    private String chargeMoney;

    @JsonProperty("OPERATE_ID")
    private String operateId;

    @JsonProperty("OPERATE_NAME")
    private String operateName;

    @JsonProperty("BUSINESS_DATE")
    private String businessDate;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getHospitalNo() {
        return hospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public String getInHospitalNo() {
        return InHospitalNo;
    }

    public void setInHospitalNo(String inHospitalNo) {
        InHospitalNo = inHospitalNo;
    }

    public String getAdviceNo() {
        return adviceNo;
    }

    public void setAdviceNo(String adviceNo) {
        this.adviceNo = adviceNo;
    }

    public String getChargeItemCode() {
        return chargeItemCode;
    }

    public void setChargeItemCode(String chargeItemCode) {
        this.chargeItemCode = chargeItemCode;
    }

    public String getChargeItemName() {
        return chargeItemName;
    }

    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    public String getChargeItemUnit() {
        return chargeItemUnit;
    }

    public void setChargeItemUnit(String chargeItemUnit) {
        this.chargeItemUnit = chargeItemUnit;
    }

    public String getChargeItemSpec() {
        return chargeItemSpec;
    }

    public void setChargeItemSpec(String chargeItemSpec) {
        this.chargeItemSpec = chargeItemSpec;
    }

    public String getChargeKindCode() {
        return chargeKindCode;
    }

    public void setChargeKindCode(String chargeKindCode) {
        this.chargeKindCode = chargeKindCode;
    }

    public String getChargeKindName() {
        return chargeKindName;
    }

    public void setChargeKindName(String chargeKindName) {
        this.chargeKindName = chargeKindName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getAdmissDeptCode() {
        return admissDeptCode;
    }

    public void setAdmissDeptCode(String admissDeptCode) {
        this.admissDeptCode = admissDeptCode;
    }

    public String getAdmissDeptName() {
        return admissDeptName;
    }

    public void setAdmissDeptName(String admissDeptName) {
        this.admissDeptName = admissDeptName;
    }

    public String getOrderDeptCode() {
        return orderDeptCode;
    }

    public void setOrderDeptCode(String orderDeptCode) {
        this.orderDeptCode = orderDeptCode;
    }

    public String getOrderDeptName() {
        return orderDeptName;
    }

    public void setOrderDeptName(String orderDeptName) {
        this.orderDeptName = orderDeptName;
    }

    public String getExecDeptCode() {
        return execDeptCode;
    }

    public void setExecDeptCode(String execDeptCode) {
        this.execDeptCode = execDeptCode;
    }

    public String getExecDeptName() {
        return execDeptName;
    }

    public void setExecDeptName(String execDeptName) {
        this.execDeptName = execDeptName;
    }

    public String getDocCode() {
        return docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getAdmissDiag() {
        return admissDiag;
    }

    public void setAdmissDiag(String admissDiag) {
        this.admissDiag = admissDiag;
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

    public String getPatientDate() {
        return patientDate;
    }

    public void setPatientDate(String patientDate) {
        this.patientDate = patientDate;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getChargeNum() {
        return chargeNum;
    }

    public void setChargeNum(String chargeNum) {
        this.chargeNum = chargeNum;
    }

    public String getChagePrice() {
        return chagePrice;
    }

    public void setChagePrice(String chagePrice) {
        this.chagePrice = chagePrice;
    }

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }
}

