package com.zebone.nhis.pro.zsba.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

import java.util.List;

public class OpOrdVo {

    /****/
    private List<String> pkCnords; //医嘱主键

    private String pkPi;//患者主键

    private String pkPv;//患者就诊主键

    private String pkCnord;//医嘱主键

    private List<String> pkPds; //药品主键

    private String pkPd;//药品主键--单个

    private String pkDept;//当前科室

    private String pkEmp;//当前医生

    private List<CnOrder> orders;//修改医嘱

    private String flagRefund;//退费申请标志

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public List<String> getPkCnords() {
        return pkCnords;
    }

    public void setPkCnords(List<String> pkCnords) {
        this.pkCnords = pkCnords;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public List<String> getPkPds() {
        return pkPds;
    }

    public void setPkPds(List<String> pkPds) {
        this.pkPds = pkPds;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public List<CnOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CnOrder> orders) {
        this.orders = orders;
    }

    public String getFlagRefund() {
        return flagRefund;
    }

    public void setFlagRefund(String flagRefund) {
        this.flagRefund = flagRefund;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }
}