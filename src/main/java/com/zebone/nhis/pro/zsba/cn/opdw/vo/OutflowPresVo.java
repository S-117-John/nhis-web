package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import java.util.List;

public class OutflowPresVo {

    private String patientName;

    private String phone;

    private String pkPv;

    private List<String> pkPresList;

    /** 1：撤销 0：删除*/
    private String status;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public List<String> getPkPresList() {
        return pkPresList;
    }

    public void setPkPresList(List<String> pkPresList) {
        this.pkPresList = pkPresList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
