package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * 微信对接webservice接口-json格式的出入参数
 *
 * @author yangxue
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface INHISWeChatWebService {

    @WebMethod
    public String getOrgInfo();
    
    /**
     * 查询医院机构信息
     *
     * @return
     */
    @WebMethod
    public String getOrgList();

    /**
     * 查询机构院区信息
     *
     * @return
     */
    @WebMethod
    public String qryOrgAreaInfo(@WebParam(name = "param") String param);
    
    @WebMethod
    public String getDeptInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getEmpInfo(@WebParam(name = "param") String param);

    /**
     * 查询患者信息（微信用）
     *ds
     * @param param
     * @return
     */
    @WebMethod
    public String getPiMasterInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getPiCateInfo();

    /*@WebMethod
    public String savePiMaster(@WebParam(name = "param") String param);*/
    /**
     * 患者注册
     *
     * @param param
     * @return
     */
    @WebMethod
    public String savePiMaster(@WebParam(name = "param") String param);

    /**
     * 患者修改
     *
     * @param param
     * @return
     */
    @WebMethod
    public String updatePiMaster(@WebParam(name = "param") String param);
    @WebMethod
    public String getItemCateInfo();
    @WebMethod
    public String getHpInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getSchInfo(@WebParam(name = "param") String param);
    @WebMethod
    public String getAppSchInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getTickets(@WebParam(name = "param") String param);
    /**
     * 查询预约号源信息按时间段分组-灵璧
     * @param param
     * @return
     */
    @WebMethod
    public String getTicketsGroupDate(@WebParam(name = "param") String param);

    @WebMethod
    public String saveAppointment(@WebParam(name = "param") String param);
    /**
     * 保存预约挂号-按日期分组保存，灵璧用
     * @param param
     * @return
     */
    @WebMethod
    public String saveAppointmentGroupDate(@WebParam(name = "param") String param);


    @WebMethod
    public String cancelAppointment(@WebParam(name = "param") String param);

    @WebMethod
    public String getPiCard(@WebParam(name = "param") String param);

    @WebMethod
    public String getPvInfoByIp(@WebParam(name = "param") String param);

    @WebMethod
    public String getPvInfoByOp(@WebParam(name = "param") String param);

    @WebMethod
    public String getUnpaidFeeByOp(@WebParam(name = "param") String param);

    @WebMethod
    public String getPaidFeeByOp(@WebParam(name = "param") String param);

    @WebMethod
    public String getPayDetailByOp(@WebParam(name = "param") String param);
    //门诊结算方法名未定义

    @WebMethod
    public String cardRecharge(@WebParam(name = "param") String param);

    @WebMethod
    public String prePayRecharge(@WebParam(name = "param") String param);

    @WebMethod
    public String getIpCgDetail(@WebParam(name = "param") String param);
    @WebMethod
    public String saveRegister(@WebParam(name = "param") String param);

    @WebMethod
    public String getIpCgDayDetail(@WebParam(name = "param") String param);

    @WebMethod
    public String getPrePayDetail(@WebParam(name = "param") String param);

    @WebMethod
    public String getDepositInfo(@WebParam(name = "param") String param);

	@WebMethod
	public String preRegister(@WebParam(name="param")String param);
	
	@WebMethod
	public String preRtnRegister(@WebParam(name="param")String param);
	
	@WebMethod
	public String saveRtnRegister(@WebParam(name="param")String param);
	
	@WebMethod
    public String getDeptSchInfo(@WebParam(name="param")String param);

	/**
     * 查询门诊有效就诊记录
     *
     * @param param
     * @return
     */
    @WebMethod
    public String getEffectPvnfo(@WebParam(name = "param") String param);

    /**
     * 床旁系统查询住院患者就诊信息
     *
     * @param param
     * @return
     */
    public String getPvInfoByPv(@WebParam(name = "param") String param);
    
    /**
     * 查询排队叫号信息
     *
     * @param param
     * @return
     */
    @WebMethod
    public String getRegistered(@WebParam(name = "param") String param);
    
    /**
     * 微信服务，支付订单号查询
     * @param param
     * @return
     */
    public String queryOrderCenterInfo(@WebParam(name="param") String param);
    
    /**
     * 查询预约挂号信息
     *
     * @param param
     * @return
     */
    public String getPiAppointment(@WebParam(name = "param") String param);
    /**
     * 查询挂号记录（已缴费的）
     * @param param
     * @return
     */
    public String getRegistRecord(@WebParam(name = "param") String param);
    
    /**
     * 查询门诊待缴费用
     *
     * @param param
     * @return
     */
    @WebMethod
    public String getPvToChargeInfo(@WebParam(name = "param") String param);
    
    /**
     * 门诊收费结算
     *
     * @param param
     * @return
     */
    public String chargeOpSettle(@WebParam(name = "param") String param);
    
    /**
     * 门诊预结算
     * @param param
     * @return
     */
    @WebMethod
    public String getOpPreSettle(@WebParam(name = "param") String param);
    
    /**
     * 查询人员信息
     *
     * @param param
     * @return
     */
    public String getEmployees(@WebParam(name = "param") String param);

    /**
     * 短信状态回传
     * @param param
     * @return
     */
    @WebMethod
    public String callBackSmsResult(@WebParam(name = "param") String param);
    /**
     * 微信账户充值接口
     * @author ds
     * @param param
     * @return
     */
    @WebMethod
	public String accountRecharge(@WebParam(name="param")String param);
    
    /**
	 * 门诊费用缴纳
	 * @param param
	 * @return
	 */
	@WebMethod
	public String payment(@WebParam(name="param")String param);
	
	/**
	 * 住院预交金充值
	 * @param param
	 * @return
	 */
	@WebMethod
	public String RechargeInpatientDeposit(@WebParam(name="param")String param);

	/**
	 * 挂号接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String register(@WebParam(name="param")String param);
	/**
	 * 微信预约挂号
	 * @param param
	 * @return
	 */
	@WebMethod
	public String appointmentRegister(@WebParam(name="param")String param);
	/**
	 * 挂号锁号接口
	 * @param param
	 * @return
	 */
	@WebMethod
	public String lockReg(@WebParam(name="param")String param);
}

