package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiTokenResultVo {
    //0：成功
    //ERR0003：token 过期
    //ERR0004：APPID 信息不存在
    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
