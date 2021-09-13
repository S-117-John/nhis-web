package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "res")
public class QryChecklistInfoResData {

    @XmlElement(name = "orderId")
    private String orderId;

    @XmlElement(name = "payNum")
    private String payNum;

    @XmlElement(name = "agtOrdNum")
    private String agtOrdNum;

    @XmlElement(name = "payAmout")
    private String payAmout;

    @XmlElement(name = "payTime")
    private String payTime;

    @XmlElement(name = "payDesc")
    private String payDesc;

    @XmlElement(name = "Type")
    private String Type;

    @XmlElement(name="resultCode")
    private String resultCode;

    @XmlElement(name="resultDesc")
    private String resultDesc;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }

    public String getAgtOrdNum() {
        return agtOrdNum;
    }

    public void setAgtOrdNum(String agtOrdNum) {
        this.agtOrdNum = agtOrdNum;
    }

    public String getPayAmout() {
        return payAmout;
    }

    public void setPayAmout(String payAmout) {
        this.payAmout = payAmout;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public void setPayDesc(String payDesc) {
        this.payDesc = payDesc;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
