package com.zebone.nhis.webservice.vo.appschvo;

import javax.xml.bind.annotation.XmlElement;

public class ApptData {
	private ApptDataVo apptDataVo;
	
	@XmlElement(name="apptList")
	public ApptDataVo getApptDataVo() {
		return apptDataVo;
	}

	public void setApptDataVo(ApptDataVo apptDataVo) {
		this.apptDataVo = apptDataVo;
	}
	

}
