package com.zebone.nhis.ma.pub.zsrm.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZsrmHrpResult<T> implements Serializable {
    private String errorCode;
    private String errorMsg;
    private final static String CODE = "1";
    private final static String SUCCESS = "success";
    private T Data;


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public ZsrmHrpResult(T data) {
        Data = data;
    }

    public ZsrmHrpResult(String errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        Data = data;
    }

    @Override
    public String toString() {
        return "ZsrmHrpResult{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", Data=" + Data +
                '}';
    }

    public static <Type> ZsrmHrpResult<Type> resultMessage(Type data){
        return new ZsrmHrpResult<Type>(data);
    }

    public static <Type> ZsrmHrpResult<Type> successMessage(Type data){
        return new ZsrmHrpResult<Type>(CODE, SUCCESS, data);
    }
}
