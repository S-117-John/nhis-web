package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.BdExtension;

import java.util.List;

public class Code {

    private List<Coding> coding;

    private String text;

    public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Code() {
    }

    public Code(List<Coding> coding) {
        this.coding = coding;
    }

    public Code(List<Coding> coding, String text) {
        this.coding = coding;
        this.text = text;
    }
}
