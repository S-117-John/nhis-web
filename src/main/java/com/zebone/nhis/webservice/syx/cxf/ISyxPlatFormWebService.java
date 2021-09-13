package com.zebone.nhis.webservice.syx.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ISyxPlatFormWebService {
    /**
     * Xml 格式对外接口声明
     * @param content 参数内容
     * @return
     */
    @WebMethod
    public String SyxPlatFormNhisInterface(@WebParam(name="content")String content);
    
    /**
     * Xml 格式对外接口声明
     * @param content 参数内容
     * @return
     */
    @WebMethod
    public String PrivateHIPMessageServer(@WebParam(name="action")String action,@WebParam(name="message")String message);
}
