package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("serviceDeliveryLocation")
public class ServiceDeliveryLocation {
	@XStreamAsAttribute
	private String classCode;
	private Location location;
	private ServiceProviderOrganization serviceProviderOrganization;
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Location getLocation() {
		if(location==null) location=new Location();
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public ServiceProviderOrganization getServiceProviderOrganization() {
		if (serviceProviderOrganization==null)serviceProviderOrganization=new ServiceProviderOrganization();
		return serviceProviderOrganization;
	}
	public void setServiceProviderOrganization(
			ServiceProviderOrganization serviceProviderOrganization) {
		this.serviceProviderOrganization = serviceProviderOrganization;
	}
	
}
