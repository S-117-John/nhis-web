package com.zebone.nhis.ma.pub.sd.vo.health;

import java.math.BigDecimal;

/**
 * 电子健康码 PM020 请求类
 * @author zhangtao
 *
 */
public class EhealthCodePM020ReqVO {

	/**
	 * 医院唯一标识 pkOrg
	 */
	private String orgUuid;
	
	/**
	 * 患者唯一id
	 */
	private String patientId;
	
	/**
	 * 就诊卡号码
	 */
	private String medicalCard;
	
	/**
	 * 原收费单据ID号
	 */
	private String receiptId;
	
	/**
	 * 退款单据号
	 */
	private String refundId;
	
	/**
	 * 业务系统订单号
	 */
	private String payOrderSerial;
	
	/**
	 * 支付交易流水号
	 */
	private String paySerial;
	
	/**
	 * 部分退款时，新的收费单据ID号
	 */
	private String receiptIdNew;
	
	/**
	 * 退还费用（分）
	 */
	private BigDecimal refundCost;
	
	/**
	 * 自费退款金额（分）
	 */
	private BigDecimal cashAmount;
	
	/**
	 * 医保退款金额（分）
	 */
	private BigDecimal insurAmount;
	
	/**
	 * 退还日期 格式为yyyy-MM-dd HH:mm:ss
	 */
	private String refundTime;
	
	/**
	 * 退费类型 1 挂号退费,2 门诊退费,3住院押金退费
	 */
	private String refundType;
	
	/**
	 * 电子健康卡ID
	 */
	private String ehealthCardId;

	public String getOrgUuid() {
		return orgUuid;
	}

	public void setOrgUuid(String orgUuid) {
		this.orgUuid = orgUuid;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getMedicalCard() {
		return medicalCard;
	}

	public void setMedicalCard(String medicalCard) {
		this.medicalCard = medicalCard;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getPayOrderSerial() {
		return payOrderSerial;
	}

	public void setPayOrderSerial(String payOrderSerial) {
		this.payOrderSerial = payOrderSerial;
	}

	public String getPaySerial() {
		return paySerial;
	}

	public void setPaySerial(String paySerial) {
		this.paySerial = paySerial;
	}

	public String getReceiptIdNew() {
		return receiptIdNew;
	}

	public void setReceiptIdNew(String receiptIdNew) {
		this.receiptIdNew = receiptIdNew;
	}

	public BigDecimal getRefundCost() {
		return refundCost;
	}

	public void setRefundCost(BigDecimal refundCost) {
		this.refundCost = refundCost;
	}

	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public BigDecimal getInsurAmount() {
		return insurAmount;
	}

	public void setInsurAmount(BigDecimal insurAmount) {
		this.insurAmount = insurAmount;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundType() {
		return refundType;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public String getEhealthCardId() {
		return ehealthCardId;
	}

	public void setEhealthCardId(String ehealthCardId) {
		this.ehealthCardId = ehealthCardId;
	}

}
