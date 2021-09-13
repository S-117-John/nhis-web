package com.zebone.nhis.ma.pub.lb.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.common.support.SM4Util;
import com.zebone.nhis.ma.pub.lb.dao.LbXFReferralMapper;
import com.zebone.nhis.ma.pub.lb.vo.HighValueHRPVo;
import com.zebone.nhis.ma.pub.lb.vo.referral.*;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LbXFReferralHandler {
    private static final Logger logger = LoggerFactory.getLogger("nhis.lbWebServiceLog");
    @Autowired
    private LbXFReferralMapper lbXFReferralMapper;
    @Value("#{applicationProperties['ext.XFReferral.appKey']}")
    private String appKey;
    @Value("#{applicationProperties['ext.XFReferral.clientId']}")
    private  String clientId;
    @Value("#{applicationProperties['ext.XFReferral.clientSecret']}")
    private  String clientSecret;
    @Value("#{applicationProperties['ext.XFReferral.grantType']}")
    private  String grantType;
    @Value("#{applicationProperties['ext.XFReferral.authUrl']}")
    private String authUrl;
    @Value("#{applicationProperties['ext.XFReferral.visitUrl']}")
    private  String visitUrl;
    @Value("#{applicationProperties['ext.XFReferral.leaveUrl']}")
    private  String leaveUrl;

    public Object invokeMethod(String methodName, Object[] args) {
        Object result;
        try {
            Method method = ReflectUtils.findDeclaredMethod(this.getClass(), methodName, new Class[]{args==null?Object[].class:args.getClass()});
            result = method.invoke(this, new Object[]{args});
        } catch (Exception e) {
            logger.error("智联网平台-其他异常：",e);
            throw new BusException("其他异常："+ ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }

    /**
     * 鉴权接口
     * @return
     */
    private String Auth(){
        AuthVo authVo = new AuthVo();
        authVo.setGrantType(grantType);
        authVo.setClientId(clientId);
        authVo.setClientSecret(clientSecret);
        String resp =HttpClientUtil.sendHttpPostJson(authUrl, JSON.toJSONString(authVo));
//        Map<String, Object> respMap = (Map<String, Object>) JSONObject.parse(resp);
        Map<String,Object> respMap = JsonUtil.readValue(resp,new TypeReference<Map<String,Object>>() {});
        return MapUtils.getString(respMap,"access_token");
    }
    /**
     * 门诊转诊推送
     * @param args
     * @return
     */
    public void visitReferral(Object[] args){
        Map<String,Object> param = JsonUtil.readValue(args[0].toString(), new TypeReference<Map<String, Object>>() {});
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("就诊号不能为空");
        }
        //组装实体
        PatientInfo patientInfo = lbXFReferralMapper.qryPatientInfo(param);
        if(patientInfo == null){
            throw new BusException("未查询到患者数据");
        }
        patientInfo.setPatientName(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getPatientName())));
        patientInfo.setSexCode(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getSexCode())));
        patientInfo.setBirthday(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getBirthday())));
        patientInfo.setAge(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getAge())));
        patientInfo.setCertificateId(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getCertificateId())));
//        patientInfo.setHealthCardId(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getHealthCardId())));
        if (patientInfo.getPhone() == null){
            patientInfo.setPhone("");
        }else {
            patientInfo.setPhone(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getPhone())));
        }

        patientInfo.setResidentialAddress(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getResidentialAddress())));
        patientInfo.setInsuranceCardNo(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfo.getInsuranceCardNo())));

        DoctorInfo doctorInfo = lbXFReferralMapper.qryDoctorInfo(param);
        if(doctorInfo == null){
            throw new BusException("未查询到医生数据");
        }
        doctorInfo.setDoctorName(SM4Util.encryptEcb16(appKey,String.valueOf(doctorInfo.getDoctorName())));
        doctorInfo.setReferralDoctorName(SM4Util.encryptEcb16(appKey,String.valueOf(doctorInfo.getReferralDoctorName())));

        ProviderOrg providerOrg = lbXFReferralMapper.qryOrgInfo(param);
        if(providerOrg == null){
            throw new BusException("未查询到机构数据");
        }
        providerOrg.setSourceId("0352");
        providerOrg.setAreaCode("341323");
        PhysicalExamination physicalExamination = lbXFReferralMapper.qryPhysicalExamination(param);
        if (physicalExamination == null){
            physicalExamination = new PhysicalExamination();
        }
        LaboratoryExamination laboratoryExamination = new LaboratoryExamination();
