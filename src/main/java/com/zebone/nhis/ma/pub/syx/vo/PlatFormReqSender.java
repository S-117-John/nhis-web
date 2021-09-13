package com.zebone.nhis.ma.pub.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sender")
public class PlatFormReqSender {
    /**
     * 系统编号
     */
    @XmlElement(name = "systemId")
    private String systemId;
    /**
     * 系统名称
     */
    @XmlElement(name = "systemName")
    private String systemName;
    /**
     * 医务人员编号
     */
    @XmlElement(name = "senderId")
    private String senderId;
    /**
     * 医务人员名称
     */
    @XmlElement(name = "sendername")
    private String sendername;

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

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }
}
