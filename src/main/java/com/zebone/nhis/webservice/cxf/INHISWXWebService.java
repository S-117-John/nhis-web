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
public interface INHISWXWebService {

    @WebMethod
    public String getOrgInfo();

    @WebMethod
    public String getDeptInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getEmpInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getPiMasterInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getPiCateInfo();

    @WebMethod
    public String savePiMaster(@WebParam(name = "param") String param);

    @WebMethod
    public String getItemCateInfo();
    @WebMethod
    public String getHpInfo();

    @WebMethod
    public String getSchInfo(@WebParam(name = "param") String param);
    @WebMethod
    public String getAppSchInfo(@WebParam(name = "param") String param);

    @WebMethod
    public String getTickets(@WebParam(name = "param") String param);

    @WebMethod
    public String saveAppointment(@WebParam(name = "param") String param);

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
	
}
