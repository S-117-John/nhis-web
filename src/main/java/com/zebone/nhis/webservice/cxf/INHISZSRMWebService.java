package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface INHISZSRMWebService {

	/**
	 * 接口集成声明
	 * @param Input_info{Json格式对外接口，其中json字符串中必须包含func_id（功能号）}
	 * @return
	 */
	@WebMethod
	public String NHISInterface(@WebParam(name="Input_info")String Input_info);
	/**
	 * String 格式对外接口声明
	 * @param func_id 功能号（必传参数）
	 * @param pk_org 机构ID
	 * @param content 参数内容
	 * @return
	 */
	@WebMethod
	public String NHISInterfaceStr(@WebParam(name="func_id")String func_id,@WebParam(name="pk_org")String pk_org,
			@WebParam(name="content")String content);
}
