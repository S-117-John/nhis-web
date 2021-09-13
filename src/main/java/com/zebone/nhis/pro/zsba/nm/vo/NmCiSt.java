package com.zebone.nhis.pro.zsba.nm.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-收费项目-结算
 * @author Administrator
 *
 */
@Table(value="nm_ci_st")
public class NmCiSt extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 291064934158684582L;
	
	@PK
	@Field(value="pk_ci_st", id=KeyId.UUID)
    private String pkCiSt;// 收费项目结算主键

	@Field(value="pk_dept")
    private String pkDept;// 所属病区
	
	@Field(value="input_dept")
    private String inputDept;// 录入病区

	@Field(value="pk_pv")
    private String pkPv;// 就诊主键
	
	@Field(value="name_pi")
    private String namePi;// 患者姓名

	@Field(value="pv_type")
    private String pvType;// 就诊类型，1门诊、2住院

	@Field(value="code_pv")
    private String codePv;// 住院号|门诊号

	@Field(value="times")
    private Integer times;// 就诊次数

	@Field(value="amount")
    private BigDecimal amount;//结算金额
	
	@Field(value="refund_amount")
    private BigDecimal refundAmount;//退费金额
	
	@Field(value="is_push")
    private String isPush;//通知标志，0未通知、1已通知
    
    @Field(value="is_pay")
    private String isPay;//付款标志，0未付款、1已付款、2全退、3部分退

    @Field(value="pay_method")
    private String payMethod;//付款渠道，W微信、A支付宝、U银联

    @Field(value="pk_pay")
    private String pkPay;//付款记录主键

    @Field(value="pk_refund")
    private String pkRefund;//退款记录主键

    @Field(value="sett_code")
    private String settCode;//结算员

    @Field(value="sett_name")
    private String settName;//结算员姓名

    @Field(value="charge_code")
    private String chargeCode;//收费员

    @Field(value="charge_name")
    private String chargeName;//收费员名称

    @Field(value="charge_time")
    private Date chargeTime;//收费时间

    @Field(value="refunder_code")
    private String refunderCode;//退费员

    @Field(value="refunder_name")
    private String refunderName;//退费员名称

    @Field(value="refund_time")
    private Date refundTime;//退费时间

    @Field(value="modity_time")
    private Date modityTime;//修改时间

	public String getPkCiSt() {
		return pkCiSt;
	}

	public void setPkCiSt(String pkCiSt) {
		this.pkCiSt = pkCiSt;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	public String getInputDept() {
		return inputDept;
	}

	public void setInputDept(String inputDept) {
		this.inputDept = inputDept;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPvType() {
		return pvType;
	}

	public void setPvType(String pvType) {
		this.pvType = pvType;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getIsPush() {
		return isPush;
	}

	public void setIsPush(String isPush) {
		this.isPush = isPush;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPkPay() {
		return pkPay;
	}

	public void setPkPay(String pkPay) {
		this.pkPay = pkPay;
	}

	public String getPkRefund() {
		return pkRefund;
	}

	public void setPkRefund(String pkRefund) {
		this.pkRefund = pkRefund;
	}

	public String getSettCode() {
		return settCode;
	}

	public void setSettCode(String settCode) {
		this.settCode = settCode;
	}

	public String getSettName() {
		return settName;
	}

	public void setSettName(String settName) {
		this.settName = settName;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
	}

	public String getRefunderCode() {
		return refunderCode;
	}

	public void setRefunderCode(String refunderCode) {
		this.refunderCode = refunderCode;
	}

	public String getRefunderName() {
		return refunderName;
	}

	public void setRefunderName(String refunderName) {
		this.refunderName = refunderName;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
}