//        laboratoryExamination.setAuxExam(MapUtils.getString(elseMap,"laboratoryExamination"));
        TreatmentPlan treatmentPlan = new TreatmentPlan();
        MedicalAdvice medicalAdvice = new MedicalAdvice();
        BackupInfo backupInfo = new BackupInfo();
        backupInfo.setEncryption("1");
        List<DiagList> diagList = lbXFReferralMapper.qryDiagList(param);
        if (diagList == null || diagList.size()<=0){
            DiagList diag = new DiagList();
            diagList.add(diag);
        }else {
            while (diagList.remove(null));
        }
        if (diagList == null || diagList.size()<=0){
            DiagList diag = new DiagList();
            diagList.add(diag);
        }
//        CollectionUtils.filter(diagList, PredicateUtils.notNullPredicate());
        //组装发送数据
        VisitReferralMsg visitReferralMsg = new VisitReferralMsg();
        visitReferralMsg.setPatientInfo(patientInfo);
        visitReferralMsg.setDoctorInfo(doctorInfo);
        visitReferralMsg.setProviderOrg(providerOrg);
        visitReferralMsg.setPhysicalExamination(physicalExamination);
        visitReferralMsg.setLaboratoryExamination(laboratoryExamination);
        visitReferralMsg.setTreatmentPlan(treatmentPlan);
        visitReferralMsg.setMedicalAdvice(medicalAdvice);
        visitReferralMsg.setBackupInfo(backupInfo);
        visitReferralMsg.setBackupInfo(backupInfo);
        visitReferralMsg.setDiagList(diagList);
        Map<String, Object> elseMap = lbXFReferralMapper.qryElse(param);
        visitReferralMsg.setIrritabilityHistory(MapUtils.getString(elseMap,"IRRITABILITYHISTORY"));//过敏史irritabilityHistory
        visitReferralMsg.setMainSuit(MapUtils.getString(elseMap,"MAINSUIT"));//主诉
        visitReferralMsg.setIllnessHistory(MapUtils.getString(elseMap,"ILLNESSHISTORY"));//现病史illnessHistory
        visitReferralMsg.setPreviousHistory(MapUtils.getString(elseMap,"PREVIOUSHISTORY"));//既往史previousHistory
        String sendMsg = JSON.toJSONString(visitReferralMsg, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        logger.info(sendMsg);
        //推送数据
        String accessToken = this.Auth();
        logger.info(visitUrl+accessToken);
//        String resp = HttpClientUtil.sendHttpPostJson(visitUrl+accessToken,JSON.toJSONString(visitReferralMsg, (ValueFilter) (object, name, value) -> {
//            if(value == null){
//                return "";
//            }
//            return value;
//        }));
//        logger.info(resp);
//
//        Map<String, Object> respMap = JsonUtil.readValue(resp,new TypeReference<Map<String,Object>>() {});
//        if (MapUtils.getString(respMap,"msg") != null && !MapUtils.getString(respMap,"msg").equals("") )
//            throw new BusException(MapUtils.getString(respMap,"msg"));
////        if (MapUtils.getString(respMap,"code"))
        doPost(sendMsg,visitUrl+accessToken);
    }

    /**
     * 出院转诊推送
     * @param args
     * @return
     */
    public void leaveReferral(Object[] args){
        Map<String,Object> param = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(args[1], User.class);
        param.put("pkPv",args[0]);
        String pkPv = MapUtils.getString(param, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            throw new BusException("就诊号不能为空");
        }
        //组装实体
        PatientInfoleave patientInfoleave = lbXFReferralMapper.qryPatientLeave(param);
        if(patientInfoleave == null){
            throw new BusException("未查询到患者数据");
        }
        patientInfoleave.setPatientName(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getPatientName())));
        patientInfoleave.setPatientPhone(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getPatientPhone())));
        patientInfoleave.setCertificateId(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getCertificateId())));
        patientInfoleave.setSexCode(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getSexCode())));
        patientInfoleave.setAge(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getAge())));
        patientInfoleave.setMaritalStatus(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getMaritalStatus())));
//        patientInfoleave.setHealthCardId(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getHealthCardId())));
        patientInfoleave.setResidentialAddress(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getResidentialAddress())));
        patientInfoleave.setInsuranceCardNo(SM4Util.encryptEcb16(appKey,String.valueOf(patientInfoleave.getInsuranceCardNo())));

        DoctorInfoleave doctorInfoleave = lbXFReferralMapper.qryDoctorleave(param);
        if(doctorInfoleave == null){
            throw new BusException("未查询到医生数据");
        }
        doctorInfoleave.setReferralDoctorId(user.getCodeEmp());
        doctorInfoleave.setReferralDoctorName(SM4Util.encryptEcb16(appKey,user.getNameEmp()));
        doctorInfoleave.setResidentDctSign(SM4Util.encryptEcb16(appKey,String.valueOf(doctorInfoleave.getResidentDctSign())));
        doctorInfoleave.setAttendingDctSign(SM4Util.encryptEcb16(appKey,String.valueOf(doctorInfoleave.getAttendingDctSign())));
        doctorInfoleave.setChiefPhysicianSign(SM4Util.encryptEcb16(appKey,String.valueOf(doctorInfoleave.getChiefPhysicianSign())));
