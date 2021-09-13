package com.zebone.nhis.common.module.base.transcode;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
    * 记录拦截相同请求
    */
@Table(value="SYS_SAME_URL")
public class SysSameUrl {
    /**
    * 主键
    */
    @PK
    @Field(value="PK_SAME_URL",id= Field.KeyId.UUID)
    private String pkSameUrl;

    /**
    * 接口类型
    */
    @Field(value="API_TYPE")
    private String apiType;

    /**
    * 接口名称
    */
    @Field(value="API_NAME")
    private String apiName;

    /**
    * 操作ip
    */
    @Field(value="REMOTE_ADDR")
    private String remoteAddr;

    /**
    * 客户端
    */
    @Field(value="USER_AGENT")
    private String userAgent;

    /**
    * 请求URI
    */
    @Field(value="REQUEST_URI")
    private String requestUri;

    /**
    * 请求方式
    */
    @Field(value="METHOD")
    private String method;

    /**
    * 参数
    */
    @Field(value="PARAMS")
    private String params;


    /**
    * 创建时间
    */
    @Field(value="CREATE_TIME")
    private Date createTime;

    public String getPkSameUrl() {
        return pkSameUrl;
    }

    public void setPkSameUrl(String pkSameUrl) {
        this.pkSameUrl = pkSameUrl;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}