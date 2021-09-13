package com.zebone.nhis.webservice.syx.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface IEmrSyxWebService {
	

	/**
	 * Xml 格式对外接口声明
	 * @param param 参数内容
	 * @return
	 */
	@WebMethod
	public String EmrInterfaceXml(@WebParam(name="param")String param);
	
	/**
	 * 血透接口-获取首次病程记录信息
	 * @param xml
	 * @return xml
	 */
	@WebMethod
	public String Hm_GetFisrtCourseInfo(@WebParam(name="param")String param);
	
	/**
	 * 血透接口-获取病程记录
	 * @param Mrn
	 * @param ReordTime
	 * @return
	 */
	@WebMethod
	public String Hm_EmrCourse(@WebParam(name="param")String param);
	
	/**
	 * 房颤接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String EmrInterfaceAF(@WebParam(name="param")String param);
}
