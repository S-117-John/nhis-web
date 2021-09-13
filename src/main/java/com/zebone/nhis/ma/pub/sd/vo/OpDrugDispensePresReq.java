package com.zebone.nhis.ma.pub.sd.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "MedPresinfo")
public class OpDrugDispensePresReq {
	
	@XmlElement(name="Med")
	private List<OpDrugDispensePresInfo> med;

	public List<OpDrugDispensePresInfo> getMed() {
		return med;
	}

	public void setMed(List<OpDrugDispensePresInfo> med) {
		this.med = med;
	}
	
}
