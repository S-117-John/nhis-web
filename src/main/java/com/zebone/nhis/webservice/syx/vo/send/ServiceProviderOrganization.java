package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("serviceProviderOrganization")
public class ServiceProviderOrganization {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Id id;
	private Name name;
	private AsOrganizationPartOf asOrganizationPartOf;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeterminerCode() {
		return determinerCode;
	}
	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}
	public Id getId() {
		if(id==null) id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public AsOrganizationPartOf getAsOrganizationPartOf() {
		if(asOrganizationPartOf==null)asOrganizationPartOf=new AsOrganizationPartOf();
		return asOrganizationPartOf;
	}
	public void setAsOrganizationPartOf(AsOrganizationPartOf asOrganizationPartOf) {
		this.asOrganizationPartOf = asOrganizationPartOf;
	}
	
}
