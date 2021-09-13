package com.zebone.nhis.webservice.vo.tmisvo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.tmisvo.area.ResponseSickRoomInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.dept.ResponseDeptVo;
import com.zebone.nhis.webservice.vo.tmisvo.employee.ResponseStaffInfoVo;
import com.zebone.nhis.webservice.vo.tmisvo.item.ResponseFeeItemInfoVo;


/**
 * 输血返回构造xml
 * @author frank
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseImisVo {
	@XmlElement(name = "ResultCode")
	private String resultCode;
	@XmlElement(name = "ResultContent")
	private String resultContent;
	
	@XmlElement(name = "PatientInfo")
	private PatientInfoVo patientInfoVo;
	
	@XmlElement(name = "BldTypeDisInfos")
	private List<BldTypeDisInfoVo> bldTypeDisInfos;
	
	@XmlElement(name = "HISFeeLists")
	private List<ResponseHISFeeVo> hISFeeLists;
	
	
	
	@XmlElement(name = "DoctorAdvices")
	private List<ResponseDoctorAdviceVo> doctorAdvices;



	public String getResultCode() {
		return resultCode;
	}



	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}



	public String getResultContent() {
		return resultContent;
	}



	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}



	public PatientInfoVo getPatientInfoVo() {
		return patientInfoVo;
	}



	public void setPatientInfoVo(PatientInfoVo patientInfoVo) {
		this.patientInfoVo = patientInfoVo;
	}



	public List<BldTypeDisInfoVo> getBldTypeDisInfos() {
		return bldTypeDisInfos;
	}



	public void setBldTypeDisInfos(List<BldTypeDisInfoVo> bldTypeDisInfos) {
		this.bldTypeDisInfos = bldTypeDisInfos;
	}



	public List<ResponseHISFeeVo> gethISFeeLists() {
		return hISFeeLists;
	}



	public void sethISFeeLists(List<ResponseHISFeeVo> hISFeeLists) {
		this.hISFeeLists = hISFeeLists;
	}



	public List<ResponseDoctorAdviceVo> getDoctorAdvices() {
		return doctorAdvices;
	}



	public void setDoctorAdvices(List<ResponseDoctorAdviceVo> doctorAdvices) {
		this.doctorAdvices = doctorAdvices;
	}
	
	
	
}
