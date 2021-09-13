package com.zebone.nhis.ma.kangMei.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"orderTime", "treatCard", "regNum", "addrStr", "consignee", "conTel",
        "sendGoodsTime", "isHosAddr", "pdetailReq", "operName", "reason", "orderId", })
@XmlRootElement(name = "DataReq")
public class DataReq {

    private String orderTime;

    private String treatCard;

    private String regNum;

    private String addrStr;

    private String consignee;

    private String conTel;

    private String sendGoodsTime;

    private String isHosAddr;

    private List<PdetailReq> pdetailReq;

    private String operName;

    private String reason;

    private String orderId;

    /**
     * 附加字段
     **/

    private String pkPv;

    private String codeOrd;

    private Date birthDate;

    private String agePv;

    private String ordsn;

    private String codeEmp;
    private  String pkPi;
    /**
     * 处方性质
     * */
    private  String dtProperties;
    public String getCodeEmp() {
        return codeEmp;
    }
    @XmlTransient
    public void setCodeEmp(String codeEmp) {
        this.codeEmp = codeEmp;
    }

    public String getOrdsn() {
        return ordsn;
    }
    @XmlTransient
    public void setOrdsn(String ordsn) {
        this.ordsn = ordsn;
    }

    public String getAgePv() {
        return agePv;
    }
    @XmlTransient
    public void setAgePv(String agePv) {
        this.agePv = agePv;
    }

    public Date getBirthDate() {
        return birthDate;
    }
    @XmlTransient
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPkPv() {
        return pkPv;
    }
    @XmlTransient
    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getCodeOrd() {
        return codeOrd;
    }
    @XmlTransient
    public void setCodeOrd(String codeOrd) {
        this.codeOrd = codeOrd;
    }

    @XmlElement(name = "oper_name")
    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    @XmlElement(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @XmlElement(name = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @XmlElementWrapper(name = "prescript")
    @XmlElement(name = "pdetail")
    public List<PdetailReq> getPdetailReq() {
        return pdetailReq;
    }

    public void setPdetailReq(List<PdetailReq> pdetailReq) {
        this.pdetailReq = pdetailReq;
    }

    @XmlElement(name = "order_time")
    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @XmlElement(name = "treat_card")
    public String getTreatCard() {
        return treatCard;
    }

    public void setTreatCard(String treatCard) {
        this.treatCard = treatCard;
    }

    @XmlElement(name = "reg_num")
    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    @XmlElement(name = "addr_str")
    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    @XmlElement(name = "consignee")
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @XmlElement(name = "con_tel")
    public String getConTel() {
        return conTel;
    }

    public void setConTel(String conTel) {
        this.conTel = conTel;
    }

    @XmlElement(name = "send_goods_time")
    public String getSendGoodsTime() {
        return sendGoodsTime;
    }

    public void setSendGoodsTime(String sendGoodsTime) {
        this.sendGoodsTime = sendGoodsTime;
    }

    @XmlElement(name = "is_hos_addr")
    public String getIsHosAddr() {
        return isHosAddr;
    }

    public void setIsHosAddr(String isHosAddr) {
        this.isHosAddr = isHosAddr;
    }


    public String getPkPi() {
        return pkPi;
    }
    @XmlTransient
    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getDtProperties() {
        return dtProperties;
    }
    @XmlTransient
    public void setDtProperties(String dtProperties) {
        this.dtProperties = dtProperties;
    }
}
