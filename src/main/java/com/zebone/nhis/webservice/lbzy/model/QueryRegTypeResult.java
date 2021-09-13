package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 卡卡西
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryRegTypeResult {

    @XmlElement(name = "ResultCode")
    private String resultCode;

    @XmlElement(name = "ErrorMsg")
    private String errorMsg;

    @XmlElement(name = "List")
    private QueryRegTypeList list;

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

    public QueryRegTypeList getList() {
        return list;
    }

    public void setList(QueryRegTypeList list) {
        this.list = list;
    }
}
