package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.ValueQuantity;

public class TextElement {
    public TextElement(){}
    public TextElement(String text) {
        this.text = text;
    }

    private String text;

    private ValueQuantity low;

    public ValueQuantity getLow() {
        return low;
    }

    public void setLow(ValueQuantity low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
