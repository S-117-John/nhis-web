package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;

import java.util.List;

public class BdPdCoding {
    private Coding coding;

    public BdPdCoding() {
    }

    public BdPdCoding(Coding coding) {
        this.coding = coding;
    }

    public Coding getCoding() {
        return coding;
    }

    public void setCoding(Coding coding) {
        this.coding = coding;
    }
}
