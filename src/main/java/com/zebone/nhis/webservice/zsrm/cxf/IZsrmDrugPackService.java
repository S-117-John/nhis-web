package com.zebone.nhis.webservice.zsrm.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 中山人医门诊摆药机接口封装
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IZsrmDrugPackService {

    /**
     * 摆药机--获取处方信息，命名、入参必须和对方提供的一致（必须大写）
     * @param param
     * @return
     */
    @WebMethod
    String GetAllPurPoseData(@WebParam(name="Xml") String param);

    /**
     * 获取草药处方信息999
     * @param param
     * @return
     */
    public String getHerbPresInfo(@WebParam(name="param")String param);

    /**
     * 获取草药处方明细信息 999
     * @param param
     * @return
     */
    public String getHerbPresDtInfo(@WebParam(name="param")String param);
}
