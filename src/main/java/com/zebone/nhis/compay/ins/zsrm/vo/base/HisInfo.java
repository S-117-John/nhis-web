package com.zebone.nhis.compay.ins.zsrm.vo.base;

public class HisInfo {
    private String pkPv;
    private String pkSettle;
    private String ybPkReg;
    private String ybPkSettle;
    private String euStatus;

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getYbPkReg() {
        return ybPkReg;
    }

    public void setYbPkReg(String ybPkReg) {
        this.ybPkReg = ybPkReg;
    }

    public String getYbPkSettle() {
        return ybPkSettle;
    }

    public void setYbPkSettle(String ybPkSettle) {
        this.ybPkSettle = ybPkSettle;
    }
}
