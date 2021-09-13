package com.zebone.nhis.ma.pub.platform.emr.vo.sendvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * create by: gao shiheng
 *
 * @Param: null
 * @return
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sender")
public class ReqEmrSender {

    @XmlElement(name="systemId")
    private String systemId;

    @XmlElement(name="systemName")
    private String systemName;

    @XmlElement(name="senderId")
    private String senderId;

    @XmlElement(name="sendername")
    private String senderName;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
