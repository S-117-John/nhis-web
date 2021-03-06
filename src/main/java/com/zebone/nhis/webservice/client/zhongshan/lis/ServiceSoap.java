package com.zebone.nhis.webservice.client.zhongshan.lis;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.13
 * 2017-05-18T15:17:36.110+08:00
 * Generated source version: 3.0.13
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "ServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface ServiceSoap {

    @WebResult(name = "HelloWorldResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "HelloWorld", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.HelloWorld")
    @WebMethod(operationName = "HelloWorld", action = "http://tempuri.org/HelloWorld")
    @ResponseWrapper(localName = "HelloWorldResponse", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.HelloWorldResponse")
    public java.lang.String helloWorld();

    @WebResult(name = "LisReportResResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "LisReportRes", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisReportRes")
    @WebMethod(operationName = "LisReportRes", action = "http://tempuri.org/LisReportRes")
    @ResponseWrapper(localName = "LisReportResResponse", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisReportResResponse")
    public java.lang.String lisReportRes(
        @WebParam(name = "str_pat_id", targetNamespace = "http://tempuri.org/")
        java.lang.String strPatId
    );

    @WebResult(name = "LisOrigFileListResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "LisOrigFileList", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisOrigFileList")
    @WebMethod(operationName = "LisOrigFileList", action = "http://tempuri.org/LisOrigFileList")
    @ResponseWrapper(localName = "LisOrigFileListResponse", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisOrigFileListResponse")
    public java.lang.String lisOrigFileList(
        @WebParam(name = "str_patient", targetNamespace = "http://tempuri.org/")
        java.lang.String strPatient,
        @WebParam(name = "str_times", targetNamespace = "http://tempuri.org/")
        java.lang.String strTimes,
        @WebParam(name = "str_visit_flag", targetNamespace = "http://tempuri.org/")
        java.lang.String strVisitFlag
    );

    @WebResult(name = "LisReportListResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "LisReportList", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisReportList")
    @WebMethod(operationName = "LisReportList", action = "http://tempuri.org/LisReportList")
    @ResponseWrapper(localName = "LisReportListResponse", targetNamespace = "http://tempuri.org/", className = "com.zebone.nhis.webservice.client.lis.LisReportListResponse")
    public java.lang.String lisReportList(
        @WebParam(name = "dt_sdate", targetNamespace = "http://tempuri.org/")
        javax.xml.datatype.XMLGregorianCalendar dtSdate,
        @WebParam(name = "dt_edate", targetNamespace = "http://tempuri.org/")
        javax.xml.datatype.XMLGregorianCalendar dtEdate
    );
}
