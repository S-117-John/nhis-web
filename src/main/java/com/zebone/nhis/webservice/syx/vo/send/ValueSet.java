package com.zebone.nhis.webservice.syx.vo.send;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("valueSet")
public class ValueSet {
	private Id id;
	private StatusCode statusCode;
	private Desc desc;
	private ValueSetItems valueSetItems;
	private Version version;
	@XStreamImplicit
	private List<ValueSetItems> valueSetItemss;
	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public Desc getDesc() {
		if(desc==null) desc=new Desc();
		return desc;
	}
	public void setDesc(Desc desc) {
		this.desc = desc;
	}
	public ValueSetItems getValueSetItems() {
		if(valueSetItems==null) valueSetItems=new ValueSetItems();
		return valueSetItems;
	}
	public void setValueSetItems(ValueSetItems valueSetItems) {
		this.valueSetItems = valueSetItems;
	}
	public Version getVersion() {
		if(version==null)version=new Version();
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
	public List<ValueSetItems> getValueSetItemss() {
		if(valueSetItemss==null)valueSetItemss=new ArrayList<ValueSetItems>();
		return valueSetItemss;
	}
	public void setValueSetItemss(List<ValueSetItems> valueSetItemss) {
		this.valueSetItemss = valueSetItemss;
	}
	
}
