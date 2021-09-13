package com.zebone.nhis.webservice.vo.orgvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class OrgData {
  private OrgDataVo orgDataVo;
  @XmlElement(name="orgList")
  public OrgDataVo getOrgDataVo() {
	return orgDataVo;
  }

  public void setOrgDataVo(OrgDataVo orgDataVo) {
	this.orgDataVo = orgDataVo;
  }
  
}
