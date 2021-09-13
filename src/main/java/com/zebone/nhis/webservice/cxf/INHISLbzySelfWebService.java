package com.zebone.nhis.webservice.cxf;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * 灵璧中医院自助机
 * @author 卡卡西
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface INHISLbzySelfWebService {

    /**
     * HIS服务器时间获取
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryHISTime")
    String queryHISTime(@WebParam(name="param")String param);

    /**
     * 获取患者档案信息
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryCardInfo")
    String queryCardInfo(@WebParam(name="param")String param);

    /**
     * 3.1	建立档案信息
     * @param param
     * @return
     */
    @WebMethod(operationName = "CreateCardInfo")
    String createCardInfo(@WebParam(name="param")String param);

    /**
     * 4.1	门诊充值
     * @param param
     * @return
     */
    @WebMethod(operationName = "DoPrePay")
    String doPrePay(@WebParam(name="param")String param);


    /**
     * 查询门诊充值记录
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryPrePayRecord")
    String queryPrePayRecord(@WebParam(name="param")String param);

    /**
     * 查询挂号类别
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryRegType")
    String queryRegType(@WebParam(name="param")String param);

    /**
     * 查询一级挂号科室
     * @return
     */
    @WebMethod(operationName = "QueryDepartmentLevelOneList")
    String queryDepartmentLevelOneList(@WebParam(name="param")String param);

    /**
     * 查询医生列表
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryDoctorList")
    String queryDoctorList(@WebParam(name="param")String param);

    /**
     * 挂号
     * 挂号确认
     * @param param
     * @return
     */
    @WebMethod(operationName = "DoReg")
    String doReg(@WebParam(name="param")String param);

    /**
     * 预约挂号
     * @param param
     * @return
     */
    @WebMethod(operationName = "DoPreReg")
    String doPreReg(@WebParam(name="param")String param);

    /**
     * 预约挂号查询
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryPreRegRecord")
    String queryPreRegRecord(@WebParam(name="param")String param);

    /**
     * 缴费
     * @param param
     * @return
     */
    @WebMethod(operationName = "DoPay")
    String doPay(@WebParam(name="param")String param);

    /**
     * 6.1.查询待缴费挂号列表信息
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryToPayRegList")
    String queryToPayRegList(@WebParam(name="param")String param);

    /**
     * 6.2.查询待缴费处方列表信息
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryToPayList")
    String queryToPayList(@WebParam(name="param")String param);

    /**
     * 待缴费处方明细
     * @return
     */
    @WebMethod(operationName = "QueryToPayDetail")
    String querytopaydetail(@WebParam(name="param")String param);

    /**
     * 查询缴费记录列表
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryPayRecordList")
    String queryPayRecordList(@WebParam(name="param")String param);

    /**
     * 查询缴费记录详细
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryPayRecordDetail")
    String queryPayRecordDetail(@WebParam(name="param")String param);

    /**
     * 查询患者未打印的检验报告列表
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryLaboratoryRecordList")
    String queryLaboratoryRecordList(@WebParam(name="param")String param);


    /**
     * 查询医院收费项目信息
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryDrugList")
    String queryDrugList(@WebParam(name="param")String param);

    /**
     * 查询挂号记录
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryRegRecord")
    String queryRegRecord(@WebParam(name="param")String param);

    /**
     * 查询预约时段列表
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryPreRegDoctorList")
    String queryPreRegDoctorList(@WebParam(name="param")String param);

    /**
     * 患者在院信息查询
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryInHospitalPatientInfo")
    String queryInHospitalInfo(@WebParam(name="param")String param);

    /**
     * 住院押金充值
     * @param param
     * @return
     */
    @WebMethod(operationName = "DoPrePayInHospital")
    String prePayInHospital(@WebParam(name="param")String param);

    /**
     * 预交金明细查询
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryInHospitalPrePayRecord")
    String queryPrePayInHospital(@WebParam(name="param")String param);

    /**
     * 7.6.住院费用信息列表
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryInHospitalPayRecordList")
    String queryInHospitalPayRecordList(@WebParam(name="param")String param);

    /**
     * 住院费用信息详细
     * @param param
     * @return
     */
    @WebMethod(operationName = "QueryInHospitalPayRecordDetail")
    String queryInHospitalPayRecordDetail(@WebParam(name="param")String param);

    /**
     * 预约挂号取号（等于挂号确认-缴费）
     * @param param
     * @return
     */
    @WebMethod(operationName = "PayPreReg")
    String payPreReg(@WebParam(name="param")String param);

    /**
     * 预约挂号取消
     * @param param
     * @return
     */
    @WebMethod(operationName = "CancelOrder")
    String cancelOrder(@WebParam(name="param")String param);
}