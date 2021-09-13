package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="sender")
public class ResponseEmrSender {

    @XmlAttribute(name="typeCode")
    private String typeCode;

    @XmlElement(name = "device")
    private ResponseEmrDev responseEmrDev;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public ResponseEmrDev getResponseEmrDev() {
        if(responseEmrDev==null)responseEmrDev=new ResponseEmrDev();
        return responseEmrDev;
    }

    public void setResponseEmrDev(ResponseEmrDev responseEmrDev) {
        this.responseEmrDev = responseEmrDev;
    }
}
