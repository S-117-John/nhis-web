package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class ResponseBusiness {

    private ResponseHeader header;

    private ResponseBody body;

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }

    public class ResponseHeader {

        private String sourceSystem;
        private String messageID;

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
    }

    public class ResponseBody {
        private String resultCode;
        private String resultContent;

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultContent() {
            return resultContent;
        }

        public void setResultContent(String resultContent) {
            this.resultContent = resultContent;
        }
    }

}
