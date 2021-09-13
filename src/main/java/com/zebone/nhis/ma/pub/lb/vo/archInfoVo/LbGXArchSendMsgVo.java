package com.zebone.nhis.ma.pub.lb.vo.archInfoVo;

public class LbGXArchSendMsgVo {
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 调用 UserId
     */
    private String userId;
    /**
     * 随机值
     */
    private String nonce;
    /**
     *业务数据
     */
    private String data;
    /**
     *安全效验方式
     */
    private String siginMethod;
    /**
     *签名信息
     */
    private String signData;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSiginMethod() {
        return siginMethod;
    }

    public void setSiginMethod(String siginMethod) {
        this.siginMethod = siginMethod;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
