package com.zebone.nhis.pro.sd.wechat.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bill")
public class WechatResBillVo {
	@XmlElement(name="insuranceAmount")
    private String insuranceAmount;
	@XmlElement(name="orderType")
    private String orderType;
	@XmlElement(name="trade_state")
    private String tradeState;
	@XmlElement(name="trade_amt")
    private String tradeAmt;
	@XmlElement(name="refund_amt")
    private String refundAmt;
	@XmlElement(name="pay_method")
    private String payMethod;
	
	@XmlElement(name="userName")
    private String userName;
	
	@XmlElement(name="mch_id")
    private String mchId;
	
	@XmlElement(name="product_name")
    private String productName;
	@XmlElement(name="refundNo")
    private String refundNo;
	@XmlElement(name="trade_no")
    private String tradeNo;
	@XmlElement(name="payChannel")
    private String payChannel;
	
	@XmlElement(name="refund_insurance_amt")
    private String refundInsuranceAmt;
	
	@XmlElement(name="unit_id")
    private String unitId;
	@XmlElement(name="trade_time")
    private String tradeTime;
	
	@XmlElement(name="app_id")
    private String appId;
	@XmlElement(name="sub_mch_id")
    private String subMchId;
	
	@XmlElement(name="mch_trade_no")
    private String mchTradeNo;
	
	@XmlElement(name="trade_type")
    private String tradeType;
	
	//@XmlElement(name="refund_no")
   // private String refundNo;
	
	@XmlElement(name="mch_refund_no")
    private String mchRefundNo;
	
	@XmlElement(name="refund_type")
    private String refund_type;
	
	@XmlElement(name="refund_state")
    private String refundState;
	
	@XmlElement(name="fee_rate")
    private String feeRate;
	
	@XmlElement(name="payHisInvoiceNo")
    private String payHisInvoiceNo;

	
	@XmlElement(name="business_type")
    private String businessType;
	
	@XmlElement(name="cardNo")
    private String card_no;
	
	@XmlElement(name="hisPayNo")
    private String hisPayNo;
	
	@XmlElement(name="trade_medical_amt")
    private String tradeMedicalAmt;
	
	@XmlElement(name="trade_total_amt")
    private String tradeTotalAmt;
	
	@XmlElement(name="refund_medical_amt")
    private String refundMedicalAmt;
	
	@XmlElement(name="refundTotalFee")
    private String refundTotalFee;
	
	@XmlElement(name="totalAmount")
    private String totalAmount;

	
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInsuranceAmount() {
		return insuranceAmount;
	}

	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getTradeAmt() {
		return tradeAmt;
	}

	public void setTradeAmt(String tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	public String getRefundAmt() {
		return refundAmt;
	}

	public void setRefundAmt(String refundAmt) {
		this.refundAmt = refundAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}


	public String getRefundTotalFee() {
		return refundTotalFee;
	}

	public void setRefundTotalFee(String refundTotalFee) {
		this.refundTotalFee = refundTotalFee;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getRefundInsuranceAmt() {
		return refundInsuranceAmt;
	}

	public void setRefundInsuranceAmt(String refundInsuranceAmt) {
		this.refundInsuranceAmt = refundInsuranceAmt;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getMchTradeNo() {
		return mchTradeNo;
	}

	public void setMchTradeNo(String mchTradeNo) {
		this.mchTradeNo = mchTradeNo;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getMchRefundNo() {
		return mchRefundNo;
	}

	public void setMchRefundNo(String mchRefundNo) {
		this.mchRefundNo = mchRefundNo;
	}

	public String getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(String refund_type) {
		this.refund_type = refund_type;
	}

	public String getRefundState() {
		return refundState;
	}

	public void setRefundState(String refundState) {
		this.refundState = refundState;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getPayHisInvoiceNo() {
		return payHisInvoiceNo;
	}

	public void setPayHisInvoiceNo(String payHisInvoiceNo) {
		this.payHisInvoiceNo = payHisInvoiceNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getHisPayNo() {
		return hisPayNo;
	}

	public void setHisPayNo(String hisPayNo) {
		this.hisPayNo = hisPayNo;
	}

	public String getTradeMedicalAmt() {
		return tradeMedicalAmt;
	}

	public void setTradeMedicalAmt(String tradeMedicalAmt) {
		this.tradeMedicalAmt = tradeMedicalAmt;
	}

	public String getTradeTotalAmt() {
		return tradeTotalAmt;
	}

	public void setTradeTotalAmt(String tradeTotalAmt) {
		this.tradeTotalAmt = tradeTotalAmt;
	}

	public String getRefundMedicalAmt() {
		return refundMedicalAmt;
	}

	public void setRefundMedicalAmt(String refundMedicalAmt) {
		this.refundMedicalAmt = refundMedicalAmt;
	}

}
