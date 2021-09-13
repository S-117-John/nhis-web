package com.zebone.nhis.webservice.lbzy.model;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)

public class QueryRegRecordResult {

    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "List")
    private QueryRegRecordList list;

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

    public QueryRegRecordList getList() {
        return list;
    }

    public void setList(QueryRegRecordList list) {
        this.list = list;
    }
}
