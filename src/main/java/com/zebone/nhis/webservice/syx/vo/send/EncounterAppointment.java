package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("encounterAppointment")
public class EncounterAppointment {
	
	private Id id;
	
	private Code code;
	
	private StatusCode statusCode;
	
	private EffectiveTime effectiveTime;

	private Subject subject;
	
	private ReusableDevice reusableDevice;
	
	private Performer performer;
	
	private ArrivedBy arrivedBy;

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

	public StatusCode getStatusCode() {
		if(statusCode == null ) {
			statusCode = new StatusCode();
		}
		
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime == null) {
			effectiveTime = new EffectiveTime(); 
		}		
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Subject getSubject() {
		if(subject == null) {
			subject = new Subject();
		}
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public ReusableDevice getReusableDevice() {
		if(reusableDevice == null) {
			reusableDevice = new ReusableDevice();
		}
		return reusableDevice;
	}

	public void setReusableDevice(ReusableDevice reusableDevice) {
		this.reusableDevice = reusableDevice;
	}

	public Performer getPerformer() {
		if(performer == null) {
			performer = new Performer();
		}
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

	public ArrivedBy getArrivedBy() {
		if(arrivedBy == null) {
			arrivedBy = new ArrivedBy();
		}
		
		return arrivedBy;
	}

	public void setArrivedBy(ArrivedBy arrivedBy) {
		this.arrivedBy = arrivedBy;
	}
	
	
}
