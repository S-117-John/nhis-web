package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("consumable2")
public class Consumable2 {
	@XStreamAsAttribute
	private String typeCode;
	private ManufacturedProduct1 manufacturedProduct1;
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public ManufacturedProduct1 getManufacturedProduct1() {
		if(manufacturedProduct1==null) manufacturedProduct1=new ManufacturedProduct1();
		return manufacturedProduct1;
	}
	public void setManufacturedProduct1(ManufacturedProduct1 manufacturedProduct1) {
		this.manufacturedProduct1 = manufacturedProduct1;
	}
	
}
