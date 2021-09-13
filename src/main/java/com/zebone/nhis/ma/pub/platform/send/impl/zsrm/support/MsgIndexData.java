package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

/**
 * SYS_MSG_REC 的索引数据
 */
public class MsgIndexData {

    private String codePi;

    private String codeOp;

    private String codeIp;

    private String codePv;

    private String euEme;


    private String codeOther;

    private MsgIndexData(Builder builder) {
        setCodePi(builder.codePi);
        setCodeOp(builder.codeOp);
        setCodeIp(builder.codeIp);
        setCodePv(builder.codePv);
        setCodeOther(builder.codeOther);
        setEuEme(builder.euEme);

    }

    public String getEuEme() {
        return euEme;
    }

    public void setEuEme(String euEme) {
        this.euEme = euEme;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodeOther() {
        return codeOther;
    }

    public void setCodeOther(String codeOther) {
        this.codeOther = codeOther;
    }

    public static final class Builder {
        private String codePi;
        private String codeOp;
        private String codeIp;
        private String codePv;
        private String codeOther;

        private String euEme;


        private Builder() {
        }

        public Builder codePi(String val) {
            codePi = val;
            return this;
        }

        public Builder codeOp(String val) {
            codeOp = val;
            return this;
        }

        public Builder codeIp(String val) {
            codeIp = val;
            return this;
        }

        public Builder codePv(String val) {
            codePv = val;
            return this;
        }

        public Builder codeOther(String val) {
            codeOther = val;
            return this;
        }

        public Builder euEme(String val) {
            euEme = val;
            return this;
        }

        public MsgIndexData build() {
            return new MsgIndexData(this);
        }
    }
}
