package com.zebone.nhis.pro.zsba.nm.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-收费项目-结算明细
 * @author Administrator
 *
 */
@Table(value="nm_ci_st_details")
public class NmCiStDetails extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7427427748202454886L;
	

	@PK
	@Field(value="pk_ci_std", id=KeyId.UUID)
    private String pkCiStd;//收费项目结算主键
	
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

	@Field(value="date_annal")
    private String dateAnnal;// 计费日期

	@Field(value="num_ord")
    private BigDecimal numOrd;// 执行数量

	@Field(value="pk_ci")
    private String pkCi;// 收费项目主键

	@Field(value="ci_price")
    private BigDecimal ciPrice;// 收费项目单价

	@Field(value="total")
    private BigDecimal total;// 小计金额

	@Field(value="is_sett")
    private String isSett;// 是否结算，0未结算、1已结算

	@Field(value="pk_ci_st")
    private String pkCiSt;// 收费结算主键

	@Field(value="is_pay")
    private String isPay;// 付款标志，0未付款、1已付款、2有退款

	@Field(value="annal_code")
    private String annalCode;// 计费员

	@Field(value="annal_name")
    private String annalName;// 计费员姓名
    
    @Field(value="charge_time")
    private Date chargeTime;// 收费时间

    @Field(value="refund_time")
    private Date refundTime;// 退费时间

    @Field(value="modity_time")
    private Date  modityTime;// 修改时间

	public String getPkCiStd() {
		return pkCiStd;
	}

	public void setPkCiStd(String pkCiStd) {
		this.pkCiStd = pkCiStd;
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

	public String getDateAnnal() {
		return dateAnnal;
	}

	public void setDateAnnal(String dateAnnal) {
		this.dateAnnal = dateAnnal;
	}

	public BigDecimal getNumOrd() {
		return numOrd;
	}

	public void setNumOrd(BigDecimal numOrd) {
		this.numOrd = numOrd;
	}

	public String getPkCi() {
		return pkCi;
	}

	public void setPkCi(String pkCi) {
		this.pkCi = pkCi;
	}

	public BigDecimal getCiPrice() {
		return ciPrice;
	}

	public void setCiPrice(BigDecimal ciPrice) {
		this.ciPrice = ciPrice;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getIsSett() {
		return isSett;
	}

	public void setIsSett(String isSett) {
		this.isSett = isSett;
	}

	public String getPkCiSt() {
		return pkCiSt;
	}

	public void setPkCiSt(String pkCiSt) {
		this.pkCiSt = pkCiSt;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getAnnalCode() {
		return annalCode;
	}

	public void setAnnalCode(String annalCode) {
		this.annalCode = annalCode;
	}

	public String getAnnalName() {
		return annalName;
	}

	public void setAnnalName(String annalName) {
		this.annalName = annalName;
	}

	public Date getChargeTime() {
		return chargeTime;
	}

	public void setChargeTime(Date chargeTime) {
		this.chargeTime = chargeTime;
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
