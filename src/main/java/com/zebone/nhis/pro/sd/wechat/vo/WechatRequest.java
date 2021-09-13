package com.zebone.nhis.pro.sd.wechat.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class WechatRequest {
	 @XmlElement(name="head")
	 private WechatReqHeadvo head;
	 
	 @XmlElement(name="body")
	 private WechatReqBodyVo body;
	 
	 public void setHead(WechatReqHeadvo head) {
	     this.head = head;
	 }
	 public WechatReqHeadvo getHead() {
	     return head;
	 }

	 public void setBody(WechatReqBodyVo body) {
	     this.body = body;
	 }
	 public WechatReqBodyVo getBody() {
	     return body;
	 }
}


