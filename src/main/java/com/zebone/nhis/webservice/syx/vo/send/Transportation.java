package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("transportation")
public class Transportation {
	@XStreamAsAttribute
	private String moodCode;
	
	private Code code;
	
	private Location location;

	public String getMoodCode() {
		return moodCode;
	}

	public void setMoodCode(String moodCode) {
		this.moodCode = moodCode;
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

	public Location getLocation() {
		if(location == null) {
			location = new Location();	
		}
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	

}
