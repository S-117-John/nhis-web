package com.zebone.nhis.ma.pub.zsba.vo.outflow;

public class SignCheckReq {

    private String publicKey;

    private String appId;
    /**
     * 请求的接口路径
     */
    private String path;

    /**
     * 请求参数体
     */
    private String body;


    /**
     * 是否为文件上传
     */
    private boolean isMultipart;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isMultipart() {
        return isMultipart;
    }

    public void setMultipart(boolean multipart) {
        isMultipart = multipart;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}