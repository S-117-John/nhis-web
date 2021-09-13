package com.zebone.nhis.ma.pub.platform.pskq.factory;

import com.google.common.collect.Lists;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.factory.impl.*;
import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.MessageLog;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.platform.modules.utils.JsonUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MessageFactory {

    public Message getInstance(Object type){

        if(type == null){
            return null;
        }
        String className = type.getClass().getSimpleName();
        switch (className){
            case "Patient":
                if(type instanceof Patient){
                    return new PatientMessageImpl((Patient) type);
                }
                break;
            case "PriceItem":
                if(type instanceof PriceItem){
                    return new PriceItemImpl((PriceItem) type);
                }
                break;
            case "DrugItem":
                if(type instanceof DrugItem){
                    return new DrugItemImpl((DrugItem) type);
                }
                break;
            case "OrderItem":
                if(type instanceof OrderItem){
                    return new OrderItemImpl((OrderItem) type);
                }
                break;
            case "SurgeryApplicationOrder":
                if(type instanceof SurgeryApplicationOrder){
                    return new SurgeryApplicationOrderImpl((SurgeryApplicationOrder) type);
                }
                break;
            case "MaterialItem":
                if(type instanceof MaterialItem){
                    return new MaterialItemImpl((MaterialItem) type);
                }
                break;
            case "LabApply":
                if(type instanceof LabApply){
                    return new LabApplyImpl((LabApply) type);
                }
                break;
            case "ExamApply":
                if(type instanceof ExamApply){
                    return new ExamApplyImpl((ExamApply) type);
                }
                break;
            case "EncounterInpatient":
                if(type instanceof EncounterInpatient){
                    return new IpEncounterImpl((EncounterInpatient) type);
                }
                break;
            case "CostDetailInpatList":
                if(type instanceof CostDetailInpatList){
                    return new CostDetailInpatImpl((CostDetailInpatList) type);
                }
                break;
            case "Encounter":
                if(type instanceof Encounter){
                    return new EncounterImpl((Encounter) type);
                }
                break;
            case "OrdDiagInfo":
                if(type instanceof OrdDiagInfo){
                    return new OrdDiagInfoImpl((OrdDiagInfo) type);
                }
                break;
            case "SettlementOutpat":
                if(type instanceof SettlementOutpat){
                    return new SettlementOutpatImpl((SettlementOutpat) type);
                }
                break;
            case "MainData":
                if(type instanceof MainData){
                    return new MainDataImpl((MainData) type);
                }
                break;
            case "OrderOutpatList":
                if(type instanceof OrderOutpatList){
                    return new OrderOutpatImpl((OrderOutpatList) type);
                }
                break;
            case "OrderInpat":
                if(type instanceof OrderInpat){
                    return new OrderInpatImpl((OrderInpat) type);
                }
                break;
            case "InEncounter":
                if(type instanceof InEncounter){
                    return new InEncounterImpl((InEncounter) type);
                }
                break;
            case "AdtChangeInfo":
                if(type instanceof AdtChangeInfo){
                    return new AdtChangeInfoImpl((AdtChangeInfo) type);
                }
                break;
            case "AdvancePayment":
                if(type instanceof AdvancePayment){
                    return new AdvancePaymentImpl((AdvancePayment) type);
                }
                break;

            default:
                return null;
        }
        return null;
    }

    public static MessageLog getMessageLog(String requset,MessageType messageType,String status){
       MessageLog messageLog = new MessageLog();
        RequestBody requestBody = JsonUtil.readValue(requset,RequestBody.class);
        switch (messageType){
           case SEND:
               messageLog.setTransType("send");
               messageLog.setMsgId(requestBody.getId().replace("-", ""));
               messageLog.setSysCode("NHIS");
               messageLog.setMsgStatus(status);
               messageLog.setMsgType(requestBody.getEvent().getEventCode());
               messageLog.setMsgContent(requset);
              // String responseBody, messageLog.setErrTxt(responseBody);
               break;
           case RECEIVE:
               break;
           default:
               break;
       }
       return messageLog;
    }

    public enum MessageType{
        SEND,
        RECEIVE
    }


    /**
     * 反序列化消息替中的message数据
     * @param list
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public static Object deserialization(List<DataElement> list, Object object) throws IllegalAccessException {

        Field[] fields = object.getClass().getDeclaredFields();
        List<DataElement> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            if(metadataDescribe!=null){
                field.setAccessible(true);

                for (int i = 0; i < list.size(); i++) {
                    Map<String,Object> map = (Map<String, Object>) list.get(i);
                    if(metadataDescribe.id().equalsIgnoreCase(map.get("DATA_ELEMENT_ID").toString())){
                        field.set(object,map.get("DATA_ELEMENT_VALUE"));
                    }
                }
            }
        }

        return object;
    }
}
