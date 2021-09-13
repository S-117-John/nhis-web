package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("verifier")
public class Verifier {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String contextControlCode;
	private Time time;
	private SignatureCode signatureCode;
	private SignatureText signatureText;
	private AssignedEntity assignedEntity;
	
	
	public String getContextControlCode() {
		return contextControlCode;
	}

	public void setContextControlCode(String contextControlCode) {
		this.contextControlCode = contextControlCode;
	}

	public SignatureCode getSignatureCode() {
		if(signatureCode==null) signatureCode=new SignatureCode();
		return signatureCode;
	}

	public void setSignatureCode(SignatureCode signatureCode) {
		this.signatureCode = signatureCode;
	}

	public SignatureText getSignatureText() {
		if(signatureText==null) signatureText=new SignatureText();
		return signatureText;
	}

	public void setSignatureText(SignatureText signatureText) {
		this.signatureText = signatureText;
	}

	public AssignedEntity getAssignedEntity() {
		if(assignedEntity==null)assignedEntity=new AssignedEntity();
		return assignedEntity;
	}

	public void setAssignedEntity(AssignedEntity assignedEntity) {
		this.assignedEntity = assignedEntity;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Time getTime() {
		if(time==null)time=new Time();
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
}
