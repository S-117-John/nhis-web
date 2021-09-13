package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 衫德银联pos机交易记录
 * @author zrj
 * @date 2018-1-10
 */
@Table(value = "PAY_UNIONPAY_RECORD")
public class PayUnionpayRecord {

	@PK
	@Field(value = "PK_UNIONPAY_RECORD", id = KeyId.UUID)
	private String pkUnionpayRecord;//衫德银联交易主键
	
	@Field(value = "PK_ORG")
	private String pkOrg;//机构主键

	@Field(value = "PK_EXTPAY")
	private String pkExtpay;//支付主键

	@Field(value = "PK_PI")
	private String pkPi;//患者主键
	
	@Field(value = "PK_PV")
	private String pkPv;//就诊主键
	
	@Field(value = "SYSTEM_MODULE")
	private String systemModule;//系统或模块
	
	@Field(value = "PRODUCT_ID")
	private String productId;//产品主键
	
	@Field(value = "OUT_TRADE_NO")
	private String outTradeNo;//商户订单号
	
	@Field(value = "TRADE_STATE")
	private String tradeState;//交易状态，INIT-待支付、SUCCESS—成功、FAIL-失败

	@Field(value = "OPERATETYPE")
	private String operatetype;//操作类型

	@Field(value = "TRANSTYPE")
	private String transtype;//交易类型：31消费、41撤销、71退货

	@Field(value = "CARDTYPE")
	private String cardtype;//卡类型

	@Field(value = "RESPONSECODE")
	private String responsecode;//返回码

	@Field(value = "RESPONSEMSG")
	private String responsemsg;//返回信息

	@Field(value = "CASHREGNO")
	private String cashregno;//收银机编号

	@Field(value = "CASHERNO")
	private String casherno;//柜员号

	@Field(value = "AMOUNT")
	private BigDecimal amount;//金额，元

	@Field(value = "SELLTENUM")
	private String selltenum;//结算批次

	@Field(value = "MERCHANTID")
	private String merchantid;//商户号

	@Field(value = "MERCHANTNAME")
	private String merchantname;//商户名称
	
	@Field(value = "TERMINALID")
	private String terminalid;//终端号

	@Field(value = "CARDNO")
	private String cardno;//卡号

	@Field(value = "EXP_DATE")
	private String expDate;//卡有效期（不具有日期格式）

	@Field(value = "BANKNO")
	private String bankno;//发卡行编码

	@Field(value = "TRANSDATE")
	private String transdate;//交易日期（YYYYMMDD）

	@Field(value = "TRANSTIME")
	private String transtime;//交易时间(HHMMSS)

	@Field(value = "AUTH_CODE")
	private String authCode;//授权号

	@Field(value = "SYSREFNO")
	private String sysrefno;//系统参照号
	
	@Field(value = "ORIGIN_SYSREFNO")
	private String originSysrefno;//原系统参照号，被退货后填写该值

	@Field(value = "CASHTRACENO")
	private String cashtraceno;//收银流水号

	@Field(value = "ORIGINTRACENO")
	private String origintraceno;//原收银流水号

	@Field(value = "SYSTRACDNO")
	private String systracdno;//系统流水号

	@Field(value = "ORIGINSYSTRACENO")
	private String originsystraceno;//原系统流水号

	@Field(value = "RESERVED")
	private String reserved;//预留字段
	
	@Field(value = "BILL_STATUS")
	private String billStatus;//对账状态：0未对账、1对账成功、2、金额不一致、3单边账、4、交易类型不一致
	
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

	public String getPkUnionpayRecord() {
		return pkUnionpayRecord;
	}

	public void setPkUnionpayRecord(String pkUnionpayRecord) {
		this.pkUnionpayRecord = pkUnionpayRecord;
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

	public String getSystemModule() {
		return systemModule;
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
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOperatetype() {
		return operatetype;
	}

	public void setOperatetype(String operatetype) {
		this.operatetype = operatetype;
	}

	public String getTranstype() {
		return transtype;
	}

	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getResponsemsg() {
		return responsemsg;
	}

	public void setResponsemsg(String responsemsg) {
		this.responsemsg = responsemsg;
	}

	public String getCashregno() {
		return cashregno;
	}

	public void setCashregno(String cashregno) {
		this.cashregno = cashregno;
	}

	public String getCasherno() {
		return casherno;
	}

	public void setCasherno(String casherno) {
		this.casherno = casherno;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSelltenum() {
		return selltenum;
	}

	public void setSelltenum(String selltenum) {
		this.selltenum = selltenum;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public String getMerchantname() {
		return merchantname;
	}

	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public String getTerminalid() {
		return terminalid;
	}

	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getTransdate() {
		return transdate;
	}

	public void setTransdate(String transdate) {
		this.transdate = transdate;
	}

	public String getTranstime() {
		return transtime;
	}

	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getSysrefno() {
		return sysrefno;
	}

	public void setSysrefno(String sysrefno) {
		this.sysrefno = sysrefno;
	}
	
	public String getOriginSysrefno() {
		return originSysrefno;
	}

	public void setOriginSysrefno(String originSysrefno) {
		this.originSysrefno = originSysrefno;
	}

	public String getCashtraceno() {
		return cashtraceno;
	}

	public void setCashtraceno(String cashtraceno) {
		this.cashtraceno = cashtraceno;
	}

	public String getOrigintraceno() {
		return origintraceno;
	}

	public void setOrigintraceno(String origintraceno) {
		this.origintraceno = origintraceno;
	}

	public String getSystracdno() {
		return systracdno;
	}

	public void setSystracdno(String systracdno) {
		this.systracdno = systracdno;
	}

	public String getOriginsystraceno() {
		return originsystraceno;
	}

	public void setOriginsystraceno(String originsystraceno) {
		this.originsystraceno = originsystraceno;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
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

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

}