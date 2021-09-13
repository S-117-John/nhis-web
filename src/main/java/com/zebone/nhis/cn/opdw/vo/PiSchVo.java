package com.zebone.nhis.cn.opdw.vo;

import java.util.*;

public class PiSchVo {
    /// 排班主键
    private String pkSch ;
    /// 日期分组
    private String pkDateslot ;
    /// 科室名称
    private String nameDept ;
    /// 预约开始时间
    private String beginTime ;
    /// 预约结束时间
    private String endTime ;
    /// 可预约数
    private Integer cnt ;
    /// 出诊时段
    private String time ;
    /// 操作
    private String appointment ;
    /// 患者编码
    private String pkPi ;
    /// 日期
    private String dateWork ;

    private String dateApt;
    /// 午别
    private String nameDateslot ;
    /// 票号
    private String ticketno ;
    /// 备注
    private String apptNote ;
    /// 预约科室
    private String pkDept ;
    /// 预约方式
    private String euApptmode ;

    /** 主预约数据*/
    private String flagAppt;
    /** 预约渠道*/
    private String dtApptype;
    /** 当前人员主键 **/
    private String pkEmp;
    /** 当前人员姓名 **/
    private String nameEmp;

    public String getPkSch() {
        return pkSch;
    }

    public void setPkSch(String pkSch) {
        this.pkSch = pkSch;
    }

    public String getPkDateslot() {
        return pkDateslot;
    }

    public void setPkDateslot(String pkDateslot) {
        this.pkDateslot = pkDateslot;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getDateWork() {
        return dateWork;
    }

    public void setDateWork(String dateWork) {
        this.dateWork = dateWork;
    }

    public String getNameDateslot() {
        return nameDateslot;
    }

    public void setNameDateslot(String nameDateslot) {
        this.nameDateslot = nameDateslot;
    }

    public String getTicketno() {
        return ticketno;
    }

    public void setTicketno(String ticketno) {
        this.ticketno = ticketno;
    }

    public String getApptNote() {
        return apptNote;
    }

    public void setApptNote(String apptNote) {
        this.apptNote = apptNote;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getEuApptmode() {
        return euApptmode;
    }

    public void setEuApptmode(String euApptmode) {
        this.euApptmode = euApptmode;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDateApt() {
        return dateApt;
    }

    public void setDateApt(String dateApt) {
        this.dateApt = dateApt;
    }

    public String getFlagAppt() {
        return flagAppt;
    }

    public void setFlagAppt(String flagAppt) {
        this.flagAppt = flagAppt;
    }

    public String getDtApptype() {
        return dtApptype;
    }

    public void setDtApptype(String dtApptype) {
        this.dtApptype = dtApptype;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }
}
