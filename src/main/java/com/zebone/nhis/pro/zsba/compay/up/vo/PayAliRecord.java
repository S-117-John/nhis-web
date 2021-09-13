package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 第三方支付宝交易记录
 * @author songs
 * @date 2017-7-19
 */
@Table(value = "PAY_ALI_RECORD")
public class PayAliRecord { 

	@PK
	@Field(value = "PK_PAY_ALI_RECORD", id = KeyId.UUID)
	private String pkPayAliRecord;//支付宝交易记录主键

	@Field(value = "PK_ORG")
	private String pkOrg;//机构主键
	
	@Field(value = "PK_EXTPAY")
	private String pkExtpay;//支付主键
	
	@Field(value = "PK_PI")
	private String pkPi;//患者主键
	
	@Field(value = "PK_PV")
	private String pkPv;//就诊主键

	@Field(value = "APPID")
	private String appid;//应用ID
	
	@Field(value = "TERMINAL_ID")
	private String terminalId;//商户机具终端编号|操作员工号
	
	@Field(value = "SYSTEM_MODULE")
	private String systemModule="nhis";//系统或模块
	
	@Field(value = "PRODUCT_ID")
	private String productId="nhis";//产品主键

	@Field(value = "ORDER_TYPE")
	private String orderType;//订单类型, pay:消费, refund:退费

	@Field(value = "ALI_TRADE_NO")
	private String aliTradeNo;//支付宝交易号

	@Field(value = "OUT_TRADE_NO")
	private String outTradeNo;//商户订单号
	
	@Field(value = "REFUND_TRADE_NO")
	private String refundTradeNo;//退款单号
	
	@Field(value = "BODY")
	private String body;//商品名称
	
	@Field(value = "DETAIL")
	private String detail; //商品详情

	@Field(value = "TOTAL_AMOUNT")
	private BigDecimal totalAmount;//总金额，元
	
	@Field(value = "ATTACH")
	private String attach; //附加数据
	
	@Field(value = "TRADE_STATE")
	private String tradeState;//交易状态, 订单类型pay{INIT-生成待支付、SUCCESS—支付成功}、订单类型refund{SUCCESS-退款成功、FAIL-退款失败}

	@Field(value = "BUYER_ID")
	private String buyerId;//支付用户标识
	
	@Field(value = "PAY_TIME")
	private String payTime;//支付完成时间
	
	@Field(value = "INIT_DATA")
	private String initData;//下单返回的数据
	
	@Field(value = "PAY_DATA")
	private String payData;//支付返回的数据
	
	@Field(value = "REFUND_DATA")
	private String refundData;//退款返回的数据
	
	@Field(value = "BILL_STATUS")
	private String billStatus;//对账状态：0未对账、1对账成功、2金额不一致、3单边账
	
	@Field(value = "BILL_TIME")
	private Date billTime;//对账时间
	
	@Field(value = "BILL_DESC")
	private String billDesc;//对账描述
	
	@Field(value = "NHIS_BILL_STATUS")
	private String nhisBillStatus;//对账状态：0未对账、1对账成功、2金额不一致、3单边账
	
	@Field(value = "NHIS_BILL_TIME")
	private Date nhisBillTime;//对账时间
	
	@Field(value = "NHIS_BILL_DESC")
	private String nhisBillDesc;//对账描述
	
	@Field(value = "REMARKS")
	private String remarks;//备注
	
	@Field(value = "DEL_FLAG")
	private String delFlag;//删除标志，0正常，1删除
	
	@Field(value = "CREATOR")
	private String creator;//创建者
	
	@Field(value = "CREATE_TIME")
	private Date createTime;//创建时间
	
	@Field(value = "MODIFIER")
	private String modifier;//更新者
	
	@Field(value = "MODITY_TIME",date=FieldType.UPDATE)
	private Date modityTime;//更新时间

	
	


	public String getPkPayAliRecord() {
		return pkPayAliRecord;
	}

	public void setPkPayAliRecord(String pkPayAliRecord) {
		this.pkPayAliRecord = pkPayAliRecord;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkExtpay() {
		return pkExtpay;
	}

	public void setPkExtpay(String pkExtpay) {
		this.pkExtpay = pkExtpay;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSystemModule() {
		return systemModule;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public void setSystemModule(String systemModule) {
		this.systemModule = systemModule;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getAliTradeNo() {
		return aliTradeNo;
	}

	public void setAliTradeNo(String aliTradeNo) {
		this.aliTradeNo = aliTradeNo;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getRefundTradeNo() {
		return refundTradeNo;
	}

	public void setRefundTradeNo(String refundTradeNo) {
		this.refundTradeNo = refundTradeNo;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getInitData() {
		return initData;
	}

	public void setInitData(String initData) {
		this.initData = initData;
	}

	public String getPayData() {
		return payData;
	}

	public void setPayData(String payData) {
		this.payData = payData;
	}

	public String getRefundData() {
		return refundData;
	}

	public void setRefundData(String refundData) {
		this.refundData = refundData;
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

	public String getNhisBillStatus() {
		return nhisBillStatus;
	}

	public void setNhisBillStatus(String nhisBillStatus) {
		this.nhisBillStatus = nhisBillStatus;
	}

	public Date getNhisBillTime() {
		return nhisBillTime;
	}

	public void setNhisBillTime(Date nhisBillTime) {
		this.nhisBillTime = nhisBillTime;
	}

	public String getNhisBillDesc() {
		return nhisBillDesc;
	}

	public void setNhisBillDesc(String nhisBillDesc) {
		this.nhisBillDesc = nhisBillDesc;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
}