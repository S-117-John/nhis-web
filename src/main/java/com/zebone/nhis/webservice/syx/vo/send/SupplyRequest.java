package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("supplyRequest")
public class SupplyRequest {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	
	private Id id;
	
	private Code code;
	
	private StatusCode statusCode;
	
	private Quantity quantity;
	
	private ExpectedUseTime expectedUseTime;

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

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Code getCode() {
		if(code==null)code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public Quantity getQuantity() {
		if(quantity==null) quantity=new Quantity();
		return quantity;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public ExpectedUseTime getExpectedUseTime() {
		if(expectedUseTime==null) expectedUseTime=new ExpectedUseTime();
		return expectedUseTime;
	}

	public void setExpectedUseTime(ExpectedUseTime expectedUseTime) {
		this.expectedUseTime = expectedUseTime;
	}
	
	
}
