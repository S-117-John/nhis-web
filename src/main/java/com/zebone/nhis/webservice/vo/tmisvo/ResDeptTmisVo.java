package com.zebone.nhis.webservice.vo.tmisvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.support.XmlElementAnno;

/**
 * 输血科室请求参数构造xml
 * @author frank
 *
 */
@XmlRootElement(name = "Request")
public class ResDeptTmisVo {
	@XmlElementAnno
	private String pkOrg;
	@XmlElementAnno
	private String orgCode;
	
	/**
	 * 就诊类型
	 */
	@XmlElementAnno
	private String euPvtype;
	/**
	 * 病案号
	 */
	@XmlElementAnno
	private String caseNum;
	
	/**
	 * 计费时间查询开始节点。
	 */
	@XmlElementAnno
	private String businessStartDate;
	
	/**
	 * 计费时间查询结束节点。
	 */
	@XmlElementAnno
	private String businessEndDate;
	
	/**
	 * 患者姓名。
	 */
	@XmlElementAnno
	private String patientName;
	
	/**
	 * 费用代码。多个用英文逗号分隔，需要解析。
	 */
	@XmlElementAnno
	private String feeItemHISCode;
	
	@XmlElementAnno
	private List<RequestDeptTmisVo> deptIDs;
	
	
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	@XmlElement(name = "HospHISCode")
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@XmlElement(name = "InHospitalID")
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	@XmlElement(name = "CaseNum")
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	
	@XmlElement(name = "BusinessStartDate")
	public String getBusinessStartDate() {
		return businessStartDate;
	}
	public void setBusinessStartDate(String businessStartDate) {
		this.businessStartDate = businessStartDate;
	}
	@XmlElement(name = "BusinessEndDate")
	public String getBusinessEndDate() {
		return businessEndDate;
	}
	public void setBusinessEndDate(String businessEndDate) {
		this.businessEndDate = businessEndDate;
	}
	@XmlElement(name = "PatientName")
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	@XmlElement(name = "FeeItemHISCode")
	public String getFeeItemHISCode() {
		return feeItemHISCode;
	}
	public void setFeeItemHISCode(String feeItemHISCode) {
		this.feeItemHISCode = feeItemHISCode;
	}
	@XmlElement(name = "DeptIDs")
	public List<RequestDeptTmisVo> getDeptIDs() {
		return deptIDs;
	}
	public void setDeptIDs(List<RequestDeptTmisVo> deptIDs) {
		this.deptIDs = deptIDs;
	}
}
