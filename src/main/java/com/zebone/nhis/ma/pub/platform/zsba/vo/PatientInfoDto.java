package com.zebone.nhis.ma.pub.platform.zsba.vo;


public class PatientInfoDto {
    // 状态码
    private int status;
    // 错误信息
    private String error;
    // 返回信息
    private String msg;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}