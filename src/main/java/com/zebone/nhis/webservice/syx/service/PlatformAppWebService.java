package com.zebone.nhis.webservice.syx.service;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiBind;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.webservice.syx.dao.PlatformAppMapper;
import com.zebone.nhis.webservice.syx.support.SetUserUtils;
import com.zebone.nhis.webservice.syx.utils.PfWsUtils;
import com.zebone.nhis.webservice.syx.vo.ResPiInfoResVo;
import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 中二住院Apphis提供的webService
 *
 * @author jd
 */
@Service
public class PlatformAppWebService {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    @Resource
    private PlatformAppMapper platformAppMapper;

    /**
     * 住院患者信息查询接口
     *
     * @param reqXml
     * @return
     */
    public String qryPiInfoForIn(String reqXml) {
        String resXml = "";
        QryPiInfoForInReqVo reqpiInfo = (QryPiInfoForInReqVo) XmlUtil.XmlToBean(reqXml, QryPiInfoForInReqVo.class);
        SetUserUtils.setUserByEmpCode(reqpiInfo.getSender().getSenderId());
        List<QryPiInfoForInSub> subList = reqpiInfo.getSubject();
        if (subList == null) {
            resXml = retPiInfoMessage(reqpiInfo, "AE", null, "未获得请求消息");
        }
        ReqPiInfo piInfo = subList.get(0).getReqPiInfo();
        if (piInfo == null) {
            resXml = retPiInfoMessage(reqpiInfo, "AE", null, "未获得患者信息");
        } else if (CommonUtils.isEmptyString(piInfo.getPatientName())) {
            resXml = retPiInfoMessage(reqpiInfo, "AE", null, "未获得患者姓名");
        } else if (CommonUtils.isEmptyString(piInfo.getUserCardId())) {
            resXml = retPiInfoMessage(reqpiInfo, "AE", null, "未获得患者身份证号");
        } else {
            List<ResIpUserInfo> resUserInfos = platformAppMapper.qryPiInfoForIn(piInfo);
            if (resUserInfos == null || resUserInfos.size() == 0) {
                resXml = retPiInfoMessage(reqpiInfo, "AE", null, "未查到匹配患者信息");
            } else {
                resXml = retPiInfoMessage(reqpiInfo, "AA", resUserInfos, null);
            }
        }
        return resXml;
    }

