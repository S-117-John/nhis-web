package com.zebone.nhis.webservice.vo.orgareavo;

import javax.xml.bind.annotation.XmlElement;


public class OrgAreaData {
  private OrgAreaDataVo orgAreaDataVo;
  @XmlElement(name="orgAreaList")
  public OrgAreaDataVo getOrgAreaDataVo() {
	return orgAreaDataVo;
  }

  public void setOrgAreaDataVo(OrgAreaDataVo orgAreaDataVo) {
	this.orgAreaDataVo = orgAreaDataVo;
  }
  
}
