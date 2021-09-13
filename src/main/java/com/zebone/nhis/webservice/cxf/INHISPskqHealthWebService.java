package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 深圳坪山口腔医院-对健康网webservice接口
 * @author zhangtao
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISPskqHealthWebService {
	 /**
	  * @Description 查询患者信息
	  * @auther zhangtao
	  * @Date 2020/8/16
	  * @Param [param]
	  * @return java.lang.String
	  */
	 @WebMethod
	 public String getPiMasterInfo(@WebParam(name = "param") String param);
	 /**
	  * @Description 查询患者住院记录
	  * @auther zhangtao
	  * @Date 2020/8/16
	  * @Param [param]
	  * @return java.lang.String
	  */
	@WebMethod
	public String getPvInfoByIp(@WebParam(name = "param") String param);
	/**
     * @Description 住院预交金充值
     * @auther zhangtao
     * @Date 2020/8/16
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String prePayRecharge(@WebParam(name="param") String param);
    /**
     * @Description 查询住院预交金充值记录
     * @auther zhangtao
     * @Date 2020/8/16
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getPrePayDetail(@WebParam(name="param") String param);

	 /**
     * @Description 住院总费用查询
     * @auther zhangtao
     * @Date 2020/8/16
     * @Param [param]
     * @return java.lang.String
     */
    @WebMethod
    public String  getIpCgDetail(@WebParam(name="param") String param);
    /**
     * @Description  住院日费用类别查询
     * @auther zhangtao
     * @Date 2020/8/16
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getIpCgDay(@WebParam(name="param") String param);
    /**
     * @Description 住院日费用明细查询
     * @auther zhangtao
     * @Date 2020/8/16
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getIpCgDayDetail(@WebParam(name="param") String param);

    /**
     * @Description 患者注册
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String savePiMaster(@WebParam(name = "param") String param);

    /**
     * 查询医院机构信息
     *
     * @return
     */
    @WebMethod
    public String getOrgList();
    
    /**
     * @Description 查询科室信息
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getDeptInfo(@WebParam(name = "param") String param);

    /**
     * @Description 查询医生信息
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getEmpInfo(@WebParam(name = "param") String param);
    
	/**
	 * 查询排班科室信息接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String getSchDept(@WebParam(name="param")String param);
    
    /**
     * @Description 查询排班信息
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getSchInfo(@WebParam(name = "param") String param);

    /**
     * @Description 查询号源信息
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getTickets(@WebParam(name = "param") String param);

    /**
     * @Description 号源锁定
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String lockReg(@WebParam(name = "param") String param);

    /**
     * @Description 号源解锁
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String unLockReg(@WebParam(name = "param") String param);


    /**
     * @Description 预约登记
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String saveAppointment(@WebParam(name = "param") String param);

    /**
     * @Description 预约登记撤销
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String cancelAppointment(@WebParam(name = "param") String param);

    /**
     * @Description 提交挂号
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String registerConfirg(@WebParam(name = "param") String param);

    /**
     * @Description 挂号撤销
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String cancelRegister(@WebParam(name = "param") String param);

    /**
     * @Description 查询门诊待缴费列表
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getPvToChargeInfo(@WebParam(name = "param") String param);

	/**
     * 查询门诊有效就诊记录
     *
     * @param param
     * @return
     */
    @WebMethod
    public String getEffectPvInfo(@WebParam(name = "param") String param);
    
    /**
     * @Description 查询门诊已缴费列表（第三方缴费记录）
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String getThirdPaidFeeByOp(@WebParam(name = "param") String param);

    /**
     * @Description 门诊收费结算（诊中支付）
     * @Param[param]
     * @returnjava.lang.String
     */
    @WebMethod
    public String chargeOpSettle(@WebParam(name = "param") String param);
    
    /**
     * @Description 查询预约号源记录
     * @Param[param]
     * @returnjava.lang.String
     */
	@WebMethod
	public String getApptRegRecords(@WebParam(name="param")String param);
	
	/**
     * @Description 查询电子票据信息
     * @Param[param]
     * @returnjava.lang.String
     */
	@WebMethod
	public String getEnoteInvInfo(@WebParam(name="param")String param);
	
	/**
     * @Description 获取医院微信公众号每日账单
     * @Param[param]
     * @returnjava.lang.String
     */
	@WebMethod
	public String HisMerchantSum(@WebParam(name="param")String param);
	
	/**
     * @Description 获取医院微信公众号每日账单详情
     * @Param[param]
     * @returnjava.lang.String
     */
	@WebMethod
	public String HisMerchantDetail(@WebParam(name="param")String param);
	
    /**
     * @Description 资源池测试
     * @Param[param]
     * @returnjava.lang.String
     */
	@WebMethod
	public String sendResourcePool(@WebParam(name="param")String param);

}