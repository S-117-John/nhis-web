package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;


public class BlClas {
    public BlClas(){}
    public BlClas(String code, String display) {
        this.code = code;
        this.display = display;
    }
    private String code;
    private String display;


    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}