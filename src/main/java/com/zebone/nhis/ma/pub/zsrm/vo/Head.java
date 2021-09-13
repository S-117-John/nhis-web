package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class Head {
	@XmlElement(name = "Merid")
	private String merid;
	@XmlElement(name = "Retcode")
	private String retCode;
	@XmlElement(name = "Errmsg")
	private String errMsg;
	@XmlElement(name = "Funcid")
	private String funcid;
	@XmlElement(name = "Timer")
	private String timer;
	@XmlElement(name = "Termid")
	private String termid;

	public Head() {
	}

	public Head(String merid, String funcid, String timer) {
		this.merid = merid;
		this.funcid = funcid;
		this.timer = timer;
	}

	public Head(String merid, String retCode, String errMsg, String funcid, String timer, String termid) {
		this.merid = merid;
		this.retCode = retCode;
		this.errMsg = errMsg;
		this.funcid = funcid;
		this.timer = timer;
		this.termid = termid;
	}

	public String getMerid() {
		return merid;
	}

	public void setMerid(String merid) {
		this.merid = merid;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getFuncid() {
		return funcid;
	}

	public void setFuncid(String funcid) {
		this.funcid = funcid;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

}
