package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("schedule")
public class Schedule {
	
	@XStreamAsAttribute
	private String classCode;
	
	@XStreamAsAttribute
	private String moodCode;
	
	private ResourceSlot resourceSlot;

	public ResourceSlot getResourceSlot() {
		if(resourceSlot == null) {
			resourceSlot = new ResourceSlot();
		}
		
		return resourceSlot;
	}

	public void setResourceSlot(ResourceSlot resourceSlot) {
		this.resourceSlot = resourceSlot;
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
	
	

}