//        doctorInfoleave.setSignTime("");

        ProviderOrg providerOrg = lbXFReferralMapper.qryOrgInfo(param);
        if(providerOrg == null){
            throw new BusException("未查询到机构数据");
        }
        providerOrg.setSourceId("0352");
        providerOrg.setAreaCode("341323");
        MainHealthIssues mainHealthIssues= lbXFReferralMapper.qryMainhealthIssues(param);
        if(mainHealthIssues == null) mainHealthIssues = new MainHealthIssues();
//        mainHealthIssues.setAdmissionTime("");
        AdmissionDiagnosis admissionDiagnosis  = lbXFReferralMapper.qryAdmissionDiagnosis(param);
        if (admissionDiagnosis == null) admissionDiagnosis = new AdmissionDiagnosis();
        LeaveDiagnosis leaveDiagnosis = lbXFReferralMapper.qryLeaveDiagnosis(param);
        if (leaveDiagnosis == null ) leaveDiagnosis = new LeaveDiagnosis();
//        leaveDiagnosis.setLeaveTime("");
        Operation operation = new Operation();
        TreatmentPlanning treatmentPlanning = lbXFReferralMapper.qryTreatmentPlanning(param);
        if (treatmentPlanning == null) treatmentPlanning = new TreatmentPlanning();
        HospitalizationProcess hospitalizationProcess = new HospitalizationProcess();
        MedicalAdviceLeave medicalAdviceLeave = lbXFReferralMapper.qryMedicalAdviceLeave(param);
        if (medicalAdviceLeave == null) medicalAdviceLeave = new MedicalAdviceLeave();
        Checkup checkup = new Checkup();
        BackupInfo backupInfo = new BackupInfo();
        backupInfo.setEncryption("1");

        LeaveReferralMsg leaveReferralMsg = new LeaveReferralMsg();
        leaveReferralMsg.setPatientInfo(patientInfoleave);
        leaveReferralMsg.setDoctorInfo(doctorInfoleave);
        leaveReferralMsg.setProviderOrg(providerOrg);
        leaveReferralMsg.setMainHealthIssues(mainHealthIssues);
        leaveReferralMsg.setAdmissionDiagnosis(admissionDiagnosis);
        leaveReferralMsg.setLeaveDiagnosis(leaveDiagnosis);
        leaveReferralMsg.setOperation(operation);
        leaveReferralMsg.setTreatmentPlanning(treatmentPlanning);
        leaveReferralMsg.setHospitalizationProcess(hospitalizationProcess);
        leaveReferralMsg.setMedicalAdvice(medicalAdviceLeave);
        leaveReferralMsg.setCheckup(checkup);
        leaveReferralMsg.setBackupInfo(backupInfo);
        String sendMsg = JSON.toJSONString(leaveReferralMsg, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        });
        logger.info(JSON.toJSONString(leaveReferralMsg, (ValueFilter) (object, name, value) -> {
            if(value == null){
                return "";
            }
            return value;
        }));
        //推送数据
        String accessToken = this.Auth();
        logger.info(leaveUrl+accessToken);
//        String resp = HttpClientUtil.sendHttpPostJson(leaveUrl+accessToken,JSON.toJSONString(leaveReferralMsg, (ValueFilter) (object, name, value) -> {
//            if(value == null){
//                return "";
//            }
//            return value;
//        }));
//        logger.info(resp);
//        Map<String, Object> respMap = JsonUtil.readValue(resp,new TypeReference<Map<String,Object>>() {});
//        if (MapUtils.getString(respMap,"msg") != null && !MapUtils.getString(respMap,"msg").equals("") )
//            throw new BusException(MapUtils.getString(respMap,"msg"));
//        return respMap;

        doPost(sendMsg,leaveUrl+accessToken);

    }

    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     * @param mobiles json数据
     * @param reqUrl 方法名称
     * @return
     */
    public void doPost(String mobiles, String reqUrl) {

        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        httpHeaders.setContentType(mediaType);
        httpHeaders.add("Accept",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(mobiles,httpHeaders);

        try {
            restTemplate.postForObject(reqUrl,httpEntity,String.class);
        }catch (HttpClientErrorException e){
            String newStr = null;
            try {
                newStr = new String(e.getResponseBodyAsString().getBytes("iso-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
            }
            logger.info(newStr);
            throw new BusException(newStr);
        }
    }




}
