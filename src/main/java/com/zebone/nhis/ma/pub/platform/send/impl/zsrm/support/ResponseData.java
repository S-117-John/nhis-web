package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

public class ResponseData {
    private boolean success;
    private String data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
