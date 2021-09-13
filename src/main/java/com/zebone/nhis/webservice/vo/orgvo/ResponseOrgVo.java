package com.zebone.nhis.webservice.vo.orgvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="res")
public class ResponseOrgVo {
  private String status;
  private String desc;
  private String errorMessage;
  private OrgData orgData;
  
  @XmlElement(name="status")
  public String getStatus() {
	return status;
 }
   public void setStatus(String status) {
	this.status = status;
 }
   @XmlElement(name="desc")
   public String getDesc() {
	   return desc;
   }
   public void setDesc(String desc) {
	   this.desc = desc;
   }
   @XmlElement(name="errorMessage")
   public String getErrorMessage() {
	   return errorMessage;
   }
   public void setErrorMessage(String errorMessage) {
	   this.errorMessage = errorMessage;
   }
   @XmlElement(name="data")
public OrgData getOrgData() {
	return orgData;
}
public void setOrgData(OrgData orgData) {
	this.orgData = orgData;
}
 
   
  
  
}
