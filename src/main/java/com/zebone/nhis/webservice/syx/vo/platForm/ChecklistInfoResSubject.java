package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class ChecklistInfoResSubject {

    @XmlElementWrapper(name="res")
    @XmlElement(name="item")
    private List<ChecklistInfoRes> item;

	public List<ChecklistInfoRes> getItem() {
		return item;
	}

	public void setItem(List<ChecklistInfoRes> item) {
		this.item = item;
	}

	

    
}
