package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 输血请求参数构造xml
 * 医嘱回传新增
 * @author frank
 *
 */
@XmlRootElement(name = "DoctorAdvice")
@XmlAccessorType(XmlAccessType.FIELD)
public class DoctorAdviceVo {
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	
	@XmlElement(name = "DoctorAdviceMapInfoID")
	public String doctorAdviceMapInfoID;
	@XmlElement(name = "InHospitalID")
	public String inHospitalID;
	@XmlElement(name = "CaseNum")
	public String caseNum;
	
	@XmlElement(name = "VisitID")
	public String visitID;
	
	@XmlElement(name = "ABORhD")
	public String aBORhD;
	
	@XmlElement(name = "ApplyNum")
	public String applyNum;
	
	@XmlElement(name = "ApplyDate")
	public String applyDate;
	
	@XmlElement(name = "PreTransDate")
	public String preTransDate;
	
	@XmlElement(name = "FeeSourceID")
	public String feeSourceID;
	
	@XmlElement(name = "ApplyTypeName")
	public String applyTypeName;
	
	@XmlElement(name = "ApplyDoctorHISCode")
	public String applyDoctorHISCode;
	
	@XmlElement(name = "DoctorAdviceHisCode")
	public String doctorAdviceHisCode;
	
	@XmlElement(name = "ABORhDStr")
	public String aBORhDStr;
	
	@XmlElement(name = "Spec")
	public String spec;
	
	@XmlElement(name = "Count")
	public String count;
	
	@XmlElement(name = "BagCount")
	public String bagCount;
	
	@XmlElement(name = "DeptHISCode")
	public String deptHISCode;
	
	@XmlElement(name = "DoctorAdviceContent")
	public String doctorAdviceContent;
	
	@XmlElement(name = "SysDate")
	public String sysDate;
	
	@XmlElement(name = "DoctorAdviceNum")
	public String doctorAdviceNum;
	

	
	public String getHospHISCode() {
		return hospHISCode;
	}

	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}
	
	public String getDoctorAdviceMapInfoID() {
		return doctorAdviceMapInfoID;
	}

	public void setDoctorAdviceMapInfoID(String doctorAdviceMapInfoID) {
		this.doctorAdviceMapInfoID = doctorAdviceMapInfoID;
	}
	
	public String getInHospitalID() {
		return inHospitalID;
	}

	public void setInHospitalID(String inHospitalID) {
		this.inHospitalID = inHospitalID;
	}
	
	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	
	public String getVisitID() {
		return visitID;
	}

	public void setVisitID(String visitID) {
		this.visitID = visitID;
	}
	
	public String getaBORhD() {
		return aBORhD;
	}

	public void setaBORhD(String aBORhD) {
		this.aBORhD = aBORhD;
	}
	
	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}
	
	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	
	public String getPreTransDate() {
		return preTransDate;
	}

	public void setPreTransDate(String preTransDate) {
		this.preTransDate = preTransDate;
	}
	
	public String getFeeSourceID() {
		return feeSourceID;
	}

	public void setFeeSourceID(String feeSourceID) {
		this.feeSourceID = feeSourceID;
	}
	
	public String getApplyTypeName() {
		return applyTypeName;
	}

	public void setApplyTypeName(String applyTypeName) {
		this.applyTypeName = applyTypeName;
	}
	
	public String getApplyDoctorHISCode() {
		return applyDoctorHISCode;
	}

	public void setApplyDoctorHISCode(String applyDoctorHISCode) {
		this.applyDoctorHISCode = applyDoctorHISCode;
	}
	
	public String getDoctorAdviceHisCode() {
		return doctorAdviceHisCode;
	}

	public void setDoctorAdviceHisCode(String doctorAdviceHisCode) {
		this.doctorAdviceHisCode = doctorAdviceHisCode;
	}
	
	public String getaBORhDStr() {
		return aBORhDStr;
	}

	public void setaBORhDStr(String aBORhDStr) {
		this.aBORhDStr = aBORhDStr;
	}
	
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	public String getBagCount() {
		return bagCount;
	}

	public void setBagCount(String bagCount) {
		this.bagCount = bagCount;
	}
	
	public String getDeptHISCode() {
		return deptHISCode;
	}

	public void setDeptHISCode(String deptHISCode) {
		this.deptHISCode = deptHISCode;
	}
	
	public String getDoctorAdviceContent() {
		return doctorAdviceContent;
	}

	public void setDoctorAdviceContent(String doctorAdviceContent) {
		this.doctorAdviceContent = doctorAdviceContent;
	}
	
	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
	
	public String getDoctorAdviceNum() {
		return doctorAdviceNum;
	}

	public void setDoctorAdviceNum(String doctorAdviceNum) {
		this.doctorAdviceNum = doctorAdviceNum;
	}
	
	
	
}
