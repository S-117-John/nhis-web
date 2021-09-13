package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

/**
 * packageVo
 */
public class CiPackageVo {

    /**
     * head
     */
    private CiHeadVo head;

    private List<Object> body;

    private CiAdditionInfo additionInfo;

    /**
     * 响应原始json串
     */
    private String resJson;

    public String getResJson() {
        return resJson;
    }

    public void setResJson(String resJson) {
        this.resJson = resJson;
    }

    public CiHeadVo getHead() {
        return head;
    }

    public void setHead(CiHeadVo head) {
        this.head = head;
    }

    public List<Object> getBody() {
        return body;
    }

    public void setBody(List<Object> body) {
        this.body = body;
    }

    public CiAdditionInfo getAdditionInfo() {
        return additionInfo;
    }

    public void setAdditionInfo(CiAdditionInfo additionInfo) {
        this.additionInfo = additionInfo;
    }
}
