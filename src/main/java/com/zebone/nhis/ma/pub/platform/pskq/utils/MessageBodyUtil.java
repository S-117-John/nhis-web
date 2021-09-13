package com.zebone.nhis.ma.pub.platform.pskq.utils;

import com.google.common.collect.Lists;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.CvIdType;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.GbMarriage;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.SourceSystemCode;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.TypeCode;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElementmini;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageBodyUtil {

    public static List<DataElement> dataElementsFactory(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        List<DataElement> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            GbMarriage gbMarriage = field.getAnnotation(GbMarriage.class);
            SourceSystemCode sourceSystemCode = field.getAnnotation(SourceSystemCode.class);
            CvIdType cvIdType = field.getAnnotation(CvIdType.class);
            TypeCode typeCode = field.getAnnotation(TypeCode.class);
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

                if("ORG_CODE".equals(metadataDescribe.eName())){

                    dataElement.setValue("10");
                }
                if("ORG_NAME".equals(metadataDescribe.eName())){
                    dataElement.setValue("南方医科大学深圳口腔医院（坪山）");
                }

                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    dataElement.setValue("10_2000_"+result);
                }
                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    dataElement.setValue("10_2000_"+result);
                }

                if("NATIONALITY_CODE".equals(metadataDescribe.eName())){
                    //国际代码
                    dataElement.setValue("156");
                }
                
                if("NATIONALITY_NAME".equals(metadataDescribe.eName())){
                    //国际名称
                    dataElement.setValue("中国");
                }
            
                if("ORG_CODE".equals(metadataDescribe.eName())){

                    dataElement.setValue("10");
                }
                elements.add(dataElement);
            }
            //婚姻状况编码
            if(gbMarriage!=null){
            	String type = gbMarriage.type();
            	
            	if("code".equals(type)) {
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
            	}else if("name".equals(type)){
	               	 if ("未婚".equals(result)) {
	                     dataElement.setValue("未婚");
	                 }else if("已婚".equals(result)){
	                     dataElement.setValue("已婚");
	                 }else if("初婚".equals(result)){
	                     dataElement.setValue("初婚");
	                 }else if("再婚".equals(result)){
	                     dataElement.setValue("再婚");
	                 }else if("复婚".equals(result)){
	                     dataElement.setValue("复婚");
	                 }else if("离婚".equals(result)){
	                     dataElement.setValue("离婚");
	                 }else if("丧偶".equals(result)){
	                     dataElement.setValue("丧偶");
	                 }else {
	                     dataElement.setValue("未说明的婚姻状况");
	                 }

            	}
                
            }
            
            //付款方式
            if(typeCode != null) {
            	String type = typeCode.value();
            	if("code".equals(type)){
            		 if ("15".equals(result)) {
                         dataElement.setValue("2");//微信
                     }else if("16".equals(result)){
                         dataElement.setValue("3");//支付宝
                     }else if("3".equals(result)){
                         dataElement.setValue("1");//银行
                     }else if("1".equals(result)){
                         dataElement.setValue("0");//自费
                     }else {
                         dataElement.setValue("5");//其他
                     }
                }else if("name".equals(type)){
                	 if ("微信".equals(result)) {
                         dataElement.setValue("微信支付");
                     }else if("支付宝".equals(result)){
                         dataElement.setValue("支付宝支付");
                     }else if("银行卡".equals(result)){
                         dataElement.setValue("银行卡支付");
                     }else if("现金".equals(result)){
                         dataElement.setValue("院内预交金余额支付");
                     }else {
                         dataElement.setValue("其他第三方支付");
                     }
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
        }
        return elements;
    }
    /**
     * 简化版
     * @param o
     * @return
     * @throws IllegalAccessException
     */
    public static List<DataElementmini> dataElementsFactoryMini(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        List<DataElementmini> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
            GbMarriage gbMarriage = field.getAnnotation(GbMarriage.class);
            SourceSystemCode sourceSystemCode = field.getAnnotation(SourceSystemCode.class);
            CvIdType cvIdType = field.getAnnotation(CvIdType.class);
            TypeCode typeCode = field.getAnnotation(TypeCode.class);
            field.setAccessible(true);
            Object result =  field.get(o);
            DataElementmini dataElement = new DataElementmini();
            if(metadataDescribe!=null){
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

                if("ORG_CODE".equals(metadataDescribe.eName())){

                    dataElement.setValue("10");
                }
                if("ORG_NAME".equals(metadataDescribe.eName())){
                    dataElement.setValue("南方医科大学深圳口腔医院（坪山）");
                }

                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    dataElement.setValue("10_2000_"+result);
                }
                if("PK_PATIENT".equals(metadataDescribe.eName())){
                    dataElement.setValue("10_2000_"+result);
                }

                if("NATIONALITY_CODE".equals(metadataDescribe.eName())){
                    //国际代码
                    dataElement.setValue("156");
                }
                
                if("NATIONALITY_NAME".equals(metadataDescribe.eName())){
                    //国际名称
                    dataElement.setValue("中国");
                }
            
                if("ORG_CODE".equals(metadataDescribe.eName())){

                    dataElement.setValue("10");
                }
                elements.add(dataElement);
            }
            //婚姻状况编码
            if(gbMarriage!=null){
            	String type = gbMarriage.type();
            	
            	if("code".equals(type)) {
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
            	}else if("name".equals(type)){
	               	 if ("未婚".equals(result)) {
	                     dataElement.setValue("未婚");
	                 }else if("已婚".equals(result)){
	                     dataElement.setValue("已婚");
	                 }else if("初婚".equals(result)){
	                     dataElement.setValue("初婚");
	                 }else if("再婚".equals(result)){
	                     dataElement.setValue("再婚");
	                 }else if("复婚".equals(result)){
	                     dataElement.setValue("复婚");
	                 }else if("离婚".equals(result)){
	                     dataElement.setValue("离婚");
	                 }else if("丧偶".equals(result)){
	                     dataElement.setValue("丧偶");
	                 }else {
	                     dataElement.setValue("未说明的婚姻状况");
	                 }

            	}
                
            }
            
            //付款方式
            if(typeCode != null) {
            	String type = typeCode.value();
            	if("code".equals(type)){
            		 if ("15".equals(result)) {
                         dataElement.setValue("2");//微信
                     }else if("16".equals(result)){
                         dataElement.setValue("3");//支付宝
                     }else if("3".equals(result)){
                         dataElement.setValue("1");//银行
                     }else if("1".equals(result)){
                         dataElement.setValue("0");//自费
                     }else {
                         dataElement.setValue("5");//其他
                     }
                }else if("name".equals(type)){
                	 if ("微信".equals(result)) {
                         dataElement.setValue("微信支付");
                     }else if("支付宝".equals(result)){
                         dataElement.setValue("支付宝支付");
                     }else if("银行卡".equals(result)){
                         dataElement.setValue("银行卡支付");
                     }else if("现金".equals(result)){
                         dataElement.setValue("院内预交金余额支付");
                     }else {
                         dataElement.setValue("其他第三方支付");
                     }
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
}
