package com.zebone.nhis.webservice.lbzy.model.ipin;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryInPay {

    @XmlElement(name = "InPatientID")
    private String inPatientID;
    @XmlElement(name = "TranSerialNO")
    private String tranSerialNO;
    @XmlElement(name = "PrePayCost")
    private Double prePayCost;
    @XmlElement(name = "PayWay")
    private String payWay;
    @XmlElement(name = "IsBalance")
    private String isBalance;
    @XmlElement(name = "OperatorNO")
    private String operatorNO;
    @XmlElement(name = "OperDate")
    private String operDate;


    public String getInPatientID() {
        return inPatientID;
    }

    public void setInPatientID(String inPatientID) {
        this.inPatientID = inPatientID;
    }

    public String getTranSerialNO() {
        return tranSerialNO;
    }

    public void setTranSerialNO(String tranSerialNO) {
        this.tranSerialNO = tranSerialNO;
    }

    public Double getPrePayCost() {
        return prePayCost;
    }

    public void setPrePayCost(Double prePayCost) {
        this.prePayCost = prePayCost;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getIsBalance() {
        return isBalance;
    }

    public void setIsBalance(String isBalance) {
        this.isBalance = isBalance;
    }

    public String getOperatorNO() {
        return operatorNO;
    }

    public void setOperatorNO(String operatorNO) {
        this.operatorNO = operatorNO;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }
}
