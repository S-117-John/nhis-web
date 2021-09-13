package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: tp_serv_unionpay_trading - 银联交易记录表(参照杉德接口) 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_UNIONPAY_TRADING")
public class TpServUnionpayTrading   {

    /** PK_UNIONPAY_TRADING - 银联交易主键 */
	@PK
	@Field(value="PK_UNIONPAY_TRADING",id=KeyId.UUID)
    private String pkUnionpayTrading;
	
	/** FK_DEPT - 所属机构 */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;
	
	/** FK_DEPT - 就诊主键 */
	@Field(value="pk_pv")
    private String pkPv;
	
    /** PK_PATIENT - 患者主键 */
	@Field(value="PK_PATIENT")
    private String pkPatient;

    /** AMOUNT - 金额 */
	@Field(value="AMOUNT")
    private String amount;

    /** OPERATETYPE - 操作类别 */
	@Field(value="OPERATETYPE")
    private String operatetype;

    /** TRANSTYPE - 交易类别 */
	@Field(value="TRANSTYPE")
    private String transtype;

    /** CARDTYPE - 卡类别 */
	@Field(value="CARDTYPE")
    private String cardtype;

    /** RESPONSECODE - 返回码 */
	@Field(value="RESPONSECODE")
    private String responsecode;

    /** RESPONSEMSG - 返回信息 */
	@Field(value="RESPONSEMSG")
    private String responsemsg;

    /** CASHREGNO - 收银机编号 */
	@Field(value="CASHREGNO")
    private String cashregno;

    /** CASHERNO - 柜员号 */
	@Field(value="CASHERNO")
    private String casherno;

    /** SELLTENUM - 结算批次 */
	@Field(value="SELLTENUM")
    private String selltenum;

    /** MERCHANTID - 商户号 */
	@Field(value="MERCHANTID")
    private String merchantid;

    /** MERCHANTNAME - 商户名称 */
	@Field(value="MERCHANTNAME")
    private String merchantname;

    /** TERMINALID - 终端号 */
	@Field(value="TERMINALID")
    private String terminalid;

    /** CARDNO - 银行卡号 */
	@Field(value="CARDNO")
    private String cardno;

    /** EXP_DATE - 卡有效期 */
	@Field(value="EXP_DATE")
    private String expDate;

    /** BANKNO - 发卡行编码 */
	@Field(value="BANKNO")
    private String bankno;

    /** TRANSDATE - 交易日期 */
	@Field(value="TRANSDATE")
    private String transdate;

    /** TRANSTIME - 交易时间 */
	@Field(value="TRANSTIME")
    private String transtime;

    /** AUTH_CODE - 授权号 */
	@Field(value="AUTH_CODE")
    private String authCode;

    /** SYSREFNO - 系统参照号 */
	@Field(value="SYSREFNO")
    private String sysrefno;

    /** CASHTRACENO - 收银流水号 */
	@Field(value="CASHTRACENO")
    private String cashtraceno;

    /** ORIGINTRACENO - 原收银流水号 */
	@Field(value="ORIGINTRACENO")
    private String origintraceno;

    /** SYSTRACDNO - 系统流水号 */
	@Field(value="SYSTRACDNO")
    private String systracdno;

    /** ORIGINSYSTRACENO - 原系统流水号 */
	@Field(value="ORIGINSYSTRACENO")
    private String originsystraceno;

    /** RESERVED - 预留字段 */
	@Field(value="RESERVED")
    private String reserved;

    /** WORKID - 操作员工号 */
	@Field(value="WORKID")
    private String workid;

    /** HTIME - 操作时间 */
	@Field(value="HTIME")
    private Date htime;

    /** BILL_STATUS - 对账状态，null：未对账，0：已对账 */
	@Field(value="BILL_STATUS")
    private String billStatus;

    /** BILL_TIME - 对账时间 */
	@Field(value="BILL_TIME")
    private Date billTime;

    /** BILL_DESC - 对账描述 */
	@Field(value="BILL_DESC")
    private String billDesc;

    /** PK_DATA_BILL - 对账明细编号 */
	@Field(value="PK_DATA_BILL")
    private String pkDataBill;

	public String getPkUnionpayTrading() {
		return pkUnionpayTrading;
	}

	public void setPkUnionpayTrading(String pkUnionpayTrading) {
		this.pkUnionpayTrading = pkUnionpayTrading;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPatient() {
		return pkPatient;
	}

	public void setPkPatient(String pkPatient) {
		this.pkPatient = pkPatient;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public Date getHtime() {
		return htime;
	}

	public void setHtime(Date htime) {
		this.htime = htime;
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

	public String getPkDataBill() {
		return pkDataBill;
	}

	public void setPkDataBill(String pkDataBill) {
		this.pkDataBill = pkDataBill;
	}
}