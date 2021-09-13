/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 */
public class ListOperationDetail {

    @JSONField(name = "OperationRecordNo")
    private String operationRecordNo;
    @JSONField(name = "OperationNo")
    private String operationNo;
    @JSONField(name = "OperationCode")
    private String operationCode;
    @JSONField(name = "OperationName")
    private String operationName;
    @JSONField(name = "OperationLevel")
    private String operationLevel;
    @JSONField(name = "OperationIncisionClass")
    private String operationIncisionClass;
    @JSONField(name = "OperationHealClass")
    private String operationHealClass;
    @JSONField(name = "IsMajorIden")
    private String isMajorIden;
    @JSONField(name = "IsIatrogenic")
    private String isIatrogenic;

    public String getOperationRecordNo() {
        return operationRecordNo;
    }

    public void setOperationRecordNo(String operationRecordNo) {
        this.operationRecordNo = operationRecordNo;
    }

    public String getOperationNo() {
        return operationNo;
    }

    public void setOperationNo(String operationNo) {
        this.operationNo = operationNo;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationLevel() {
        return operationLevel;
    }

    public void setOperationLevel(String operationLevel) {
        this.operationLevel = operationLevel;
    }

    public String getOperationIncisionClass() {
        return operationIncisionClass;
    }

    public void setOperationIncisionClass(String operationIncisionClass) {
        this.operationIncisionClass = operationIncisionClass;
    }

    public String getOperationHealClass() {
        return operationHealClass;
    }

    public void setOperationHealClass(String operationHealClass) {
        this.operationHealClass = operationHealClass;
    }

    public String getIsMajorIden() {
        return isMajorIden;
    }

    public void setIsMajorIden(String isMajorIden) {
        this.isMajorIden = isMajorIden;
    }

    public String getIsIatrogenic() {
        return isIatrogenic;
    }

    public void setIsIatrogenic(String isIatrogenic) {
        this.isIatrogenic = isIatrogenic;
    }
}