package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("registrationRequest")
public class RegistrationRequest {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private StatusCode statusCode ;
	
	private Subject1 subject1 ;
	
	private Author author;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public Subject1 getSubject1() {
		if(subject1==null) subject1=new Subject1();
		return subject1;
	}

	public void setSubject1(Subject1 subject1) {
		this.subject1 = subject1;
	}

	public Author getAuthor() {
		if(author==null) author=new Author();
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	

}
