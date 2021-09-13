package com.zebone.nhis.webservice.vo.effpvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class EffpvDataVo {
  private List<ResEffpVo> resEffpVos;
  @XmlElement(name="effPvInfo")
  public List<ResEffpVo> getResEffpVos() {
	  return resEffpVos;
  }

  public void setResEffpVos(List<ResEffpVo> resEffpVos) {
	  this.resEffpVos = resEffpVos;
  }
  
  
}
