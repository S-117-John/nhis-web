package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("transportationIntent")
public class TransportationIntent {
	@XStreamAsAttribute
	private String moodCode;
	
	private Id id;
	
	private Code code;
	
	private EffectiveTime effectiveTime;

	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
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

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime ==null) {
			effectiveTime = new EffectiveTime();
		}
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	
	

}
