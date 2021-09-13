package com.zebone.nhis.ma.pub.platform.zsba.vo;

public class VitalSignsInfoDto {
    private VitalSignsData data;
    private String usercode;

    public VitalSignsData getData() {
        return data;
    }

    public void setData(VitalSignsData data) {
        this.data = data;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }


}