    /**
     * 构建住院患者消息返回
     *
     * @param reqpiInfo 请求对象
     * @param retType   成功：AA ;失败：AE
     * @param res       响应详细信息
     * @return
     */
    private String retPiInfoMessage(QryPiInfoForInReqVo reqpiInfo, String retType, List<ResIpUserInfo> resUserInfos, String message) {
        QryPiInfoResVo response = new QryPiInfoResVo();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(reqpiInfo.getActionId());
        response.setActionName(reqpiInfo.getActionName());
        QryPiInfoResult result = new QryPiInfoResult();
        ResPiInfoResVo infoResVo = new ResPiInfoResVo();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
        } else {
            result.setText("处理失败！" + message);
        }
        result.setRequestId(reqpiInfo.getId());
        result.setRequestTime(reqpiInfo.getCreateTime());
        infoResVo.setUserInfos(resUserInfos);
        result.setInfoResVo(infoResVo);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryPiInfoResVo.class);
        return resXml;
    }

    /**
     * 住院费用查询接口
     *
     * @param reqXml
     * @return
     */
    public String getInPatientFeeInfo(String reqXml) {
        String resXml = "";
        QryPiCostRequest request = (QryPiCostRequest) XmlUtil.XmlToBean(reqXml, QryPiCostRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().size() == 0 || request.getSubject().get(0).getQryPiCostReq() == null) {
            resXml = retPiCostMessage(request, "AE", null, "未获得请求数据！");
        }
        SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
        QryPiCostReq costReq = request.getSubject().get(0).getQryPiCostReq();
        if (CommonUtils.isEmptyString(costReq.getIpSeqnoText())) {
            resXml = retPiCostMessage(request, "AE", null, "请求参数患者住院号未传入！");
        } else if (CommonUtils.isEmptyString(costReq.getIpSeqnoText())) {
            resXml = retPiCostMessage(request, "AE", null, "请求参数患者住院就诊流水号未传入！");
        } else {
            QryPiCostResVo resVo = platformAppMapper.getInPatientFeeInfo(costReq);
            if (resVo == null) {
                resXml = retPiCostMessage(request, "AE", null, "未查询到匹配患者信息！");
            } else {
                resXml = retPiCostMessage(request, "AA", resVo, null);
            }
        }
        return resXml;
    }

    /***
     * 构建住院患者费用查询返回
     * @param reqpiCost
     * @param retType
     * @param resVo
     * @param message
     * @return
     */
    private String retPiCostMessage(QryPiCostRequest reqpiCost, String retType, QryPiCostResVo resVo, String message) {
        QryPiCostResponse response = new QryPiCostResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(reqpiCost.getActionId());
        response.setActionName(reqpiCost.getActionName());
        QryPiCostResult result = new QryPiCostResult();
        QrypiCostResSub infoResVo = new QrypiCostResSub();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
        } else {
            result.setText("处理失败！" + message);
        }
        result.setRequestId(reqpiCost.getId());
        result.setRequestTime(reqpiCost.getCreateTime());
        infoResVo.setQryPiCostRes(resVo);
        result.setCostSubject(infoResVo);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryPiCostResponse.class);
        return resXml;
    }

    /**
     * 按金缴纳记录查询接口
     *
     * @param reqXml
     * @return
     */
    public String getInPatientForegiftInfo(String reqXml) {
        String resXml = "";
        QryPiDeposRequest request = (QryPiDeposRequest) XmlUtil.XmlToBean(reqXml, QryPiDeposRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().size() == 0 || request.getSubject().get(0).getDeposReqVo() == null) {
            resXml = retDeposMessage(request, "AE", null, "未获得请求参数！");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryPiDeposReqVo reqVo = request.getSubject().get(0).getDeposReqVo();
            if (CommonUtils.isEmptyString(reqVo.getInpatientId())) {
                resXml = retDeposMessage(request, "AE", null, "住院就诊流水号必须传入！");
            } else if (!CommonUtils.isNotNull(reqVo.getPaySource())) {
                resXml = retDeposMessage(request, "AE", null, "付款来源必须传入！");
            } else {
                String sql = "select count(1) from PV_ENCOUNTER where CODE_PV=? ";
                int count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{reqVo.getInpatientId()});
                if (count > 0) {
                    sql = "select count(1) from PV_ENCOUNTER where CODE_PV=? and EU_STATUS in ('0','1','2')";
                    count = DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{reqVo.getInpatientId()});
                    if (count < 1) {
                        resXml = retDeposMessage(request, "AE", null, "匹配的住院就诊流水号为【" + reqVo.getInpatientId() + "】的患者，不是在院状态，不允许查询按金缴费记录！");
                    } else {
                        reqVo.setPaySourceValue(PaySource.getName(Integer.parseInt(reqVo.getPaySource())));
                        reqVo.setNote(reqVo.getOrderId());
                        List<QryPiDeposOrderInfo> orderInfos = platformAppMapper.getInPatientForegiftInfo(reqVo);
                        if (orderInfos == null || orderInfos.size() == 0) {
                            resXml = retDeposMessage(request, "AE", null, "未匹配到住院就诊流水号为【" + reqVo.getInpatientId() + "】患者的按金缴纳记录！");
                        } else {
                            resXml = retDeposMessage(request, "AA", orderInfos, null);
                        }
                    }
                } else {
                    resXml = retDeposMessage(request, "AE", null, "未匹配到住院就诊流水号为【" + reqVo.getInpatientId() + "】的住院患者！");
                }
            }
        }
        return resXml;
    }

    /**
     * 构建按金缴纳记录查询返回
     *
     * @param request
     * @param retType
     * @param orderInfos
     * @param message
     * @return
     */
    private String retDeposMessage(QryPiDeposRequest request, String retType, List<QryPiDeposOrderInfo> orderInfos, String message) {
        QryPiDeposResponse response = new QryPiDeposResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());
        QryPiDeposResult result = new QryPiDeposResult();
        QryPiDeposResSubject subject = new QryPiDeposResSubject();
        QryPiDeposResVo resvo = new QryPiDeposResVo();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
            resvo.setResultCode("0");
            resvo.setResultDesc("处理成功！");
        } else {
            result.setText("处理失败！" + message);
            resvo.setResultCode("1");
            resvo.setResultDesc(message);
        }
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());
        resvo.setOrderInfos(orderInfos);
        subject.setResVo(resvo);
        result.setResSubject(subject);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryPiDeposResponse.class);
        return resXml;
    }

    /**
     * 一日清单基本信息查询接口
     *
     * @param reqXml
     * @return
     */
    public String getInPatientListOfDayInfo(String reqXml) {
        String resXml = "";
        QryPiDeposRequest request = (QryPiDeposRequest) XmlUtil.XmlToBean(reqXml, QryPiDeposRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().size() == 0 || request.getSubject().get(0) == null) {
            resXml = retPiDayMessage(request, "AE", null, "未获得请求对象！");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryPiDeposReqVo reqVo = request.getSubject().get(0).getDeposReqVo();
            if (reqVo == null || CommonUtils.isEmptyString(reqVo.getInpatientId())) {
                resXml = retPiDayMessage(request, "AE", null, "请求参数患者住院就诊流水号必须传入！");
            } else {
                String sql = "select count(1) cnt,eu_status from pv_encounter where code_pv=? group by eu_status";
                Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{reqVo.getInpatientId()});
                if (resMap == null) {
                    resXml = retPiDayMessage(request, "AE", null, "未找到住院就诊流水号为【" + reqVo.getInpatientId() + "】的患者！");
                } else if (Integer.parseInt(CommonUtils.getString(resMap.get("euStatus"))) > 3) {
                    resXml = retPiDayMessage(request, "AE", null, "当前住院就诊流水号【" + reqVo.getInpatientId() + "】的患者不是在院状态，不允许查询一日清单记录！");
                } else {
                    QryPiDeposResVo resVo = platformAppMapper.getInPiListOfDayInfo(reqVo);
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("pkPv", resVo.getPkPv());
                    paramMap.put("beginDate", reqVo.getBeginDate());
                    paramMap.put("endDate", reqVo.getEndDate());
                    List<QryPiDeposOrderInfo> orderInfos = platformAppMapper.getInPiItemOfDayInfo(paramMap);
                    resVo.setOrderInfos(orderInfos);
                    resXml = retPiDayMessage(request, "AA", resVo, null);
                }
            }

        }
        return resXml;
    }

    /**
     * 构建一日清单基本信息查询返回
     *
     * @param request
     * @param retType
     * @param orderInfos
     * @param message
     * @return
     */
    private String retPiDayMessage(QryPiDeposRequest request, String retType, QryPiDeposResVo resVo, String message) {
        QryPiDeposResponse response = new QryPiDeposResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());
        QryPiDeposResult result = new QryPiDeposResult();
        QryPiDeposResSubject subject = new QryPiDeposResSubject();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
            resVo.setResultCode("0");
            resVo.setResultDesc("处理成功！");
        } else {
            result.setText("处理失败！" + message);
            resVo.setResultCode("1");
            resVo.setResultDesc(message);
        }
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());
        subject.setResVo(resVo);
        result.setResSubject(subject);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryPiDeposResponse.class);
        return resXml;
    }

    /**
     * 3.3	缴纳按金接口
     *
     * @param reqXml
     * @return
     */
    public String payForegiftOrder(String reqXml) {
        String resXml = "";
        PayDepoRequest request = (PayDepoRequest) XmlUtil.XmlToBean(reqXml, PayDepoRequest.class);
        PayDepoRes res = new PayDepoRes();

        boolean flagSuc = true;//标志是否成功
        String msg = "";

        if (request == null || request.getSubject() == null || request.getSubject().size() == 0 || request.getSubject().get(0).getPayDepoReq() == null) {
            flagSuc = false;
            msg = "未获得请求数据！";

            resXml = assemblyResInfo(request, res, flagSuc, msg);
        }
        SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());

        if (CommonUtils.isEmptyString(resXml)) {
            PayDepoReq depoInfo = request.getSubject().get(0).getPayDepoReq();
            PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where code_pv = ?",
                    PvEncounter.class, new Object[]{depoInfo.getInpatientId()});
            if (CommonUtils.isEmptyString(depoInfo.getInpatientId())) {
                flagSuc = false;
                msg = "住院患者流水主键(InPatientID)未传入！";

                resXml = assemblyResInfo(request, res, flagSuc, msg);
            } else if (pv == null) {
                flagSuc = false;
                msg = "找不到对应的住院患者！";

                resXml = assemblyResInfo(request, res, flagSuc, msg);
            } else if (Integer.valueOf(pv.getEuStatus()).compareTo(3) >= 0) {
                flagSuc = false;
                msg = "患者" + pv.getNamePi() + "不是在院状态，不允许缴纳按金！";

                resXml = assemblyResInfo(request, res, flagSuc, msg);
            } else {
                //查询患者已交预交金
                Double depoAmt = DataBaseHelper.queryForScalar(
                        "select sum(amount) from bl_deposit where pk_pv = ? and flag_settle = '0'"
                        , Double.class, new Object[]{pv.getPkPv()});

                //订单相关信息描述
                String orderDesc = "请到住院收费处补打按金收据!";

                //组装收款信息
                BlDeposit depo = new BlDeposit();

                //查询此订单号是否已经缴费
                String sql = "select * from BL_DEPOSIT " +
                        " where pk_pv = ? and PAY_INFO = ?  and DT_PAYMODE = ?";
                depo = DataBaseHelper.queryForBean(sql, BlDeposit.class, new Object[]{pv.getPkPv(), depoInfo.getPayNum(), depoInfo.getPayMode()});

                if (depo == null || CommonUtils.isEmptyString(depo.getPkDepo())) {
                    depo = new BlDeposit();
                    /**组装缴款信息*/
                    depoAmt = MathUtils.add(depoAmt == null ? 0D : depoAmt, depoInfo.getPayAmout());//账户原有金额+现缴纳金额
                    depo.setPkDepo(NHISUUID.getKeyId());
                    depo.setPkOrg("a41476368e2943f48c32d0cb1179dab8");
                    depo.setEuDptype("9");
                    depo.setEuDirect("1");
                    depo.setEuPvtype("3");
                    depo.setPkPi(pv.getPkPi());
                    depo.setPkPv(pv.getPkPv());
                    depo.setAmount(BigDecimal.valueOf(depoInfo.getPayAmout()));
                    depo.setDtPaymode(depoInfo.getPayMode());
                    depo.setBankNo(depoInfo.getAgtCode());
                    depo.setDatePay(DateUtils.strToDate(depoInfo.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                    //查询110105码表信息(付款来源方式)
//                    Map<String, Object> docMap = DataBaseHelper.queryForMap(
//                            "select name,val_attr from bd_defdoc where code_defdoclist = '110105' and code = ?"
//                            , new Object[]{depoInfo.getPaySource()});
                    //Map<String,Object> deptMap = new HashMap<>();
                    Map<String, Object> empMap = new HashMap<>();
//                    if (docMap != null && docMap.size() > 0) {
//                        String[] codeArray = CommonUtils.getString(docMap.get("valAttr")).split("\\|");
//                        //查询收款部门信息
//                        //deptMap = DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = ?", new Object[]{codeArray[0].trim()});
//                        //查询收费员信息
//                        //empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ?", new Object[]{codeArray[1].trim()});
//                        //备注
//                        depo.setNote(depoInfo.getOrderId() + "|" + depoInfo.getPayNum() + "|" + CommonUtils.getString(docMap.get("name")) + "|" + depoInfo.getAgtOrdNum() + " " + orderDesc);
//                    } else {
                        //备注
                        depo.setNote(depoInfo.getOrderId() + "|" + depoInfo.getPayNum() + "|" + depoInfo.getPaySource() + "|" + depoInfo.getAgtOrdNum() + " " + orderDesc);
//                    }
                    //查询收费员信息
                    if (request.getSender().getSenderId() != null && !CommonUtils.isEmptyString(request.getSender().getSenderId())) {
                        empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ? and flag_active='1' and del_flag='0'", new Object[]{request.getSender().getSenderId().trim()});
                    }
					/*if(deptMap!=null && deptMap.size()>0){
						depo.setPkDept(CommonUtils.getString(deptMap.get("pkDept")));
					}else{
						depo.setPkDept("-");
					}*/
                    depo.setPkDept("76E6AA37C47344E4A062B21786787F2C");

                    if (empMap != null && empMap.size() > 0) {
                        depo.setPkEmpPay(CommonUtils.getString(empMap.get("pkEmp")));
                        depo.setNameEmpPay(CommonUtils.getString(empMap.get("nameEmp")));
                    } else {
                        depo.setPkEmpPay("-");
                        depo.setNameEmpPay("-");
                    }
                    depo.setFlagAcc("0");
                    depo.setFlagSettle("0");
                    depo.setFlagCc("0");
                    depo.setCreateTime(new Date());
                    depo.setTs(new Date());
                    depo.setDelFlag("0");
                    depo.setPayInfo(depoInfo.getPayNum());

                    DataBaseHelper.insertBean(depo);

                    BlExtPay extPay = new BlExtPay();
                    extPay.setPkExtpay(NHISUUID.getKeyId());
                    extPay.setPkOrg("a41476368e2943f48c32d0cb1179dab8");
                    extPay.setAmount(BigDecimal.valueOf(depoInfo.getPayAmout()));
                    extPay.setEuPaytype("9");
                    extPay.setDateAp(new Date());
                    extPay.setFlagPay("1");
                    extPay.setDatePay(new Date());
                    extPay.setTradeNo(depoInfo.getPayNum());
                    extPay.setSerialNo(depoInfo.getOrderId());
                    extPay.setDescPay(depo.getNote());
                    extPay.setSysname("平安APP");
                    extPay.setPkPi(pv.getPkPi());
                    extPay.setPkPv(pv.getPkPv());
                    extPay.setPkDepo(depo.getPkDepo());
                    extPay.setEuBill("0");
                    extPay.setCreateTime(new Date());
                    extPay.setCreator(depo.getPkEmpPay());
                    extPay.setTs(new Date());

                    DataBaseHelper.insertBean(extPay);
                }

                res.setResultCode("0");
                res.setResultDesc("处理成功！");
                res.setOrderIdHis(depo.getPkDepo());
                res.setOrderDesc("");//返回空
                res.setForegiftAmount(depoAmt.toString());

                msg = "处理成功！";
                resXml = assemblyResInfo(request, res, flagSuc, msg);
            }
        }

        return resXml;
    }

    /**
     * 组装相应参数
     *
     * @param request
     * @param res
     * @param flagSuc
     * @param msg
     * @return
     */
    private String assemblyResInfo(PayDepoRequest request, PayDepoRes res, boolean flagSuc, String msg) {
        PayDepoResponse response = new PayDepoResponse();
        PayDepoResult result = new PayDepoResult();
        PayDepoResSubject subject = new PayDepoResSubject();

        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());
        if (flagSuc) {
            result.setText("处理成功！");
        } else {
            result.setText("处理失败！" + msg);

            res.setResultCode("-1");
            res.setResultDesc(msg);
        }
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());

        subject.setPayDepoRes(res);
        result.setSubject(subject);
        response.setResult(result);

        String resXml = XmlUtil.beanToXml(response, PayDepoResponse.class);
        return resXml;
    }


    /**
     * 一日清单项目明细查询接口
     *
     * @param reqXml
     * @return
     */
    public String getInPatientListOfDayItemInfo(String reqXml) {
        String resXml = "";
        QryPiDeposRequest request = (QryPiDeposRequest) XmlUtil.XmlToBean(reqXml, QryPiDeposRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().size() == 0 || request.getSubject().get(0) == null) {
            resXml = retDeposMessage(request, "AE", null, "未获得请求参数！");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryPiDeposReqVo reqVo = request.getSubject().get(0).getDeposReqVo();
            if (reqVo == null || CommonUtils.isEmptyString(reqVo.getInpatientId())) {
                resXml = retDeposMessage(request, "AE", null, "住院就诊流水号必须传入！");
            } else {
                String sql = "select count(1) cnt,eu_status from pv_encounter where code_pv=? group by eu_status";
                Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{reqVo.getInpatientId()});

                if (resMap == null) {
                    resXml = retDeposMessage(request, "AE", null, "未找到住院就诊流水号为【" + reqVo.getInpatientId() + "】的患者！");
                } else if (Integer.parseInt(CommonUtils.getString(resMap.get("euStatus"))) > 3) {
                    resXml = retPiDayMessage(request, "AE", null, "当前住院就诊流水号【" + reqVo.getInpatientId() + "】的患者不是在院状态，不允许查询一日清单记录！");
                } else {
                    List<QryPiDeposOrderInfo> orderInfos = platformAppMapper.getInPiListOfDayItemKind(reqVo);
                    List<QryPiItemInfo> itemInfos = platformAppMapper.getInPiListOfDayItemInfo(reqVo);
                    for (QryPiDeposOrderInfo order : orderInfos) {
                        List<QryPiItemInfo> items = new ArrayList<QryPiItemInfo>();
                        for (QryPiItemInfo item : itemInfos) {
                            if (order.getFeekindId().equals(item.getFeekindId())) {
                                items.add(item);
                            }
                        }
                        order.setItemInfos(items);
                    }
                    resXml = retDeposMessage(request, "AA", orderInfos, null);
                }
            }
        }
        return resXml;
    }

    /**
     * 3.7	查询患者信息接口
     *
     * @param reqXml
     * @return
     * @throws Exception
     */
    public String getPatiInfo(String reqXml) {
        String resXml = "";
        QryPiInfoForInReqVo reqpiInfo = (QryPiInfoForInReqVo) XmlUtil.XmlToBean(reqXml, QryPiInfoForInReqVo.class);
        List<QryPiInfoForInSub> subList = reqpiInfo.getSubject();
        SetUserUtils.setUserByEmpCode(reqpiInfo.getSender().getSenderId());
        if (subList == null) {
            resXml = retGetPatiInfo(reqpiInfo, "AE", null, "未获得请求消息");
        }
        ReqPiInfo piInfo = subList.get(0).getReqPiInfo();
        if (piInfo == null) {
            resXml = retGetPatiInfo(reqpiInfo, "AE", null, "未获得患者信息");
        } else if (CommonUtils.isEmptyString(piInfo.getPatientName())) {
            resXml = retGetPatiInfo(reqpiInfo, "AE", null, "未获得患者姓名");
        } else if (CommonUtils.isEmptyString(piInfo.getUserCardId())) {
            resXml = retGetPatiInfo(reqpiInfo, "AE", null, "未获得患者身份证号");
        } else {
            //查询住院患者数据，若his系统不存在，查询建讯系统患者数据
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("namePi", piInfo.getPatientName());
            paramMap.put("idNo", piInfo.getUserCardId());

            //List<QryPatiInfoVo> oldPi = (List<QryPatiInfoVo>) ExtSystemProcessUtils.processExtMethod("PiInfo", "piInfo", JsonUtil.writeValueAsString(paramMap));
            ApplicationUtils unit = new ApplicationUtils();
            Map<String, Object> oldParam = new HashMap<String, Object>();
            oldParam.put("methodName", "piInfo");
            oldParam.put("param", JsonUtil.writeValueAsString(paramMap));
            ResponseJson resvo = unit.execService("MA", "QryOldSystemPiToHisHandler", "invokeMethod", oldParam, new User());
            List<QryPatiInfoVo> oldPi = (List<QryPatiInfoVo>) resvo.getData();
            List<QryPatiInfoVo> piList = platformAppMapper.getPiMasterListNoPhoto(paramMap);
            if (oldPi != null) {
                piList.addAll(oldPi);
            }
            if (piList == null || piList.size() == 0) {
                resXml = retGetPatiInfo(reqpiInfo, "AE", null, "未查到匹配患者信息");
            } else {
                resXml = retGetPatiInfo(reqpiInfo, "AA", piList, null);
            }
        }
        return resXml;
    }

    /**
     * 3.7 查询患者信息接口 返回Xml构建
     *
     * @param reqpiInfo
     * @param patiInfoVos
     * @param status
     * @param message
     * @return
     */
    private String retGetPatiInfo(QryPiInfoForInReqVo reqpiInfo, String status, List<QryPatiInfoVo> patiInfoVos, String message) {
        QryPatiInfoResponse response = new QryPatiInfoResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(reqpiInfo.getActionId());
        response.setActionName(reqpiInfo.getActionName());

        QryPatiInfoResult patResvo = new QryPatiInfoResult();
        QryPatiInfoSubject patSub = new QryPatiInfoSubject();
        QryPatiInfoResVo resVo = new QryPatiInfoResVo();
        if ("AA".equals(status)) {
            patResvo.setText("处理成功！");
            resVo.setResultCode("0");
            resVo.setResultDesc("处理成功");
        } else {
            patResvo.setText("处理失败！" + message);
            resVo.setResultCode("1");
            resVo.setResultDesc("处理失败" + message);
        }
        patResvo.setId(status);
        patResvo.setRequestId(reqpiInfo.getId());
        patResvo.setRequestTime(reqpiInfo.getCreateTime());

        resVo.setPatiInfoVos(patiInfoVos);
        patSub.setInfoResVo(resVo);
        patResvo.setPatiInfoSubject(patSub);
        response.setResult(patResvo);
        return XmlUtil.beanToXml(response, QryPatiInfoResponse.class);
    }

    /**
     * 3.8	入院登记接口
     *
     * @param reqXml
     * @return
     */
    public String regiAdmission(String reqXml) {
        String resXml = "";
        QryPatiInfoRegRequest request = (QryPatiInfoRegRequest) XmlUtil.XmlToBean(reqXml, QryPatiInfoRegRequest.class);
        if (request.getSubject() == null || request.getSubject().get(0) == null || request.getSubject().get(0).getPatiInfoReqReqVo() == null) {
            resXml = retRegiAdmissionMessage(request, "AE", null, "请求数据未传入");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryPatiInfoRegReq reqVo = request.getSubject().get(0).getPatiInfoReqReqVo();

            if (!StringUtils.isEmpty(reqVo.getPkPi())) {
                List<PvEncounter> pvEncounters = DataBaseHelper.queryForList("select * from pv_encounter where pk_pi=? and eu_status < 3", PvEncounter.class, new Object[]{reqVo.getPkPi()});
                if (pvEncounters != null && pvEncounters.size() > 0) {
                    throw new BusException("患者已在院，不能再次登记！");
                }
            }

            //构建入院登记请求参数
            AdtRegParam adrparam = new AdtRegParam();
            PvEncounter pvent = new PvEncounter();
            PiMaster master = new PiMaster();

            BeanUtils.copyProperties(reqVo, adrparam);
            BeanUtils.copyProperties(reqVo, pvent);
            BeanUtils.copyProperties(reqVo, master);

            String codePv = ApplicationUtils.getCode("0302");

            adrparam.setIpTimes(reqVo.getIpTimes() + 1);
            Map<String, Object> deptPv = getDeptInfo(reqVo.getCodeDept());
            Map<String, Object> deptNsPv = getDeptInfo(reqVo.getCodeDeptNs());
            adrparam.setPkDeptAdmit(getPropValueStr(deptPv, "pkDept"));
            adrparam.setPkDeptNsAdmit(getPropValueStr(deptNsPv, "pkDept"));
            adrparam.setDtLevelDise("00");//病情等级
            adrparam.setDtIntype("01");
            adrparam.setCodePv(codePv);
            Map<String, Object> empTre = getEmpInfo(reqVo.getCodeEmpTre());
            adrparam.setPkEmpTre(getPropValueStr(empTre, "pkEmp"));
            adrparam.setNameEmpTre(getPropValueStr(empTre, "nameEmp"));
            adrparam.setDateReg(new Date());
            adrparam.setPkInsu("EA7A36C38B454455B3A1B4C5507B1831");
            adrparam.setDtPaymode("1");
            adrparam.setPkDiag(getPropValueStr(getDiagInfo(reqVo.getCodeDiag()), "pkDiag"));
            adrparam.setDtDiagtype("0100");
            adrparam.setEuHptype("0");
            adrparam.setAllowReHospital("1");

            //组建pimaster数据
            if (CommonUtils.isEmptyString(reqVo.getCodeIp())) {
                master.setCodeIp(ApplicationUtils.getCode("0203"));//住院号
            }
            if (CommonUtils.isEmptyString(reqVo.getCodePi())) {
                master.setCodePi(ApplicationUtils.getCode("0201"));//患者编码
            }
            // TODO 需确定第三方系统是否会传入门诊号，暂时默认不传入
            master.setCodeOp(ApplicationUtils.getCode("0202"));//门诊号
            master.setDtIdtype("01");
            master.setBirthDate(DateUtils.strToDate(reqVo.getBirthDate(), "yyyyMMddHHmmss"));
            master.setPkPicate("24d7bf6ec4034d248d1d0db083bcf7d1");//患者分类主键

            //组建pvencounter参数
            pvent.setCodePv(codePv);//就诊号
            pvent.setDateBegin(new Date());
            pvent.setFlagIn("0");
            pvent.setFlagSettle("0");
            //TODO 根据科室编码查询 科室主键信息
            pvent.setPkDept(getPropValueStr(deptPv, "pkDept"));
            pvent.setPkDeptNs(getPropValueStr(deptNsPv, "pkDept"));
            if (StringUtils.isEmpty(pvent.getPkDeptNs())) {
                List<Map<String, Object>> busInfo = platformAppMapper.getPkDeptNs(pvent.getPkDept());
                if (busInfo != null && busInfo.size() > 0) {
                    pvent.setPkDeptNs(busInfo.get(0).get("pkDeptNs") + "");
                } else {
                    throw new BusException("入院失败！未找到该科室对应的护理单元！");
                }
            }

            //TODO 根据收治医生编码获取医师主键和医师名称
            pvent.setPkEmpTre(getPropValueStr(empTre, "pkEmp"));
            pvent.setNameEmpTre(getPropValueStr(empTre, "nameEmp"));
            pvent.setPkPicate("24d7bf6ec4034d248d1d0db083bcf7d1");
            pvent.setEuPvmode("0");
            pvent.setFlagCancel("0");

            adrparam.setPvEncounter(pvent);
            adrparam.setPiMaster(master);
            ApplicationUtils unit = new ApplicationUtils();

            //TODO 组装session中user数据
            User user = new User();
            user.setPkOrg(reqVo.getPkOrg());
            user.setPkDept(getPropValueStr(getDeptInfo(reqVo.getCodeDeptNow()), "pkDept"));
            Map<String, Object> empNow = getEmpInfo(reqVo.getCodeEmpNow());
            user.setPkEmp(getPropValueStr(empNow, "pkEmp"));
            user.setNameEmp(getPropValueStr(empNow, "nameEmp"));
            UserContext.setUser(user);

//            Map<String, Object> oldParam = new HashMap<String, Object>();
//            oldParam.put("methodName", "piInfo");
//            Map<String, Object> qryMap = Maps.newHashMap();
//            qryMap.put("idNo", master.getIdNo());
//            qryMap.put("namePi", master.getNamePi());
//            String param = JsonUtil.writeValueAsString(qryMap);
//            oldParam.put("param", param);
//            ResponseJson oldresVo = unit.execService("MA", "QryOldSystemPiToHisHandler", "invokeMethod", oldParam, user);

            AdtRegParamVo paramvo = new AdtRegParamVo();
            paramvo.setRegParam(adrparam);
            ResponseJson resVo = unit.execService("PV", "PvAdtPubService", "saveADTReg", adrparam, user);

            if (resVo.getData() == null) {
                throw new BusException(resVo.getDesc() == null ? "处理返回结果失败！" : resVo.getDesc());
            } else {
                String resParam = JsonUtil.writeValueAsString(resVo);
                JsonNode dataNode = JsonUtil.getJsonNode(resParam, "data");
                PvEncounter pvEncounter = JsonUtil.readValue(dataNode, PvEncounter.class);
                QryPatiInfoRegRes resvo = new QryPatiInfoRegRes();
                resvo.setCodePv(pvEncounter.getCodePv());
                resvo.setCodeIp(master.getCodeIp());
                resvo.setIpTimes(adrparam.getIpTimes());
                resvo.setPkPv(pvEncounter.getPkPv());
                resvo.setPkPi(pvEncounter.getPkPi());
                resvo.setCodePi(master.getCodePi());
                resXml = retRegiAdmissionMessage(request, "AA", resvo, "");
            }

        }
        return resXml;
    }

    /**
     * 构建入院登记返回消息节点
     *
     * @param request
     * @param status
     * @param resVo
     * @param message
     * @return
     */
    private String retRegiAdmissionMessage(QryPatiInfoRegRequest request, String status, QryPatiInfoRegRes resVo, String message) {
        QryPatiInfoRegResponse response = new QryPatiInfoRegResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());

        QryPatiInfoRegResult result = new QryPatiInfoRegResult();
        result.setId(status);
        result.setRequestId(request.getActionId());
        result.setRequestTime(request.getCreateTime());
        if (resVo == null) resVo = new QryPatiInfoRegRes();
        if ("AA".equals(status)) {
            result.setText("处理成功！");
            resVo.setResultCode("0");
            resVo.setResultDesc("处理成功！");
        } else {
            result.setText("处理失败，" + message);
            resVo.setResultCode("1");
            resVo.setResultDesc("处理失败！" + message);
        }

        QryPatiInfoRegResSubject subject = new QryPatiInfoRegResSubject();
        subject.setPatiInfoRegRes(resVo);
        result.setRegsub(subject);
        response.setResult(result);
        return XmlUtil.beanToXml(response, QryPatiInfoRegResponse.class);
    }

    /**
     * 取消入院登记
     *
     * @param reqXml
     * @return
     */
    public String cancelRegiAdmission(String reqXml) {
        String resXml = "";
        QryPatiInfoRegRequest request = (QryPatiInfoRegRequest) XmlUtil.XmlToBean(reqXml, QryPatiInfoRegRequest.class);
        if (request.getSubject() == null || request.getSubject().get(0) == null) {
            resXml = retRegiAdmissionMessage(request, "AE", null, "未获得请求数据！");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryPatiInfoRegReq reqVo = request.getSubject().get(0).getPatiInfoReqReqVo();
            String pkPv = request.getSubject().get(0).getPatiInfoReqReqVo().getPkPv();
            ApplicationUtils unit = new ApplicationUtils();
            User user = new User();
            Map<String, Object> deptPv = getDeptInfo(reqVo.getCodeDept());
            user.setPkDept(getPropValueStr(deptPv, "pkDept"));
            UserContext.setUser(user);
            PvEncounter pvent = new PvEncounter();
            pvent.setPkPv(pkPv);
            //String sql="select count(1) from pv_enounter where pk_pv=? and eu_status";
            ResponseJson resVo = unit.execService("PV", "AdtRegService", "deleteAdtReg", pvent, user);
            if (resVo.getStatus() == 0) {
                resXml = retRegiAdmissionMessage(request, "AA", null, null);
            } else {
                resXml = retRegiAdmissionMessage(request, "AE", null, resVo.getErrorMessage());
            }
        }
        return resXml;
    }

    /**
     * 出院结算接口
     *
     * @param reqXml
     * @return
     */
    public String noticeSettle(String reqXml) {
        String resXml = "";
        //TODO 根据传入xml解析请求参数
        QryAppSettleRequest request = new QryAppSettleRequest();
        request = (QryAppSettleRequest) XmlUtil.XmlToBean(reqXml, QryAppSettleRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().get(0) == null || request.getSubject().get(0).getReqVo() == null) {
            throw new BusException("未获取请求数据！");
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryAppSettleReqVo reqVo = request.getSubject().get(0).getReqVo();
            List<QryAppBlDeposit> depo = reqVo.getDeposit();

            Date dateEnd = DateUtils.strToDate(reqVo.getDateEnd(), "yyyy-MM-dd HH:mm:ss");
            reqVo.setDateEnd(DateUtils.dateToStr("yyyyMMddHHmmss", dateEnd));
            Date dateBegin = DateUtils.strToDate(reqVo.getDateBegin(), "yyyy-MM-dd HH:mm:ss");
            reqVo.setDateBegin(DateUtils.dateToStr("yyyyMMddHHmmss", dateBegin));
            //校验是否重复结算判断是否传入重复订单号和住院号, 校验传入的结算金额是否和实际需要结算的金额是否一致
            checkSettleAmount(reqVo, depo);

            //TODO 组织调用HIS结算接口的参数对象，进行调用
            SettleInfo settlevo = createSettlevo(reqVo, depo,request);
            ApplicationUtils unit = new ApplicationUtils();
            User user = new User();
            user.setPkOrg("a41476368e2943f48c32d0cb1179dab8");
            user.setPkDept("76E6AA37C47344E4A062B21786787F2C");//科室数据
            user.setPkEmp("7d7ab69d1e8749d6843ad4e67da133d7");//人员
            user.setCodeEmp("pinganapp");
            user.setNameEmp("平安APP");
            ResponseJson resvo = unit.execService("BL", "IpSettleGzgyService", "dealZcSettleData", settlevo, user);
            if (resvo.getStatus() < 0) {
                throw new BusException(resvo.getDesc());
            }

            // 保存bl_ext_pay
            List<BlExtPay> extList = new ArrayList<BlExtPay>();

            List<String> hisOrder = new ArrayList<String>();
            for (BlDeposit appdep : settlevo.getDepoList()) {
                BlExtPay extPay = new BlExtPay();
                extPay.setPkExtpay(NHISUUID.getKeyId());
                extPay.setPkOrg("a41476368e2943f48c32d0cb1179dab8");
                extPay.setAmount(appdep.getAmount());
                extPay.setEuPaytype("9");
                extPay.setDateAp(new Date());
                extPay.setFlagPay("1");
                extPay.setDatePay(new Date());
                extPay.setTradeNo(appdep.getPayInfo());
                String[] payInfo = appdep.getNote().split("\\|");
                if (payInfo != null && payInfo[0] != null) {
                    extPay.setSerialNo(payInfo[0]);
                }
                extPay.setDescPay(appdep.getNote());
                extPay.setSysname("平安APP");
                extPay.setPkPi(appdep.getPkPi());
                extPay.setPkPv(appdep.getPkPv());
                extPay.setPkDepo(appdep.getPkDepo());
                extPay.setEuBill("0");
                extPay.setCreateTime(new Date());
                extPay.setCreator(appdep.getPkEmpPay());
                extPay.setTs(new Date());
                hisOrder.add(appdep.getPkDepo());
                extList.add(extPay);
            }

            DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlExtPay.class), extList);
            resXml = retSettleMessage(request, "AA", null, hisOrder);
        }
        return resXml;
    }

    /**
     * 检验结算接口数据中金额与his/医保返回的金额是否相同
     *
     * @param reqVo
     * @param depo
     */
    private void checkSettleAmount(QryAppSettleReqVo reqVo, List<QryAppBlDeposit> depoList) throws BusException {
        //1.校验是否重复结算判断是否传入重复订单号和住院号
        int count = 0;
        for (QryAppBlDeposit depo : depoList) {
            StringBuffer depoSql = new StringBuffer();
            depoSql.append("select count(1) from BL_DEPOSIT de");
            depoSql.append(" inner join PI_MASTER pi on de.PK_PI = pi.PK_PI");
            depoSql.append(" where pi.CODE_IP = ?");
            depoSql.append(" and PAY_INFO = ?");
            depoSql.append(" and DT_PAYMODE = ?");
            depoSql.append("  and de.AMOUNT=?");
            count += DataBaseHelper.queryForScalar(depoSql.toString(), Integer.class, new Object[]{reqVo.getCodeIp(), depo.getPayNum(), depo.getPayMode(), depo.getPayAmount()});
        }

        if (count > 0) {
            //已经存在结算记录，直接返回提示信息
            throw new BusException("本次结算包含已经存在的结算信息，请检查！");
        } else {
            //2.校验传入的结算金额是否和实际需要结算的金额是否一致
            //2.1 先确定本次结算的患者是内部医保还是外部医保
            ApplicationUtils unit = new ApplicationUtils();
            String pvSql = "select PK_INSU from PV_ENCOUNTER  where pk_pv=?";
            Map<String, Object> hpMap = DataBaseHelper.queryForMap(pvSql, new Object[]{reqVo.getPkPv()});
            int hpCount = 0;
            if (hpMap != null && hpMap.get("pkInsu") != null) {
                String hpSql = "select count(1) from BD_HP where  PK_HP=? and DT_EXTHP is not null and (del_flag='0' or del_flag is null)";
                hpCount = DataBaseHelper.queryForScalar(hpSql, Integer.class, new Object[]{hpMap.get("pkInsu")});
            }
            //hpCount>0 为外部医保，否则内部医保
            if (hpCount > 0) {
                //获取预缴金数据 未参与结算
                String prepSql = "select sum(amount) amount_prep from BL_DEPOSIT where EU_DPTYPE = '9' and FLAG_SETTLE = '0' and pk_pv = ?";
                Map<String, Object> prepMap = DataBaseHelper.queryForMap(prepSql, new Object[]{reqVo.getPkPv()});
                BigDecimal amtPrep = new BigDecimal(0.0);
                if (prepMap != null) {
                    amtPrep = BigDecimal.valueOf(CommonUtils.getDouble(prepMap.get("amountPrep")));
                }
                StringBuffer amtHpSql = new StringBuffer();
                amtHpSql.append("select * from (select row_number() over(partition by PK_PV order by ts desc) cnt ,amtst amount_st,AMTINSU amount_insu,AMTPI  amount_pi ,ts");
                amtHpSql.append(" from INS_GZYB_TRIALST where PK_PV=?) res where  res.cnt=1");
                Map<String, Object> amthHMap = DataBaseHelper.queryForMap(amtHpSql.toString(), new Object[]{reqVo.getPkPv()});
                if (amthHMap != null) {
                    BigDecimal amtSt = BigDecimal.valueOf(CommonUtils.getDouble(amthHMap.get("amountSt")));
                    BigDecimal amtPi = BigDecimal.valueOf(CommonUtils.getDouble(amthHMap.get("amountPi")));
                    BigDecimal amtInsu = BigDecimal.valueOf(CommonUtils.getDouble(amthHMap.get("amountInsu")));
                    if (amtSt.compareTo(reqVo.getAmountSt()) != 0) {
                        throw new BusException("传入结算金额与医保返回结算金额不符，请确认！");
                    } else if (amtPi.compareTo(reqVo.getAmountPi()) != 0) {
                        throw new BusException("传入患者自付金额与医保返回患者自付金额不符，请确认！");
                    } else if (amtInsu.compareTo(reqVo.getAmountInsu()) != 0) {
                        throw new BusException("传入医保支付金额与医保返回医保支付金额不符，请确认！");
                    } else if (amtPrep.compareTo(reqVo.getAmountPrep()) != 0) {
                        throw new BusException("传入患者未结算预缴金金额与HIS未参与结算的预缴金金额不符，请确认！");
                    }
                } else {//未查询到医保预结算金额

                }
            } else {
                ResponseJson resVo = unit.execService("BL", "IpSettleGzgyService", "getAmtInfo", reqVo, new User());
                if (resVo != null && resVo.getData() != null) {
                    String resParam = JsonUtil.writeValueAsString(resVo);
                    JsonNode resNode = JsonUtil.getJsonNode(resParam, "data");
                    Map<String, Object> hisAmtMap = JsonUtil.readValue(resNode, Map.class);
                    BigDecimal amtSt = BigDecimal.valueOf(CommonUtils.getDouble(hisAmtMap.get("AmtSt")));
                    BigDecimal amtPi = BigDecimal.valueOf(CommonUtils.getDouble(hisAmtMap.get("AmtPi")));
                    BigDecimal amtInsu = BigDecimal.valueOf(CommonUtils.getDouble(hisAmtMap.get("AmtInsu")));
                    BigDecimal amtPrep = BigDecimal.valueOf(CommonUtils.getDouble(hisAmtMap.get("AmtPrep")));
					/*if(amtSt.compareTo(reqVo.getAmountSt())!=0){
						throw new Exception("传入结算金额与HIS返回结算金额不符，请确认！");
					}else if(amtPi.compareTo(reqVo.getAmountPi())!=0){
						throw new Exception("传入患者自付金额与HIS返回患者自付金额不符，请确认！");
					}else if(amtInsu.compareTo(reqVo.getAmountInsu())!=0){
						throw new Exception("传入医保支付金额与HIS返回医保支付金额不符，请确认！");
					}else if(amtPrep.compareTo(reqVo.getAmountPrep())!=0){
						throw new Exception("传入患者未结算预缴金金额与HIS未参与结算的预缴金金额不符，请确认！");
					}*/
                } else {
                    throw new BusException("未获取HIS结算返回数据！");
                }
            }
        }
    }

    /**
     * 组建结算接口参数
     *
     * @param reqVo
     * @param depos
     * @param request
     * @return
     */
    private SettleInfo createSettlevo(QryAppSettleReqVo reqVo, List<QryAppBlDeposit> depos, QryAppSettleRequest request) {
        //组织调用HIS结算接口的参数对象，进行调用
        SettleInfo settleVo = new SettleInfo();
        settleVo.setPkPv(reqVo.getPkPv());
        settleVo.setEuSttype(reqVo.getEuSttype());
        settleVo.setEuStresult(reqVo.getEuStresult());
        settleVo.setDateEnd(reqVo.getDateEnd());
        settleVo.setDateBegin(reqVo.getDateBegin());
        settleVo.setAmountPrep(reqVo.getAmountPrep());
        settleVo.setAmountSt(reqVo.getAmountSt());
        settleVo.setAmountPi(reqVo.getAmountPi());
        settleVo.setAmountInsu(reqVo.getAmountInsu());
        settleVo.setPkDeptSt(reqVo.getPkDeptSt());
        settleVo.setPkEmpSt(reqVo.getPkEmpSt());
        settleVo.setNameEmpSt(reqVo.getNameEmpSt());

        List<BlDeposit> depoList = new ArrayList<BlDeposit>();
        String piSql = "select pk_pi from pv_encounter where pk_pv=?";
        List<Map<String, Object>> piMapList = DataBaseHelper.queryForList(piSql, new Object[]{reqVo.getPkPv()});

        for (QryAppBlDeposit blde : depos) {
            BlDeposit depovo = new BlDeposit();
            depovo.setPkDepo(NHISUUID.getKeyId());
            depovo.setPkOrg("a41476368e2943f48c32d0cb1179dab8");
            depovo.setEuDptype("0");
            depovo.setPkPi(piMapList.get(0).get("pkPi").toString());
            depovo.setPkPv(reqVo.getPkPv());
            depovo.setAmount(BigDecimal.valueOf(blde.getPayAmount()));
            depovo.setDtPaymode("59");
            depovo.setBankNo(blde.getAgtCode());
            depovo.setPayInfo(blde.getPayNum());
            depovo.setDatePay(blde.getPayTime());
            depovo.setEuDirect("1");
            String orderDesc = "请到住院收费处补打收据!";
//            //查询110105码表信息
//            Map<String, Object> docMap = DataBaseHelper.queryForMap(
//                    "select name,val_attr from bd_defdoc where code_defdoclist = '110105' and code = ?"
//                    , new Object[]{blde.getPaySource()});
//            Map<String, Object> deptMap = new HashMap<>();
//            Map<String, Object> empMap = new HashMap<>();
//            if (docMap != null && docMap.size() > 0) {
//                String[] codeArray = CommonUtils.getString(docMap.get("valAttr")).split("\\|");
//                //查询收款部门信息
//                deptMap = DataBaseHelper.queryForMap(
//                        "select pk_dept from bd_ou_dept where code_dept = ?"
//                        , new Object[]{codeArray[0].trim()});
//                //查询收费员信息
//                empMap = DataBaseHelper.queryForMap(
//                        "select pk_emp,name_emp from bd_ou_employee where code_emp = ?"
//                        , new Object[]{codeArray[1].trim()});
//                //备注
//                depovo.setNote(blde.getOrderId() + "|" + blde.getPayNum() + "|" + CommonUtils.getString(docMap.get("name")) + "|" + blde.getAgtOrdNum() + " " + orderDesc);
//            } else {
                //备注
                depovo.setNote(blde.getOrderId() + "|" + blde.getPayNum() + "|" + blde.getPaySource() + "|" + blde.getAgtOrdNum() + " " + orderDesc);
//            }
//            if (deptMap != null && deptMap.size() > 0) {
                depovo.setPkDept("76E6AA37C47344E4A062B21786787F2C");
//            } else {
//                depovo.setPkDept("-");
//            }

            Map<String, Object> empMap = new HashMap<>();
            if (request.getSender().getSenderId() != null && !CommonUtils.isEmptyString(request.getSender().getSenderId())) {
                empMap = DataBaseHelper.queryForMap("select pk_emp,name_emp from bd_ou_employee where code_emp = ? and flag_active='1' and del_flag='0'", new Object[]{request.getSender().getSenderId().trim()});
            }

            if (empMap != null && empMap.size() > 0) {
                depovo.setPkEmpPay(CommonUtils.getString(empMap.get("pkEmp")));
                depovo.setNameEmpPay(CommonUtils.getString(empMap.get("nameEmp")));
            } else {
                depovo.setPkEmpPay("-");
                depovo.setNameEmpPay("-");
            }

            depovo.setFlagAcc("0");
            depovo.setFlagSettle("1");
            depovo.setFlagCc("0");
            depoList.add(depovo);
        }
        settleVo.setDepoList(depoList);
        return settleVo;
    }

    /***
     * 构建结算返回信息
     * @param request
     * @param status
     * @param message
     * @param hisOrderId
     * @return
     */
    private String retSettleMessage(QryAppSettleRequest request, String status, String message, List<String> hisOrderId) {
        QryAppSettleResponse response = new QryAppSettleResponse();
        if (request == null) request = new QryAppSettleRequest();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());

        QryappSettleResult result = new QryappSettleResult();
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());
        QryAppSettleResSubject subject = new QryAppSettleResSubject();

        QryAppSettleResVo resVo = new QryAppSettleResVo();
        if ("AA".equals(status)) {
            result.setId("AA");
            result.setText("处理成功！");
            resVo.setResultCode("0");
            resVo.setResultDesc("结算成功！");
        } else {
            result.setId("AE");
            result.setText("处理失败！" + message);
            resVo.setResultCode("1");
            resVo.setResultDesc("结算失败!" + message);
        }
        resVo.setOrderIdHis(hisOrderId);
        subject.setResVo(resVo);
        result.setSubject(subject);
        response.setResult(result);

        return XmlUtil.beanToXml(response, QryAppSettleResponse.class);
    }

    /**
     * 获取字典信息
     *
     * @param reqXml
     * @return
     */
    public String getDictInfo(String reqXml) {
        QryAppDictRequest request = (QryAppDictRequest) XmlUtil.XmlToBean(reqXml, QryAppDictRequest.class);
        String resXml = "";
        if (request == null || request.getSubject() == null || request.getSubject().get(0) == null || request.getSubject().get(0).getReqVo() == null) {
            return retDictInfoMessage(request, "AE", null, "未获取请求数据！");
        }
        SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
        String codeSource = request.getSubject().get(0).getReqVo().getCodeSource();
        List<QryAppItem> itemList = new ArrayList<QryAppItem>();
        try {
            switch (codeSource) {
                case "A00000"://医保计划编码
                    itemList = getHpInfo();
                    break;
                case "A00001"://行政区划数据
                    itemList = getDivsion();
                    break;
                case "A00002"://籍贯区域编码
                    itemList = getDivsionBir();
                    break;
                case "A00003"://入院诊断编码
                    itemList = getDiagInfo();
                    break;
                case "A00004"://获取科室数据
                    itemList = getDeptInfos();
                    break;
                case "000000":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000003":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000006":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000007":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000009":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000010":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000013":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000014":
                    itemList = getDictDefInfo(codeSource);
                    break;
                case "000202":
                    itemList = getDictDefInfo(codeSource);
                    break;
                default:
                    resXml = retDictInfoMessage(request, "AE", null, "传入字典编码有误！");
                    return resXml;
            }
            resXml = retDictInfoMessage(request, "AA", itemList, "");
        } catch (Exception e) {
            resXml = retDictInfoMessage(request, "AE", null, e.getMessage());
        }
        return resXml;
    }

    /**
     * 构建字典返回消息
     *
     * @param request
     * @param status
     * @param itemList
     * @param message
     * @return
     */
    private String retDictInfoMessage(QryAppDictRequest request, String status, List<QryAppItem> itemList, String message) {
        QryAppDictResponse response = new QryAppDictResponse();
        if (request == null) request = new QryAppDictRequest();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());

        QryAppDictResult result = new QryAppDictResult();
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());
        QryAppDictResSubject subject = new QryAppDictResSubject();
        if ("AA".equals(status)) {
            result.setId("AA");
            result.setText("处理成功！");
        } else {
            result.setId("AE");
            result.setText("处理失败！" + message);
        }
        QryAppDictResVo resVo = new QryAppDictResVo();
        resVo.setItemList(itemList);
        subject.setResVo(resVo);
        result.setSubject(subject);
        response.setResult(result);

        return XmlUtil.beanToXml(response, QryAppDictResponse.class);
    }


    /**
     * 3.13	 查询出院结算信息接口（新增）
     *
     * @param reqXml
     * @return
     */
    public String querySettleAccounts(String reqXml) {
        String resXml = "";
        QryAppSettleRequest request = (QryAppSettleRequest) XmlUtil.XmlToBean(reqXml, QryAppSettleRequest.class);
        if (request == null || request.getSubject() == null || request.getSubject().get(0) == null || request.getSubject().get(0).getReqVo() == null) {
            resXml = retSettleAccounts(request, "AE", null, "未获得请求消息");
            return resXml;
        } else {
            SetUserUtils.setUserByEmpCode(request.getSender().getSenderId());
            QryAppSettleReqVo reqVo = request.getSubject().get(0).getReqVo();
            if (CommonUtils.isEmptyString(reqVo.getPkPv())) {
                resXml = retSettleAccounts(request, "AE", null, "未获得患者就诊主键");
            } else if (CommonUtils.isEmptyString(reqVo.getCodeIp())) {
                resXml = retSettleAccounts(request, "AE", null, "未获得患者住院号");
            } else if (CommonUtils.isEmptyString(reqVo.getUserCardId())) {
                resXml = retSettleAccounts(request, "AE", null, "未获得患者身份证号");
            } else {
                QryAppSettleResVo appSettleResVo = platformAppMapper.getSettleAccounts(reqVo);
                if (appSettleResVo == null) {
                    resXml = retSettleAccounts(request, "AE", null, "未查到对应数据");
                } else {
                    resXml = retSettleAccounts(request, "AA", appSettleResVo, null);
                }
            }
        }
        return resXml;
    }

    /**
     * 构建出院信息接口
     *
     * @param request
     * @param retType
     * @param appSettle
     * @param message
     * @return
     */
    private String retSettleAccounts(QryAppSettleRequest request, String retType, QryAppSettleResVo appSettle, String message) {
        QryAppSettleResponse response = new QryAppSettleResponse();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(request.getActionId());
        response.setActionName(request.getActionName());
        QryappSettleResult result = new QryappSettleResult();
        QryAppSettleResSubject AppSettleSub = new QryAppSettleResSubject();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
        } else {
            result.setText("处理失败！" + message);
        }
        result.setRequestId(request.getId());
        result.setRequestTime(request.getCreateTime());
        AppSettleSub.setResVo(appSettle);
        result.setSubject(AppSettleSub);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryAppSettleResponse.class);
        return resXml;
    }

    /**
     * 获取医保数据
     *
     * @return
     */
    private List<QryAppItem> getHpInfo() {
        String sql = "select pk_hp pk, pk_parent pk_father, code ,  name from BD_HP";
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql, new Object[]{});
        return convertListMapToListBean(itemList);
    }

    /**
     * 获取行政区划
     *
     * @return
     */
    private List<QryAppItem> getDivsion() {
        String sql = "select   pk_division pk,  code_div as code,   name_div as name,  pk_Father,    prov, city, dist from BD_ADMIN_DIVISION ";
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql, new Object[]{});
        return convertListMapToListBean(itemList);
    }

    /**
     * 获取籍贯区域信息
     *
     * @return
     */
    private List<QryAppItem> getDivsionBir() {
        StringBuffer sql = new StringBuffer();
        sql.append("select div.pk_division pk,div.code_div code,div.name_div name,divi.pk_division,divi.code_div ,divi.name_div");
        sql.append(" from BD_ADMIN_DIVISION div inner join BD_ADMIN_DIVISION divi on div.pk_division=divi.pk_Father where div.pk_Father is null");
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql.toString(), new Object[]{});
        return convertListMapToListBean(itemList);
    }

    /**
     * 获取诊断数据
     *
     * @return
     */
    private List<QryAppItem> getDiagInfo() {
        String sql = "select pk_cndiag pk,code_icd code,name_cd name from bd_cndiag";
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql, new Object[]{});
        return convertListMapToListBean(itemList);
    }

    /**
     * 获取公共字典数据
     *
     * @param codeDefList
     * @return
     */
    private List<QryAppItem> getDictDefInfo(String codeDefList) {
        String sql = "select code, name from bd_defdoc  where code_defdoclist=?";
        List<Map<String, Object>> itemList = DataBaseHelper.queryForList(sql, new Object[]{codeDefList});
        return convertListMapToListBean(itemList);
    }

    /**
     * 获取科室数据
     *
     * @param codeDefList
     * @return
     */
    private List<QryAppItem> getDeptInfos() {

        List<QryAppItem> itemList = platformAppMapper.getDeptInfos();

        return itemList;
    }

    /**
     * List<Map>转list<bean>
     *
     * @param listMap
     * @return
     */
    private List<QryAppItem> convertListMapToListBean(List<Map<String, Object>> listMap) {
        List<QryAppItem> itemList = new ArrayList<QryAppItem>();
        try {
            for (Map<String, Object> map : listMap) {
                QryAppItem item = ApplicationUtils.mapToBean(map, QryAppItem.class);
                itemList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }


    /**
     * 查询科室信息
     *
     * @param codeType
     * @param code
     * @return
     */
    private Map<String, Object> getDeptInfo(String code) {
        String sql = "select pk_dept,name_dept from bd_ou_dept where code_dept=?";
        Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{code});
        if (resMap == null) resMap = new HashMap<String, Object>();
        return resMap;
    }

    /**
     * map获取对应key的value
     *
     * @param map
     * @param key
     * @return
     */
    private String getPropValueStr(Map<String, Object> map, String key) {
        if (map == null) return "";
        String value = "";
        if (map.containsKey(key)) {
            Object obj = map.get(key);
            value = obj == null ? "" : obj.toString();
        }
        return value;
    }

    /**
     * 根据标准诊断ICD编码获取诊断数据
     *
     * @param codeDiag
     * @return
     */
    private Map<String, Object> getDiagInfo(String codeDiag) {
        String sql = "select pk_cndiag pk_diag,name_cd from bd_cndiag where code_icd=?";
        Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{codeDiag});
        if (resMap == null) resMap = new HashMap<String, Object>();
        return resMap;
    }

    /**
     * 根据人员编码获取人员信息
     *
     * @param codeEmp
     * @return
     */
    private Map<String, Object> getEmpInfo(String codeEmp) {
        String sql = "select pk_emp,name_emp from bd_ou_employee where code_emp=?";
        Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{codeEmp});
        if (resMap == null) resMap = new HashMap<String, Object>();
        return resMap;
    }

    /**
     * 获取行政区划名称
     *
     * @param code
     * @return
     */
    private String getAddresName(String code) {
        if (CommonUtils.isEmptyString(code)) {
            return "";
        }
        String sql = "select name_div from bd_admin_division where code_div=?";
        Map<String, Object> resMap = DataBaseHelper.queryForMap(sql, new Object[]{code});
        if (resMap == null || resMap.get("nameDiv") == null) {
            return "";
        } else {
            return resMap.get("nameDiv").toString();
        }
    }

    /**
     * 3.15	获取HIS系统对帐信息接口
     *
     * @param message
     * @return
     */
    public String getinpatientchecklistInfo(String reqXml) {

        String resXml = "";
        QryChecklistInfoReqVo reqCheckInfo = (QryChecklistInfoReqVo) XmlUtil.XmlToBean(reqXml, QryChecklistInfoReqVo.class);
        if (reqCheckInfo.getSubject() == null) {
            resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未获得请求消息");
            return resXml;
        }
        SetUserUtils.setUserByEmpCode(reqCheckInfo.getSender().getSenderId());
        QryCheckListInfo chkInfo = reqCheckInfo.getSubject().get(0).getCheckListInfo();
        if (!CommonUtils.isEmptyString(chkInfo.getType())) {
            if (chkInfo.getType().equals("1")) {
                chkInfo.setType("9");
            }
        }
        if (CommonUtils.isEmptyString(chkInfo.getPaySource())) {
            resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未获得付款来源");
        } else if (CommonUtils.isEmptyString(chkInfo.getPayMode())) {
            resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未获得支付方式");
        } else if (CommonUtils.isEmptyString(chkInfo.getStartDate())) {
            resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未获得开始日期");
        } else if (CommonUtils.isEmptyString(chkInfo.getEndDate())) {
            resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未获得结束日期");
        } else {
            List<QryChecklistInfoResData> resDatas = platformAppMapper.getChkInfoList(chkInfo);
            if (resDatas == null || resDatas.size() == 0) {
                resXml = retChecklistMessage(reqCheckInfo, "AE", null, "未查到对应数据");
            } else {
                resXml = retChecklistMessage(reqCheckInfo, "AA", resDatas, null);
            }
        }
        return resXml;
    }

    /**
     * 系统对帐信息接口返回
     *
     * @param reqpiInfo
     * @param retType
     * @param resUserInfos
     * @param message
     * @return
     */
    private String retChecklistMessage(QryChecklistInfoReqVo reqCheckInfo, String retType, List<QryChecklistInfoResData> checklistInfos, String message) {
        QryChecklistInfoExt response = new QryChecklistInfoExt();
        response.setId(NHISUUID.getKeyId());
        response.setCreateTime(sdf.format(new Date()));
        response.setActionId(reqCheckInfo.getActionId());
        response.setActionName(reqCheckInfo.getActionName());
        QryChecklistInfoResult result = new QryChecklistInfoResult();
        QryChecklistInfoResSub checklistInfo = new QryChecklistInfoResSub();
        result.setId(retType);
        if ("AA".equals(retType)) {
            result.setText("处理成功！");
        } else {
            result.setText("处理失败！" + message);
        }
        result.setRequestId(reqCheckInfo.getId());
        result.setRequestTime(reqCheckInfo.getCreateTime());
        checklistInfo.setData(checklistInfos);
        result.setListInfoSub(checklistInfo);
        response.setResult(result);
        String resXml = XmlUtil.beanToXml(response, QryChecklistInfoExt.class);
        return resXml;
    }

    /**
     * 更新绑定状态接口
     *
     * @param message
     * @return
     */
    public String Updatebind(String message) {
        String resXml = "";
        BindInfoRedSub bindInfoReq = (BindInfoRedSub) XmlUtil.XmlToBean(message, BindInfoRedSub.class);
        if (bindInfoReq.getSubject() == null) {
            resXml = XmlUtil.beanToXml(PfWsUtils.constructErrResBean(resXml, "未获得请求消息"), PlatFormResErr.class);
            return resXml;
        }
        SetUserUtils.setUserByEmpCode(bindInfoReq.getSender().getSenderId());
        BindInfoVo bindInfoVo = bindInfoReq.getSubject().get(0);
        if (!StringUtils.isEmpty(bindInfoReq.getSubject().get(0).getOperType())) {
            if ("1".equals(bindInfoReq.getSubject().get(0).getOperType())) {
                Integer count = DataBaseHelper.queryForScalar("select count(1) from pi_master pi inner join pi_bind bind on pi.pk_pi=bind.pk_pi where pi.code_ip=? ", Integer.class, bindInfoVo.getIpSeqnoText());
                if (count != null && count > 0) {
                    int update = DataBaseHelper.update("update PI_BIND bind set FLAG_VALID='1' where PK_PI in (select PK_PI from PI_MASTER where CODE_IP=?)", new Object[]{bindInfoVo.getIpSeqnoText()});
                    if (update <= 0) {
                        throw new BusException("绑定失败，未获取到绑定信息");
                    }
                } else {
                    PiMaster piMaster = DataBaseHelper.queryForBean("select pk_pi from pi_master where code_ip=?", PiMaster.class, bindInfoVo.getIpSeqnoText());
                    PiBind piBind = new PiBind();
                    piBind.setPkPi(piMaster.getPkPi());
                    piBind.setLogon(bindInfoVo.getLogon());
                    piBind.setPassword(bindInfoVo.getPassword());
                    piBind.setDateRegistre(DateUtils.strToDate(bindInfoVo.getRegisterDate(),"yyyy-MM-dd HH:mm:ss"));
                    piBind.setDtPlatform(bindInfoVo.getDtPplatform());
                    piBind.setCreator(bindInfoVo.getCreator());
                    piBind.setFlagValid("1");
                    DataBaseHelper.insertBean(piBind);
                }
            } else if("2".equals(bindInfoReq.getSubject().get(0).getOperType())){
                int update = DataBaseHelper.update("update PI_BIND bind set FLAG_VALID='0',modifier=?,modity_time=? where PK_PI in (select PK_PI from PI_MASTER where CODE_IP=?)", new Object[]{bindInfoVo.getIpSeqnoText(), null, null});
                if(update>0){
                    return XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(bindInfoReq, "解除绑定成功！", "0"), PlatFormResSucc.class);
                }
            }else{
                throw new BusException("未获取到operType字段传值错误！");
            }
        }else {
            throw new BusException("未获取到operType字段值！");
        }
        resXml = XmlUtil.beanToXml(PfWsUtils.constructSuccResBean(bindInfoReq, "绑定成功！", "0"), PlatFormResSucc.class);
        return resXml ;
    }

}
