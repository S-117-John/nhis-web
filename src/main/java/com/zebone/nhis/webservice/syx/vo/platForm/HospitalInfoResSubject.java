package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class HospitalInfoResSubject {

    @XmlElement(name = "res")
    private HospitalInfo res;

    public HospitalInfo getRes() {
        return res;
    }

    public void setRes(HospitalInfo res) {
        this.res = res;
    }
}
