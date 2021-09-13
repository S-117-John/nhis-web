package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("location")
public class Location {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	@XStreamAsAttribute
	private String typeCode;
	
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private Time time;
	private StatusCode statusCode;
	private ServiceDeliveryLocation serviceDeliveryLocation;
	private Id id;
	private Name name;
	private AsLocatedEntityPartOf asLocatedEntityPartOf;
	private ServiceProviderOrganization serviceProviderOrganization;
	private LocatedEntityHasParts locatedEntityHasParts;
	private LocatedEntity locatedEntity;
	
	public String getXSI_NIL() {
		return XSI_NIL;
	}
	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	public Time getTime() {
		if(time==null) time=new Time();
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	
	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}
	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	public ServiceDeliveryLocation getServiceDeliveryLocation() {
		if(serviceDeliveryLocation==null)serviceDeliveryLocation=new ServiceDeliveryLocation();
		return serviceDeliveryLocation;
	}
	public void setServiceDeliveryLocation(
			ServiceDeliveryLocation serviceDeliveryLocation) {
		this.serviceDeliveryLocation = serviceDeliveryLocation;
	}
	public Id getId() {
		if(id==null)id=new Id();
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
	public AsLocatedEntityPartOf getAsLocatedEntityPartOf() {
		if(asLocatedEntityPartOf==null)asLocatedEntityPartOf=new AsLocatedEntityPartOf();
		return asLocatedEntityPartOf;
	}
	public void setAsLocatedEntityPartOf(AsLocatedEntityPartOf asLocatedEntityPartOf) {
		this.asLocatedEntityPartOf = asLocatedEntityPartOf;
	}
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
	public ServiceProviderOrganization getServiceProviderOrganization() {
		if(serviceProviderOrganization==null)serviceProviderOrganization=new ServiceProviderOrganization();
		return serviceProviderOrganization;
	}
	public void setServiceProviderOrganization(
			ServiceProviderOrganization serviceProviderOrganization) {
		this.serviceProviderOrganization = serviceProviderOrganization;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public LocatedEntityHasParts getLocatedEntityHasParts() {
		if(locatedEntityHasParts==null)locatedEntityHasParts=new LocatedEntityHasParts();
		return locatedEntityHasParts;
	}
	public void setLocatedEntityHasParts(LocatedEntityHasParts locatedEntityHasParts) {
		this.locatedEntityHasParts = locatedEntityHasParts;
	}
	public LocatedEntity getLocatedEntity() {
		if(locatedEntity==null) locatedEntity=new LocatedEntity();
		return locatedEntity;
	}
	public void setLocatedEntity(LocatedEntity locatedEntity) {
		this.locatedEntity = locatedEntity;
	}
	
}
