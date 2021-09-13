package com.zebone.nhis.ma.pub.zsba.vo.outflow;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

/**
 * 患者信息
 */
public class VisitInfo {
    @JSONField(format = "yyyy-MM-dd")
    private Date birthday;
    private String deptIdOutter;
    private String deptNameOutter;
    private String localDoctorId;
    private String icdCodeOutter;
    private String icdNameOutter;
    private String idcard;
    private String idcardType;
    private String orgIdOutter;
    private String orgNameOutter;
    private String patientName;
    private String patientPhone;
    private Integer sex;
    private String visitIdOutter;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;
    private Double weight;
    private List<Diagnoses> diagnoses;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDeptIdOutter() {
        return deptIdOutter;
    }

    public void setDeptIdOutter(String deptIdOutter) {
        this.deptIdOutter = deptIdOutter;
    }

    public String getDeptNameOutter() {
        return deptNameOutter;
    }

    public void setDeptNameOutter(String deptNameOutter) {
        this.deptNameOutter = deptNameOutter;
    }

    public String getLocalDoctorId() {
        return localDoctorId;
    }

    public void setLocalDoctorId(String localDoctorId) {
        this.localDoctorId = localDoctorId;
    }

    public String getIcdCodeOutter() {
        return icdCodeOutter;
    }

    public void setIcdCodeOutter(String icdCodeOutter) {
        this.icdCodeOutter = icdCodeOutter;
    }

    public String getIcdNameOutter() {
        return icdNameOutter;
    }

    public void setIcdNameOutter(String icdNameOutter) {
        this.icdNameOutter = icdNameOutter;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(String idcardType) {
        this.idcardType = idcardType;
    }

    public String getOrgIdOutter() {
        return orgIdOutter;
    }

    public void setOrgIdOutter(String orgIdOutter) {
        this.orgIdOutter = orgIdOutter;
    }

    public String getOrgNameOutter() {
        return orgNameOutter;
    }

    public void setOrgNameOutter(String orgNameOutter) {
        this.orgNameOutter = orgNameOutter;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getVisitIdOutter() {
        return visitIdOutter;
    }

    public void setVisitIdOutter(String visitIdOutter) {
        this.visitIdOutter = visitIdOutter;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public List<Diagnoses> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnoses> diagnoses) {
        this.diagnoses = diagnoses;
    }

}