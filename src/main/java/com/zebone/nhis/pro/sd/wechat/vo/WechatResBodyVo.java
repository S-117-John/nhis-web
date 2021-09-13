package com.zebone.nhis.pro.sd.wechat.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "body")
public class WechatResBodyVo {

	 @XmlElement(name="bill")
	 private List<WechatResBillVo> bill;
	 
	 public void setBill(List<WechatResBillVo> bill) {
	      this.bill = bill;
	 }
	 
	 public List<WechatResBillVo> getBill() {
	      return bill;
	 }

}