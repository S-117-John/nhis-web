package com.zebone.nhis.webservice.vo.bapivas;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.common.support.DateUtils;

/**
 * 灵璧静配接口公共响应参数
 * @param <T>
 */
public class BaPivasCommonRes<T> {

    public BaPivasCommonRes() {
    }

    public BaPivasCommonRes(String resCode, String resMsg, String reqId, T data) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.sysdate = DateUtils.getDate("yyyy-MM-dd HH:mm:ss") ;
        this.reqId = reqId;
        this.data = data;
    }

    @JSONField(name = "res_code")
    private String resCode;

    @JSONField(name = "res_msg")
    private String resMsg;

    @JSONField(name = "sysdate")
    private String sysdate;

    @JSONField(name = "req_id")
    private String reqId;

    @JSONField(name = "data")
    private T data;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
