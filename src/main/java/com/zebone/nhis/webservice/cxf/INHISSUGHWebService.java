package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface INHISSUGHWebService {

	/**
	 * Iron门诊包药机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String getDispensingStatus(@WebParam(name="param")String param);
}
