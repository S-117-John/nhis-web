package com.zebone.nhis.ma.pub.platform.pskq.model.param;

import com.zebone.nhis.common.support.XmlElementAnno;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ResponseInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseInfo {

    @XmlElementAnno
    @XmlElement(name = "CodeUrl")
    private String codeUrl;

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
