package com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo;

public class TriageReqBaseVo<T> {
    public TriageReqBaseVo(T param){
        this.param = param;
    }

   private T param;

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
