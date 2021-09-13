package com.zebone.nhis.ma.pub.platform.pskq.aop;

import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.Patient;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;

import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendPatientMapper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@Aspect
public class EmpiIdAspect {

    @Autowired
    private PskqPlatFormSendPatientMapper pskqPlatFormSendPatientMapper;

    @AfterReturning(value="@annotation(com.zebone.nhis.ma.pub.platform.pskq.annotation.EmpiId)",returning = "object")
    public void after(JoinPoint joinPoint,Object object){
        try {
            if(object!=null&& object instanceof String){
                String result = (String) object;
                ResponseBody responseBody = JsonUtil.readValue(result,ResponseBody.class);
                Map<String,Object> resultMap = responseBody.getAck();
                if(resultMap.containsKey("ackCode") &&
                        !StringUtils.isEmpty(resultMap.get("ackCode"))&&
                        "AA".equals(resultMap.get("ackCode"))){
                    if(resultMap.containsKey("PATIENT") && resultMap.get("PATIENT")!=null){
                        List<DataElement> dataElementList = (List<DataElement>) resultMap.get("PATIENT");

                        Patient patient = (Patient) MessageFactory.deserialization(dataElementList, new Patient());
                        if(!StringUtils.isEmpty(patient.getPkPatient())){
                            String temp[] = patient.getPkPatient().split("_");
                            if(temp.length==3){
                                patient.setPkPatient(temp[2]);
                                pskqPlatFormSendPatientMapper.upatePiMasterByCodePi(patient);
                            }
                        }
                    }
                }
            }

        } catch (IllegalAccessException e) {

        }
    }
}
