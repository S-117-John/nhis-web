package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="id")
public class ResponseEmrDevId {

    @XmlElement(name = "item")
    private ResponseEmrItem responseEmrItem;

    public ResponseEmrItem getResponseEmrItem() {
        if(responseEmrItem==null)responseEmrItem=new ResponseEmrItem();
        return responseEmrItem;
    }

    public void setResponseEmrItem(ResponseEmrItem responseEmrItem) {
        this.responseEmrItem = responseEmrItem;
    }
}
