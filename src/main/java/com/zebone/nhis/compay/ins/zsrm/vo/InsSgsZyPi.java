package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "program")
public class InsSgsZyPi extends InsSgsPubParam {
    @XmlElement(name = "personinfo")
    private List<ZyPersonInfo> personinfo;

    public List<ZyPersonInfo> getPersoninfo() {
        return personinfo;
    }

    public void setPersoninfo(List<ZyPersonInfo> personinfo) {
        this.personinfo = personinfo;
    }
}
