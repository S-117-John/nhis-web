package com.zebone.nhis.webservice.vo.orgvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class OrgDataVo {
  private List<ResOrgVo> resOrgVo;
  @XmlElement(name="org")
  public List<ResOrgVo> getResOrgVo() {
	 return resOrgVo;
  }

  public void setResOrgVo(List<ResOrgVo> resOrgVo) {
 	this.resOrgVo = resOrgVo;
  }
  
  
}
