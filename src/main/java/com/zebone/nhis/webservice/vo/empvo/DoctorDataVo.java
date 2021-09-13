package com.zebone.nhis.webservice.vo.empvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DoctorDataVo {
	private List<ResDoctorVo> resDoctorVo;

	@XmlElement(name = "doctor")
	public List<ResDoctorVo> getResDoctorVo() {
		return resDoctorVo;
	}

	public void setResDoctorVo(List<ResDoctorVo> resDoctorVo) {
		this.resDoctorVo = resDoctorVo;
	}

}
