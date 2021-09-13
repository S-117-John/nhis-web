package com.zebone.nhis.webservice.client.zhongshan.pacs;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.13
 * 2017-05-18T15:17:19.443+08:00
 * Generated source version: 3.0.13
 * 
 */
@WebService(targetNamespace = "http://www.gdpacs.com/", name = "MathServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface MathServiceSoap {

    @WebResult(name = "PacsOrigFileListResult", targetNamespace = "http://www.gdpacs.com/")
    @RequestWrapper(localName = "PacsOrigFileList", targetNamespace = "http://www.gdpacs.com/", className = "com.zebone.nhis.webservice.client.pacs.PacsOrigFileList")
    @WebMethod(operationName = "PacsOrigFileList", action = "http://www.gdpacs.com/PacsOrigFileList")
    @ResponseWrapper(localName = "PacsOrigFileListResponse", targetNamespace = "http://www.gdpacs.com/", className = "com.zebone.nhis.webservice.client.pacs.PacsOrigFileListResponse")
    public java.lang.String pacsOrigFileList(
        @WebParam(name = "patient_id", targetNamespace = "http://www.gdpacs.com/")
        java.lang.String patientId,
        @WebParam(name = "times", targetNamespace = "http://www.gdpacs.com/")
        java.lang.String times,
        @WebParam(name = "visit_flag", targetNamespace = "http://www.gdpacs.com/")
        java.lang.String visitFlag
    );
}
