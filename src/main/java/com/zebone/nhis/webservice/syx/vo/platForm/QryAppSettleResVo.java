package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "res")
public class QryAppSettleResVo {

    @XmlElement(name = "resultCode")
    private String resultCode;

    @XmlElement(name = "resultDesc")
    private String resultDesc;

    @XmlElement(name="orderIdHis")
    private List<String> orderIdHis;

    @XmlElement(name = "pkPv")
    private String pkPv;

    @XmlElement(name = "codeIp")
    private String codeIp;

    @XmlElement(name = "namePi")
    private String namePi;

    @XmlElement(name = "dtSex")
    private String dtSex;

    @XmlElement(name = "birthdate")
    private String birthdate;

    @XmlElement(name = "dateBegin")
    private String dateBegin;

    @XmlElement(name = "dateEnd")
    private String dateEnd;

    @XmlElement(name = "inDays")
    private String inDays;

    @XmlElement(name = "pkInsu")
    private String pkInsu;

    @XmlElement(name = "nameInsu")
    private String nameInsu;

    @XmlElement(name = "dtOutcomes")
    private String dtOutcomes;

    @XmlElement(name = "euSettleWay")
    private String euSettleWay;

    @XmlElement(name = "totalAmount")
    private String totalAmount;

    @XmlElement(name = "foregiftAmount")
    private String foregiftAmount;

    @XmlElement(name = "unpaidAmount")
    private String unpaidAmount;

    @XmlElement(name = "insuAmount")
    private String insuAmount;

    @XmlElement(name = "selfAmout")
    private String selfAmout;

    @XmlElement(name = "payAmount")
    private String payAmount;

    @XmlElement(name = "settleStatus")
    private String settleStatus;

    public List<String> getOrderIdHis() {
        return orderIdHis;
    }

    public void setOrderIdHis(List<String> orderIdHis) {
        this.orderIdHis = orderIdHis;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getInDays() {
        return inDays;
    }

    public void setInDays(String inDays) {
        this.inDays = inDays;
    }

    public String getPkInsu() {
        return pkInsu;
    }

    public void setPkInsu(String pkInsu) {
        this.pkInsu = pkInsu;
    }

    public String getNameInsu() {
        return nameInsu;
    }

    public void setNameInsu(String nameInsu) {
        this.nameInsu = nameInsu;
    }

    public String getDtOutcomes() {
        return dtOutcomes;
    }

    public void setDtOutcomes(String dtOutcomes) {
        this.dtOutcomes = dtOutcomes;
    }

    public String getEuSettleWay() {
        return euSettleWay;
    }

    public void setEuSettleWay(String euSettleWay) {
        this.euSettleWay = euSettleWay;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getForegiftAmount() {
        return foregiftAmount;
    }

    public void setForegiftAmount(String foregiftAmount) {
        this.foregiftAmount = foregiftAmount;
    }

    public String getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(String unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public String getInsuAmount() {
        return insuAmount;
    }

    public void setInsuAmount(String insuAmount) {
        this.insuAmount = insuAmount;
    }

    public String getSelfAmout() {
        return selfAmout;
    }

    public void setSelfAmout(String selfAmout) {
        this.selfAmout = selfAmout;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
