package com.zebone.nhis.webservice.zhongshan.cxf;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @author wq
 * @Classname IZsbaTaiKangService
 * @Description 中山博爱泰康人寿对外接口
 * @Date 2020-11-24 9:37
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IZsbaTaiKangService {

    /**
     * @return java.lang.String
     * @Description 对接总接口
     * @auther wuqiang
     * @Date 2020-11-24
     * @Param [inxml 查询入参, tradetype：交易类型]
     */
    @WebMethod
    public String GetTradeInfo(String inxml, String tradetype);
}
