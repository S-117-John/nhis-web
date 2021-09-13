package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 银联对账单数据Entity
 * @author lipz
 * @version 2019-12-10
 */
@Table(value = "PAY_UNIONPAY_BILL")
public class PayUnionpayBill {

	@PK
	@Field(value = "PK_PAY_UNIONPAY_BILL", id = KeyId.UUID)
	private String pkPayUnionpayBill;// 银联对账单主键
	
	@Field(value = "BILL_BACKAGE_PK")
	private String billBackagePk;//账单包主键
	
	@Field(value = "MCH_NO")
	private String mchNo;//商户号
	
	@Field(value = "TERMINAL_NO")
	private String terminalNo;//终端号
	
	@Field(value = "CARD_NO")
	private String cardNo;//银行卡号
	
	@Field(value = "TRANS_TYPE")
	private String transType;//交易类型：31消费、41撤销、71退货
	
	@Field(value = "TRANS_TYPE_NAME")
	private String transTypeName;//交易类型名称
	
	@Field(value = "TRANS_DATE")
	private String transDate;//交易日期
	
	@Field(value = "TRANS_TIME")
	private String transTime;//交易时间
	
	@Field(value = "SETT_DATE")
	private String settDate;//结算日期
	
	@Field(value = "TRANS_AMOUNT")
	private BigDecimal transAmount;//交易金额
	
	@Field(value = "FORMALITIES")
	private BigDecimal formalities;//手续费
	
	@Field(value = "REAL_AMOUNT")
	private BigDecimal realAmount;//实拨金额
	
	@Field(value = "CARD_TYPE_NAME")
	private String cardTypeName;//借贷卡种：1借记卡、2贷记卡
	
	@Field(value = "SYS_REF_NO")
	private String sysRefNo;//系统参照号
	
	@Field(value = "PAY_METHOD")
	private String payMethod;//支付方式：1银联、2支付宝、3微信
	
	@Field(value = "PAY_METHOD_NAME")
	private String payMethodName;//支付方式名称
	
	@Field(value = "CREATE_TIME")
	private Date createTime;//生成时间
	
	@Field(value = "BILL_STATUS")
	private String billStatus;//对账状态：0未对账、1对账成功、2、金额不一致、3单边账、4、交易类型不一致
	
	@Field(value = "BILL_TIME")
	private Date billTime;//对账时间
	
	@Field(value = "BILL_DESC")
	private String billDesc;//对账描述
	
	
	public PayUnionpayBill(){}
	
	public PayUnionpayBill(String billBackagePk, String[] arrayData){
		this.billBackagePk = billBackagePk;
		this.mchNo = arrayData[0];//商户号
		this.terminalNo = arrayData[1];//终端号
		this.cardNo = arrayData[2];//银行卡号
		this.transType = arrayData[3].equals("消费")?"31":arrayData[3].equals("消费撤销")?"41":"71";//交易类型：31消费、41撤销、71退货
		this.transTypeName = arrayData[3];//
		this.transDate = arrayData[4];//交易日期
		this.transTime = arrayData[5];//交易时间
		this.settDate = arrayData[6];//结算日期
		this.transAmount = numberFormatString(arrayData[7]);//交易金额
		this.formalities = numberFormatString(arrayData[8]);//手续费
		this.realAmount = numberFormatString(arrayData[9]);//实拨金额
		this.cardTypeName = arrayData[10];//借贷卡种：借记卡、贷记卡
		this.sysRefNo = arrayData[11];//系统参照号
		this.payMethod = arrayData[12].equals("银行卡")?"1":arrayData[12].equals("支付宝")?"2":"3";//支付方式：1银联、2支付宝、3微信
		this.payMethodName = arrayData[12];//支付方式名称
		this.createTime = new Date();//生成时间
		this.billStatus = "0";//对账状态：0未对账、1对账成功、2对账失败
		this.billTime = null;//对账时间
		this.billDesc = null;//对账描述
	}
	
	/**
	 * 格式化双精度，保留两个小数
	 * @return
	 */
	public BigDecimal numberFormatString(String b) {
		BigDecimal bg = new BigDecimal(b);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public String getPkPayUnionpayBill() {
		return pkPayUnionpayBill;
	}
	public void setPkPayUnionpayBill(String pkPayUnionpayBill) {
		this.pkPayUnionpayBill = pkPayUnionpayBill;
	}

	public String getMchNo() {
		return mchNo;
	}
	public void setMchNo(String mchNo) {
		this.mchNo = mchNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransTypeName() {
		return transTypeName;
	}
	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}

	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getSettDate() {
		return settDate;
	}
	public void setSettDate(String settDate) {
		this.settDate = settDate;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public BigDecimal getFormalities() {
		return formalities;
	}
	public void setFormalities(BigDecimal formalities) {
		this.formalities = formalities;
	}

	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}

	public String getSysRefNo() {
		return sysRefNo;
	}
	public void setSysRefNo(String sysRefNo) {
		this.sysRefNo = sysRefNo;
	}

	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayMethodName() {
		return payMethodName;
	}
	public void setPayMethodName(String payMethodName) {
		this.payMethodName = payMethodName;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public Date getBillTime() {
		return billTime;
	}
	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public String getBillDesc() {
		return billDesc;
	}
	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}
	

	public String getBillBackagePk() {
		return billBackagePk;
	}

	public void setBillBackagePk(String billBackagePk) {
		this.billBackagePk = billBackagePk;
	}

	
	
	
}
