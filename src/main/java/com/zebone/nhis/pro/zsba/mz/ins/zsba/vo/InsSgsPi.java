package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "program")
public class InsSgsPi {

	private List<Personinfo> personinfo;
	@XmlElement(name = "freezeinfo")
	private List<Freezeinfo> freezeinfo;
	
	private List<Injuryorbirthinfo> injuryorbirthinfo;

	@XmlElement(name = "return_code")
	private String return_code;// 返回状态码 1>0 -成功，<0 -失败,-9-session过期

	// @XmlAttribute(name="return_code_message")
	@XmlElement(name = "return_code_message")
	private String return_code_message;// 提示信息

	public String getReturn_code() {
		return return_code;
	}

	public List<Injuryorbirthinfo> getInjuryorbirthinfo() {
		return injuryorbirthinfo;
	}

	public void setInjuryorbirthinfo(List<Injuryorbirthinfo> injuryorbirthinfo) {
		this.injuryorbirthinfo = injuryorbirthinfo;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_code_message() {
		return return_code_message;
	}

	public void setReturn_code_message(String return_code_message) {
		this.return_code_message = return_code_message;
	}

	public List<Freezeinfo> getFreezeinfo() {
		return freezeinfo;
	}

	public void setFreezeinfo(List<Freezeinfo> freezeinfo) {
		this.freezeinfo = freezeinfo;
	}

	public List<Personinfo> getPersoninfo() {
		return personinfo;
	}

	public void setPersoninfo(List<Personinfo> personinfo) {
		this.personinfo = personinfo;
	}

	// private List<Object> clinicapplyinfo;

}
