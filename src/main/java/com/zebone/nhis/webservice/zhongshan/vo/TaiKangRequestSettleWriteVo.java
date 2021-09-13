package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Classname TaiKangRequestSettleWriteVo
 * @Description TODO
 * @Date 2020-12-04 15:46
 * @Created by wuqiang
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "InsuranceBackDetailSet")
public class TaiKangRequestSettleWriteVo {

    /*
     * 住院号
     * */

    @XmlElement(name = "PatientNumber")
    private String PatientNumber;
    /*
     * 客户姓名
     * */

    @XmlElement(name = "CustomerName")
    private String customerName;
    /*
     * 客户性别
     * */

    @XmlElement(name = "CustomerName")
    private String customerSex;

    /*
     * 客户证件号码
     * */

    @XmlElement(name = "CustomerIdNumber")
    private String customerIdNumber;
    /*
     * 结算金额
     * */

    @XmlElement(name = "CompensateMoney")
    private String compensateMoney;
    /*
     * 结算日期
     * */

    @XmlElement(name = "CompensateDate")
    private String compensateDate;
    /*
     * 入院日期
     * */

    @XmlElement(name = "InHosDate")
    private String inHosDate;
    /*
     *公司名称
     * */

    @XmlElement(name = "CompanyName")
    private String companyName;
    /*
     * 公司编码  COMPANY_CODE
     * */

    @XmlElement(name = "CompanyCode")
    private String companyCode;
    /*
     * 结算id
     * */

    @XmlElement(name = "CompensateId")
    private String compensateId;

    public String getPatientNumber() {
        return PatientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        PatientNumber = patientNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(String customerSex) {
        this.customerSex = customerSex;
    }

    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    public String getCompensateMoney() {
        return compensateMoney;
    }

    public void setCompensateMoney(String compensateMoney) {
        this.compensateMoney = compensateMoney;
    }

    public String getCompensateDate() {
        return compensateDate;
    }

    public void setCompensateDate(String compensateDate) {
        this.compensateDate = compensateDate;
    }

    public String getInHosDate() {
        return inHosDate;
    }

    public void setInHosDate(String inHosDate) {
        this.inHosDate = inHosDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompensateId() {
        return compensateId;
    }

    public void setCompensateId(String compensateId) {
        this.compensateId = compensateId;
    }




}
