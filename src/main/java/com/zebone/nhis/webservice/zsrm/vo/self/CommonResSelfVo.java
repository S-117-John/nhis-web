package com.zebone.nhis.webservice.zsrm.vo.self;

/**
 * 公共响应参数构造
 * @param <T>
 */
public class CommonResSelfVo <T>{
    public CommonResSelfVo()
    {
    }

    public CommonResSelfVo(String resCode, String resMsg, String reqId, String sysdate, T data) {
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.reqId = reqId;
        this.sysdate = sysdate;
        this.data = data;
    }

    /**
     * 返回状态
     */
    private String resCode;

    /**
     * 返回信息内容
     */
    private String resMsg;

    /**
     * 请求Id
     */
    private String reqId;

    /**
     * 当前时间
     */
    private String sysdate;

    /**
     * 返回数据
     */
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

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
