package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

public class CiInsuPatiInfo {

    /**
     * 证件类型
     */
    private String credentialType;

    /**
     * 证件号码
     */
    private String credentialNum;

    /**
     *姓名
     */
    private String name;

    /**
     *性别
     */
    private String gender;

    /**
     *出生日期
     * 格式：YYYYMMDD
     */
    private String birthday;

    /**
     *民族
     * 数据字典：
     * 民族
     */
    private String race;

    /**
     *备注
     */
    private String remark;

    /**
     *商保出参列表
     */
    private List<CommerInsuVo> commercialInsuranceList;



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

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<CommerInsuVo> getCommercialInsuranceList() {
        return commercialInsuranceList;
    }

    public void setCommercialInsuranceList(List<CommerInsuVo> commercialInsuranceList) {
        this.commercialInsuranceList = commercialInsuranceList;
    }
}
