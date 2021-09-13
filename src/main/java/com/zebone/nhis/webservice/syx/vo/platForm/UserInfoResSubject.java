package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class UserInfoResSubject {
	
	@XmlElementWrapper(name="res")
	@XmlElement(name="userInfo")
    private List<UserInfo> res;

	public List<UserInfo> getRes() {
		return res;
	}

	public void setRes(List<UserInfo> res) {
		this.res = res;
	}

}
