package com.zebone.nhis.ma.pub.lb.vo;

import java.math.BigDecimal;

public class ResDetail {
    /**
     * 科室名称
     */
    private String inOfficeName;
    /**
     * 主治医生姓名
     */
    private String clinicDoctor;
    /**
     * 就诊流水号
     */
    private String visitno;
    /**
     * 明细编码
     */
    private String detailid;
    /**
     * 费用发生日期
     */
    private String useDate;
    /**
     * 医院收费项目编码
     */
    private String hiscode;
    /**
     * 医院收费项目名称
     */
    private String hisname;
    /**
     * 违规金额
     */
    private BigDecimal dedunumberctions;
    /**
     * 违规数量
     */
    private BigDecimal denumber;
    /**
     * 违规说明
     */
    private String remark;


    public String getInOfficeName() {
        return inOfficeName;
    }

    public void setInOfficeName(String inOfficeName) {
        this.inOfficeName = inOfficeName;
    }

    public String getClinicDoctor() {
        return clinicDoctor;
    }

    public void setClinicDoctor(String clinicDoctor) {
        this.clinicDoctor = clinicDoctor;
    }

    public String getVisitno() {
        return visitno;
    }

    public void setVisitno(String visitno) {
        this.visitno = visitno;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getUseDate() {
        return useDate;
    }
    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public String getHiscode() {
        return hiscode;
    }

    public void setHiscode(String hiscode) {
        this.hiscode = hiscode;
    }

    public String getHisname() {
        return hisname;
    }

    public void setHisname(String hisname) {
        this.hisname = hisname;
    }

    public BigDecimal getDedunumberctions() {
        return dedunumberctions;
    }

    public void setDedunumberctions(BigDecimal dedunumberctions) {
        this.dedunumberctions = dedunumberctions;
    }

    public BigDecimal getDenumber() {
        return denumber;
    }

    public void setDenumber(BigDecimal denumber) {
        this.denumber = denumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
