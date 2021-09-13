package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("organizer")
public class Organizer {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String moodCode;
	@XStreamImplicit
	private List<Component> components;
	private Component component;
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
	public Component getComponent() {
		if(component==null)component=new Component();
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public List<Component> getComponents() {
		if(components==null)components = new ArrayList<Component>();
		return components;
	}
	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
	
}
