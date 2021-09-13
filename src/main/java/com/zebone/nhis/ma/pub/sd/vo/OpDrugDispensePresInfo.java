package com.zebone.nhis.ma.pub.sd.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Med")
public class OpDrugDispensePresInfo {

	/**
	 * 处方号
	 */
	@XmlElement(name = "PresNO")
	private String presNo;

	/**
	 * 窗口号
	 */
	@XmlElement(name="WindowNO")
	private String windowno;

	/**
	 * 发票号
	 */
	@XmlElement(name="FPNO")
	private String fpno;

	/**
	 * 药品流水号
	 */
	@XmlElement(name="MedID")
	private String medid;

	/**
	 * 药品编码
	 */
	@XmlElement(name="MedOnlyCode")
	private String medOnlyCode;

	/**
	 * 药品名称
	 */
	@XmlElement(name="MedName")
	private String medName;

	/**
	 * 发药数量
	 */
	@XmlElement(name="MedAMT")
	private BigDecimal medAmt;

	/**
	 * 发药单位
	 */
	@XmlElement(name="MedPack")
	private String medPack;

	/**
	 * 包装单位（零售）
	 */
	@XmlElement(name="MedUnitPack")
	private String medUnitPack;

	/**
	 * 规格
	 */
	@XmlElement(name="Medunit")
	private String medUnit;

	/**
	 * 转换系数
	 */
	@XmlElement(name="MedConvercof")
	private BigDecimal medConvercof;

	/**
	 * 生产厂家
	 */
	@XmlElement(name="MedFactory")
	private String medFactory;

	/**
	 * 药品类型
	 */
	@XmlElement(name="MedType")
	private String medType;

	/**
	 * 单价
	 */
	@XmlElement(name="MedUnitPrice")
	private BigDecimal medUnitPrice;

	/**
	 * 总金额
	 */
	@XmlElement(name="MedTotalPrice")
	private BigDecimal medTotalPrice;

	/**
	 * 收费时间
	 */
	@XmlElement(name="MedOutTime")
	private Date medOutTime;

	/**
	 * 门诊号
	 */
	@XmlElement(name="PatientID")
	private String patientId;

	/**
	 * 姓名
	 */
	@XmlElement(name="PatientName")
	private String patientName;

	/**
	 * 性别
	 */
	@XmlElement(name="PatientSex")
	private String patientSex;

	/**
	 * 年龄
	 */
	@XmlElement(name="PatientAge")
	private String patientAge;

	/**
	 * 出生日期
	 */
	@XmlElement(name="PatientBirth")
	private Date patientBirth;

	/**
	 * 诊断
	 */
	@XmlElement(name="Diagnosis")
	private String diagnosis;

	/**
	 * 科室编码
	 */
	@XmlElement(name="WardNo")
	private String wardNo;

	/**
	 * 科室名称
	 */
	@XmlElement(name="WardName")
	private String wardName;

	/**
	 * 用量
	 */
	@XmlElement(name="MedPerDos")
	private String medPerDos;

	/**
	 * 用法
	 */
	@XmlElement(name="MedUsage")
	private String medUsage;

	/**
	 * 频次
	 */
	@XmlElement(name="MedPerDay")
	private String medPerDay;

	/**
	 * 医生姓名
	 */
	@XmlElement(name="DoctorName")
	private String doctorName;

	/**
	 * 处方类型
	 */
	@XmlElement(name="PresType")
	private String presType;

	/**
	 * 备注
	 */
	@XmlElement(name="Remark")
	private String remark;

	/**
	 * 货位号
	 */
	@XmlElement(name="MedShelf")
	private String medShelf;
	
	/**
	 * 药房代码
	 */
	@XmlElement(name="YFcode")
	private String yfcode;
	
	/**
	 * 标签单位
	 */
	private String euLabeltype;
	
	/**
	 * 医疗含量II
	 */
	private Double weight;
	
	/**
	 * 医疗含量II单位
	 */
	private String pkUnitWt;
		
	/**
	 * 医疗含量I
	 */
	private Double vol;
	
	/**
	 * 医疗含量I单位
	 */
	private String pkUnitVol;
	
	/**
	 * 基本单位
	 */
	private String pkUnitMin;
	
	/**
	 * 用量
	 */
	private Double dosage;
	
	/**
	 * 用量单位
	 */
	private String pkUnitDos;
	
	/**
	 * 用量单位名称
	 */
	private String nameUnitDos;
	
	/**
	 * 基本单位名称
	 */
	private String nameUnitVol;
	
	/**
	 * 医疗含量I名称
	 */
	private String nameUnitMin;
	
	/**
	 * 医疗含量II名称
	 */
	private String nameUnitWt;

