package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;

public class Timing {
    private List event;
    private TextElement type;
    private Numerator doseQuantity;

    public Timing() {
    }

    public Timing(List event) {
        this.event = event;
    }

    public Timing(TextElement type, Numerator doseQuantity) {
        this.type = type;
        this.doseQuantity = doseQuantity;
    }

    public List getEvent() {
        return event;
    }

    public void setEvent(List event) {
        this.event = event;
    }

    public TextElement getType() {
        return type;
    }

    public void setType(TextElement type) {
        this.type = type;
    }

    public Numerator getDoseQuantity() {
        return doseQuantity;
    }

    public void setDoseQuantity(Numerator doseQuantity) {
        this.doseQuantity = doseQuantity;
    }
}
