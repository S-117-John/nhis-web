package com.zebone.nhis.pro.zsba.rent.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-设备出租记录
 * @author lipz
 *
 */
@Table(value="nm_rent_record")
public class NmRentRecord extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4294033711776836892L;

	@PK
	@Field(value="pk_rent",id=KeyId.UUID)
	private String pkRent;// 出租记录主键
	
	@Field(value="pk_app")
    private String pkApp;// 设备主键

	@Field(value="app_no")
    private String appNo;// 设备编号
	
	@Field(value="pk_dept")
    private String pkDept;// 所属病区
	
	@Field(value="pv_type")
    private String pvType;// 就诊类型
	
	@Field(value="times")
    private Integer times;// 就诊次数
	
	@Field(value="name_pi")
    private String namePi;// 患者姓名
	
	@Field(value="code_ip")
    private String codeIp;// 住院号|门诊号
	
	@Field(value="depo_amt")
    private BigDecimal depoAmt;// 押金金额
	
	@Field(value="is_pay")
    private String isPay;// 付款标志,0未付款、1已付款
	
	@Field(value="pay_method")
    private String payMethod;// 付款渠道，W微信、A支付宝、U银联
	
	@Field(value="pk_pay")
    private String pkPay;// 付款记录主键
	
	@Field(value="charge_code")
    private String chargeCode;// 收费员

	@Field(value="charge_name")
    private String chargeName;// 收费员名称
	
	@Field(value="charge_time")
    private Date chargeTime;// 收费时间
	
	@Field(value="is_refund")
    private String isRefund;// 是否归还设备,0未归还、1已归还
	
	@Field(value="pk_refund")
    private String pkRefund;// 退款记录主键

	@Field(value="refunder_code")
    private String refunderCode;// 退费员
	
	@Field(value="refunder_name")
    private String refunderName;// 退费员名称
	
	@Field(value="refund_time")
    private Date refundTime;// 退费时间

    @Field(value="modity_time")
    private Date modityTime;//
	
	public String getPkRent() {
		return pkRent;
	}
	public void setPkRent(String pkRent) {
		this.pkRent = pkRent;
	}

	public String getPkApp() {
		return pkApp;
	}
	public void setPkApp(String pkApp) {
		this.pkApp = pkApp;
	}

	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
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

	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	
	public BigDecimal getDepoAmt() {
		return depoAmt;
	}
	public void setDepoAmt(BigDecimal depoAmt) {
		this.depoAmt = depoAmt;
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

	public String getIsRefund() {
		return isRefund;
	}
	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getPkRefund() {
		return pkRefund;
	}
	public void setPkRefund(String pkRefund) {
		this.pkRefund = pkRefund;
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
