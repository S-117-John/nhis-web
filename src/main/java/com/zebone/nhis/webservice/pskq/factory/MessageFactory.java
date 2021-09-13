package com.zebone.nhis.webservice.pskq.factory;

import com.google.common.collect.Lists;
import com.zebone.nhis.webservice.pskq.annotation.*;
import com.zebone.nhis.webservice.pskq.factory.impl.*;
import com.zebone.nhis.webservice.pskq.model.*;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.MessageLog;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.PskqInitService;
import com.zebone.platform.modules.utils.JsonUtil;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageFactory {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String DEPARTMENT = "DEPARTMENT";

    public Message getInstance(Object type){

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
               messageLog.setMsgStatus("SAVE");
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
            Birthday birthday = field.getAnnotation(Birthday.class);
            Marry marry = field.getAnnotation(Marry.class);
            Occupation occupation = field.getAnnotation(Occupation.class);
            Education education = field.getAnnotation(Education.class);
            Nationality nationality = field.getAnnotation(Nationality.class);
            Ethnic ethnic = field.getAnnotation(Ethnic.class);
            ContactRelationship contactRelationship = field.getAnnotation(ContactRelationship.class);
            Gender gender = field.getAnnotation(Gender.class);
            TypeCode typeCode = field.getAnnotation(TypeCode.class);
            field.setAccessible(true);
            if(metadataDescribe==null){
                continue;
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String,Object> map = (Map<String, Object>) list.get(i);
                if(metadataDescribe.eName().equalsIgnoreCase(map.get("DATA_ELEMENT_EN_NAME").toString())){
                    if(field.getType() == String.class){
                        field.set(object,map.get("DATA_ELEMENT_VALUE"));
                    }
                    if(field.getType() == Date.class){
                        try{
                            if(birthday!=null){
                                field.set(object,format.parse((String) map.get("DATA_ELEMENT_VALUE")));
                            }
                        }catch (Exception exception){
                        }
                    }
                    if(marry!=null){
                        if("code".equals(marry.value())){
                            field.set(object,PskqInitService.getInstance().marryMap.get(map.get("DATA_ELEMENT_VALUE")));
                        }
                    }
                    //职业
                    if(occupation!=null){
                        if("code".equals(occupation.value())){
                            field.set(object,"99");
                        }
                    }
                    //教育
                    if(education!=null){
                        if("code".equals(education.value())){
                            field.set(object,"01");
                        }
                    }
                    //国籍
                    if(nationality!=null){
                        if("code".equals(nationality.value())){
                            field.set(object,"2");
                        }
                    }
                    if(ethnic!=null){
                        if("code".equals(ethnic.value())){
                            field.set(object,"4");
                        }
                    }
                    if(contactRelationship!=null){
                        if("code".equals(contactRelationship.value())){
                            field.set(object,"110");
                        }
                    }
                    if(gender!=null){
                        if("code".equals(gender.type())){
                            field.set(object,PskqInitService.getInstance().sexMap.get(map.get("DATA_ELEMENT_VALUE")));
                        }
                    }
                    
                    //付款方式
	                if(typeCode != null) {
	                	if("code".equals(typeCode.type())){
                            field.set(object,PskqInitService.getInstance().typeCodeMap.get(map.get("DATA_ELEMENT_VALUE")));
                        }
	                	if("name".equals(typeCode.type())){
                            field.set(object,PskqInitService.getInstance().typeNameMap.get(map.get("DATA_ELEMENT_VALUE")));
                        }
	                }

                }
            }
        }

        return object;
    }
}
