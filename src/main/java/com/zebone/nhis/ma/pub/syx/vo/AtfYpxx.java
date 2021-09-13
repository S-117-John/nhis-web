package com.zebone.nhis.ma.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 
 * 东山包药机，接口表，该表位于sqlserver数据库中（中山二院）
 * 医嘱明细表
 *
 */
@Table(value = "ATF_YPXX")
public class AtfYpxx extends AtfYpxxSyx {
	
	@Field(value = "INPATIENT_NO")
	private String inpatientNo;
	
	@Field(value = "P_ID")
	private String pId;
	
	@Field(value = "NAME")
	private String name;
	
	@Field(value = "AGE")
	private String age;
	
	@Field(value = "SEX")
	private String sex;
	
	@Field(value = "DEPT_SN")
	private String deptSn;
	
	@Field(value = "DEPT_NAME")
	private String deptName;
	
	@Field(value = "WARD_SN")
	private String wardSn;
	
	@Field(value = "WARD_NAME")
	private String wardName;
	
	@Field(value = "DOCTOR")
	private String doctor;
	
	@Field(value = "BED_NO")
	private String bedNo;
	
	@Field(value = "COMMENT")
	private String commt;
	
	@Field(value = "COMM2")
	private String comm2;
	
	@Field(value = "DRUG_CODE")
	private String drugCode;
	
	@Field(value = "DRUGNAME")
	private String drugname;
	
	@Field(value = "SPECIFICATION")
	private String specification;
	
	@Field(value = "DRUG_SPEC")
	private String drugSpec;
	
	@Field(value = "DOS_PER")
	private String dosPer;
	
	@Field(value = "DOS_PER_UNITS")
	private String dosPerUnits;
	
	@Field(value = "DOSAGE")
	private String dosage;
	
	@Field(value = "DOS_UNIT")
	private String dosUnit;
	
	@Field(value = "AMOUNT")
	private String amount;
	
	@Field(value = "TOTAL")
	private String total;
	
	@Field(value = "OCC_TIME")
	private String occTime;
	
	@Field(value = "FLAG")
	private String flag;
	
	@Field(value = "ATF_NO")
	private String atfNo;
	
	@Field(value = "PRI_FLAG")
	private String priFlag;
	
	@Field(value = "PAGE_NO")
	private String pageNo;
	
	@Field(value = "DETAIL_SN")
	private String detailSn;
	
	@Field(value = "GEN_TIME")
	private String genTime;
	
	@Field(value = "MZ_FLAG")
	private String mzFlag;
	
	@Field(value = "PRN_COMM")
	private String prnComm;
	
	@Field(value = "ZL")
	private String zl;
	
	@Field(value = "CS")
	private String cs;
	
	@Field(value = "JLDW")
	private String jldw;
	
	@Field(value = "APPLY_NO")
	private String applyNo;
	
	@Field(value = "DOSE_ONCE")
	private String doseOnce;
	
	@Field(value = "ORDER_TYPE")
	private String orderType;
	
	@Field(value = "EXECDALISTID")
	private String execdalistid;
	
	@Field(value = "SEQNO")
	private String seqno;
	
	@Field(value = "BAGNO")
	private String bagno;

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptSn() {
		return deptSn;
	}

	public void setDeptSn(String deptSn) {
		this.deptSn = deptSn;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getWardSn() {
		return wardSn;
	}

	public void setWardSn(String wardSn) {
		this.wardSn = wardSn;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCommt() {
		return commt;
	}

	public void setCommt(String commt) {
		this.commt = commt;
	}

	public String getComm2() {
		return comm2;
	}

	public void setComm2(String comm2) {
		this.comm2 = comm2;
	}

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugname() {
		return drugname;
	}

	public void setDrugname(String drugname) {
		this.drugname = drugname;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getDrugSpec() {
		return drugSpec;
	}

	public void setDrugSpec(String drugSpec) {
		this.drugSpec = drugSpec;
	}

	public String getDosPer() {
		return dosPer;
	}

	public void setDosPer(String dosPer) {
		this.dosPer = dosPer;
	}

	public String getDosPerUnits() {
		return dosPerUnits;
	}

	public void setDosPerUnits(String dosPerUnits) {
		this.dosPerUnits = dosPerUnits;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getDosUnit() {
		return dosUnit;
	}

	public void setDosUnit(String dosUnit) {
		this.dosUnit = dosUnit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getOccTime() {
		return occTime;
	}

	public void setOccTime(String occTime) {
		this.occTime = occTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAtfNo() {
		return atfNo;
	}

	public void setAtfNo(String atfNo) {
		this.atfNo = atfNo;
	}

	public String getPriFlag() {
		return priFlag;
	}

	public void setPriFlag(String priFlag) {
		this.priFlag = priFlag;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getDetailSn() {
		return detailSn;
	}

	public void setDetailSn(String detailSn) {
		this.detailSn = detailSn;
	}

	public String getGenTime() {
		return genTime;
	}

	public void setGenTime(String genTime) {
		this.genTime = genTime;
	}

	public String getMzFlag() {
		return mzFlag;
	}

	public void setMzFlag(String mzFlag) {
		this.mzFlag = mzFlag;
	}

	public String getPrnComm() {
		return prnComm;
	}

	public void setPrnComm(String prnComm) {
		this.prnComm = prnComm;
	}

	public String getZl() {
		return zl;
	}

	public void setZl(String zl) {
		this.zl = zl;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getDoseOnce() {
		return doseOnce;
	}

	public void setDoseOnce(String doseOnce) {
		this.doseOnce = doseOnce;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getExecdalistid() {
		return execdalistid;
	}

	public void setExecdalistid(String execdalistid) {
		this.execdalistid = execdalistid;
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getBagno() {
		return bagno;
	}

	public void setBagno(String bagno) {
		this.bagno = bagno;
	}
	
}
