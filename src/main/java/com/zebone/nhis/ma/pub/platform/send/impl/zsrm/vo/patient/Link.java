package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;

public class Link {

    private Other other;
    private List<TextElement> name;
    private List<Identifier> telecom;

    public Other getOther() {
        return other;
    }

    public void setOther(Other other) {
        this.other = other;
    }

    public List<TextElement> getName() {
        return name;
    }

    public void setName(List<TextElement> name) {
        this.name = name;
    }

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }
}
