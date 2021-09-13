package com.zebone.nhis.ma.pub.lb.vo;



public class VerifyVo {
    /**
     * 返回码
     * 0 表示成功，数据正常返回，数据存于 data 中。
     * 非 0 表示失败，msg 中存入失 败原因。
     */
    private String code;
    /**
     * 返回错误信息
     * code 非 0 时有效
     */
    private String msg;
    /**
     * 响应参数
     * 单条数据
     */

    private RespData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RespData getData() {
        return data;
    }

    public void setData(RespData data) {
        this.data = data;
    }
}
