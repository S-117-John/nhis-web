package com.zebone.nhis.webservice.pskq.factory;

import com.google.common.collect.Lists;
import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.webservice.pskq.factory.impl.*;
import com.zebone.nhis.webservice.pskq.model.*;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.MessageLog;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.platform.modules.utils.JsonUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class MessageFactoryBean {

    private static class SingletonHolder{
        private static MessageFactoryBean instance = new MessageFactoryBean();
    }

    private MessageFactoryBean() {
    }

    public static MessageFactoryBean getInstance(){
        return SingletonHolder.instance;
    }

    public static final String DEPARTMENT = "DEPARTMENT";

    public Message getMessage(Object type){

        if(type == null){
            return null;
        }
        String className = type.getClass().getSimpleName();
        switch (className){
            case "PatientInfo":
                if(type instanceof PatientInfo){
                    return new PatientMessageImpl((PatientInfo) type);
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
            case "AckElement":
                if(type instanceof AckElement){
                    return new AckImpl((AckElement) type);
                }
                break;
            case "LabResult":
                if(type instanceof LabResult){
                    return new AckImpl((AckElement) type);
                }
            case "InAppPay":
                if(type instanceof InAppPay){
                    return new InAppPayImpl((InAppPay) type);
                }
                break;
            case "Outpatient":
                if(type instanceof Outpatient){
                    return new OutpatientImpl((Outpatient) type);
                }
                break;

            default:
                return null;
        }
        return null;
    }

    public static MessageLog getMessageLog(String content, MessageType messageType,String status){
       MessageLog messageLog = new MessageLog();
        RequestBody requestBody = JsonUtil.readValue(content, RequestBody.class);
        switch (messageType){
           case SEND:
               messageLog.setTransType("send");
               messageLog.setMsgId(requestBody.getId().replace("-", ""));
               messageLog.setSysCode("NHIS");
               messageLog.setMsgStatus(status);
               messageLog.setMsgType(requestBody.getService().getServiceName());
               messageLog.setMsgContent(content);
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
    public static Object deserialization(List<DataElement> list,Object object) throws IllegalAccessException {

        Field[] fields = object.getClass().getDeclaredFields();
        List<DataElement> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            if(metadataDescribe!=null){
                field.setAccessible(true);

                for (int i = 0; i < list.size(); i++) {
                    Map<String,Object> map = (Map<String, Object>) list.get(i);
                    if(metadataDescribe.eName().equalsIgnoreCase(map.get("DATA_ELEMENT_EN_NAME").toString())){
                        field.set(object,map.get("DATA_ELEMENT_VALUE"));
                    }
                }
            }
        }

        return object;
    }
}
