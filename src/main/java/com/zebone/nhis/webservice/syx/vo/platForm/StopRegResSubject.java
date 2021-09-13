package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class StopRegResSubject {

    @XmlElementWrapper(name="res")
    @XmlElement(name="Item")
    private List<StopRegRes> Item;

	public List<StopRegRes> getItem() {
		return Item;
	}

	public void setItem(List<StopRegRes> item) {
		Item = item;
	}

    
}
