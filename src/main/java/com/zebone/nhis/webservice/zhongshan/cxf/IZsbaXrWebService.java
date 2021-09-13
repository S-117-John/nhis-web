package com.zebone.nhis.webservice.zhongshan.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author 12370
 * @Classname IZsbaXrNATService
 * @Description 提供三方接口
 * @Date 2021-03-04 17:26
 * @Created by wuqiang
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IZsbaXrWebService {
   /**
    * @Description 博爱对外接口
    * @auther wuqiang
    * @Date 2021-03-04
    * @Param [inf 入参, function接口类型]
    * @return java.lang.String
    */
    @WebMethod
    public String InterfaceWS(String inf,String function);


}
