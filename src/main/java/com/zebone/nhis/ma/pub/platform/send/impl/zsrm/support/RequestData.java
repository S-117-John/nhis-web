package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;

public class RequestData {

    /**消息ID*/
    private String id;
    /**消息内容*/
    private String data;
    /**远程方法*/
    private String remoteMethod;
    /**URL类型，默认走的BUSINESS*/
    private EnumUrlType urlType;

    /**直连访问URL，EnumUrlType=DIRECT时才启用*/
    private String directUrl;

    private MsgIndexData msgIndexData;

    private RequestData(Builder builder) {
        setId(builder.id);
        setData(builder.data);
        setRemoteMethod(builder.remoteMethod);
        setUrlType(builder.urlType);
        setDirectUrl(builder.directUrl);
        setMsgIndexData(builder.msgIndexData);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRemoteMethod() {
        return remoteMethod;
    }

    public void setRemoteMethod(String remoteMethod) {
        this.remoteMethod = remoteMethod;
    }

    public EnumUrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(EnumUrlType urlType) {
        this.urlType = urlType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public MsgIndexData getMsgIndexData() {
        return msgIndexData;
    }

    public void setMsgIndexData(MsgIndexData msgIndexData) {
        this.msgIndexData = msgIndexData;
    }

    public static final class Builder {
        private String id;
        private String data;
        private String remoteMethod;
        private EnumUrlType urlType;
        private String directUrl;
        private MsgIndexData msgIndexData;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public Builder remoteMethod(String val) {
            remoteMethod = val;
            return this;
        }
        public Builder urlType(EnumUrlType val) {
            urlType = val;
            return this;
        }

        public Builder directUrl(String val) {
            directUrl = val;
            return this;
        }

        public Builder msgIndexData(MsgIndexData val) {
            msgIndexData = val;
            return this;
        }
        public RequestData build() {
            return new RequestData(this);
        }
    }
}
