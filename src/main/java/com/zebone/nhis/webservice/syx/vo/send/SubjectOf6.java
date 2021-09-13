package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("subjectOf6")
public class SubjectOf6 {
	@XStreamAsAttribute
	private String contextConductionInd;
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private SeperatableInd seperatableInd;
	
	private Annotation annotation;

	public String getContextConductionInd() {
		return contextConductionInd;
	}

	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public SeperatableInd getSeperatableInd() {
		if(seperatableInd==null) seperatableInd=new SeperatableInd();
		return seperatableInd;
	}

	public void setSeperatableInd(SeperatableInd seperatableInd) {
		this.seperatableInd = seperatableInd;
	}

	public Annotation getAnnotation() {
		if(annotation==null)annotation=new Annotation();
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	
	
}
