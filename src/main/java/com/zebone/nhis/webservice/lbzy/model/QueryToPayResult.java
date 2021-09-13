package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPayResult {

    @XmlElement(name = "ResultCode")
    public String resultCode;

    @XmlElement(name = "ErrorMsg")
    public String errorMsg;

    @XmlElement(name = "List")
    public QueryToPayList list;
}
