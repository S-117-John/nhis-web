package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

/**
 * 保单详细信息
 */
public class PolDetailVo {

    /**
     *保单号
     */
    private String commercialBillNum;

    /**
     *分单号
     */
    private String certNo;

    /**
     *客户号
     */
    private String customerNo;

    /**
     *机构编码
     */
    private String regionCode;

    /**
     *保险产品名称
     */
    private String insuranceProductName;

    /**
     *剩余额度
     */
    private String residualAmount;

    /**
     *保单开始时间
     * 格式：YYYYMMDDHH24MISS
     */
    private String beginDate;

    /**
     *保单结束时间
     */
    private String endDate;

    /**
     *联系人姓名
     */
    private String linkmanName;
    /**
     *联系人电话
     */
    private String linkmanMobile;

    /**
     *单位名称
     */
    private String companyName;

    /**
     *家庭地址
     */
    private String homeAddress;

    public String getCommercialBillNum() {
        return commercialBillNum;
    }

    public void setCommercialBillNum(String commercialBillNum) {
        this.commercialBillNum = commercialBillNum;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getInsuranceProductName() {
        return insuranceProductName;
    }

    public void setInsuranceProductName(String insuranceProductName) {
        this.insuranceProductName = insuranceProductName;
    }

    public String getResidualAmount() {
        return residualAmount;
    }

    public void setResidualAmount(String residualAmount) {
        this.residualAmount = residualAmount;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanMobile() {
        return linkmanMobile;
    }

    public void setLinkmanMobile(String linkmanMobile) {
        this.linkmanMobile = linkmanMobile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }
}


