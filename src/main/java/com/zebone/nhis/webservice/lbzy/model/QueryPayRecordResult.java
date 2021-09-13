package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryPayRecordResult {

    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "List")
    private QueryPayRecordList list;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public QueryPayRecordList getList() {
        return list;
    }

    public void setList(QueryPayRecordList list) {
        this.list = list;
    }
}
