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
            logger.error("?????????????????????????????????????????????-???????????????",e);
            throw new BusException("???????????????"+ ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }


    //~~~~~~~~~~~~~~????????????????????????~~~~~~~~~~~~~~~~
    /****************??????*************/
    /**
     * ??????????????????
     * @param args
     * @return
     */
    public Map<String, Object> MXBAudit(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(args[1], User.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.opPrescription(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("????????????????????????");
        }
        //??????patientInfoVo??????
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
        patientInfoVo.setType("1");//1??????????????????
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
        //????????????
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
        if(!MapUtils.getString(verMap,"code").equals("0")) throw new BusException("????????????");
        return verMap;
    }
    /**
     * ??????????????????
     * @param args
     * @return
     */
    public void mandatory(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        List<Map<String,Object>> sendlist = medicalInsuranceCostsMapper.opInterfaceInfo(param);
        Map<String,Object> msgMap = sendlist.get(0);
        msgMap.put("remarks","??????????????????");
        String resp =this.sendMsg(url+"/MXB/mandatory", msgMap);
        logger.info(resp);
        Map<String, Object> verMap = JsonUtil.readValue(resp,Map.class);
        if(!MapUtils.getString(verMap,"code").equals("0")) throw new BusException("????????????????????????");
    }
    /**
     * ????????????
     * @param args
     * @return
     */
    public void undoaudit(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        List<Map<String,Object>> sendlist = medicalInsuranceCostsMapper.opInterfaceInfo(param);
        for (Map<String, Object> map : sendlist) {
            Map<String, Object> resultMap = new HashMap<>();
            map.put("remarks", "??????????????????");
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
     * ??????????????????
     * @param args
     * @return
     */
    public void clibalance(Object[] args){
        Map<String, Object> param = JsonUtil.readValue(args[0].toString(),Map.class);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        //????????????inputdata??????
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
        msgMap.put("redeemMoney","0");//????????????
        msgMap.put("settlementno","0");//??????????????????
        String resp =this.sendMsg(url+"/MXB/clibalance", msgMap);
        logger.info(msgMap.toString());
        logger.info(resp);
        Map<String, Object> respMap = JsonUtil.readValue(resp,Map.class);
        if (!MapUtils.getString(respMap,"code").equals("0")) throw new BusException("????????????????????????");
    }
    /**
     * ????????????
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

    /**********************??????***************************/

    /**
     * ????????????????????????????????????
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
            throw new BusException("?????????????????????");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.adviceAudit(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("????????????????????????");
        }
        //??????patientInfoVo??????
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
        patientInfoVo.setType("1");//1??????????????????
        patientInfoVo.setDoctorCode(user.getCodeEmp());
        patientInfoVo.setDoctorName(user.getNameEmp());
        patientInfoVo.setTreatCode("-");
        BigDecimal totalCosts = new BigDecimal(0);

        for (MedicalAdviceVo medicalAdviceVo : paramList) {
            BigDecimal money = medicalAdviceVo.getMoney();
            totalCosts = totalCosts.add(money);
        }
        patientInfoVo.setTotalCosts(totalCosts);
        //??????patientInfoVo??????Inputdata
        patientInfoVo.setInputdata(paramList);
        //????????????
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
     * ??????????????????????????????
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
            throw new BusException("??????????????????");
        }

    }

    /**
     * ????????????????????????
     * @param args
     */
    public void adviceCancel(Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        param.put("inpatientRxno",args[1]);
        String resp  = sendAdviceSaveCancelMsg(param,url+"/Audit/AdviceCancel");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("??????????????????");
        }
    }

    /**
     * ????????????????????????
     * @param param
     * @param path
     */
    public String sendAdviceSaveCancelMsg(Map<String,Object> param, String path){

        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        List<AdviceSaveCancelVo> adviceSaveCancelVoList = medicalInsuranceCostsMapper.adviceSaveCancel(param);
        if (adviceSaveCancelVoList == null || adviceSaveCancelVoList.size() == 0) {
            throw new BusException("??????????????????????????????????????????????????????");
        }
        AdviceSaveCancelVo adviceSaveCancelVo = adviceSaveCancelVoList.get(0);
        //????????????
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
     *????????????????????????????????????
     */
    public void inpDetailInput(Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        //???db????????????
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.adviceAudit(param);
        if (patientInfoVos == null || patientInfoVos.size()==0) {
            throw new BusException("??????????????????????????????????????????");
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
        patientInfoVo.setType("3");//????????????
        //??????patientInfoVo??????Inputdata
        List<MedicalAdviceVo> medicalAdviceList = medicalInsuranceCostsMapper.getPatientInfoVo(param);
        if (medicalAdviceList == null || medicalAdviceList.size()==0) {
            throw new BusException("????????????????????????????????????");
        }

        patientInfoVo.setTotalCosts(medicalAdviceList.get(0).getMoney());
        patientInfoVo.setInputdata(medicalAdviceList);
        if (patientInfoVo.getTotalCosts() == null ){
            patientInfoVo.setTotalCosts(new BigDecimal("0"));
        }
        if(patientInfoVo.getLeaveDate() == null){
            patientInfoVo.setLeaveDate(new Date());
        }
        //????????????
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
            throw new BusException("??????????????????");
        }
    }

    /**
     * ??????????????????????????????
     */
    public void inpCancelFee (Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String resp = sendMessageByVisitno(param,url+"/Audit/InpCancelFee");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        if (verMap.get("code") == null || verMap.equals("") || !verMap.get("code").equals(0)){
            throw new BusException("??????????????????");
        }

    }

    /**
     *????????????????????????
     */
    public Map<String, Object> freeAudit (Object[] args){
        Map<String,Object> param=new HashMap<>();
        param.put("pkPv",args[0]);
        String resp = sendMessageByVisitno(param,url+"/Audit/FreeAudit");
        Map<String, Object> verMap = (Map<String, Object>) JSONObject.parse(resp);
        return verMap;
    }

    /**
     *??????????????????????????????????????????
     *
     */
    public String sendMessageByVisitno(Map<String,Object> param,String path){
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)){
            throw new BusException("?????????????????????");
        }
        List<PatientInfoVo> patientInfoVos = medicalInsuranceCostsMapper.getInpCancelFee(param);
        if(patientInfoVos==null||patientInfoVos.size()==0){
            throw new BusException("?????????????????????????????????");
        }
        PatientInfoVo patientInfoVo = patientInfoVos.get(0);

        //????????????
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
