package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import com.zebone.nhis.common.support.XmlElementAnno;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResponseInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {

    @XmlElementAnno
    @XmlElement(name = "ResponseInfo")
    private ResponseInfo responseInfo;

    @XmlElementAnno
    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElementAnno
    @XmlElement(name = "ResultDesc")
    private String resultDesc;

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
