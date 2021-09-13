/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlType(name = "ADDRESS_LIST")
public class AddressList {
//	@XmlElement(name = "ADDRESS")
//    private Address address;
	@XmlElementWrapper(name = "ADDRESS_LIST")  
	@XmlElement(name = "ADDRESS") 
	private List<Address> AddressList;
//	public Address getAddress() {
//		return address;
//	}
//	public void setAddress(Address address) {
//		this.address = address;
//	}
	public List<Address> getAddressList() {
		return AddressList;
	}
	public void setAddressList(List<Address> addressList) {
		AddressList = addressList;
	}
	
}