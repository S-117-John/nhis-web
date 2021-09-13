package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

public class PiArchInfoVo {
    /**
     * 就诊主键
     */
    private String pkpv;
    /**
     * 患者姓名
     */
    private String xm;
    /**
     * 科室
     */
    private String deptName;
    /**
     * 住院号
     */
    private String admissionNo;

    private int flagUpLoad;

    public int getFlagUpLoad() {
        return flagUpLoad;
    }

    public void setFlagUpLoad(int flagUpLoad) {
        this.flagUpLoad = flagUpLoad;
    }

    public String getPkpv() {
        return pkpv;
    }

    public void setPkpv(String pkpv) {
        this.pkpv = pkpv;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }
}
