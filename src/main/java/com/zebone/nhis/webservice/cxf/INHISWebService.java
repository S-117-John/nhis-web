package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.zebone.nhis.webservice.vo.hospinfovo.ResponseHospInfoVo;

/**
 * NHIS产品对外公共webservice接口
 * @author yangxue
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISWebService {
	 /**
	  * @Description 查询当前机构所有在院患者
	  * @auther tongjiaqi
	  * @Date 2020/3/30 
	  * @Param [param]
	  * @return java.lang.String
	  */
	 @WebMethod
	 public String queryPiListInHosps(@WebParam(name = "param") String param);
	 /**
	  * @Description 查询患者住院就诊信息/查询患者住院记录
	  * @auther tongjiaqi
	  * @Date 2020/3/30 
	  * @Param [param]
	  * @return java.lang.String
	  */
	@WebMethod
	public String getPvInfoByIp(@WebParam(name = "param") String param);
	/**
     * @Description 查询医嘱信息
     * @auther tongjiaqi
     * @Date 2020/3/30 
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String queryOrdlist(@WebParam(name="param") String param );
    /**
     * @Description 查询通用执行单信息
     * @auther tongjiaqi
     * @Date 2020/3/30 
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String queryExlist(@WebParam(name="param") String param );
     
	 /**
     * @Description 通用执行
     * @auther tongjiaqi
     * @Date 2020/3/30 
     * @Param [param]
     * @return java.lang.String
     */
    @WebMethod
    public String  confirmExlist(@WebParam(name="param") String param );
    /**
     * @Description  执行确认血糖医嘱
     * @auther tongjiaqi
     * @Date 2020/3/30 
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String confirmBsEx(@WebParam(name="param") String param );
    /**
     * @Description 执行确认试敏医嘱
     * @auther tongjiaqi
     * @Date 2020/3/30 
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String confirmStEx(@WebParam(name="param") String param );

	/**
	 * 住院费用录入接口
	 * @auther
     * @Date 2020/6/2
	 * @param param
	 * @returnjava.lang.String
	 */
	@WebMethod
	public String savePatiCgInfoByThirdParty(@WebParam(name = "param") String param);
}