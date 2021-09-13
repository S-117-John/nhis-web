package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("IdentifiedEntity")
public class IdentifiedEntity {
	
	@XStreamAsAttribute
	private String classCode;
	
	private Id id;
	
	private Code code;
	
	private Person person;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		if(id == null) {
			id = new Id();
		}
		
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Code getCode() {
		if(code == null) {
			code = new Code();
		}
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Person getPerson() {
		if(person == null) {
			person = new Person();
		}
		
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	} 
	
	

}
