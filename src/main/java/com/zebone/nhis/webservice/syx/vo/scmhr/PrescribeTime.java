package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="prescribe_time")
public class PrescribeTime {
	@XmlElement(name="prescribe_time_start")
	private String prescribeTimeStart;
	@XmlElement(name="prescribe_time_stop")
	private String prescribeTimeStop;
	public String getPrescribeTimeStart() {
		return prescribeTimeStart;
	}
	public void setPrescribeTimeStart(String prescribeTimeStart) {
		this.prescribeTimeStart = prescribeTimeStart;
	}
	public String getPrescribeTimeStop() {
		return prescribeTimeStop;
	}
	public void setPrescribeTimeStop(String prescribeTimeStop) {
		this.prescribeTimeStop = prescribeTimeStop;
	}
	
}
