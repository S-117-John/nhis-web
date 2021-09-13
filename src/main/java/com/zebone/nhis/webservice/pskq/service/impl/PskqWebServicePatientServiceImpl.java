package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.webservice.pskq.repository.PiMasterRepository;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.PskqWebservicePatientDao;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.PatientInfo;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.PatientResultResponse;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.PatientService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqDictionMapUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.nhis.webservice.service.PvPubForWsService;
import com.zebone.nhis.webservice.support.PskqSelfUtil;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import org.springframework.util.StringUtils;


public class PskqWebServicePatientServiceImpl implements PatientService {

    /**
     * 查询个人信息
     * @param param
     * @param listener
     */
    @Override
    public void findByEmpiId(String param, ResultListener listener) {

        try {
            Map<String,Object> responseMap = new HashMap<>();
            String result = "";
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            AckElement ackElement = new AckElement();
            ackElement.setTargetMessageId(requestBody.getId());
            Map<String,Object> query = requestBody.getQuery();
            if(query.containsKey("PATIENT")&&query.get("PATIENT")!=null){
                List<DataElement> dataElement = (List<DataElement>) query.get("PATIENT");
                PatientInfo patientInfo = (PatientInfo) MessageFactory.deserialization(dataElement, new PatientInfo());
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                PskqWebservicePatientDao pskqWebservicePatientDao = applicationContext.getBean("pskqWebservicePatientDao", PskqWebservicePatientDao.class);
                patientInfo = pskqWebservicePatientDao.findByEmpiId(patientInfo.getEmpiId());
                if(patientInfo!=null){
                    ackElement.setAckCode("AA");
                    ackElement.setAckDetail("查询成功");
                    List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(patientInfo);
                    PatientResultResponse patientResultResponse = new PatientResultResponse();
                    patientResultResponse.setPatient(dataElements);
                    ackElement.setPatientResult(patientResultResponse);
                    responseBody.setQueryAck(ackElement);
                    listener.success(responseBody);
                }else {
                    listener.error("没有查询到患者信息");
                }
            }else{
                listener.error("入参没有患者查询条件");
            }

        }catch (Exception exception){
            listener.error(exception.getMessage());
        }
    }

