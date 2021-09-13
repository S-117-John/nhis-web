package com.zebone.nhis.ma.pub.platform.emr.vo.receivevo;


import javax.xml.bind.annotation.*;

/**EMR质控-集成平台返回消息VO
 * create by: gao shiheng
 *
 * @Param: null
 * @return
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="MCCI_IN000002UV01")
public class ResponseEmrVo {

   /* @XmlAttribute(name="ITSVersion")
    private String ITSVersion;
    @XmlAttribute(name="xmlns")
    private String xmlns;
    @XmlAttribute(name="xmlns:xsi")
    private String xmlnsxsi;
    @XmlAttribute(name="xsi:schemaLocation=")
    private String xsischemaLocation;*/

    @XmlElement(name = "id")
    private ResponseEmrId responseEmrId;
    @XmlElement(name = "creationTime")
    private ResponseEmrCT creationTime;
    @XmlElement(name = "interactionId")
    private ResponseEmrIntId interactionId;
    @XmlElement(name = "processingCode")
    private ResponseEmrProCode processingCode;
    @XmlElement(name = "processingModeCode")
    private String processingModeCode;
    @XmlElement(name = "acceptAckCode")
    private ResponseEmrAccAckCode acceptAckCode;
    @XmlElement(name = "receiver")
    private ResponseEmrRece responseEmrRece;
    @XmlElement(name = "sender")
    private ResponseEmrSender responseEmrSender;
    @XmlElement(name = "acknowledgement")
    private ResponseEmrAckn responseEmrAckn;

    /*public String getITSVersion() {
        return ITSVersion;
    }

    public void setITSVersion(String ITSVersion) {
        this.ITSVersion = ITSVersion;
    }*/

    public ResponseEmrId getResponseEmrId() {
        if(responseEmrId==null)responseEmrId=new ResponseEmrId();
        return responseEmrId;
    }

    public void setResponseEmrId(ResponseEmrId responseEmrId) {
        this.responseEmrId = responseEmrId;
    }

    public ResponseEmrCT getCreationTime() {
        if(creationTime==null)creationTime=new ResponseEmrCT();
        return creationTime;
    }

    public void setCreationTime(ResponseEmrCT creationTime) {
        this.creationTime = creationTime;
    }

    public ResponseEmrIntId getInteractionId() {
        if(interactionId==null)interactionId=new ResponseEmrIntId();
        return interactionId;
    }

    public void setInteractionId(ResponseEmrIntId interactionId) {
        this.interactionId = interactionId;
    }

    public ResponseEmrProCode getProcessingCode() {
        if(processingCode==null)processingCode=new ResponseEmrProCode();
        return processingCode;
    }

    public void setProcessingCode(ResponseEmrProCode processingCode) {
        this.processingCode = processingCode;
    }

    public String getProcessingModeCode() {
        return processingModeCode;
    }

    public void setProcessingModeCode(String processingModeCode) {
        this.processingModeCode = processingModeCode;
    }

    public ResponseEmrAccAckCode getAcceptAckCode() {
        if(acceptAckCode==null)acceptAckCode=new ResponseEmrAccAckCode();
        return acceptAckCode;
    }

    public void setAcceptAckCode(ResponseEmrAccAckCode acceptAckCode) {
        this.acceptAckCode = acceptAckCode;
    }

    public ResponseEmrRece getResponseEmrRece() {
        if(responseEmrRece==null)responseEmrRece=new ResponseEmrRece();
        return responseEmrRece;
    }

    public void setResponseEmrRece(ResponseEmrRece responseEmrRece) {
        this.responseEmrRece = responseEmrRece;
    }

    public ResponseEmrSender getResponseEmrSender() {
        if(responseEmrSender==null)responseEmrSender=new ResponseEmrSender();
        return responseEmrSender;
    }

    public void setResponseEmrSender(ResponseEmrSender responseEmrSender) {
        this.responseEmrSender = responseEmrSender;
    }

    public ResponseEmrAckn getResponseEmrAckn() {
        if(responseEmrAckn==null)responseEmrAckn=new ResponseEmrAckn();
        return responseEmrAckn;
    }

    public void setResponseEmrAckn(ResponseEmrAckn responseEmrAckn) {
        this.responseEmrAckn = responseEmrAckn;
    }

    /*public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getXmlnsxsi() {
        return xmlnsxsi;
    }

    public void setXmlnsxsi(String xmlnsxsi) {
        this.xmlnsxsi = xmlnsxsi;
    }

    public String getXsischemaLocation() {
        return xsischemaLocation;
    }

    public void setXsischemaLocation(String xsischemaLocation) {
        this.xsischemaLocation = xsischemaLocation;
    }*/
}
