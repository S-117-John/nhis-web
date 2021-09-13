package com.zebone.nhis.ma.pub.zsba.vo.outflow;

public class CommonResponse {

    //one is bool one is string ,w,t,f?
    private Object result;
    private String msg;

    public CommonResponse() {
    }

    public CommonResponse(Object result) {
        this.result = result;
    }

    public CommonResponse(Object result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
