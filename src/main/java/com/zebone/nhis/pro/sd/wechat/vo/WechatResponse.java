package com.zebone.nhis.pro.sd.wechat.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class WechatResponse {
	@XmlElement(name="head")
	private WechatResHeadvo head;
	
	@XmlElement(name="body")
    private WechatResBodyVo body;
    public void setHead(WechatResHeadvo head) {
         this.head = head;
     }
     public WechatResHeadvo getHead() {
         return head;
     }

    public void setBody(WechatResBodyVo body) {
         this.body = body;
     }
     public WechatResBodyVo getBody() {
         return body;
     }

}
