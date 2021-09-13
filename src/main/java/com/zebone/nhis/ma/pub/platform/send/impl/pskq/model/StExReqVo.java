package com.zebone.nhis.ma.pub.platform.send.impl.pskq.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class StExReqVo {

    @XmlElement(name = "AppCheckList")
    private StExInfoVo exInfoVo;

    public StExInfoVo getExInfoVo() {
        return exInfoVo;
    }

    public void setExInfoVo(StExInfoVo exInfoVo) {
        this.exInfoVo = exInfoVo;
    }
}
