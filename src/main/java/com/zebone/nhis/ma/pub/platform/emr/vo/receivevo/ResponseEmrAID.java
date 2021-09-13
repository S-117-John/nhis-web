package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="acknowledgementDetail")
public class ResponseEmrAID {

    @XmlElement(name = "text")
    private ResponseEmrText responseEmrText;

    public ResponseEmrText getResponseEmrText() {
        if(responseEmrText==null)responseEmrText=new ResponseEmrText();
        return responseEmrText;
    }

    public void setResponseEmrText(ResponseEmrText responseEmrText) {
        this.responseEmrText = responseEmrText;
    }
}
