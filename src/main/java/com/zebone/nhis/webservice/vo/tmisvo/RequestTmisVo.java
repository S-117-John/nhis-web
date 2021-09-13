package com.zebone.nhis.webservice.vo.tmisvo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 输血请求参数构造xml
 * @author frank
 *
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestTmisVo {

	@XmlElement(name = "HospHISCode")
	private String orgCode;
	
	@XmlElementWrapper(name = "DeptIDs")
	@XmlElement(name = "DeptID")
	private List<RequestDeptTmisVo> DeptID;
	
	@XmlElementWrapper(name = "FeeInfos")
	@XmlElement(name = "FeeInfo")
	private List<FeeInfoVo> FeeInfo;
	
	@XmlElementWrapper(name = "DoctorAdvices")
	@XmlElement(name = "DoctorAdvice")
	private List<DoctorAdviceVo> DoctorAdvice;
	/**
	 * 就诊类型
	 */
	@XmlElement(name = "InHospitalID")
	private String euPvtype;
	/**
	 * 病案号
	 */
	@XmlElement(name = "CaseNum")
	private String caseNum;
	
	/**
	 * 计费时间查询开始节点。
	 */
	@XmlElement(name = "BusinessStartDate")
	private String businessStartDate;
	
	/**
	 * 计费时间查询结束节点。
	 */
	@XmlElement(name = "BusinessEndDate")
	private String businessEndDate;
	
	/**
	 * 患者姓名。
	 */
	@XmlElement(name = "PatientName")
	private String patientName;
	
	/**
	 * 费用代码。多个用英文逗号分隔，需要解析。
	 */
	@XmlElement(name = "FeeItemHISCode")
	private String feeItemHISCode;
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
    
	public List<RequestDeptTmisVo> getDeptID() {
		return DeptID;
	}
	public void setDeptID(List<RequestDeptTmisVo> deptID) {
		DeptID = deptID;
	}
	public List<FeeInfoVo> getFeeInfo() {
		return FeeInfo;
	}
	public void setFeeInfo(List<FeeInfoVo> feeInfo) {
		FeeInfo = feeInfo;
	}
	public List<DoctorAdviceVo> getDoctorAdvice() {
		return DoctorAdvice;
	}
	public void setDoctorAdvice(List<DoctorAdviceVo> doctorAdvice) {
		DoctorAdvice = doctorAdvice;
	}
	public String getBusinessStartDate() {
		return businessStartDate;
	}
	public void setBusinessStartDate(String businessStartDate) {
		this.businessStartDate = businessStartDate;
	}
	public String getBusinessEndDate() {
		return businessEndDate;
	}
	public void setBusinessEndDate(String businessEndDate) {
		this.businessEndDate = businessEndDate;
	}
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public String getFeeItemHISCode() {
		return feeItemHISCode;
	}
	public void setFeeItemHISCode(String feeItemHISCode) {
		this.feeItemHISCode = feeItemHISCode;
	}
	
}
