package com.zebone.nhis.pro.zsrm.bl.vo;

/**
 * 支付对账数据
 */
public class PaymentCheckVo{
    private String euPvtype;
    private String codeOp;
    private String namePi;
    private String datePv;
    private String pkExtpay;
    private String pkDepo;
    private String payType;
    private String dtSttype;
    private String dateSt;
    private String tradeNo;
    private String orderNo;
    private String bodyPay;
    private String sysname;
    private Double amount;
    private String datePay;
    private String nameEmp;
    private Double amountPi;
    private Double amountInsu;
    private Double amountSt;
    private String dtBank;
    private PartyPaymentVo PartyPay;
    private String flagPay;
    private PayRecordResponseVo payRecordResponseVo;
    public PartyPaymentVo getPartyPay() {
        return PartyPay;
    }

    public void setPartyPay(PartyPaymentVo partyPay) {
        PartyPay = partyPay;
    }

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getDatePv() {
        return datePv;
    }

    public void setDatePv(String datePv) {
        this.datePv = datePv;
    }

    public String getPkExtpay() {
        return pkExtpay;
    }

    public void setPkExtpay(String pkExtpay) {
        this.pkExtpay = pkExtpay;
    }

    public String getPkDepo() {
        return pkDepo;
    }

    public void setPkDepo(String pkDepo) {
        this.pkDepo = pkDepo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getDtSttype() {
        return dtSttype;
    }

    public void setDtSttype(String dtSttype) {
        this.dtSttype = dtSttype;
    }

    public String getDateSt() {
        return dateSt;
    }

    public void setDateSt(String dateSt) {
        this.dateSt = dateSt;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDatePay() {
        return datePay;
    }

    public void setDatePay(String datePay) {
        this.datePay = datePay;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountPi() {
        return amountPi;
    }

    public void setAmountPi(Double amountPi) {
        this.amountPi = amountPi;
    }

    public Double getAmountInsu() {
        return amountInsu;
    }

    public void setAmountInsu(Double amountInsu) {
        this.amountInsu = amountInsu;
    }

    public Double getAmountSt() {
        return amountSt;
    }

    public void setAmountSt(Double amountSt) {
        this.amountSt = amountSt;
    }

    public String getBodyPay() {
        return bodyPay;
    }

    public void setBodyPay(String bodyPay) {
        this.bodyPay = bodyPay;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getDtBank() {
        return dtBank;
    }

    public void setDtBank(String dtBank) {
        this.dtBank = dtBank;
    }

	public String getFlagPay() {
		return flagPay;
	}

	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}

    public PayRecordResponseVo getPayRecordResponseVo() {
        return payRecordResponseVo;
    }

    public void setPayRecordResponseVo(PayRecordResponseVo payRecordResponseVo) {
        this.payRecordResponseVo = payRecordResponseVo;
    }
}
