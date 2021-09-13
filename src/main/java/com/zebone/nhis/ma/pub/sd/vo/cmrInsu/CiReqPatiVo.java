package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiReqPatiVo {

    /**
     *证件类型
     *数据字典：
     * 证件类型
     *是
     */
    private String credentialType;

    /**
     *证件号码
     *是
     */
    private String credentialNum;

    /**
     *姓名
     *是
     */
    private String name;

    /**
     *性别
     *数据字典：
     * 1-男性
     * 2-女性
     * 9-未知的性别
     *是
     */
    private String gender;

    /**
     *出生日期
     *格式：YYYYMMDD
     *是
     */
    private String birthday;

    /**
     *保单号
     */
    private String commercialBillNum;

    /**
     *就诊类型
     *数据字典，如果为空都查询：
     * 1-门急诊
     * 2-住院
     *
     */
    private String treatType;

    /**
     *查询时间
     *格式：YYYYMMDDHH24MISS
     *
     */
    private String queryDate;

    /**
     *商保公司代码
     */
    private String professionalCode;

    /**
     *商保公司名称
     */
    private String professionalName;

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNum() {
        return credentialNum;
    }

    public void setCredentialNum(String credentialNum) {
        this.credentialNum = credentialNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCommercialBillNum() {
        return commercialBillNum;
    }

    public void setCommercialBillNum(String commercialBillNum) {
        this.commercialBillNum = commercialBillNum;
    }

    public String getTreatType() {
        return treatType;
    }

    public void setTreatType(String treatType) {
        this.treatType = treatType;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getProfessionalCode() {
        return professionalCode;
    }

    public void setProfessionalCode(String professionalCode) {
        this.professionalCode = professionalCode;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }
}
