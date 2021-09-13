package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
/**
 * @Classname INHISLBselfWebService
 * @Description  灵璧项目-对外自助机接口 xml格式
 * @Date 2019/8/2 10:05
 * @Created by wuqiang
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISLBselfWebService {
	/**
	 * 灵璧自助机
	 * 网络测试
	 * @param param
	 * @return
	 */
	 @WebMethod
	 public String NetTest(@WebParam(name = "param") String param);
	/**
	 * 查询医生信息
	 * @param param
	 * @return
	 */
	 @WebMethod
	 public String QueryDoctorInfos(@WebParam(name = "param") String param);
	@WebMethod
	String queryAllDoctorInfos(@WebParam(name = "param") String param);
	/**
	 *查询患者信息
	 * @param param
	 * @return
	  */
	 @WebMethod
	 public String QueryPiInfo(@WebParam(name = "param") String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询医院相关信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String  QueryHospitalInfo(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询全部科室以及查询单个科室详细信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryDeptInfo(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 挂号锁号接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String  LockReg(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 提交挂号接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String Register(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 挂号解锁接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String UnLockReg(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 住院预交金充值
	 * @param param
	 * @return
	 */
	@WebMethod
	public String RechargeInpatientDeposit(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询预交金充值记录
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryRechargeRecords(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询排班科室信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySchDept(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询排班医生信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySchDoctor(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询科室下医生排班号源信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QuerySch(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询就诊卡发卡缴纳费用信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryCardIss(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 患者建档/患者信息修改接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String CreatPi(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 患者发卡/补卡接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String CardIssue(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询患者门诊待缴费记录接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryUnpaidRecordList(@WebParam(name="param")String param);
	/**
	 * 查询门诊患者就诊信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryInpatientOpInfo(@WebParam(name="param")String param);
	/**
	 * 查询住院患者就诊信息
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryInpatientIpInfo(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 查询预约号源记录
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryRegisteredRecords(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 预约挂号
	 * @param param
	 * @return
	 */
	@WebMethod
	public String AppointmentRegister(@WebParam(name="param")String param);
	/**
	 * 灵璧自助机外部接口xml调用处理
	 * 预约登记
	 * @param param
	 * @return
	 */
	@WebMethod
	public String AppointmentRegistration(@WebParam(name="param")String param);
	/**
	 * 查询住院费用清单
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryInpatientBillList(@WebParam(name="param")String param);
	
	/**
	 * 查询已支付的门诊缴费详情
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryPaymentRecordList(@WebParam(name="param")String param);
	
	/**
	 * 门诊费用缴纳
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String Payment(@WebParam(name="param")String param);
	
	/**
	 * 查询门诊就诊记录
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryClinicRecordList(@WebParam(name="param")String param);
	
	/**
	 * 账户充值
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String AccountRecharge(@WebParam(name="param")String param);
	/**
	 * 门诊账户余额查询
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String QueryAccount(@WebParam(name="param")String param);
	/**
	 * 退款校验
	 * 灵璧自助机外部接口xml调用处理
	 * @param param
	 * @return
	 */
	@WebMethod
	public String RefundCheck(@WebParam(name="param")String param);
	
	/**
	 * 2.1查询科室列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryDeptInfos(@WebParam(name="param")String param);
	/**
	 *2.2查询排班列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String querySchedules(@WebParam(name="param")String param);
	/**
	 *2.3查询医生可预约时段列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryAppointTimes(@WebParam(name="param")String param);
	/**
	 *2.4预约接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String bookOrder(@WebParam(name="param")String param);
	/**
	 *2.5取消接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String cancelOrder(@WebParam(name="param")String param);
	/**
	 *2.6查询预约记录接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryOrdersStatus(@WebParam(name="param")String param);
	/**
	 *2.7停诊列表接口--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryStopSchedules(@WebParam(name="param")String param);
	/**
	 *2.8查询医生列表--便民
	 * 灵璧便民平台
	 * @param param
	 * @return
	 */
	@WebMethod
	public String queryDoctors(@WebParam(name="param")String param);
	/**
	 *收费项目维护-高值耗材
	 * 灵璧单独使用
	 * @param param
	 * @return
	 */
	@WebMethod
	public String saveItemAndHpSetPrices(@WebParam(name="param")String param);

	/**
	 * 获取His对账单
	 * 灵璧中医院单独使用
	 * @param param
	 * @return
	 */
	@WebMethod
	public String getBills(@WebParam(name="param")String param);

	/**
	 * 自助机公用接口
	 * 灵璧中医院自助机单独使用
	 * @param param
	 * @return
	 */
	@WebMethod
	public String  nhisLbzySelfService(@WebParam(name="param")String param);

	@WebMethod
	byte[] queryDoctorsImage(@WebParam(name="param")String param);
}
