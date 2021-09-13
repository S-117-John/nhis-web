package com.zebone.nhis.ma.pub.zsba.vo.outflow;


public class OutResponse<T> {
    public OutResponse() {
    }
    public OutResponse(Integer code) {
        this.code = code;
    }

    /**
     * 状态码 200为ok
     */
    private Integer code;

    private T body;

    //anyone named the result name is fk body??data? w,t,f  o(╯□╰)o
    private T data;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSus(){
        return this.code!=null && 200 == this.code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
