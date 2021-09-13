/**
  * Copyright 2021 json.cn 
  */
package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 *
 */
public class LeaveHospital {

    @JSONField (name = "DischargeOutcome")
    private String dischargeOutcome= "1";
    @JSONField (name = "HospitalizationSituation")
    private String hospitalizationSituation = "2";
    @JSONField (name = "DtProcess")
    private String dtProcess ="3";
    @JSONField (name = "LeaveHospitalStatus")
    private String leaveHospitalStatus = "4";
    @JSONField (name = "LeaveDoctorAdvice")
    private String leaveDoctorAdvice = "5";

    public String getDischargeOutcome() {
        return dischargeOutcome;
    }

    public void setDischargeOutcome(String dischargeOutcome) {
        this.dischargeOutcome = dischargeOutcome;
    }

    public String getHospitalizationSituation() {
        return hospitalizationSituation;
    }

    public void setHospitalizationSituation(String hospitalizationSituation) {
        this.hospitalizationSituation = hospitalizationSituation;
    }

    public String getDtProcess() {
        return dtProcess;
    }

    public void setDtProcess(String dtProcess) {
        this.dtProcess = dtProcess;
    }

    public String getLeaveHospitalStatus() {
        return leaveHospitalStatus;
    }

    public void setLeaveHospitalStatus(String leaveHospitalStatus) {
        this.leaveHospitalStatus = leaveHospitalStatus;
    }

    public String getLeaveDoctorAdvice() {
        return leaveDoctorAdvice;
    }

    public void setLeaveDoctorAdvice(String leaveDoctorAdvice) {
        this.leaveDoctorAdvice = leaveDoctorAdvice;
    }
}