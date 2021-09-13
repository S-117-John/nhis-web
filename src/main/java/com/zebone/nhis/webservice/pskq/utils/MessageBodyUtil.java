package com.zebone.nhis.webservice.pskq.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.CvIdType;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.GbMarriage;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.SourceSystemCode;
import com.zebone.nhis.webservice.pskq.annotation.Gender;
import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.webservice.pskq.annotation.ReturnElement;
import com.zebone.nhis.webservice.pskq.factory.Message;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageBodyUtil {

    public static List<DataElement> dataElementsFactory(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        List<DataElement> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            GbMarriage gbMarriage = field.getAnnotation(GbMarriage.class);
            SourceSystemCode sourceSystemCode = field.getAnnotation(SourceSystemCode.class);
            CvIdType cvIdType = field.getAnnotation(CvIdType.class);
            Gender gender = field.getAnnotation(Gender.class);
            ReturnElement returnElement = field.getAnnotation(ReturnElement.class);
            field.setAccessible(true);
            Object result =  field.get(o);
            DataElement dataElement = new DataElement();
            if(metadataDescribe!=null){
                dataElement.setId(metadataDescribe.id());
                dataElement.setName(metadataDescribe.name());
                dataElement.setEnName(metadataDescribe.eName());
                if(result==null){
                    dataElement.setValue("");
                }else {
                    if(result instanceof Date){
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formatStr2 =formatter2.format(result);
                        dataElement.setValue(formatStr2);

                    }else {
                        dataElement.setValue(result);
                    }

                }

//                if("ORG_CODE".equals(metadataDescribe.eName())){
//
//                    dataElement.setValue("10");
//                }
//                if("ORG_NAME".equals(metadataDescribe.eName())){
//                    dataElement.setValue("南方医科大学深圳口腔医院（坪山）");
//                }

                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    if(!(result.toString().contains("_"))){
                        dataElement.setValue("10_2000_"+result);
                    }

                }

                if("NATIONALITY_CODE".equals(metadataDescribe.eName())){
                    //国际代码
                    dataElement.setValue("156");
                }
                if("NATIONALITY_NAME".equals(metadataDescribe.eName())){
                    //国际代码
                    dataElement.setValue("中国");
                }



            }
            //婚姻状况编码
            if(gbMarriage!=null){
                if ("1".equals(result)) {
                    dataElement.setValue("10");
                }else if("2".equals(result)){
                    dataElement.setValue("20");
                }else if("3".equals(result)){
                    dataElement.setValue("30");
                }else if("4".equals(result)){
                    dataElement.setValue("40");
                }else {
                    dataElement.setValue("90");
                }
            }

            if(sourceSystemCode!=null){
                dataElement.setValue("2000");
            }

            if(cvIdType!=null){
                String type = cvIdType.value();
                if("code".equals(type)){
                    dataElement.setValue("01");
                }else if("name".equals(type)){
                    dataElement.setValue("居民身份证");
                }
            }
            //性别编码
            if(gender!=null){
                String type = gender.type();

            }
            elements.add(dataElement);
        }
        return elements;
    }

    private static Object getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  field.get(object);
        } catch (Exception e) {
            System.out.println( e.getMessage());
            return null;
        }
    }

    public static String getResponseBody(Map<String,Object> map) throws IllegalAccessException {


        MessageFactory messageFactory = new MessageFactory();
        AckElement ackElement = new AckElement();
        ackElement.setAckCode(map.get("ackCode").toString());
        ackElement.setAckDetail(map.get("ackDetail").toString());
        ackElement.setTargetMessageId(map.get("targetMessageId").toString());
        Message message = messageFactory.getInstance(ackElement);

        ResponseBody responseBody = message.getResponseBody(map);
        String result =  JSON.toJSONString(responseBody);

        return result;

    }



    public static List<DataElement> dataElementsReturnFactory(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        List<DataElement> elements = new ArrayList<>();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            GbMarriage gbMarriage = field.getAnnotation(GbMarriage.class);
            SourceSystemCode sourceSystemCode = field.getAnnotation(SourceSystemCode.class);
            CvIdType cvIdType = field.getAnnotation(CvIdType.class);
            Gender gender = field.getAnnotation(Gender.class);
            ReturnElement returnElement = field.getAnnotation(ReturnElement.class);
            field.setAccessible(true);
            Object result =  field.get(o);
            DataElement dataElement = new DataElement();
            if(metadataDescribe!=null){
                dataElement.setId(metadataDescribe.id());
                dataElement.setName(metadataDescribe.name());
                dataElement.setEnName(metadataDescribe.eName());
                if(result==null){
                    dataElement.setValue("");
                }else {
                    if(result instanceof Date){
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formatStr2 =formatter2.format(result);
                        dataElement.setValue(formatStr2);

                    }else {
                        dataElement.setValue(result);
                    }

                }

                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    if(!(result.toString().contains("_"))){
                        dataElement.setValue("10_2000_"+result);
                    }
                }

                if("NATIONALITY_CODE".equals(metadataDescribe.eName())){
                    //国际代码
                    dataElement.setValue("156");
                }

            }
            //婚姻状况编码
            if(gbMarriage!=null){
                if ("1".equals(result)) {
                    dataElement.setValue("10");
                }else if("2".equals(result)){
                    dataElement.setValue("20");
                }else if("3".equals(result)){
                    dataElement.setValue("30");
                }else if("4".equals(result)){
                    dataElement.setValue("40");
                }else {
                    dataElement.setValue("90");
                }
            }

            if(sourceSystemCode!=null){
                dataElement.setValue("2000");
            }

            if(cvIdType!=null){
                String type = cvIdType.value();
                if("code".equals(type)){
                    dataElement.setValue("01");
                }else if("name".equals(type)){
                    dataElement.setValue("居民身份证");
                }
            }
            //性别编码
            if(gender!=null){
                String type = gender.type();

            }

            if(returnElement!=null){
                elements.add(dataElement);
            }

        }
        return elements;
    }
}
