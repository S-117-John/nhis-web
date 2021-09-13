package com.zebone.nhis.ma.pub.zsrm.vo;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 高值耗材公共返回类
 * @param <T>
 */
public class HightCommonResponse<T> {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
