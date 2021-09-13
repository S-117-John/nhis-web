package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * receiver 标签中的<device> 内容
 * 
 * @author yx
 * 
 */
@XStreamAlias("device")
public class Device {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	
	private Id id ;
	

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

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}
	
	

}
