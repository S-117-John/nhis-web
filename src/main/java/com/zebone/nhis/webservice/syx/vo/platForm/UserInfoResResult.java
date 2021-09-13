package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class UserInfoResResult {
	
	/**
     * AA表示成功 AE表示失败
     */
    @XmlElement(name = "id")
    private String id;

    /**
     * 处理结果说明
     */
    @XmlElement(name = "text")
    private String text;

    /**
     * 请求消息ID
     */
    @XmlElement(name = "requestId")
    private String requestId;

    /**
     * 请求消息时间
     */
    @XmlElement(name = "requestTime")
    private String requestTime;

    @XmlElement(name="subject")
    private UserInfoResSubject subject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public UserInfoResSubject getSubject() {
		return subject;
	}

	public void setSubject(UserInfoResSubject subject) {
		this.subject = subject;
	}
        
}
