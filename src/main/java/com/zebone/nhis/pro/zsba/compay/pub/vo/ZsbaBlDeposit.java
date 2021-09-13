package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BL_DEPOSIT - 中山博爱收费结算-交款记录
 *
 * @since 2016-10-13 01:58:07
 */
@Table(value = "BL_DEPOSIT")
public class ZsbaBlDeposit extends BaseModule implements Cloneable{
	
	private static final long serialVersionUID = 1L;

	public Object clone() {
		ZsbaBlDeposit o = null;
		try {
			o = (ZsbaBlDeposit) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	/** PK_DEPO - 交款记录主键 */
	@PK
	@Field(value = "PK_DEPO", id = KeyId.UUID)
	private String pkDepo;

	/** EU_DPTYPE - 收付款类型 0 就诊结算；1 中途结算；2 结算冲账；3 欠费补缴；4 取消结算；9 住院预交金 */
	@Field(value = "EU_DPTYPE")
	private String euDptype;

	/** EU_DIRECT - 收退方向 1收 -1退 */
	@Field(value = "EU_DIRECT")
	private String euDirect;

	/** PK_PI - 患者主键 */
	@Field(value = "PK_PI")
	private String pkPi;

	/** PK_PV - 就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** AMOUNT - 交款金额 */
	@Field(value = "AMOUNT")
	private BigDecimal amount;

	/** DT_PAYMODE - 收付款方式 例如：现金；支票；银行卡；账户 */
	@Field(value = "DT_PAYMODE")
	private String dtPaymode;

	/** DT_BANK - 银行 银行卡或支票支付时对应基础数据的银行档案 */
	@Field(value = "DT_BANK")
	private String dtBank;

	/** BANK_NO - 银行卡号 银行卡支付时，对应的银行卡号 */
	@Field(value = "BANK_NO")
	private String bankNo;

	/** PAY_INFO - 收付款方式信息 对应支票号，银行交易号码等 */
	@Field(value = "PAY_INFO")
	private String payInfo;

	/** DATE_PAY - 收付款日期 */
	@Field(value = "DATE_PAY")
	private Date datePay;

	/** PK_DEPT - 收付款部门 */
	@Field(value = "PK_DEPT")
	private String pkDept;

	/** PK_EMP_PAY - 收款人 */
	@Field(value = "PK_EMP_PAY")
	private String pkEmpPay;

	/** NAME_EMP_PAY - 收款人名称 */
	@Field(value = "NAME_EMP_PAY")
	private String nameEmpPay;

	/** FLAG_ACC - 账户支付标志 */
	@Field(value = "FLAG_ACC")
	private String flagAcc;

	/** PK_ACC - 账户主键 */
	@Field(value = "PK_ACC")
	private String pkAcc;

	/** FLAG_SETTLE - 结算标志 */
	@Field(value = "FLAG_SETTLE")
	private String flagSettle;

	/** PK_SETTLE - 结算主键 */
	@Field(value = "PK_SETTLE")
	private String pkSettle;

	/** FLAG_CC - 操作员结账标志 */
	@Field(value = "FLAG_CC")
	private String flagCc;

	/** PK_CC - 操作员结账主键 */
	@Field(value = "PK_CC")
	private String pkCc;

	/** REPT_NO - 收据编号 */
	@Field(value = "REPT_NO")
	private String reptNo;

	/** FLAG_REPT_BACK - 表示预交金收据收回的标志 */
	@Field(value = "FLAG_REPT_BACK")
	private String flagReptBack;

	/** DATE_REPT_BACK - 收据收回日期 */
	@Field(value = "DATE_REPT_BACK")
	private Date dateReptBack;

	/** PK_EMP_BACK - 收据收回人员 */
	@Field(value = "PK_EMP_BACK")
	private String pkEmpBack;

	/** NAME_EMP_BACK - 收据收回人员名称 */
	@Field(value = "NAME_EMP_BACK")
	private String nameEmpBack;

	/** PK_DEPO_BACK - 如果退费时，对应被退费的收款纪录 */
	@Field(value = "PK_DEPO_BACK")
	private String pkDepoBack;

	/** PK_ST_MID - 在中途结算转入预交金时，对应的结算主键 */
	@Field(value = "PK_ST_MID")
	private String pkStMid;

	/** NOTE - 交款描述信息 */
	@Field(value = "NOTE")
	private String note;

	/**
	 * 11：门禁卡
	 */
	@Field(value = "EU_PVTYPE")
	private String euPvtype;

	@Field(value = "PK_EMPINVOICE")
	private String pkEmpinvoice;
	/**  交款编码 */
	@Field(value = "CODE_DEPO")
	private String codeDepo;
	
	/**票据结账标志 */
	@Field(value="FLAG_CC_REPT")
    private String flagCcRept;
	/**票据结账主键 */
	@Field(value="PK_CC_REPT")
    private String pkCcRept;
	/**票据打印日期 */
	@Field(value="DATE_REPT")
    private Date dateRept;
	/**票据打印人员*/
	@Field(value="PK_EMP_REPT")
    private String pkEmpRept;
	/**票据打印人员姓名 */
	@Field(value="NAME_EMP_REPT")
    private String nameEmpRept;
	
	/** 收付款方式名称 */
	private String paymodeName;

	/** 汇总时数量 */
	private Integer cnt;

	/** 银行时间 **/
	private Date bankTime;

	/** pos机编码 **/
	private String outTradeNo;

	/** 结算标志 **/
	private String payResult;

	/** 交易流水号 **/
	private String serialNum;
	
	/** 第三方支付金额 **/
	private BigDecimal extAmount;

	/** 就诊状态：pv_encounter.eu_status **/
	private String euStatus;
	
	/**第三方订单号**/
	//@Field(value = "trade_no")
	private String tradeNo;
	
	
	/**
	 * 作废标志 0正常，1退费，2作废， 3:被作废或退费的押金
	 */
	@Field(value = "VOID_TYPE")
	private String voidType;
	
	/**
	 * 作废人
	 */
	@Field(value = "VOID_EMP")
	private String voidEMP;
	
	/**
	 * 作废日期
	 */
	@Field(value = "VOID_TIME")
	private Date voidTIME;

	/**
	 * 旧系统门诊患者id；用于内部转账，如日间手术费用
	 */
	@Field(value = "MZ_PATIENT_ID")
	private String mzPatientId;
	
	/**
	 * 旧系统门诊结账次数；用于内部转账，如日间手术费用
	 */
	@Field(value = "LEDGER_SN")
	private String ledgerSn;
	
	/**
	 * 旧系统门诊发票流水号；用于内部转账，如日间手术费用
	 */
	@Field(value = "RECEIPT_SN")
	private String receiptSn;
	
	/**
	 * 汇款单位  财务转账用的
	 */
	@Field(value = "REMITTANCE_UNIT")
	private String remittanceUnit;
	
	/**
	 * 新系统门诊结算ID；用于内部转账，如日间手术费用
	 */
	@Field(value = "PK_SETTLE_OP")
	private String pkSettleOp;
	
	public String getEuStatus() {
		return euStatus;
	}
	
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getCodeDepo() {
		return codeDepo;
	}

	public void setCodeDepo(String codeDepo) {
		this.codeDepo = codeDepo;
	}

	public void setPkEmpinvoice(String pkEmpinvoice) {
		this.pkEmpinvoice = pkEmpinvoice;
	}

	public BigDecimal getExtAmount() {
		return extAmount;
	}

	public void setExtAmount(BigDecimal extAmount) {
		this.extAmount = extAmount;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Date getBankTime() {
		return bankTime;
	}

	public void setBankTime(Date bankTime) {
		this.bankTime = bankTime;
	}

	public String getPkDepo() {
		return this.pkDepo;
	}

	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}

	public String getEuDptype() {
		return this.euDptype;
	}

	public void setEuDptype(String euDptype) {
		this.euDptype = euDptype;
	}

	public String getEuDirect() {
		return this.euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}

	public String getPkPi() {
		return this.pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return this.pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDtPaymode() {
		return this.dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	public String getDtBank() {
		return this.dtBank;
	}

	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}

	public String getBankNo() {
		return this.bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getPayInfo() {
		return this.payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public Date getDatePay() {
		return this.datePay;
	}

	public void setDatePay(Date datePay) {
		this.datePay = datePay;
	}

	public String getPkDept() {
		return this.pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpPay() {
		return this.pkEmpPay;
	}

	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}

	public String getNameEmpPay() {
		return this.nameEmpPay;
	}

	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}

	public String getFlagAcc() {
		return this.flagAcc;
	}

	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}

	public String getPkAcc() {
		return this.pkAcc;
	}

	public void setPkAcc(String pkAcc) {
		this.pkAcc = pkAcc;
	}

	public String getFlagSettle() {
		return this.flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	public String getPkSettle() {
		return this.pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getFlagCc() {
		return this.flagCc;
	}

	public void setFlagCc(String flagCc) {
		this.flagCc = flagCc;
	}

	public String getPkCc() {
		return this.pkCc;
	}

	public void setPkCc(String pkCc) {
		this.pkCc = pkCc;
	}

	public String getReptNo() {
		return this.reptNo;
	}

	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}

	public String getFlagReptBack() {
		return this.flagReptBack;
	}

	public void setFlagReptBack(String flagReptBack) {
		this.flagReptBack = flagReptBack;
	}

	public Date getDateReptBack() {
		return this.dateReptBack;
	}

	public void setDateReptBack(Date dateReptBack) {
		this.dateReptBack = dateReptBack;
	}

	public String getPkEmpBack() {
		return this.pkEmpBack;
	}

	public void setPkEmpBack(String pkEmpBack) {
		this.pkEmpBack = pkEmpBack;
	}

	public String getNameEmpBack() {
		return this.nameEmpBack;
	}

	public void setNameEmpBack(String nameEmpBack) {
		this.nameEmpBack = nameEmpBack;
	}

	public String getPkDepoBack() {
		return this.pkDepoBack;
	}

	public void setPkDepoBack(String pkDepoBack) {
		this.pkDepoBack = pkDepoBack;
	}

	public String getPkStMid() {
		return this.pkStMid;
	}

	public void setPkStMid(String pkStMid) {
		this.pkStMid = pkStMid;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEuPvtype() {
		return this.euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkEmpinvoice() {
		return this.pkEmpinvoice;
	}

	public void setPkEmpinv(String pkEmpinv) {
		this.pkEmpinvoice = pkEmpinv;
	}

	public String getPaymodeName() {
		return paymodeName;
	}

	public void setPaymodeName(String paymodeName) {
		this.paymodeName = paymodeName;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public String getFlagCcRept() {
		return flagCcRept;
	}

	public void setFlagCcRept(String flagCcRept) {
		this.flagCcRept = flagCcRept;
	}

	public String getPkCcRept() {
		return pkCcRept;
	}

	public void setPkCcRept(String pkCcRept) {
		this.pkCcRept = pkCcRept;
	}

	public Date getDateRept() {
		return dateRept;
	}

	public void setDateRept(Date dateRept) {
		this.dateRept = dateRept;
	}

	public String getPkEmpRept() {
		return pkEmpRept;
	}

	public void setPkEmpRept(String pkEmpRept) {
		this.pkEmpRept = pkEmpRept;
	}

	public String getNameEmpRept() {
		return nameEmpRept;
	}

	public void setNameEmpRept(String nameEmpRept) {
		this.nameEmpRept = nameEmpRept;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	public String getVoidType() {
		return voidType;
	}

	public void setVoidType(String voidType) {
		this.voidType = voidType;
	}

	public String getVoidEMP() {
		return voidEMP;
	}

	public void setVoidEMP(String voidEMP) {
		this.voidEMP = voidEMP;
	}

	public Date getVoidTIME() {
		return voidTIME;
	}

	public void setVoidTIME(Date voidTIME) {
		this.voidTIME = voidTIME;
	}

	public String getMzPatientId() {
		return mzPatientId;
	}

	public void setMzPatientId(String mzPatientId) {
		this.mzPatientId = mzPatientId;
	}

	public String getLedgerSn() {
		return ledgerSn;
	}

	public void setLedgerSn(String ledgerSn) {
		this.ledgerSn = ledgerSn;
	}

	public String getReceiptSn() {
		return receiptSn;
	}

	public void setReceiptSn(String receiptSn) {
		this.receiptSn = receiptSn;
	}

	public String getRemittanceUnit() {
		return remittanceUnit;
	}

	public void setRemittanceUnit(String remittanceUnit) {
		this.remittanceUnit = remittanceUnit;
	}

	public String getPkSettleOp() {
		return pkSettleOp;
	}

	public void setPkSettleOp(String pkSettleOp) {
		this.pkSettleOp = pkSettleOp;
	}
	
}
