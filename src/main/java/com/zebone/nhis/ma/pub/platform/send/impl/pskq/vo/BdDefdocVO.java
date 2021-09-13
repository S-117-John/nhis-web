package com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo;

import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.BdDefdoc;

public class BdDefdocVO extends BdDefdoc {

    private String empCode;

    private String state;

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
