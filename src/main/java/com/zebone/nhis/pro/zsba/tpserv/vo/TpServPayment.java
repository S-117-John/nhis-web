package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: tp_serv_payment - 缴费记录 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="tp_serv_payment")
public class TpServPayment  extends BaseModule{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2365520906556916050L;

	@PK
	@Field(value="PK_PAYMENT",id=KeyId.UUID)
    private String pkPayment;

	/** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;
	
	/** PK_RENT - 项目出租主键（押金用的） */
	@Field(value="PK_RENT")
    private String pkRent;
	
    /** PK_PATIENT - 缴费患者 */
	@Field(value="PK_PATIENT")
    private String pkPatient;
	
    /** PK_THIRD_PARTY  - 支付宝、银联、龙闪付等的支付主键 */
	@Field(value="PK_THIRD_PARTY ")
    private String pkThirdParty;
	
    /** REVOCATION_NUM - 支付宝、银联、龙闪付撤销需要提供的号码 */
	@Field(value="REVOCATION_NUM")
    private String revocationNum;
	
	/** FK_DEPT - 支付方式:1：银联2：支付宝3：龙闪付 */
	@Field(value="pay_method")
	private String payMethod;
	
    /** AMOUNT - 交易金额(正数：收，负数：退) */
	@Field(value="AMOUNT")
    private BigDecimal amount;

    /** IS_PLEDGE - 是否为押金(0：是，1：否) */
	@Field(value="IS_PLEDGE")
    private String isPledge;

    /** PAY_INFO - 收付款方式信息 */
	@Field(value="PAY_INFO")
    private String payInfo;
	
    /** DATE_PAY - 收付款日期 */
	@Field(value="DATE_PAY")
    private Date datePay;
	
    /** PK_DEPT  - 收付款部门 */
	@Field(value="PK_DEPT ")
    private String pkDept;
	
    /** PK_EMP_PAY - 收款人 */
	@Field(value="PK_EMP_PAY")
    private String pkEmpPay;
	
    /** NAME_EMP_PAY - 收款人名称 */
	@Field(value="NAME_EMP_PAY")
    private String nameEmpPay;
	
    /** SETTLE_FLAG - 结算标志 */
	@Field(value="SETTLE_FLAG")
    private String settleFlag;
	
    /** PK_SERV_SETTLE - 结算主键 */
	@Field(value="PK_SERV_SETTLE")
    private String pkServSettle;
	
    /** MODITY_TIME - 修改时间 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkPayment() {
		return pkPayment;
	}

	public void setPkPayment(String pkPayment) {
		this.pkPayment = pkPayment;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkRent() {
		return pkRent;
	}

	public void setPkRent(String pkRent) {
		this.pkRent = pkRent;
	}

	public String getPkPatient() {
		return pkPatient;
	}

	public void setPkPatient(String pkPatient) {
		this.pkPatient = pkPatient;
	}

	public String getPkThirdParty() {
		return pkThirdParty;
	}

	public void setPkThirdParty(String pkThirdParty) {
		this.pkThirdParty = pkThirdParty;
	}

	public String getRevocationNum() {
		return revocationNum;
	}

	public void setRevocationNum(String revocationNum) {
		this.revocationNum = revocationNum;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getIsPledge() {
		return isPledge;
	}

	public void setIsPledge(String isPledge) {
		this.isPledge = isPledge;
	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public Date getDatePay() {
		return datePay;
	}

	public void setDatePay(Date datePay) {
		this.datePay = datePay;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpPay() {
		return pkEmpPay;
	}

	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}

	public String getNameEmpPay() {
		return nameEmpPay;
	}

	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}

	public String getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(String settleFlag) {
		this.settleFlag = settleFlag;
	}

	public String getPkServSettle() {
		return pkServSettle;
	}

	public void setPkServSettle(String pkServSettle) {
		this.pkServSettle = pkServSettle;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}