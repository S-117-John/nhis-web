package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="targetMessage")
public class ResponseEmrTarMess {

    @XmlElement(name = "id")
    private ResponseEmrId responseEmrId;

    public ResponseEmrId getResponseEmrId() {
        if(responseEmrId==null)responseEmrId=new ResponseEmrId();
        return responseEmrId;
    }

    public void setResponseEmrId(ResponseEmrId responseEmrId) {
        this.responseEmrId = responseEmrId;
    }
}
