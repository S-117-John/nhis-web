package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class PubElement {
	@XStreamAsAttribute
	private String root;
	@XStreamAsAttribute
	private String extension;
	@XStreamAsAttribute
	private String code;
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	@XStreamAsAttribute
	private String moodCode;
	@XStreamAsAttribute
	private String contextConductionInd;
	@XStreamAsAttribute
	private String value;
	@XStreamAsAttribute
	private String XSI_TYPE;
	@XStreamAsAttribute
	private String codeSystem;
	@XStreamAsAttribute
	private String contextControlCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	@XStreamAsAttribute
	private String use;
	@XStreamAsAttribute
	private String type;
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeterminerCode() {
		return determinerCode;
	}
	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}
	public String getMoodCode() {
		return moodCode;
	}
	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}
	public String getContextConductionInd() {
		return contextConductionInd;
	}
	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}
	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getContextControlCode() {
		return contextControlCode;
	}
	public void setContextControlCode(String contextControlCode) {
		this.contextControlCode = contextControlCode;
	}
	
	public String getXSI_NIL() {
		return XSI_NIL;
	}
	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
