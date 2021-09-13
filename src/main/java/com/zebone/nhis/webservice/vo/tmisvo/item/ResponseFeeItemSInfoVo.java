package com.zebone.nhis.webservice.vo.tmisvo.item;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.tmisvo.employee.ResponseStaffInfoVo;

/**
 * 输血返回构造xml
 * @author frank
 *收费项目信息
 */
@XmlRootElement(name = "FeeItemInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFeeItemSInfoVo {
	@XmlElement(name = "FeeItemInfo")
	private List<ResponseFeeItemInfoVo> SickRoomInfo;

	public List<ResponseFeeItemInfoVo> getSickRoomInfo() {
		return SickRoomInfo;
	}

	public void setSickRoomInfo(List<ResponseFeeItemInfoVo> sickRoomInfo) {
		SickRoomInfo = sickRoomInfo;
	}
	
}