    /**
     * 患者注册
     * @param
     * @param listener
     */
    @Override
    public void save(RequestBody requestBody, ResultListener listener) {
        try {
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("PATIENT");
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            //发送方工号登录
            User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
    		if(user == null) {
    			listener.error("发送方：【"+requestBody.getSender().getSoftwareName().getName()+ "】；未在his注册工号，请先联系his注册工号！");
    			return;
    		}
    		UserContext.setUser(user);
            PatientInfo patientInfo =(PatientInfo) MessageFactory.deserialization(list, new PatientInfo());
            if(null==patientInfo){
                listener.error("未获取到消息内容");
                return;
            }
            //校验非空
            if (CommonUtils.isEmptyString(patientInfo.getPatientName())) {
                listener.error("患者姓名为必填项");
                return;
            }
            if (CommonUtils.isEmptyString(patientInfo.getGenderCode())) {
                listener.error("患者性别代码为必填项");
                return;
            }
            if (CommonUtils.isEmptyString(patientInfo.getIdTypeCode())) {
                listener.error("患者证件类型为必填项");
                return;

            }
            if (CommonUtils.isEmptyString(patientInfo.getIdNo())) {
                listener.error("患者证件号为必填项");
                return;
            }
            if (CommonUtils.isEmptyString(patientInfo.getPhoneNo())) {
                listener.error("患者手机号为必填项");
                return;
            }
            if (patientInfo.getDateOfBirth() == null) {
                listener.error("患者出生日期为必填项");
                return;
            }
            PiMaster pi = new PiMaster();
            //患者编码
            pi.setMpi(patientInfo.getEmpiId());
            pi.setHicNo(patientInfo.getEhealthCardNo());
            pi.setNamePi(patientInfo.getPatientName());
            pi.setBirthDate(patientInfo.getDateOfBirth());
            pi.setDtSex(patientInfo.getGenderCode());
            pi.setDtIdtype(PskqDictionMapUtil.pingToHisIdnoTypeMap(patientInfo.getIdTypeCode()));
            pi.setIdNo(patientInfo.getIdNo());
            pi.setInsurNo(patientInfo.getMedInsuranceNo());
            pi.setDtCountry(patientInfo.getNationalityCode());
            pi.setDtNation(patientInfo.getEthnicCode());
            pi.setDtOccu(patientInfo.getOccupationCode());
//            pi.setDtMarry(PskqDictionMapUtil.pingToHisMaritalStatusMap(patientInfo.getMaritalStatusCode()));
            pi.setDtEdu(patientInfo.getEducationCode());
            pi.setAddrcodeBirth("");
            pi.setAddrBirth("");
            pi.setAddrcodeOrigin("");
            pi.setAddrOrigin("");
            pi.setAddrcodeRegi("");
            pi.setAddrRegi("");
            pi.setAddrcodeCur("");
            pi.setAddrCur("");
            pi.setAddrRegiDt(patientInfo.getHouseholdAddress());
            pi.setTelNo(patientInfo.getHomePhoneNo());
            pi.setAddrCurDt(patientInfo.getPresentAddress());
            pi.setMobile(patientInfo.getPhoneNo());
            pi.setUnitWork(patientInfo.getWorkUnitName());
            pi.setTelWork(patientInfo.getWorkPhoneNo());
            pi.setNameRel(patientInfo.getContactName());
            pi.setDtRalation(patientInfo.getContactRelationshipCode());
            pi.setAddrRel(patientInfo.getContactAddress());
            pi.setTelRel(patientInfo.getContactPhoneNo());
            pi.setIdnoRel(patientInfo.getContactIdNo());
            if(!CommonUtils.isEmptyString(patientInfo.getContactIdNo())) {
                pi.setDtIdtypeRel("01");
            }
            pi.setCreator(user.getPkEmp());
            pi.setCreateTime(patientInfo.getEnterDateTime());
            pi.setFlagEhr("0");
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            PvPubForWsService pvPubForWsService = applicationContext.getBean("pvPubForWsService", PvPubForWsService.class);
            pi=pvPubForWsService.pskqSavePiMaster(pi, user);
            patientInfo.setEhealthCardNo(pi.getHicNo());
            patientInfo.setPkPatient("10_2000_"+pi.getCodePi());
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(patientInfo);

            listener.success(dataElementList);

        }catch (Exception e){
            listener.exception(e.getMessage());
        }
    }

    /**
     * 患者合并
     * @param requestBody
     * @param listener
     */
    @Override
    public void merge(RequestBody requestBody, ResultListener listener) {
        try{
            List<DataElement> list = (List<DataElement>) MapUtils.getObject(requestBody.getMessage(),"PATIENT");
            PatientInfo patientInfo =(PatientInfo) MessageFactory.deserialization(list, new PatientInfo());
            String codePi = patientInfo.getPkPatient();
            String empId = patientInfo.getEmpiId();
            if(StringUtils.isEmpty(codePi)){
                listener.error("未获取到CODE_PI");
                return;
            }
            if(StringUtils.isEmpty(patientInfo.getEmpiId())){
                listener.error("未获取到EMPI");
                return;
            }
            PiMaster piMaster = PiMasterRepository.findByCodePi(codePi);
            if(piMaster==null){
                listener.error("没有查询到患者信息");
                return;
            }
            //更新empi
            int result = PiMasterRepository.updateMpi(empId,codePi);
            if(result<=0){
                listener.error("更新EMPID失败");
                return;
            }

            listener.success("SUCESS");
        }catch (Exception e){
            listener.exception(e.getMessage());
        }

    }
}
