package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("pertinentInformation1")
public class PertinentInformation1 {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	private ObservationDx observationDx;
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	public String getXSI_NIL() {
		return XSI_NIL;
	}
	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	public ObservationDx getObservationDx() {
		if(observationDx==null)observationDx = new ObservationDx();
		return observationDx;
	}
	public void setObservationDx(ObservationDx observationDx) {
		this.observationDx = observationDx;
	}
	
}
