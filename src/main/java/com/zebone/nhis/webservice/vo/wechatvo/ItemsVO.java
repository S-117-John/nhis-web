package com.zebone.nhis.webservice.vo.wechatvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemsVO {
	
   /**
    * 操作时间 yyyy-MM-dd
    */
   @XmlElement(name="billDate")
   private String billDate;
    /**
     * 卡号
     */
    @XmlElement(name="cardNo")
    private String cardNo;
    /**
     * 姓名
     */
    @XmlElement(name="cardName")
    private String cardName;
    /**
     * 订单类型：0-微信缴费 1-支付宝 3医保卡
     */
    @XmlElement(name="payType")
    private String payType;
    /**
     * 支付类型 挂号AP 缴费BL 住院IP 扫码SC
     */
    @XmlElement(name="orderType")
    private String orderType;
    /**
     * 状态：支付 "SUCCESS" 退费 "REFUND"
     */
    @XmlElement(name="transState")
    private String transState;
    /**
     * His流水号
     */
    @XmlElement(name="hisNo")
    private String hisNo;
    /**
     * 支付金额
     */
    @XmlElement(name="payFee")
    private String payFee;
    /**
     * 微信支付流水号
     */
    @XmlElement(name="treatNo")
    private String treatNo;
    /**
     * 订单号
     */
    @XmlElement(name="orderNo")
    private String orderNo;
    /**
     * 退费金额
     */
    @XmlElement(name="refundFee")
    private String refundFee;
    /**
     * 微信退费流水号
     */
    @XmlElement(name="refundNo")
    private String refundNo;
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getTransState() {
		return transState;
	}
	public void setTransState(String transState) {
		this.transState = transState;
	}
	public String getHisNo() {
		return hisNo;
	}
	public void setHisNo(String hisNo) {
		this.hisNo = hisNo;
	}
	public String getPayFee() {
		return payFee;
	}
	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}
	public String getTreatNo() {
		return treatNo;
	}
	public void setTreatNo(String treatNo) {
		this.treatNo = treatNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
    
}
