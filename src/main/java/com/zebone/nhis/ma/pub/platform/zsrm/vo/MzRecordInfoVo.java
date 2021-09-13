package com.zebone.nhis.ma.pub.platform.zsrm.vo;

import java.util.Date;

/**
 * 查询门诊病历信息
 **/
public class MzRecordInfoVo {
    //患者姓名
    private String namePi;
    //主诉
    private String problem;
    //既往史
    private String history;
    //诊断
    private String diagnose;
    //治疗经过
    private String treatmentPrograms;
    //患者床位
    private String bedNo;
    //就诊次数
    private String cntOp;
    //门诊病历主键
    private String pkEmrop;
    //出生日期
    private Date birthDate;
    //性别
    private String dtSex;
    //
    private String euPvtype;

    private String visitTime;
    private String codePv;
    private String idNo;
    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getEuPvType() {
        return euPvtype;
    }

    public void setEuPvType(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPkEmrop() {
        return pkEmrop;
    }

    public void setPkEmrop(String pkEmrop) {
        this.pkEmrop = pkEmrop;
    }

    public String getCntOp() {
        return cntOp;
    }

    public void setCntOp(String cntOp) {
        this.cntOp = cntOp;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getTreatmentPrograms() {
        return treatmentPrograms;
    }

    public void setTreatmentPrograms(String treatmentPrograms) {
        this.treatmentPrograms = treatmentPrograms;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
