package com.zebone.nhis.webservice.syx.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * 孙逸仙门诊华润包药机web服务对外处理
 * @author jd
 *
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface IScmHrWebService {
	
	/**
	 * 华润外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String scmHrInterfaceXml(@WebParam(name="param")String param);
	
	/**
	 * 平台外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String scmPtInterfaceXml(@WebParam(name="param")String param);
}
