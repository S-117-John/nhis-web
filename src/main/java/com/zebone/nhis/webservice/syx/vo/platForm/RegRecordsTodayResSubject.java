package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class RegRecordsTodayResSubject {

    @XmlElementWrapper(name="res")
    @XmlElement(name="regListInfo")
    private List<RegRecordsTodayRes> regListInfo;

	public List<RegRecordsTodayRes> getItem() {
		return regListInfo;
	}

	public void setItem(List<RegRecordsTodayRes> regListInfo) {
		this.regListInfo = regListInfo;
	}

	

    
}
