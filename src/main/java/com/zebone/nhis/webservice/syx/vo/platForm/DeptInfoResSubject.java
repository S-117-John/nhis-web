package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class DeptInfoResSubject {
	
	@XmlElementWrapper(name="res")
	@XmlElement(name="deptInfo")
    private List<DeptInfo> res;

	public List<DeptInfo> getRes() {
		return res;
	}

	public void setRes(List<DeptInfo> res) {
		this.res = res;
	}
	
	

}
