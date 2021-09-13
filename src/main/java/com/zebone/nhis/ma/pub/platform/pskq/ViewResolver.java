package com.zebone.nhis.ma.pub.platform.pskq;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import org.springframework.stereotype.Component;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewResolver {



    public static List<DataElement> dataElementsFactory(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        List<DataElement> elements = Lists.newArrayList();
        for (Field field:fields) {
            MetadataDescribe metadataDescribe = field.getAnnotation(MetadataDescribe.class);
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
                elements.add(dataElement);
            }

        }
        return elements;
    }


}
