package com.zebone.nhis.ma.pub.lb.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.SM4Util;
import com.zebone.nhis.ma.pub.lb.dao.MedicalInsuranceCostsMapper;
import com.zebone.nhis.ma.pub.lb.vo.*;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

@Component
public class MedicalInsuranceCostsHandler {
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");

    @Autowired
    private MedicalInsuranceCostsMapper medicalInsuranceCostsMapper;


    @Value("#{applicationProperties['ext.medicalInsurance.url']}")
    private String url;
    @Value("#{applicationProperties['ext.medicalInsurance.appKey']}")
    private String appKey;
    @Value("#{applicationProperties['ext.medicalInsurance.orgId']}")
    private String orgId;
    @Value("#{applicationProperties['ext.medicalInsurance.appSecret']}")
    private String appSecret;
    private final static String ENCTYPE = "SM4";


    public Object invokeMethod(String methodName, Object[] args) {
        Object result;
        try {

            Method method = ReflectUtils.findDeclaredMethod(this.getClass(), methodName, new Class[]{args==null?Object[].class:args.getClass()});
            result = method.invoke(this, new Object[]{args});
        } catch (Exception e) {
            logger.error("医保行为和医疗费用监管审核平台-其他异常：",e);
            throw new BusException("其他异常："+ ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }


    //~~~~~~~~~~~~~~对接外部接口开始~~~~~~~~~~~~~~~~
    /****************门诊*************/
    /**
     * 门诊处方审核
     * @param args
     * @return
     */
    public Map<String, Object> MXBAudit(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(args[1], User.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.opPrescription(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("未查询到病人信息");
        }
        //组装patientInfoVo信息
        PatientInfoVo patientInfoVo = new PatientInfoVo();
        patientInfoVo = patientInfoVos.get(0);
        String strPicd01 = "";
        String strPicd02 = "";
        for (PatientInfoVo patientInfo : patientInfoVos) {
            strPicd01 += patientInfo.getPicd01() + "$";
            strPicd02 += patientInfo.getPicd02() + "$";
        }
        patientInfoVo.setPicd01(strPicd01);
        patientInfoVo.setPicd02(strPicd02);
        patientInfoVo.setType("1");//1表示处方开立
        patientInfoVo.setDoctorCode(user.getCodeEmp());
        patientInfoVo.setDoctorName(user.getNameEmp());
        BigDecimal totalCosts = new BigDecimal(0);
        List<MedicalAdviceVo> medicalAdviceVos = medicalInsuranceCostsMapper.adviceInputData(param);
        for (MedicalAdviceVo medicalAdviceVo : medicalAdviceVos) {
            BigDecimal money = medicalAdviceVo.getMoney();
            totalCosts = totalCosts.add(money);
        }
        patientInfoVo.setTotalCosts(totalCosts);
        patientInfoVo.setInputdata(medicalAdviceVos);
        //最终数据
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(patientInfoVo)));

        logger.info(JSON.toJSONString(patientInfoVo));
        logger.info(JSON.toJSONString(endMsgVo));
        String resp = HttpClientUtil.sendHttpPostJson(url+"/MXB/MXBAudit",JSON.toJSONString(endMsgVo));
        logger.info(resp);
        Map<String, Object> verMap = JsonUtil.readValue(resp,Map.class);
        if(!MapUtils.getString(verMap,"code").equals("0")) throw new BusException("审核失败");
        return verMap;
    }
    /**
     * 强制结算申请
     * @param args
     * @return
     */
    public void mandatory(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        List<Map<String,Object>> sendlist = medicalInsuranceCostsMapper.opInterfaceInfo(param);
        Map<String,Object> msgMap = sendlist.get(0);
        msgMap.put("remarks","患者需要使用");
        String resp =this.sendMsg(url+"/MXB/mandatory", msgMap);
        logger.info(resp);
        Map<String, Object> verMap = JsonUtil.readValue(resp,Map.class);
        if(!MapUtils.getString(verMap,"code").equals("0")) throw new BusException("强制结算申请失败");
    }
    /**
     * 撤销处方
     * @param args
     * @return
     */
    public void undoaudit(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        List<Map<String,Object>> sendlist = medicalInsuranceCostsMapper.opInterfaceInfo(param);
        for (Map<String, Object> map : sendlist) {
            Map<String, Object> resultMap = new HashMap<>();
            map.put("remarks", "患者需要使用");
            Set<String> keyset = map.keySet();
            for (String key : keyset) {
                String newKey = key.toLowerCase();
                resultMap.put(newKey,map.get(key));
            }
            resultMap.put("Visitno",map.get("VISITNO"));
            resultMap.remove("visitno");
            String resp = this.sendMsg(url + "/MXB/undoaudit", resultMap);
            logger.info(resp);
        }
    }
    /**
     * 结算结果上报
     * @param args
     * @return
     */
    public void clibalance(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        //组装接口inputdata数据
        List<OpPrescriptionVo> opInputDatas = medicalInsuranceCostsMapper.opInputData(param);
        BigDecimal totalCosts = new BigDecimal(0);
        for (OpPrescriptionVo opPrescriptionVo : opInputDatas) {
            BigDecimal money = opPrescriptionVo.getMoney();
            totalCosts = totalCosts.add(money);
        }

        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.getInpCancelFee(param);
        Map<String,Object> msgMap = JSON.parseObject(JSON.toJSONString(patientInfoVos.get(0)), Map.class);
        msgMap.put("totalCosts",totalCosts);
        msgMap.put("inputdata",opInputDatas);
        msgMap.put("redeemMoney","0");//补偿金额
        msgMap.put("settlementno","0");//门诊补偿单号
        String resp =this.sendMsg(url+"/MXB/clibalance", msgMap);
        logger.info(msgMap.toString());
        logger.info(resp);
        Map<String, Object> respMap = JsonUtil.readValue(resp,Map.class);
        if (!MapUtils.getString(respMap,"code").equals("0")) throw new BusException("结算结果上报失败");
    }
    /**
     * 结算撤销
     * @param args
     * @return
     */
    public String cancelclibalance(Object[] args){

        return null;
    }

    private String sendMsg(String url, Map<String, Object> param){
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(param, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        })));

        logger.info(JSON.toJSONString(param, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        }));
        logger.info(JSON.toJSONString(endMsgVo));
        return  HttpClientUtil.sendHttpPostJson(url,JSON.toJSONString(endMsgVo));

    }

    /**********************住院***************************/

    /**
     * 住院医嘱上传审核接口方法
     *
     * @param args
     */
    public Map<String, Object> adviceAudit(Object[] args){
        List<MedicalAdviceVo> paramList = JsonUtil.readValue(args[0].toString(),new TypeReference<List<MedicalAdviceVo>>() {});
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(args[1], User.class);
        String resp = HttpClientUtil.sendHttpPostJson(url+"/Audit/AdviceAudit",this.sendReviewInfo(paramList,user));
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
//        VerifyVo verifyVo = (VerifyVo) com.alibaba.fastjson.JSONObject.parse(resp);
        return verMap;
    }
    private String sendReviewInfo(List<MedicalAdviceVo> paramList, User user){
        Map<String,Object> param = new HashMap<>();
        param.put("pkPv",paramList.get(0).getPkPv());
        param.put("visitno",paramList.get(0).getVisitno());
        param.put("inpatientRxno",paramList.get(0).getInpatientRxno());
        String visitno = MapUtils.getString(param, "visitno");
        if (StringUtils.isBlank(visitno)){
            throw new BusException("流水号不能为空");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.adviceAudit(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("未查询到病人信息");
        }
        //组装patientInfoVo信息
        PatientInfoVo patientInfoVo = new PatientInfoVo();
        patientInfoVo = patientInfoVos.get(0);
        String strPicd01 = "";
        String strPicd02 = "";
        for (PatientInfoVo patientInfo : patientInfoVos) {
            strPicd01 += patientInfo.getPicd01() + "$";
            strPicd02 += patientInfo.getPicd02() + "$";
        }
        patientInfoVo.setPicd01(strPicd01);
        patientInfoVo.setPicd02(strPicd02);
        patientInfoVo.setType("1");//1表示医嘱开立
        patientInfoVo.setDoctorCode(user.getCodeEmp());
        patientInfoVo.setDoctorName(user.getNameEmp());
        patientInfoVo.setTreatCode("-");
        BigDecimal totalCosts = new BigDecimal(0);

        for (MedicalAdviceVo medicalAdviceVo : paramList) {
            BigDecimal money = medicalAdviceVo.getMoney();
            totalCosts = totalCosts.add(money);
        }
        patientInfoVo.setTotalCosts(totalCosts);
        //封装patientInfoVo下的Inputdata
        patientInfoVo.setInputdata(paramList);
        //最终数据
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(patientInfoVo, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        })));

        logger.info(JSON.toJSONString(patientInfoVo, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        }));
        logger.info(JSON.toJSONString(endMsgVo));

        return JSON.toJSONString(endMsgVo);
    }


    /**
     * 住院医嘱保存接口方法
     * @param args
     */
    public void adviceSave(Object[] args){
        Map<String,Object> param=JsonUtil.readValue(args[0].toString(),new TypeReference<Map<String,Object>>() {});
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(param,(ValueFilter) (object, name, value) -> {
            if(value == null){
                return "0";
            }

            return value;
        })));

        logger.info(JSON.toJSONString(param));
        logger.info(JSON.toJSONString(endMsgVo));
        String resp  = HttpClientUtil.sendHttpPostJson(url+"/Audit/AdviceSave", JSON.toJSONString(endMsgVo));
        logger.info(resp);
        Map<String, Object> verMap = JsonUtil.readValue(resp,new TypeReference<Map<String,Object>>() {});
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("保存返回失败");
        }

    }

    /**
     * 医嘱撤销接口方法
     * @param args
     */
    public void adviceCancel(Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        param.put("inpatientRxno",args[1]);
        String resp  = sendAdviceSaveCancelMsg(param,url+"/Audit/AdviceCancel");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("撤销返回失败");
        }
    }

    /**
     * 发送住院医嘱数据
     * @param param
     * @param path
     */
    public String sendAdviceSaveCancelMsg(Map<String,Object> param, String path){

        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        List<AdviceSaveCancelVo> adviceSaveCancelVoList = medicalInsuranceCostsMapper.adviceSaveCancel(param);
        if (adviceSaveCancelVoList == null || adviceSaveCancelVoList.size() == 0) {
            throw new BusException("未查到撤销或保存数据，请先保存后再试");
        }
        AdviceSaveCancelVo adviceSaveCancelVo = adviceSaveCancelVoList.get(0);
        //最终数据
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(adviceSaveCancelVo,(ValueFilter) (object, name, value) -> {
            if(value == null){
                return "0";
            }

            return value;
        })));


        logger.info(JSON.toJSONString(adviceSaveCancelVo));
        logger.info(JSON.toJSONString(endMsgVo));
        String resp  = HttpClientUtil.sendHttpPostJson(path, JSON.toJSONString(endMsgVo));
        logger.info(resp);
        return resp;
    }


    /**
     *住院费用清单明细上传接口
     */
    public void inpDetailInput(Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        //从db查询数据
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.adviceAudit(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("未查询到费用清单上传审核数据");
        }
        PatientInfoVo patientInfoVo = new PatientInfoVo();
        patientInfoVo = patientInfoVos.get(0);
        String strPicd01 = "";
        String strPicd02 = "";
        for (PatientInfoVo patientInfo : patientInfoVos) {
            strPicd01 += patientInfo.getPicd01() + "$";
            strPicd02 += patientInfo.getPicd02() + "$";

        }

        patientInfoVo.setPicd01(strPicd01);
        patientInfoVo.setPicd02(strPicd02);
        patientInfoVo.setType("3");//出院结算
        //封装patientInfoVo下的Inputdata
        List<MedicalAdviceVo> medicalAdviceList = medicalInsuranceCostsMapper.getPatientInfoVo(param);
        if (medicalAdviceList == null || medicalAdviceList.size()==0) {
            throw new BusException("未查询到住院费用审核数据");
        }

        patientInfoVo.setTotalCosts(medicalAdviceList.get(0).getMoney());
        patientInfoVo.setInputdata(medicalAdviceList);
        if (patientInfoVo.getTotalCosts() == null ){
            patientInfoVo.setTotalCosts(new BigDecimal("0"));
        }
        if(patientInfoVo.getLeaveDate() == null){
            patientInfoVo.setLeaveDate(new Date());
        }
        //最终数据
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(patientInfoVo,(ValueFilter) (object, name, value) -> {
            if(value == null){
                return "0";
            }

            return value;
        })));


        logger.info(JSON.toJSONString(patientInfoVo,(ValueFilter) (object, name, value) -> {
            if(value == null){
                return "0";
            }
            return value;
        }));
        logger.info(JSON.toJSONString(endMsgVo));
        String resp = HttpClientUtil.sendHttpPostJson(url+"/Audit/InpDetailInput", JSON.toJSONString(endMsgVo));
        logger.info(resp);
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("上传返回失败");
        }
    }

    /**
     * 撤销住院费用明细接口
     */
    public void inpCancelFee (Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String resp = sendMessageByVisitno(param,url+"/Audit/InpCancelFee");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("撤销返回失败");
        }

    }

    /**
     *住院费用审核接口
     */
    public Map<String, Object> freeAudit (Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String resp = sendMessageByVisitno(param,url+"/Audit/FreeAudit");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        return verMap;
    }

    /**
     *根据流水号查询机构并发送消息
     *
     */
    public String sendMessageByVisitno(Map<String,Object> param,String path){
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("流水号不能为空");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.getInpCancelFee(param);
        if(patientInfoVos==null||patientInfoVos.size()==0){
            throw new BusException("未查询到撤销或审核数据");
        }
        PatientInfoVo patientInfoVo = patientInfoVos.get(0);

        //最终数据
        EndMsgVo endMsgVo = new EndMsgVo();
        endMsgVo.setAppKey(appKey);
        endMsgVo.setEncType(ENCTYPE);
        endMsgVo.setOrgId(orgId);
        endMsgVo.setSecretData(SM4Util.encryptEcb(appKey, JSON.toJSONString(patientInfoVo,(ValueFilter) (object, name, value) -> {
            if(value == null){
                return "0";
            }
            return value;
        })));


        logger.info(JSON.toJSONString(patientInfoVo));
        logger.info(JSON.toJSONString(endMsgVo));
        String resp  = HttpClientUtil.sendHttpPostJson(path, JSON.toJSONString(endMsgVo));
        logger.info(resp);
        return resp;
    }
}
