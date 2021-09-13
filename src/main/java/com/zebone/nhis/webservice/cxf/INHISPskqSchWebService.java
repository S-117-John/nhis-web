package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
/**
 * @Classname INHISPskqSchWebService
 * @Description  坪山口腔项目-对外排班接口 xml格式
 * @Date 2020/11/04 10:00
 * @Created by zhangtao
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISPskqSchWebService {
	/**
	 * 外部接口xml调用处理
	 * 查询医院相关信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String  QueryHospitalInfo(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 查询全部科室以及查询单个科室详细信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryDeptInfo(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 查询医生信息
	 * @param param
	 * @return
	 */
	 @WebMethod
	 public String QueryDoctorInfos(@WebParam(name = "param") String param);
	/**
	 * 外部接口xml调用处理
	 * 查询排班科室信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySchDept(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 查询排班医生信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySchDoctor(@WebParam(name="param")String param);
	/**
	 * 外部接口xml调用处理
	 * 查询科室下医生排班号源信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySch(@WebParam(name="param")String param);
	
	/**
	 * 外部接口xml调用处理
	 * 查询医生照片
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryDoctorPhoto(@WebParam(name="param")String param);
	/**
	 * 查询每个医生患者预约情况
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryAppointRegList(@WebParam(name="param")String param);

	@WebMethod
	String QueryAllDept(@WebParam(name="param")String param);

	@WebMethod
	String QueryAllDoctor(@WebParam(name="param")String param);
}
