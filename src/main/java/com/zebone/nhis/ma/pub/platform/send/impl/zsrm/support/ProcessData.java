package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

public class ProcessData {

    private RequestData requestData;

    private ResponseData responseData;

    public ProcessData(RequestData requestData, ResponseData responseData) {
        this.requestData = requestData;
        this.responseData = responseData;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }
}
