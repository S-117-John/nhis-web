package com.zebone.nhis.webservice.syx.vo.send;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("placerGroup")
public class PlacerGroup {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private Author author;
	
	private Verifier verifier;
	
	
	@XStreamImplicit
	private List<Component2> component2s;
	
	private ComponentOf1 componentOf1;

	private Code code;	
	
	private StatusCode statusCode;
	
	private Transcriber transcriber;
	
	private Location location;
	
	private Component2 component2;
	
	public Component2 getComponent2() {
		if(component2 == null) {
			component2 = new Component2();
		}
		return component2;
	}

	public void setComponent2(Component2 component2) {
		this.component2 = component2;
	}

	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}

	public Location getLocation() {
		if(location == null) {
			location = new Location();
		}
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Transcriber getTranscriber() {
		if(transcriber == null) {
			transcriber = new Transcriber();
		}
		return transcriber;
	}

	public void setTranscriber(Transcriber transcriber) {
		this.transcriber = transcriber;
	}

	public StatusCode getStatusCode() {
		if(statusCode == null) {
			statusCode = new StatusCode();
		}
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
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

	public Author getAuthor() {
		if(author==null) author=new Author();
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Verifier getVerifier() {
		if(verifier==null) verifier=new Verifier();
		return verifier;
	}

	public void setVerifier(Verifier verifier) {
		this.verifier = verifier;
	}

	

	public List<Component2> getComponent2s() {
		if(component2s==null) component2s=new ArrayList<>();
		return component2s;
	}

	public void setComponent2s(List<Component2> component2s) {
		this.component2s = component2s;
	}

	public ComponentOf1 getComponentOf1() {
		if(componentOf1==null)componentOf1=new ComponentOf1(); 
		return componentOf1;
	}

	public void setComponentOf1(ComponentOf1 componentOf1) {
		this.componentOf1 = componentOf1;
	}
	
	
}
