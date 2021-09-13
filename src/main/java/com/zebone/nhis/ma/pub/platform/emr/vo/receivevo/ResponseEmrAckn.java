package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="acknowledgement")
public class ResponseEmrAckn {

    @XmlAttribute(name="typeCode")
    private String typeCode;

    @XmlElement(name = "targetMessage")
    private ResponseEmrTarMess responseEmrTarMess;

    @XmlElement(name = "acknowledgementDetail")
    private ResponseEmrAID responseEmrAID;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public ResponseEmrTarMess getResponseEmrTarMess() {
        if(responseEmrTarMess==null)responseEmrTarMess=new ResponseEmrTarMess();
        return responseEmrTarMess;
    }

    public void setResponseEmrTarMess(ResponseEmrTarMess responseEmrTarMess) {
        this.responseEmrTarMess = responseEmrTarMess;
    }

    public ResponseEmrAID getResponseEmrAID() {
        if(responseEmrAID==null)responseEmrAID=new ResponseEmrAID();
        return responseEmrAID;
    }

    public void setResponseEmrAID(ResponseEmrAID responseEmrAID) {
        this.responseEmrAID = responseEmrAID;
    }

}
