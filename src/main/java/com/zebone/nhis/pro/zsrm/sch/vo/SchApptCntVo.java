package com.zebone.nhis.pro.zsrm.sch.vo;

public class SchApptCntVo {

    private String pkSch;

    /**总可预约号*/
    private Integer cntAppt;

    /**外部 可预约号*/
    private Integer cntApptOut;

    /**dtApptype<=1 可预约号*/
    private Integer cntApptIn;

    public String getPkSch() {
        return pkSch;
    }

    public void setPkSch(String pkSch) {
        this.pkSch = pkSch;
    }

    public Integer getCntAppt() {
        return cntAppt;
    }

    public void setCntAppt(Integer cntAppt) {
        this.cntAppt = cntAppt;
    }

    public Integer getCntApptOut() {
        return cntApptOut;
    }

    public void setCntApptOut(Integer cntApptOut) {
        this.cntApptOut = cntApptOut;
    }

    public Integer getCntApptIn() {
        return cntApptIn;
    }

    public void setCntApptIn(Integer cntApptIn) {
        this.cntApptIn = cntApptIn;
    }
}
