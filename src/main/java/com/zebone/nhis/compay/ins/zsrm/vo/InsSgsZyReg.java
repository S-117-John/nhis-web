package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "program")
public class InsSgsZyReg extends InsSgsPubParam {
    @XmlElement(name = "aaz218")
    private String aaz218;

    public String getAaz218() {
        return aaz218;
    }

    public void setAaz218(String aaz218) {
        this.aaz218 = aaz218;
    }
}
