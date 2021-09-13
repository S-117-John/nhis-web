package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ROOT")
public class Root {
	/**
	 * 处方号
	 */
	@XmlElement(name="PRESC_NO")
	private String prescNo;
	
	/**
	 * 发药窗口
	 */
	@XmlElement(name="RETMSG")
	private String retmsg;
	/**
	 * 配药窗口
	 */
	@XmlElement(name="PYRETMSG")
	private String pyretmsg;
	public String getPrescNo() {
		return prescNo;
	}
	public void setPrescNo(String prescNo) {
		this.prescNo = prescNo;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getPyretmsg() {
		return pyretmsg;
	}
	public void setPyretmsg(String pyretmsg) {
		this.pyretmsg = pyretmsg;
	}
	
	
}
