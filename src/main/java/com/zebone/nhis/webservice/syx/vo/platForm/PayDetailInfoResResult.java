package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class PayDetailInfoResResult{

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

    @XmlElement(name = "subject")
    private PayDetailInfoResSubject subject; 

	public PayDetailInfoResSubject getSubject() {
		return subject;
	}

	public void setSubject(PayDetailInfoResSubject subject) {
		this.subject = subject;
	}

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

}
