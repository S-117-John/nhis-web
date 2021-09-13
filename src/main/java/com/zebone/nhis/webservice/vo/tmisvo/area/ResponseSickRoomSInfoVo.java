package com.zebone.nhis.webservice.vo.tmisvo.area;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.tmisvo.dept.ResponseDeptVo;

/**
 * 输血返回构造xml
 * @author frank
 *病区列表
 */
@XmlRootElement(name = "SickRoomInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseSickRoomSInfoVo {
	@XmlElement(name = "SickRoomInfo")
	private List<ResponseSickRoomInfoVo> sickRoomInfo;


	public List<ResponseSickRoomInfoVo> getSickRoomInfo() {
		return sickRoomInfo;
	}

	public void setSickRoomInfo(List<ResponseSickRoomInfoVo> sickRoomInfo) {
		this.sickRoomInfo = sickRoomInfo;
	}
	
}
