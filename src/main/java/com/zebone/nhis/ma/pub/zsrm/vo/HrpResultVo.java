package com.zebone.nhis.ma.pub.zsrm.vo;

/**
 * HRP 公共结果返回参数
 * @param <T>
 */
public class HrpResultVo<T> {

    public HrpResultVo() {}

    public HrpResultVo(String errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    private String errorCode;

    private String errorMsg;

    private T data;

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
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
