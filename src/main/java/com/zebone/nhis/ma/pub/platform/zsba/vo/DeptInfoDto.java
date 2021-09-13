package com.zebone.nhis.ma.pub.platform.zsba.vo;

import java.util.ArrayList;
import java.util.List;

public class DeptInfoDto {
    // 状态码
    private int status;
    // 错误信息
    private String error;
    // 返回信息
    private PatientsInfo msg = new PatientsInfo();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PatientsInfo getMsg() {
        return msg;
    }

    public void setMsg(PatientsInfo msg) {
        this.msg = msg;
    }

    public static class PatientsInfo
    {
        private int totalCount;
        private List<PatientInfo> patientsInfo = new ArrayList<>();

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<PatientInfo> getPatientsInfo() {
            return patientsInfo;
        }

        public void setPatientsInfo(List<PatientInfo> patientsInfo) {
            this.patientsInfo = patientsInfo;
        }
    }

    public static class PatientInfo
    {
        // 科室ID
        private String deptId;
        // 科室信息
        private String deptInfo;
        //病区ID
        private String wardCode;
        //病区信息
        private String wardName;
        // 病人腕带号
        private String patientId;
        // 病人信息
        private String patientInfo;
        // 床号
        private String bedNo;
        // 姓名
        private String name;

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getDeptInfo() {
            return deptInfo;
        }

        public void setDeptInfo(String deptInfo) {
            this.deptInfo = deptInfo;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getPatientInfo() {
            return patientInfo;
        }

        public void setPatientInfo(String patientInfo) {
            this.patientInfo = patientInfo;
        }

        public String getBedNo() {
            return bedNo;
        }

        public void setBedNo(String bedNo) {
            this.bedNo = bedNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getWardCode() {
            return wardCode;
        }

        public void setWardCode(String wardCode) {
            this.wardCode = wardCode;
        }

        public String getWardName() {
            return wardName;
        }

        public void setWardName(String wardName) {
            this.wardName = wardName;
        }
    }
}