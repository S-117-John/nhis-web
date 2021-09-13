package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class RegListInfoResSubject {
	
	@XmlElementWrapper(name="res")
	@XmlElement(name="RegListInfo")
    private List<RegListInfo> res;

	public List<RegListInfo> getRes() {
		return res;
	}

	public void setRes(List<RegListInfo> res) {
		this.res = res;
	}

}
