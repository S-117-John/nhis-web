package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BindInfoVo {

    @XmlElement(name = "ipSeqnoText")
    private String ipSeqnoText;

    @XmlElement(name = "logon")
    private String logon;

    @XmlElement(name = "password")
    private String password;

    @XmlElement(name = "registerDate")
    private String registerDate;

    @XmlElement(name = "dtPplatform")
    private String dtPplatform;

    @XmlElement(name = "creator")
    private String creator;

    @XmlElement(name = "operType")
    private String operType;

    public String getDtPplatform() {
        return dtPplatform;
    }

    public void setDtPplatform(String dtPplatform) {
        this.dtPplatform = dtPplatform;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getIpSeqnoText() {
        return ipSeqnoText;
    }

    public void setIpSeqnoText(String ipSeqnoText) {
        this.ipSeqnoText = ipSeqnoText;
    }

    public String getLogon() {
        return logon;
    }

    public void setLogon(String logon) {
        this.logon = logon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}
