package com.zebone.nhis.webservice.vo.bapivas;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 灵璧静配公共请求参数
 * @param <T>
 */
public class BaPivasCommonReq<T> {
    @JsonProperty( "req_id")
    private String reqId;

    @JsonProperty("req_date")
    private String reqDate;

    @JsonProperty("data")
    private T data;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
