/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 *
 */
public class ListCheck {

    @JSONField(name = "CheckId")
    private String checkId;
    @JSONField(name = "DepartmentCode")
    private String departmentCode;
    @JSONField(name = "DepartmentName")
    private String departmentName;
    @JSONField(name = "ApplyProjectCode")
    private String applyProjectCode;
    @JSONField(name = "ApplyProjectName")
    private String applyProjectName;
    @JSONField(name = "ApplyDoctor")
    private String applyDoctor;
    @JSONField(name = "ApplyDoctorName")
    private String applyDoctorName ;
    @JSONField(name = "ApplyDatetime")
    private Date applyDatetime ;
    @JSONField(name = "ReportDatetime")
    private Date reportDatetime ;
    @JSONField(name = "CheckPositionCode")
    private String checkPositionCode;
    @JSONField(name = "Abnormal")
    private String abnormal ;
    @JSONField(name = "CheckResult")
    private String checkResult;

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getApplyProjectCode() {
        return applyProjectCode;
    }

    public void setApplyProjectCode(String applyProjectCode) {
        this.applyProjectCode = applyProjectCode;
    }

    public String getApplyProjectName() {
        return applyProjectName;
    }

    public void setApplyProjectName(String applyProjectName) {
        this.applyProjectName = applyProjectName;
    }

    public String getApplyDoctor() {
        return applyDoctor;
    }

    public void setApplyDoctor(String applyDoctor) {
        this.applyDoctor = applyDoctor;
    }

    public String getApplyDoctorName() {
        return applyDoctorName;
    }

    public void setApplyDoctorName(String applyDoctorName) {
        this.applyDoctorName = applyDoctorName;
    }

    public Date getApplyDatetime() {
        if(null == this.applyDatetime){
            return new Date();
        }else {
            return this.applyDatetime;
        }
    }

    public void setApplyDatetime(Date applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public Date getReportDatetime() {
        if(null == this.reportDatetime){
            return new Date();
        }else {
            return this.reportDatetime;
        }
    }

    public void setReportDatetime(Date reportDatetime) {
        this.reportDatetime = reportDatetime;
    }

    public String getCheckPositionCode() {
        return checkPositionCode;
    }

    public void setCheckPositionCode(String checkPositionCode) {
        this.checkPositionCode = checkPositionCode;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public void setAbnormal(String abnormal) {
        this.abnormal = abnormal;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}