package com.zebone.nhis.webservice.zsrm.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 中山人民医院提供自助机的w
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IZsrmSelfAppService {

    /**
     * 查询待缴费费用明细
     * @param param
     * @return
     */
    @WebMethod
    public String queryPatiCgInfoNotSettle(@WebParam(name="param")String param);

    /**
     * 自助机结算接口
     * @param param
     * @return
     */
    @WebMethod
    public String accountedSettlement(@WebParam(name="param")String param);

    /**
     * 医保预结算
     * @param param
     * @return
     */
    @WebMethod
    public String accountedPreSettlement(@WebParam(name="param")String param);
    
    /**
     * 预约核酸检测
     * @param param
     * @return
     */
    public String Covid19(@WebParam(name="param")String param);
    
    /**
     * 获取门诊费用主表服务
     * @param param
     * @return
     */
    public String queryOutpfeeMasterInfo(@WebParam(name="param")String param);
    
    
    /**
     * 获取门诊费用明细服务
     * @param param
     * @return
     */
    public String queryOutpfeeDetailInfo(@WebParam(name="param")String param);
}
