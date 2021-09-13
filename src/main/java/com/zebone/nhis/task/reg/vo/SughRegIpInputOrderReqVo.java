package com.zebone.nhis.task.reg.vo;

public class SughRegIpInputOrderReqVo {

    /// <summary>
    /// 医院HIS编码，字符串，不超过50（具体编码查看“5.1医院机构编号和机构名称”） 是
    /// </summary>
    private String orgUuid ;

    /// <summary>
    /// 医院名称，字符串，不超过100 是
    /// </summary>
    private String orgName ;

    /// <summary>
    /// 住院号（唯一），字符串，不超过50 是
    /// </summary>
    private String inpatientNo ;

    /// <summary>
    /// 住院次数，数字 是
    /// </summary>
    private int inpatientTimes ;

    /// <summary>
    /// 入院日期，格式为yyyy-MM-dd HH:mm:ss，字符串，不超过19 是
    /// </summary>
    private String admissionDate ;

    /// <summary>
    /// 姓名,字符串，不超过100 是
    /// </summary>
    private String pname ;

    /// <summary>
    /// 性别代码，数字，不超过6位, 详见字典描述DICT_HOSP_SEX 否
    /// </summary>
    private String sex ;

    /// <summary>
    /// 出生日期，格式为yyyy-MM-dd，字符串，不超过10位 否
    /// </summary>
    private String birthday ;

    /// <summary>
    /// 身份证件类型，不超过6位, 详见字典描述DICT_PUB_IDCARD_TYPE，默认为1 是
    /// </summary>
    private String idcardType ;

    /// <summary>
    /// 身份证件号码,字符串，不超过30 是
    /// </summary>
    private String idcard ;

    /// <summary>
    /// 手机号码,字符串，不超过50 是
    /// </summary>
    private String mobile ;

    /// <summary>
    /// 家庭住址,字符串，不超过500 否
    /// </summary>
    private String homeAddr ;

    public String getOrgUuid() {
        return orgUuid;
    }

    public void setOrgUuid(String orgUuid) {
        this.orgUuid = orgUuid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getInpatientNo() {
        return inpatientNo;
    }

    public void setInpatientNo(String inpatientNo) {
        this.inpatientNo = inpatientNo;
    }

    public int getInpatientTimes() {
        return inpatientTimes;
    }

    public void setInpatientTimes(int inpatientTimes) {
        this.inpatientTimes = inpatientTimes;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(String idcardType) {
        this.idcardType = idcardType;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }
}
