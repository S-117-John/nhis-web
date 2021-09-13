package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 *  PRPA_IN201311UV02 标签中的<receiver> 内容对象
 * @author yx
 *
 */
@XStreamAlias("receiver")
public class Receiver {
	@XStreamAsAttribute
	private String typeCode;
	
	private Device device;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Device getDevice() {
		if(device==null) device=new Device();
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	

}
