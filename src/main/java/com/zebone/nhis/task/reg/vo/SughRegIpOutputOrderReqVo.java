package com.zebone.nhis.task.reg.vo;

public class SughRegIpOutputOrderReqVo {
    /// <summary>
    /// 医院HIS编码，字符串，不超过50（具体编码查看“5.1医院机构编号和机构名称”） 是
    /// </summary>
    private String orgUuid ;

    /// <summary>
    /// 出院日期，格式为yyyy-MM-dd HH:mm:ss，字符串，不超过19
    /// </summary>
    private String dischargeDate ;

    /// <summary>
    /// 住院号（唯一），字符串，不超过50 是
    /// </summary>
    private String inpatientNo ;

    /// <summary>
    /// 住院次数，数字 是
    /// </summary>
    private String inpatientTimes ;

    /// <summary>
    /// 姓名,字符串，不超过100 是
    /// </summary>
    private String pname ;

    public String getOrgUuid() {
        return orgUuid;
    }

    public void setOrgUuid(String orgUuid) {
        this.orgUuid = orgUuid;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getInpatientNo() {
        return inpatientNo;
    }

    public void setInpatientNo(String inpatientNo) {
        this.inpatientNo = inpatientNo;
    }

    public String getInpatientTimes() {
        return inpatientTimes;
    }

    public void setInpatientTimes(String inpatientTimes) {
        this.inpatientTimes = inpatientTimes;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
