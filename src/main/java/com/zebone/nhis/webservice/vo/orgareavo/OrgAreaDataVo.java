package com.zebone.nhis.webservice.vo.orgareavo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class OrgAreaDataVo {
  private List<ResOrgAreaVo> resOrgAreaVo;
  @XmlElement(name="orgArea")
  public List<ResOrgAreaVo> getResOrgAreaVo() {
	 return resOrgAreaVo;
  }

  public void setResOrgAreaVo(List<ResOrgAreaVo> resOrgAreaVo) {
 	this.resOrgAreaVo = resOrgAreaVo;
  }

public void setResDoctorVo(List<ResOrgAreaVo> doctorVoList) {
	// TODO Auto-generated method stub
	
}
  
  
}
