package com.zebone.nhis.webservice.vo.appoinvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class AppointmentDataVo {
	
	private List<ResAppointmentVo> resAppointmentVo;
    
	@XmlElement(name = "schAppt")
	public List<ResAppointmentVo> getResAppointmentVo() {
		return resAppointmentVo;
	}

	public void setResAppointmentVo(List<ResAppointmentVo> resAppointmentVo) {
		this.resAppointmentVo = resAppointmentVo;
	}

}
