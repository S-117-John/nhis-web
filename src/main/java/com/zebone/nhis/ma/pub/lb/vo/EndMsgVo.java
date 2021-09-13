package com.zebone.nhis.ma.pub.lb.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class EndMsgVo {
    private String appKey;
    /**
     * 经办机构编码
     */
    private String orgId;
    /**
     * 加密类型
     */
    private String encType;
    /**
     * 加密业务数据
     */
    @JSONField(name = "data")
    private String secretData;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getEncType() {
        return encType;
    }

    public void setEncType(String encType) {
        this.encType = encType;
    }

    public String getSecretData() {
        return secretData;
    }

    public void setSecretData(String secretData) {
        this.secretData = secretData;
    }
}
