package com.zebone.nhis.ma.pub.zsba.vo;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
@Table(value = "order_hist")
public class PackPdVo{
	@Field(value = "id")
	private int id;//主键，bigint类型，定义序号发生器初始值，从800000000开始；
	@Field(value = "HsptCd")
	private String hsptCd;//医院代码，写pk_org；
	@Field(value = "DptmtCd")
	private String dptmtCd;//部门代码，写pk_dept；
	@Field(value = "WardCd")
	private String wardCd;//病区代码，写pk_dept_ns；
	@Field(value = "DataClsf")
	private String dataClsf;//写‘N’；
	@Field(value = "InOutClsf")
	private String inOutClsf;//写‘I’；
	@Field(value = "OrderDt")
	private String orderDt;//写当前日期，格式：yyyymmdd；
	@Field(value = "OrderDtm")
	private String orderDtm;//写当前日期，格式：yyyymmddhhmmss；
	@Field(value = "OrderNum")
	private String orderNum;//医嘱编号，写ordsn；
	@Field(value = "RoomNum")
	private String roomNum;//病人床号，写bed_no；
	@Field(value = "PtntNm")
	private String ptntNm;//病人姓名，写name_pi；
	@Field(value = "PtntNum")
	private String ptntNum;//病人ID号，写code_pi；
	@Field(value = "Sex")
	private String sex;//性别，写“男”或“女”；
	@Field(value = "DoctorNm")
	private String doctorNm;//医生姓名，写name_emp_ord；
	@Field(value = "Birthday")
	private String birthday;//出生年月，写birth_date，格式：yyyymmdd；
	@Field(value = "PtntAddr")
	private String ptntAddr;//为空；
	@Field(value = "PtntTel")
	private String ptntTel;//为空；
	@Field(value = "MedCd")
	private String medCd;//药品编码，写pdcode；
	@Field(value = "MedNm")
	private String medNm;//药品名称，写pdname；
	@Field(value = "MedNote")
	private String medNote;//为空；
	@Field(value = "MedSpec")
	private String medSpec;//药品规格，写spec；
	@Field(value = "MedUnit")
	private String medUnit;//药品数量单位，写unitname；
	@Field(value = "UseAtcYn")
	private String useAtcYn;//写‘Y’；
	@Field(value = "DoseList")
	private String doseList;//用药时间，写date_plan，格式：hhmm；
	@Field(value = "Dose")
	private Double dose;//用药数量，写quan_occ；
	@Field(value = "TakeDays")
	private String takeDays;//写‘1’；
	@Field(value = "TakeDt")
	private String takeDt;//用药日期，写date_plan，格式：yyyymmdd；
	@Field(value = "Xmlflag")
	private String xmlFlag;//写‘N’；
	@Field(value = "DrtsCd")
	private String drtsCd;//为空。

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHsptCd() {
		return hsptCd;
	}
	public void setHsptCd(String hsptCd) {
		this.hsptCd = hsptCd;
	}
	public String getDptmtCd() {
		return dptmtCd;
	}
	public void setDptmtCd(String dptmtCd) {
		this.dptmtCd = dptmtCd;
	}
	public String getWardCd() {
		return wardCd;
	}
	public void setWardCd(String wardCd) {
		this.wardCd = wardCd;
	}
	public String getDataClsf() {
		return dataClsf;
	}
	public void setDataClsf(String dataClsf) {
		this.dataClsf = "N";
	}
	public String getInOutClsf() {
		return inOutClsf;
	}
	public void setInOutClsf(String inOutClsf) {
		this.inOutClsf = "I";
	}
	public String getOrderDt() {
		return orderDt;
	}
	public void setOrderDt(String orderDt) {
		this.orderDt = DateUtils.getDateStr(new Date());
	}
	public String getOrderDtm() {
		return orderDtm;
	}
	public void setOrderDtm(String orderDtm) {
		this.orderDtm = DateUtils.getDateTimeStr(new Date());
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getPtntNm() {
		return ptntNm;
	}
	public void setPtntNm(String ptntNm) {
		this.ptntNm = ptntNm;
	}
	public String getPtntNum() {
		return ptntNum;
	}
	public void setPtntNum(String ptntNum) {
		this.ptntNum = ptntNum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDoctorNm() {
		return doctorNm;
	}
	public void setDoctorNm(String doctorNm) {
		this.doctorNm = doctorNm;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPtntAddr() {
		return ptntAddr;
	}
	public void setPtntAddr(String ptntAddr) {
		this.ptntAddr = ptntAddr;
	}
	public String getPtntTel() {
		return ptntTel;
	}
	public void setPtntTel(String ptntTel) {
		this.ptntTel = ptntTel;
	}
	public String getMedCd() {
		return medCd;
	}
	public void setMedCd(String medCd) {
		this.medCd = medCd;
	}
	public String getMedNm() {
		return medNm;
	}
	public void setMedNm(String medNm) {
		this.medNm = medNm;
	}
	public String getMedNote() {
		return medNote;
	}
	public void setMedNote(String medNote) {
		this.medNote = medNote;
	}
	public String getMedSpec() {
		return medSpec;
	}
	public void setMedSpec(String medSpec) {
		this.medSpec = medSpec;
	}
	public String getMedUnit() {
		return medUnit;
	}
	public void setMedUnit(String medUnit) {
		this.medUnit = medUnit;
	}
	public String getUseAtcYn() {
		return useAtcYn;
	}
	public void setUseAtcYn(String useAtcYn) {
		this.useAtcYn = "Y";
	}
	public String getDoseList() {
		return doseList;
	}
	public void setDoseList(String doseList) {
		this.doseList = doseList;
	}
	public Double getDose() {
		return dose;
	}
	public void setDose(Double dose) {
		this.dose = dose;
	}
	public String getTakeDays() {
		return takeDays;
	}
	public void setTakeDays(String takeDays) {
		this.takeDays = "1";
	}
	public String getTakeDt() {
		return takeDt;
	}
	public void setTakeDt(String takeDt) {
		this.takeDt = takeDt;
	}
	public String getXmlFlag() {
		return xmlFlag;
	}
	public void setXmlFlag(String xmlFlag) {
		this.xmlFlag = "N";
	}
	public String getDrtsCd() {
		return drtsCd;
	}
	public void setDrtsCd(String drtsCd) {
		this.drtsCd = drtsCd;
	}
}
