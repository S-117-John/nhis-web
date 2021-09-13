/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class ListOperation {

    @JSONField(name = "OperationRecordNo")
    private String operationRecordNo= "-";
    @JSONField(name = "OperationDoctorCode")
    private String operationDoctorCode= "-";
    @JSONField(name = "OperationDoctorName")
    private String operationDoctorName= "-";
    @JSONField(name = "FirstOperdoctorcode")
    private String firstOperdoctorcode= "-";
    @JSONField(name = "FirstOperdoctorname")
    private String firstOperdoctorname= "-";
    @JSONField(name = "SecondOperdoctorcode")
    private String secondOperdoctorcode= "-";
    @JSONField(name = "SecondOperdoctorname")
    private String secondOperdoctorname= "-";
    @JSONField(name = "AnesthesiologistCode")
    private String anesthesiologistCode= "-";
    @JSONField(name = "AnesthesiologistName")
    private String anesthesiologistName= "-";
    @JSONField(name = "OperationDate",format="yyyy-MM-dd HH:mm:ss")
    private Date operationDate;
    @JSONField(name = "OperationFinishDate",format="yyyy-MM-dd HH:mm:ss")
    private Date operationFinishDate;
    @JSONField(name = "AnaesthesiaType")
    private String anaesthesiaType= "-";
    @JSONField(name = "IsComplication")
    private String isComplication= "-";
    @JSONField(name = "ComplicationCode")
    private String complicationCode;
    @JSONField(name = "ComplicationName")
    private String complicationName;
    @JSONField(name = "OperationRecord")
    private String operationRecord= "-";
    @JSONField(name = "RecordDoctorCode")
    private String recordDoctorCode= "-";
    @JSONField(name = "RecordDoctorName")
    private String recordDoctorName= "-";
    @JSONField(name = "ListOperationDetail")
    private List<ListOperationDetail> listOperationDetail;

    public String getOperationRecordNo() {
        return operationRecordNo;
    }

    public void setOperationRecordNo(String operationRecordNo) {
        this.operationRecordNo = operationRecordNo;
    }

    public String getOperationDoctorCode() {
        return operationDoctorCode;
    }

    public void setOperationDoctorCode(String operationDoctorCode) {
        this.operationDoctorCode = operationDoctorCode;
    }

    public String getOperationDoctorName() {
        return operationDoctorName;
    }

    public void setOperationDoctorName(String operationDoctorName) {
        this.operationDoctorName = operationDoctorName;
    }

    public String getFirstOperdoctorcode() {
        return firstOperdoctorcode;
    }

    public void setFirstOperdoctorcode(String firstOperdoctorcode) {
        this.firstOperdoctorcode = firstOperdoctorcode;
    }

    public String getFirstOperdoctorname() {
        return firstOperdoctorname;
    }

    public void setFirstOperdoctorname(String firstOperdoctorname) {
        this.firstOperdoctorname = firstOperdoctorname;
    }

    public String getSecondOperdoctorcode() {
        return secondOperdoctorcode;
    }

    public void setSecondOperdoctorcode(String secondOperdoctorcode) {
        this.secondOperdoctorcode = secondOperdoctorcode;
    }

    public String getSecondOperdoctorname() {
        return secondOperdoctorname;
    }

    public void setSecondOperdoctorname(String secondOperdoctorname) {
        this.secondOperdoctorname = secondOperdoctorname;
    }

    public String getAnesthesiologistCode() {
        return anesthesiologistCode;
    }

    public void setAnesthesiologistCode(String anesthesiologistCode) {
        this.anesthesiologistCode = anesthesiologistCode;
    }

    public String getAnesthesiologistName() {
        return anesthesiologistName;
    }

    public void setAnesthesiologistName(String anesthesiologistName) {
        this.anesthesiologistName = anesthesiologistName;
    }

    public Date getOperationDate() {
        if(null == this.operationDate){
            return new Date();
        }else {
            return this.operationDate;
        }
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Date getOperationFinishDate() {
        if(null == this.operationFinishDate){
            return new Date();
        }else {
            return this.operationFinishDate;
        }
    }

    public void setOperationFinishDate(Date operationFinishDate) {
        this.operationFinishDate = operationFinishDate;
    }

    public String getAnaesthesiaType() {
        return anaesthesiaType;
    }

    public void setAnaesthesiaType(String anaesthesiaType) {
        this.anaesthesiaType = anaesthesiaType;
    }

    public String getIsComplication() {
        return isComplication;
    }

    public void setIsComplication(String isComplication) {
        this.isComplication = isComplication;
    }

    public String getComplicationCode() {
        return complicationCode;
    }

    public void setComplicationCode(String complicationCode) {
        this.complicationCode = complicationCode;
    }

    public String getComplicationName() {
        return complicationName;
    }

    public void setComplicationName(String complicationName) {
        this.complicationName = complicationName;
    }

    public String getOperationRecord() {
        return operationRecord;
    }

    public void setOperationRecord(String operationRecord) {
        this.operationRecord = operationRecord;
    }

    public String getRecordDoctorCode() {
        return recordDoctorCode;
    }

    public void setRecordDoctorCode(String recordDoctorCode) {
        this.recordDoctorCode = recordDoctorCode;
    }

    public String getRecordDoctorName() {
        return recordDoctorName;
    }

    public void setRecordDoctorName(String recordDoctorName) {
        this.recordDoctorName = recordDoctorName;
    }

    public List<ListOperationDetail> getListOperationDetail() {
        return listOperationDetail;
    }

    public void setListOperationDetail(List<ListOperationDetail> listOperationDetail) {
        this.listOperationDetail = listOperationDetail;
    }
}