package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

/**
 * 获取Token参数响应Vo
 */
public class CiTokenResVo {
    private CiTokenDataVo data;

    private CiTokenResultVo result;

    public CiTokenDataVo getData() {
        return data;
    }

    public void setData(CiTokenDataVo data) {
        this.data = data;
    }

    public CiTokenResultVo getResult() {
        return result;
    }

    public void setResult(CiTokenResultVo result) {
        this.result = result;
    }
}
