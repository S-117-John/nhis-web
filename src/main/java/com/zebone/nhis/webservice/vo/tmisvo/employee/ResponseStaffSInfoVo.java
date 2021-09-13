package com.zebone.nhis.webservice.vo.tmisvo.employee;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.tmisvo.dept.ResponseDeptVo;

/**
 * 输血返回构造xml
 * @author frank
 *职工信息
 */
@XmlRootElement(name = "SickRoomInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseStaffSInfoVo {
	@XmlElement(name = "SickRoomInfo")
	private List<ResponseStaffInfoVo> SickRoomInfo;

	public List<ResponseStaffInfoVo> getSickRoomInfo() {
		return SickRoomInfo;
	}

	public void setSickRoomInfo(List<ResponseStaffInfoVo> sickRoomInfo) {
		SickRoomInfo = sickRoomInfo;
	}
}
