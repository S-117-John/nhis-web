package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;


import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="device")
public class ResponseEmrDev {

    @XmlAttribute(name="classCode")
    private String classCode;

    @XmlAttribute(name="determinerCode")
    private String determinerCode;

    @XmlElement(name = "id")
    private ResponseEmrDevId responseEmrDevId;

    public String getClassCode() {

        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getDeterminerCode() {
        return determinerCode;
    }

    public void setDeterminerCode(String determinerCode) {
        this.determinerCode = determinerCode;
    }

    public ResponseEmrDevId getResponseEmrDevId() {
        if(responseEmrDevId==null)responseEmrDevId=new ResponseEmrDevId();
        return responseEmrDevId;
    }

    public void setResponseEmrDevId(ResponseEmrDevId responseEmrDevId) {
        this.responseEmrDevId = responseEmrDevId;
    }
}
