package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 深圳坪山口腔医院-对外公共webservice接口
 * @author zhangtao
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISPSKQWebService {

	/**
	 * 统一消息接口地址
	 *
	 * @param param
	 * @return
	 */
	@WebMethod
	String messageSynchronize(@WebParam(name="param") String param);


	@WebMethod
	String test(@WebParam(name="param") String param);









}