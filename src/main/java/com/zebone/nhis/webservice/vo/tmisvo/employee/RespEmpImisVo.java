package com.zebone.nhis.webservice.vo.tmisvo.employee;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 输血返回构造xml
 * @author frank
 *职工信息
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespEmpImisVo {
	@XmlElement(name = "ResultCode")
	private String resultCode;
	@XmlElement(name = "ResultContent")
	private String resultContent;
	
	
	@XmlElement(name = "SickRoomInfos")
	private ResponseStaffSInfoVo sickRoomInfos;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultContent() {
		return resultContent;
	}
	public void setResultContent(String resultContent) {
		this.resultContent = resultContent;
	}
	public ResponseStaffSInfoVo getSickRoomInfos() {
		return sickRoomInfos;
	}
	public void setSickRoomInfos(ResponseStaffSInfoVo sickRoomInfos) {
		this.sickRoomInfos = sickRoomInfos;
	}
	
	
	
	
	
}
