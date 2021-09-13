package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WeiXinPayInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonInfo {

    @XmlElement(name = "HospitalId")
    private String hospitalId;

    @XmlElement(name = "PatientId")
    private String patientId;

    @XmlElement(name = "NoticeTime")
    private String noticeTime;

    @XmlElement(name = "NoticeType")
    private String noticeType;

    @XmlElement(name = "Title")
    private String title;

    @XmlElement(name = "Content")
    private String content;

    @XmlElement(name = "Url")
    private String url;

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


}
