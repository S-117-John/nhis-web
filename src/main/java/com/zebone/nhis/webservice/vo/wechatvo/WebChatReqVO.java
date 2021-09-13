package com.zebone.nhis.webservice.vo.wechatvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "req")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebChatReqVO {
	
   /**
    * 院区编码
    */
   @XmlElement(name="hospId")
   private String hospId;
    /**
     * 设备编码
     */
    @XmlElement(name="deviceId")
    private String deviceid;
    /**
     * 开始时间
     */
    @XmlElement(name="startDate")
    private String startDate;
    /**
     * 结束时间
     */
    @XmlElement(name="endDate")
    private String endDate;
    
	public String getHospId() {
		return hospId;
	}
	public void setHospId(String hospId) {
		this.hospId = hospId;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	    
}
