package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
public class PlatFormResResultSucc {

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

    @XmlElementWrapper(name="subject")
    @XmlElement(name="res")
    private List<PlatFormResSubjectErr> subject;

    public void setSubject(List<PlatFormResSubjectErr> subject) {
        this.subject = subject;
    }

    public List<PlatFormResSubjectErr> getSubject() {
        return subject;
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
