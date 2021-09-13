package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "res")
public class PlatFormOnlineInfoReqSubjectRes {
	@XmlElement(name = "onlineBlopdtVo")
	private List<OnlineBlopdtVo> onlineBlopdtVo;

	@XmlElement(name = "pkPv")
	private String pkPv;

	@XmlElement(name = "codeHpst")
	private String codeHpst;
	@XmlElement(name = "userName")
	private String userName;
	public List<OnlineBlopdtVo> getOnlineBlopdtVo() {
		return onlineBlopdtVo;
	}
	public void setOnlineBlopdtVo(List<OnlineBlopdtVo> onlineBlopdtVo) {
		this.onlineBlopdtVo = onlineBlopdtVo;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeHpst() {
		return codeHpst;
	}
	public void setCodeHpst(String codeHpst) {
		this.codeHpst = codeHpst;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
