package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 第三方支付宝支付对账单
 * @author songs
 * @date 2017-7-19
 */
@Table(value = "PAY_ALI_BILL")
public class PayAliBill {

	@PK
	@Field(value = "PK_PAY_ALI_BILL", id = KeyId.UUID)
	private String pkPayAliBill;//支付宝对账单主键
	
	@Field(value = "BILL_BACKAGE_PK")
	private String billBackagePk;//账单包主键

	@Field(value = "TRANSAC_NO")
	private String transacNo;//支付宝交易号

	@Field(value = "ORDER_NO")
	private String orderNo;//商户订单号

	@Field(value = "SERVICE_TYPE")
	private String serviceType;//业务类型

	@Field(value = "GOODS_NAME")
	private String goodsName;//商品名称

	@Field(value = "CREATE_TIME")
	private String createTime;//订单创建时间

	@Field(value = "FINISH_TIME")
	private String finishTime;//订单完成时间

	@Field(value = "STORE_NO")
	private String storeNo;//门店编号

	@Field(value = "STORE_NAME")
	private String storeName;//门店名称

	@Field(value = "OPERATOR")
	private String operator;//操作员

	@Field(value = "TERMINAL_NO")
	private String terminalNo;//终端号

	@Field(value = "ACCOUNT")
	private String account;//对方账户

	@Field(value = "ORDER_MONEY")
	private BigDecimal orderMoney;//订单金额
	
	@Field(value = "SELLER_INCOME")
	private BigDecimal sellerIncome;//商家实收

	@Field(value = "ALIPAY_RED")
	private BigDecimal alipayRed;//支付宝红包

	@Field(value = "POINTS_TREASURE")
	private BigDecimal pointsTreasure;//集分宝

	@Field(value = "ALIPAY_OFFERS")
	private BigDecimal alipayOffers;//支付宝优惠

	@Field(value = "SELLER_DISCOUNT")
	private BigDecimal sellerDiscount;//商家优惠

	@Field(value = "COUPON_MONEY")
	private BigDecimal couponMoney;//券核销金额

	@Field(value = "COUPON_NAME")
	private String couponName;//券名称

	@Field(value = "SELLER_RED")
	private BigDecimal sellerRed;//商家红包消费金额

	@Field(value = "CARD_AMOUNT")
	private BigDecimal cardAmount;//卡消费金额

	@Field(value = "REFUND_NO")
	private String refundNo;//退款批次号/请求号

	@Field(value = "SERVICE_CHARGE")
	private BigDecimal serviceCharge;//服务费

	@Field(value = "SHARE_PROFIT")
	private BigDecimal shareProfit;//分润

	@Field(value = "REMARKS")
	private String remarks;//备注

	@Field(value = "GENERATE_TIME")
	private Date generateTime;//记录生成时间
	
	@Field(value = "BILL_STATUS")
	private String billStatus;//对账状态：0未对账、1对账成功、2金额不一致、3单边账
	
	@Field(value = "BILL_TIME")
	private Date billTime;//对账时间
	
	@Field(value = "BILL_DESC")
	private String billDesc;//对账描述

	public PayAliBill(){
		
	}
	

	public PayAliBill(String[] data){
		this.transacNo 			= data[0].trim();//支付宝交易号
		this.orderNo 			= data[1].trim();//商户订单号
		this.serviceType 		= data[2].trim();//业务类型
		this.goodsName 			= data[3].trim();//商品名称
		this.createTime 		= data[4].trim();//订单创建时间
		this.finishTime 		= data[5].trim();//订单完成时间
		this.storeNo 			= data[6].trim();//门店编号
		this.storeName 			= data[7].trim();//门店名称
		this.operator 			= data[8].trim();//操作员
		this.terminalNo 		= data[9].trim();//终端号
		this.account 			= data[10].trim();//对方账户
		this.orderMoney 		= new BigDecimal(data[11].trim());//订单金额
		this.sellerIncome 		= new BigDecimal(data[12].trim());//商家实收
		this.alipayRed 			= new BigDecimal(data[13].trim());//支付宝红包
		this.pointsTreasure 	= new BigDecimal(data[14].trim());//集分宝
		this.alipayOffers 		= new BigDecimal(data[15].trim());//支付宝优惠
		this.sellerDiscount		= new BigDecimal(data[16].trim());//商家优惠
		this.couponMoney 		= new BigDecimal(data[17].trim());//券核销金额
		this.couponName 		= data[18].trim();//券名称
		this.sellerRed 			= new BigDecimal(data[19].trim());//商家红包消费金额
		this.cardAmount 		= new BigDecimal(data[20].trim());//卡消费金额
		this.refundNo 			= data[21].trim();//退款批次号/请求号
		this.serviceCharge 		= new BigDecimal(data[22].trim());//服务费
		this.shareProfit 		= new BigDecimal(data[23].trim());//分润
		this.remarks 			= data[24].trim();//备注
		this.generateTime 		= new Date();//记录生成时间
		this.billStatus 		= "0";//对账状态：0未对账、1对账成功、2对账失败
	}
	
	
	public String getPkPayAliBill() {
		return pkPayAliBill;
	}

	public void setPkPayAliBill(String pkPayAliBill) {
		this.pkPayAliBill = pkPayAliBill;
	}

	public String getTransacNo() {
		return transacNo;
	}

	public void setTransacNo(String transacNo) {
		this.transacNo = transacNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getSellerIncome() {
		return sellerIncome;
	}

	public void setSellerIncome(BigDecimal sellerIncome) {
		this.sellerIncome = sellerIncome;
	}

	public BigDecimal getAlipayRed() {
		return alipayRed;
	}

	public void setAlipayRed(BigDecimal alipayRed) {
		this.alipayRed = alipayRed;
	}

	public BigDecimal getPointsTreasure() {
		return pointsTreasure;
	}

	public void setPointsTreasure(BigDecimal pointsTreasure) {
		this.pointsTreasure = pointsTreasure;
	}

	public BigDecimal getAlipayOffers() {
		return alipayOffers;
	}

	public void setAlipayOffers(BigDecimal alipayOffers) {
		this.alipayOffers = alipayOffers;
	}

	public BigDecimal getSellerDiscount() {
		return sellerDiscount;
	}

	public void setSellerDiscount(BigDecimal sellerDiscount) {
		this.sellerDiscount = sellerDiscount;
	}

	public BigDecimal getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(BigDecimal couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public BigDecimal getSellerRed() {
		return sellerRed;
	}

	public void setSellerRed(BigDecimal sellerRed) {
		this.sellerRed = sellerRed;
	}

	public BigDecimal getCardAmount() {
		return cardAmount;
	}

	public void setCardAmount(BigDecimal cardAmount) {
		this.cardAmount = cardAmount;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public BigDecimal getShareProfit() {
		return shareProfit;
	}

	public void setShareProfit(BigDecimal shareProfit) {
		this.shareProfit = shareProfit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}


	public String getBillStatus() {
		return billStatus;
	}


	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}


	public String getBillDesc() {
		return billDesc;
	}


	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}


	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public String getBillBackagePk() {
		return billBackagePk;
	}

	public void setBillBackagePk(String billBackagePk) {
		this.billBackagePk = billBackagePk;
	}


}