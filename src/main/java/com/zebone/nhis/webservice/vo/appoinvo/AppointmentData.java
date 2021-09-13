package com.zebone.nhis.webservice.vo.appoinvo;

import javax.xml.bind.annotation.XmlElement;

public class AppointmentData {
	
	private AppointmentDataVo appointmentDataVo;

	@XmlElement(name = "schApptList")
	public AppointmentDataVo getAppointmentDataVo() {
		return appointmentDataVo;
	}

	public void setAppointmentDataVo(AppointmentDataVo appointmentDataVo) {
		this.appointmentDataVo = appointmentDataVo;
	}
}
