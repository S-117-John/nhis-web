package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.ma.pub.platform.pskq.dictionary.DictionaryUtil;
import com.zebone.nhis.ma.pub.platform.pskq.dictionary.Education;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.Patient;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqDictionMapUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendPatientMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * @author lijin
 * 病人信息
 */
@Service
public class PskqPlatFormSendPatientHandler {


    @Autowired
    private HttpRestTemplate httpRestTemplate;

    @Autowired
    private PskqPlatFormSendPatientMapper pskqPlatFormSendPatientMapper;

    /**
     * 患者信息
     * isAdd{0：新增，1：更新}
     * @param paramMap
     * @param listener
     */
    public void getPatientInfo(Map<String, Object> paramMap, ResultListener listener){
        String result = "";
        String responseBody="";
        try {
            Patient patient = pskqPlatFormSendPatientMapper.getPiMasterById(paramMap);

            if(patient ==null){
                listener.error("未获取到患者信息！","","");
                return;
            }
            
            //设置证件类型：国家标准
            patient.setIdTypeCode(PskqDictionMapUtil.hisToPingIdnoTypeMap(patient.getIdTypeCode()));
            //设置民族：国家标准
            patient.setEthnicCode(PskqDictionMapUtil.getCollationInfo("noHis","NATION",patient.getEthnicCode()));
            //设置学历：国家标准
            String educationCode = patient.getEducationCode();
            patient.setEducationCode(DictionaryUtil.getCode(Education.educationMap,educationCode));
            patient.setEducationName(DictionaryUtil.getName(Education.educationMap,educationCode));
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(patient);
            patient.setOrgCode("10");
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
           
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                //更新empi 
            	Map<String,Object> resultMap = paramVo.getAck();
                 if(resultMap.containsKey("ackCode") &&
                         !StringUtils.isEmpty(resultMap.get("ackCode"))&&
                         "AA".equals(resultMap.get("ackCode"))){
                     if(resultMap.containsKey("PATIENT") && resultMap.get("PATIENT")!=null){
                         List<DataElement> dataElementList = (List<DataElement>) resultMap.get("PATIENT");
                         Patient patientReq = (Patient) MessageFactory.deserialization(dataElementList, new Patient());
                         if(!StringUtils.isEmpty(patientReq.getPkPatient())){
                             String temp[] = patientReq.getPkPatient().split("_");
                             if(temp.length==3){
                            	 patientReq.setPkPatient(temp[2]);
                                 pskqPlatFormSendPatientMapper.upatePiMasterByCodePi(patientReq);
                             }
                         }
                     }
                 }
            	listener.success(result,responseBody);
            }else {
                listener.error("发消息时出错",result,responseBody);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }

    } 
}
