package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输血返回构造xml
 * @author frank
 *医嘱回传返回
 */
@XmlRootElement(name = "DoctorAdvice")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseDoctorAdviceVo {
	@XmlElement(name = "DoctorAdviceMapInfo")
	public String doctorAdviceMapInfo;
	
	@XmlElement(name = "DoctorAdviceNumFee")
	public String doctorAdviceNumFee;

	public String getDoctorAdviceMapInfo() {
		return doctorAdviceMapInfo;
	}

	public void setDoctorAdviceMapInfo(String doctorAdviceMapInfo) {
		this.doctorAdviceMapInfo = doctorAdviceMapInfo;
	}

	public String getDoctorAdviceNumFee() {
		return doctorAdviceNumFee;
	}

	public void setDoctorAdviceNumFee(String doctorAdviceNumFee) {
		this.doctorAdviceNumFee = doctorAdviceNumFee;
	}
	
	
	
}
