package com.zebone.nhis.webservice.vo.pvinfovo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PvInfoDataVo {
	
  private List<ResPvInfoVo> resPvInfoVos;
  
  @XmlElement(name="pvInfo")
  public List<ResPvInfoVo> getResPvInfoVos() {
		return resPvInfoVos;
  }

  public void setResPvInfoVos(List<ResPvInfoVo> resPvInfoVos) {
	this.resPvInfoVos = resPvInfoVos;
  }

  
}
