package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.google.common.collect.Maps;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.Patient;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

public class PatientMessageImpl implements Message {

    public PatientMessageImpl(Patient patient) {
        this.patient = patient;
    }

    private Patient patient;

    public Patient getPatientInfo() {
        return patient;
    }

    public void setPatientInfo(Patient patient) {
        this.patient = patient;
    }

    @Override
    public RequestBody getRequestBody(Map<String,Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("isAdd")&&param.get("isAdd")!=null&& !StringUtils.isEmpty(param.get("isAdd").toString())){
            if("0".equalsIgnoreCase(param.get("isAdd").toString())){
                //新增
                serviceCode = "S0001";
                serviceName = "个人信息注册服务";
                eventCode = "E000101";
                eventName = "患者建档";
            }else if("1".equalsIgnoreCase(param.get("isAdd").toString())) {
                //更新
                serviceCode = "S0002";
                serviceName = "个人信息更新服务";
                eventCode = "E000201";
                eventName = "修改患者信息";
            }
        }
        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院信息管理系统"),
                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(patient);
        Map<String,Object> map = Maps.newHashMap();
        map.put("PATIENT",dataElements);
        RequestBody requestBody = new RequestBody(
                serviceElement,
                eventElement,
                senderElement,
                receiverElement,
                map
        );
        return requestBody;
    }
}
