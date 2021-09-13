package com.zebone.nhis.webservice.vo.appschvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ApptDataVo {
	private List<ResAppSchVo> apptSchVos;
	@XmlElement(name="appt")
	public List<ResAppSchVo> getApptSchVos() {
		return apptSchVos;
	}

	public void setApptSchVos(List<ResAppSchVo> apptSchVos) {
		this.apptSchVos = apptSchVos;
	}

}
