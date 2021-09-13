package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("containerPackagedProduct")
public class ContainerPackagedProduct {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Code code;
	private FormCode formCode;
	private CapacityQuantity capacityQuantity;
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
	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public FormCode getFormCode() {
		if(formCode==null) formCode=new FormCode();
		return formCode;
	}
	public void setFormCode(FormCode formCode) {
		this.formCode = formCode;
	}
	public CapacityQuantity getCapacityQuantity() {
		if(capacityQuantity==null) capacityQuantity=new CapacityQuantity();
		return capacityQuantity;
	}
	public void setCapacityQuantity(CapacityQuantity capacityQuantity) {
		this.capacityQuantity = capacityQuantity;
	}
	
}
