package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface INHISGZWebService {
	
	/**
	 * 康美  患者地址注册接口
	 * @param strBase64  Base64加密的字符串
	 * @return
	 */
	@WebMethod
	public String acceptUserAddrInfo(@WebParam(name="strBase64")String strBase64);
	
	/**
	 * 接口集成声明
	 * @param Input_info{Json格式对外接口，其中json字符串中必须包含func_id（功能号）}
	 * @return
	 */
	@WebMethod
	public String NHISInterface(@WebParam(name="Input_info")String Input_info);
	
	@WebMethod
	public String NHISInterfaceStr(@WebParam(name="func_id")String func_id,@WebParam(name="pk_org")String pk_org,
			@WebParam(name="content")String content);
	
	@WebMethod
	public String NHISInterfaceExt(@WebParam(name="request")String request);
}
