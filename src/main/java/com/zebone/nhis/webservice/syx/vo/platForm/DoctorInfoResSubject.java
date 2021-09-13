package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class DoctorInfoResSubject {

	@XmlElementWrapper(name="res")
	@XmlElement(name="doctorInfo")
    private List<DoctorInfo> res;

	public List<DoctorInfo> getRes() {
		return res;
	}

	public void setRes(List<DoctorInfo> res) {
		this.res = res;
	}
}
