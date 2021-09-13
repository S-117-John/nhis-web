package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class BusinessOther {

    private Request request;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public class Request<T>{
        private Header header;

        private T body;

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }

    }

    public class Header {
        private String sourceSystem;
        private String messageID;
        @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
        private Date createTime;
        private String methodName;
        private String implicitRules;

        public String getSourceSystem() {
            return sourceSystem;
        }

        public void setSourceSystem(String sourceSystem) {
            this.sourceSystem = sourceSystem;
        }

        public String getMessageID() {
            return messageID;
        }

        public void setMessageID(String messageID) {
            this.messageID = messageID;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getImplicitRules() {
            return implicitRules;
        }

        public void setImplicitRules(String implicitRules) {
            this.implicitRules = implicitRules;
        }
    }
}