	public String getPresNo() {
		return presNo;
	}

	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}

	public String getWindowno() {
		return windowno;
	}

	public void setWindowno(String windowno) {
		this.windowno = windowno;
	}

	public String getFpno() {
		return fpno;
	}

	public void setFpno(String fpno) {
		this.fpno = fpno;
	}

	public String getMedid() {
		return medid;
	}

	public void setMedid(String medid) {
		this.medid = medid;
	}

	public String getMedOnlyCode() {
		return medOnlyCode;
	}

	public void setMedOnlyCode(String medOnlyCode) {
		this.medOnlyCode = medOnlyCode;
	}

	public String getMedName() {
		return medName;
	}

	public void setMedName(String medName) {
		this.medName = medName;
	}

	public BigDecimal getMedAmt() {
		return medAmt;
	}

	public void setMedAmt(BigDecimal medAmt) {
		this.medAmt = medAmt;
	}

	public String getMedPack() {
		return medPack;
	}

	public void setMedPack(String medPack) {
		this.medPack = medPack;
	}

	public String getMedUnitPack() {
		return medUnitPack;
	}

	public void setMedUnitPack(String medUnitPack) {
		this.medUnitPack = medUnitPack;
	}

	public String getMedUnit() {
		return medUnit;
	}

	public void setMedUnit(String medUnit) {
		this.medUnit = medUnit;
	}

	public BigDecimal getMedConvercof() {
		return medConvercof;
	}

	public void setMedConvercof(BigDecimal medConvercof) {
		this.medConvercof = medConvercof;
	}

	public String getMedFactory() {
		return medFactory;
	}

	public void setMedFactory(String medFactory) {
		this.medFactory = medFactory;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}

	public BigDecimal getMedUnitPrice() {
		return medUnitPrice;
	}

	public void setMedUnitPrice(BigDecimal medUnitPrice) {
		this.medUnitPrice = medUnitPrice;
	}

	public BigDecimal getMedTotalPrice() {
		return medTotalPrice;
	}

	public void setMedTotalPrice(BigDecimal medTotalPrice) {
		this.medTotalPrice = medTotalPrice;
	}

	public Date getMedOutTime() {
		return medOutTime;
	}

	public void setMedOutTime(Date medOutTime) {
		this.medOutTime = medOutTime;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public Date getPatientBirth() {
		return patientBirth;
	}

	public void setPatientBirth(Date patientBirth) {
		this.patientBirth = patientBirth;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getWardNo() {
		return wardNo;
	}

	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getMedPerDos() {
		return medPerDos;
	}

	public void setMedPerDos(String medPerDos) {
		this.medPerDos = medPerDos;
	}

	public String getMedUsage() {
		return medUsage;
	}

	public void setMedUsage(String medUsage) {
		this.medUsage = medUsage;
	}

	public String getMedPerDay() {
		return medPerDay;
	}

	public void setMedPerDay(String medPerDay) {
		this.medPerDay = medPerDay;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPresType() {
		return presType;
	}

	public void setPresType(String presType) {
		this.presType = presType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMedShelf() {
		return medShelf;
	}

	public void setMedShelf(String medShelf) {
		this.medShelf = medShelf;
	}

	public String getYfcode() {
		return yfcode;
	}

	public void setYfcode(String yfcode) {
		this.yfcode = yfcode;
	}

	public String getEuLabeltype() {
		return euLabeltype;
	}

	public void setEuLabeltype(String euLabeltype) {
		this.euLabeltype = euLabeltype;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getPkUnitWt() {
		return pkUnitWt;
	}

	public void setPkUnitWt(String pkUnitWt) {
		this.pkUnitWt = pkUnitWt;
	}

	public Double getVol() {
		return vol;
	}

	public void setVol(Double vol) {
		this.vol = vol;
	}

	public String getPkUnitMin() {
		return pkUnitMin;
	}

	public void setPkUnitMin(String pkUnitMin) {
		this.pkUnitMin = pkUnitMin;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getPkUnitDos() {
		return pkUnitDos;
	}

	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}

	public String getPkUnitVol() {
		return pkUnitVol;
	}

	public void setPkUnitVol(String pkUnitVol) {
		this.pkUnitVol = pkUnitVol;
	}

	public String getNameUnitDos() {
		return nameUnitDos;
	}

	public void setNameUnitDos(String nameUnitDos) {
		this.nameUnitDos = nameUnitDos;
	}

	public String getNameUnitVol() {
		return nameUnitVol;
	}

	public void setNameUnitVol(String nameUnitVol) {
		this.nameUnitVol = nameUnitVol;
	}

	public String getNameUnitMin() {
		return nameUnitMin;
	}

	public void setNameUnitMin(String nameUnitMin) {
		this.nameUnitMin = nameUnitMin;
	}

	public String getNameUnitWt() {
		return nameUnitWt;
	}

	public void setNameUnitWt(String nameUnitWt) {
		this.nameUnitWt = nameUnitWt;
	}
	
}
