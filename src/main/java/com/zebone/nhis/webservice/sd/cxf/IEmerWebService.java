package com.zebone.nhis.webservice.sd.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface IEmerWebService {

	@WebMethod
	public String saveOpOrderInfo(@WebParam(name = "param") String param);


}
