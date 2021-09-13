package com.zebone.nhis.pro.sd.wechat.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "head")
public class WechatReqHeadvo {

	@XmlElement(name="key")
   private String key;
	
	@XmlElement(name="channelId")
   private String channelId;
	
	@XmlElement(name="token")
   private String token;
	
	@XmlElement(name="time")
   private String time;
	
   public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }

   public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }

   public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }

}
