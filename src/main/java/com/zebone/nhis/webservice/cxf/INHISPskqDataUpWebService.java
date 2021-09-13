package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
/**
 * @Classname INHISPskqSchWebService
 * @Description  坪山口腔项目-数据上报接口
 * @Created by ds
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISPskqDataUpWebService {
	/**
	 * 外部接口xml调用处理
	 * 获取门诊实时情况
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Realtime_MZ(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 获取就诊情况
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Recipe_MZ(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 获取门诊费用月报
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Income_MZ(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 病区信息查询接口
	 * @param param
	 * @return
	 */
	@WebMethod
	String getWardInfo(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 获取住院日报表
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Overview_ZY(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 获取住院费用月报
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Income_ZY(@WebParam(name="param")String param);
	
	/**
	 * 获取全院费用月报
	 * @param param
	 * @return
	 */
	@WebMethod
	String getHospitalReport_Income_ALL(@WebParam(name="param")String param);
}
