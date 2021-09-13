package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("manufacturedProduct")
public class ManufacturedProduct {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	
	private Code code;
	private Name name;
	private AsContent asContent;
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
		if(code==null)code=new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public Name getName() {
		if(name==null) name=new Name();
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public AsContent getAsContent() {
		if(asContent==null) asContent=new AsContent();
		return asContent;
	}
	public void setAsContent(AsContent asContent) {
		this.asContent = asContent;
	}
	
	
}
