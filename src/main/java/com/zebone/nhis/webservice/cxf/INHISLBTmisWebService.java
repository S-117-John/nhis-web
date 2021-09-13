package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * @Classname INHISLBTmisWebService
 * @Description  灵璧项目-输血接口 xml格式
 * @Date 2019/8/2 10:05
 * @Created by wuqiang
 */
@WebService(targetNamespace = "http://tempuri.org/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface INHISLBTmisWebService {
	/**
	 * 测试
	 * @param param
	 * @return
	 */
	public String test(@WebParam(name = "param") String param);
	/**
	 * 查询科室列表信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryDepts(@WebParam(name = "param") String param);
	/**
	 * 查询病区列表信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryAreas(@WebParam(name = "param") String param);
	/**
	 * 查询职工列表信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryEmployees(@WebParam(name = "param") String param);
	/**
	 * 查询收费项目信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryBdItem(@WebParam(name = "param") String param);
	/**
	 * 查询患者信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryPiMaster(@WebParam(name = "param") String param);
	/**
	 * 查询患者血型分布
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryMasterBlood(@WebParam(name = "param") String param);
	/**
	 * 异体输血-医嘱回传(新增) 
	 * @param param
	 * @return
	 */
	@WebMethod
	public String saveBlood(@WebParam(name = "param") String param) ;
	
	/**
	 * 异体输血-医嘱回传(作废) 
	 * @param param
	 * @return
	 */
	@WebMethod
	public String delOrder(@WebParam(name = "param") String param) ;
	/**
	 * 输血计费
	 * @param param
	 * @return
	 */
	public String chargingBlood(@WebParam(name = "param") String param);
	/**
	 * 获取输血费用列表（）
	 * @param param
	 * @return
	 */
	public String queryBloodCost(@WebParam(name = "param") String param);

}
