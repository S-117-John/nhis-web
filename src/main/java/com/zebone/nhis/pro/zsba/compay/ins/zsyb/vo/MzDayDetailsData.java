package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

/**
 * 门诊日间手术费用明细表
 * @author zrj
 *
 */
public class MzDayDetailsData {
	
	private String zyPatientId; //住院ID号
	private String confirmAdmissTimes; //住院次数
	private String chargeCode; //收费编码
	private String serialNo; //包装序号
	private String origPrice; //原单价
	private String chargePrice; //现单价
	private String chargeAmount; //数量
	private String applyUnit; //申请科室编码
	private String applyName;//申请科室名称
	private String execSn; //执行科室编码
	private String execName;//执行科室名称
	private String happenDate; //发生时间
	private String chargeStatus; //收费状态
	private String confirmDate; //确认时间
	private String confirmOpera; //确认人
	private String billCode; //旧系统门诊账单码
	private String auditCode; //旧系统核算码
	private String doctorCode; //医生工号
	private String doctor;//医生名称
	private String groupNo; //旧系统药房编码
	private String supplyCode; //用法编码
	private String supplyName;//用法名称
	private String freqCode; //频率编码
	private String freqName;//频率名称
	private String dosage; //剂量
	private String dosageUnit; //剂量单位
	private String mzPatientId; //门诊ID号
	private String mzTimes; //门诊就诊次数
	private String receiptSn; //发票流水号
	private String itemName;//项目名称
	private String specification;//项目规格
	private String caoyaoFu;//草药副数
	private String amount;//草药副数
	private String pkCgop;//新门诊费用明细主键，用于区分新旧系统，旧系统时为null
	public String getZyPatientId() {
		return zyPatientId;
	}
	public void setZyPatientId(String zyPatientId) {
		this.zyPatientId = zyPatientId;
	}
	public String getConfirmAdmissTimes() {
		return confirmAdmissTimes;
	}
	public void setConfirmAdmissTimes(String confirmAdmissTimes) {
		this.confirmAdmissTimes = confirmAdmissTimes;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getOrigPrice() {
		return origPrice;
	}
	public void setOrigPrice(String origPrice) {
		this.origPrice = origPrice;
	}
	public String getChargePrice() {
		return chargePrice;
	}
	public void setChargePrice(String chargePrice) {
		this.chargePrice = chargePrice;
	}
	public String getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(String chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	public String getApplyUnit() {
		return applyUnit;
	}
	public void setApplyUnit(String applyUnit) {
		this.applyUnit = applyUnit;
	}
	public String getExecSn() {
		return execSn;
	}
	public void setExecSn(String execSn) {
		this.execSn = execSn;
	}
	public String getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(String happenDate) {
		this.happenDate = happenDate;
	}
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getConfirmOpera() {
		return confirmOpera;
	}
	public void setConfirmOpera(String confirmOpera) {
		this.confirmOpera = confirmOpera;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getAuditCode() {
		return auditCode;
	}
	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getSupplyCode() {
		return supplyCode;
	}
	public void setSupplyCode(String supplyCode) {
		this.supplyCode = supplyCode;
	}
	public String getFreqCode() {
		return freqCode;
	}
	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getDosageUnit() {
		return dosageUnit;
	}
	public void setDosageUnit(String dosageUnit) {
		this.dosageUnit = dosageUnit;
	}
	public String getMzPatientId() {
		return mzPatientId;
	}
	public void setMzPatientId(String mzPatientId) {
		this.mzPatientId = mzPatientId;
	}
	public String getMzTimes() {
		return mzTimes;
	}
	public void setMzTimes(String mzTimes) {
		this.mzTimes = mzTimes;
	}
	public String getReceiptSn() {
		return receiptSn;
	}
	public void setReceiptSn(String receiptSn) {
		this.receiptSn = receiptSn;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getExecName() {
		return execName;
	}
	public void setExecName(String execName) {
		this.execName = execName;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getFreqName() {
		return freqName;
	}
	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getCaoyaoFu() {
		return caoyaoFu;
	}
	public void setCaoyaoFu(String caoyaoFu) {
		this.caoyaoFu = caoyaoFu;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPkCgop() {
		return pkCgop;
	}
	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}

	
}
