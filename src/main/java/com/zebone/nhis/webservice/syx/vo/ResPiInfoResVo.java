package com.zebone.nhis.webservice.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.syx.vo.platForm.ResIpUserInfo;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResPiInfoResVo {
	@XmlElementWrapper(name="res")
	@XmlElement(name="userInfo")
	private List<ResIpUserInfo> userInfos;

	public List<ResIpUserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<ResIpUserInfo> userInfos) {
		this.userInfos = userInfos;
	}
	
	
}
