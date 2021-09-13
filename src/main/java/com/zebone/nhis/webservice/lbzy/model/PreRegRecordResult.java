package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 预约挂号查询结果
 * @author 卡卡西
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreRegRecordResult {


    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "List")
    private PreRegRecordList regRecordList;

    public static class Builder{
        private String resultCode;
        private String errorMsg;
        private PreRegRecordList regRecordList;

        public Builder() {
        }

        public Builder resultCode(String val){
            this.resultCode = val;
            return this;
        }

        public Builder errorMsg(String val){
            this.errorMsg = val;
            return this;
        }

        public Builder list(PreRegRecordList val){
            this.regRecordList = val;
            return this;
        }

        public PreRegRecordResult build(){
            return new PreRegRecordResult(this);
        }

    }
    private PreRegRecordResult(Builder builder){
        this.resultCode = builder.resultCode;
        this.errorMsg = builder.errorMsg;
        this.regRecordList = builder.regRecordList;
    }

    public PreRegRecordResult() {
    }
}
