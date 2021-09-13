package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

/***
 * 商保出参列表
 */
public class CommerInsuVo {

    /**
     *保险公司代码
     */
    private String professionalCode;

    /**
     *保险公司名称
     */
    private String professionalName;

    /**
     *有效客户标识
     */
    private String identifyCustomers;

    /**
     *可选择理赔方式
     */
    private String claimWay;

    /**
     *保单详细信息
     */
    private List<PolDetailVo> polDetailList;

    public List<PolDetailVo> getPolDetailList() {
        return polDetailList;
    }

    public void setPolDetailList(List<PolDetailVo> polDetailList) {
        this.polDetailList = polDetailList;
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

    public String getIdentifyCustomers() {
        return identifyCustomers;
    }

    public void setIdentifyCustomers(String identifyCustomers) {
        this.identifyCustomers = identifyCustomers;
    }

    public String getClaimWay() {
        return claimWay;
    }

    public void setClaimWay(String claimWay) {
        this.claimWay = claimWay;
    }
}
