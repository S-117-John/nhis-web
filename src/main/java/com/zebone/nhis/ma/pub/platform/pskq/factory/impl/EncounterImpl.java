package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.Encounter;
import com.zebone.nhis.ma.pub.platform.pskq.model.EncounterSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EncounterImpl implements Message {

    private Encounter encounter;

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public EncounterImpl(Encounter encounter) {
        this.encounter = encounter;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("eventCode")&&param.get("eventCode")!=null&& !StringUtils.isEmpty(param.get("eventCode").toString())){
            eventCode = (String) param.get("eventCode");
            eventName = (String) param.get("eventName");
            serviceCode = (String) param.get("serviceCode");
            serviceName = (String) param.get("serviceName");
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

        try{
            MessageBodyUtil.dataElementsFactory(encounter.getPatientInfo());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        List<DataElement> dataElementsPatine = MessageBodyUtil.dataElementsFactory(encounter.getPatientInfo());
        List<DataElement> dataElementsOutPat = MessageBodyUtil.dataElementsFactory(encounter.getOutPatient());

        EncounterSend encounterSend = new EncounterSend();
        encounterSend.setOutPatient(dataElementsOutPat);
        encounterSend.setPatientInfo(dataElementsPatine);

        Map<String,Object> map = new HashMap<>();
        map.put("ENCOUNTER", encounterSend);
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
