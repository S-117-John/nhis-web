package com.zebone.nhis.webservice.syx.vo.selfmac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "req")
public class SettleInfoReq {

	//方法名
    @XmlElement(name = "actionId")
	private String actionId;
	
	//住院号
    @XmlElement(name = "ipSeqnoText")
	private String ipSeqnoText;
	
	//患者姓名
    @XmlElement(name = "patientName")
	private String patientName;
	
	//身份证号
    @XmlElement(name = "userCardId")
	private String userCardId;
	
	//开始时间
    @XmlElement(name = "beginDate")
	private String beginDate;
	
	//结束时间
    @XmlElement(name = "endDate")
	private String endDate;

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getIpSeqnoText() {
		return ipSeqnoText;
	}

	public void setIpSeqnoText(String ipSeqnoText) {
		this.ipSeqnoText = ipSeqnoText;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
