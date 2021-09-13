package com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo;

public class TriageRegVo {
    /**挂号主键/检查号*/
    private String pkRegistered;
    /**患者主键/影像号*/
    private String pkPatient;
    /**病人ID号（没有默认为患者主键）/身份证号*/
    private String codePatient;
    /**患者姓名*/
    private String namePatient;
    /**患者等级（急诊用）Ⅰ，Ⅱ，Ⅲ*/
    private String levelPatient;
    /**患者年龄*/
    private String age;
    /**患者性别（1：男 2：女） */
    private String gender;
    /**挂号日期 yyyyMMddHHmmss*/
    private String dateRegistered;
    /**挂号科室主键/检查类型主键（医技模式下）*/
    private String pkDeptRegistered;
    /**挂号诊室主键（没有默认为空）*/
    private String pkClinicRegistered;
    /**挂号医生主键（没有默认为空）*/
    private String pkDoctoRegistered;
    /**挂号检查类型主键（没有默认为空）*/
    private String pkDeptItemRegistered;
    /**挂号类别 1：专家，2：普通，3：急诊*/
    private String cateRegistered;
    /**就诊卡号/医保卡号（没有默认为空）*/
    private String cardNo;
    /**挂号/排队号码*/
    private int registeredNo;
    /**是否预挂号 0：否，1：是*/
    private String isPreRegistered;
    /**医保名称（常州医保，武进医保。） */
    private String medicalInsurance;
    /**挂号午别 上午，下午，全天*/
    private String flagSch;
    /**就诊号码（没有默认为空）*/
    private String visitingNo;
    /**挂号科室类别（非必选）*/
    private String cateDept;
    /**预约开始时间（非必选） yyyyMMddHHmmss*/
    private String preBeginTime;
    /**预约结束时间（非必选）*/
    private String preEndTime;
    /**身份证（必填）*/
    private String IdNo;

    public String getPkRegistered() {
        return pkRegistered;
    }

    public void setPkRegistered(String pkRegistered) {
        this.pkRegistered = pkRegistered;
    }

    public String getPkPatient() {
        return pkPatient;
    }

    public void setPkPatient(String pkPatient) {
        this.pkPatient = pkPatient;
    }

    public String getCodePatient() {
        return codePatient;
    }

    public void setCodePatient(String codePatient) {
        this.codePatient = codePatient;
    }

    public String getNamePatient() {
        return namePatient;
    }

    public void setNamePatient(String namePatient) {
        this.namePatient = namePatient;
    }

    public String getLevelPatient() {
        return levelPatient;
    }

    public void setLevelPatient(String levelPatient) {
        this.levelPatient = levelPatient;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getPkDeptRegistered() {
        return pkDeptRegistered;
    }

    public void setPkDeptRegistered(String pkDeptRegistered) {
        this.pkDeptRegistered = pkDeptRegistered;
    }

    public String getPkClinicRegistered() {
        return pkClinicRegistered;
    }

    public void setPkClinicRegistered(String pkClinicRegistered) {
        this.pkClinicRegistered = pkClinicRegistered;
    }

    public String getPkDoctoRegistered() {
        return pkDoctoRegistered;
    }

    public void setPkDoctoRegistered(String pkDoctoRegistered) {
        this.pkDoctoRegistered = pkDoctoRegistered;
    }

    public String getPkDeptItemRegistered() {
        return pkDeptItemRegistered;
    }

    public void setPkDeptItemRegistered(String pkDeptItemRegistered) {
        this.pkDeptItemRegistered = pkDeptItemRegistered;
    }

    public String getCateRegistered() {
        return cateRegistered;
    }

    public void setCateRegistered(String cateRegistered) {
        this.cateRegistered = cateRegistered;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getRegisteredNo() {
        return registeredNo;
    }

    public void setRegisteredNo(int registeredNo) {
        this.registeredNo = registeredNo;
    }

    public String getIsPreRegistered() {
        return isPreRegistered;
    }

    public void setIsPreRegistered(String isPreRegistered) {
        this.isPreRegistered = isPreRegistered;
    }

    public String getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(String medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public String getFlagSch() {
        return flagSch;
    }

    public void setFlagSch(String flagSch) {
        this.flagSch = flagSch;
    }

    public String getVisitingNo() {
        return visitingNo;
    }

    public void setVisitingNo(String visitingNo) {
        this.visitingNo = visitingNo;
    }

    public String getCateDept() {
        return cateDept;
    }

    public void setCateDept(String cateDept) {
        this.cateDept = cateDept;
    }

    public String getPreBeginTime() {
        return preBeginTime;
    }

    public void setPreBeginTime(String preBeginTime) {
        this.preBeginTime = preBeginTime;
    }

    public String getPreEndTime() {
        return preEndTime;
    }

    public void setPreEndTime(String preEndTime) {
        this.preEndTime = preEndTime;
    }

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
    }
}
