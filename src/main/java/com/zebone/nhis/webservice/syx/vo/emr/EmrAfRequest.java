package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 房颤接口-请求
 * @author chengjia
 *
 */
public class EmrAfRequest {
	@JsonProperty("ChannelID")
	private String channelID;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endtime;
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	
	public Date getStartime() {
		return startime;
	}
	public void setStartime(Date startime) {
		this.startime = startime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	
}
