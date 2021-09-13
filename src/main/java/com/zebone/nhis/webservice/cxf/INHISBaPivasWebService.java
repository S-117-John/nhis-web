package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISBaPivasWebService {
    /**
     * 静配发药接口
     * @param param
     * @return
     */
    @WebMethod
    public String sendPivasDrug(@WebParam(name = "param") String param);

    /**
     * 静配退药接口
     * @param param
     * @return
     */
    @WebMethod
    public String sendRetPivasDrug(@WebParam(name = "param") String param);

    /**
     * 静配附加费用补录
     * @param param
     * @return
     */
    @WebMethod
    public String sendPivasAddCgdt(@WebParam(name = "param") String param);

    /**
     * 静配配置状态修改
     * @param param
     * @return
     */
    @WebMethod
    public String updatePivasStatus(@WebParam(name="param") String param);
}
