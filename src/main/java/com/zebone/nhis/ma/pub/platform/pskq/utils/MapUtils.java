package com.zebone.nhis.ma.pub.platform.pskq.utils;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MapKey;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MapUtils extends org.apache.commons.collections.MapUtils {

    public static <T> T mapToObject(Map<String,Object> var1,Class<T> var2){
        try{
            Field[] fields = var2.getDeclaredFields();
            T instance = null;
            for (Field field : fields) {
                MapKey mapKey = field.getAnnotation(MapKey.class);
                if(mapKey==null){
                    continue;
                }
                String key = mapKey.value();
                if(!var1.containsKey(key)){
                    continue;
                }
                String value = (String) var1.get(key);
                instance  = var2.newInstance();
                String fieldName = field.getName();
                if(fieldName.equals("serialVersionUID")) {
                    continue;
                }
                Class<?> type = var2.getDeclaredField(fieldName).getType();
                String invokeMethod = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
                if(StringUtils.isNotBlank(invokeMethod)) {
                    Method method = var2.getMethod("set"+invokeMethod,type);
                    if (type.isAssignableFrom(String.class)) {
                        method.invoke(instance, value);
                    } else if (type.isAssignableFrom(int.class)
                            || type.isAssignableFrom(Integer.class)) {
                        method.invoke(instance, Integer.parseInt(value));
                    } else if (type.isAssignableFrom(Double.class)
                            || type.isAssignableFrom(double.class)) {
                        method.invoke(instance, Double.parseDouble(value));
                    } else if (type.isAssignableFrom(Boolean.class)
                            || type.isAssignableFrom(boolean.class)) {
                        method.invoke(instance, Boolean.parseBoolean(value));
                    } else if (type.isAssignableFrom(Date.class)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if(StringUtils.isNotBlank(value)) {
                            if(!"0".equals(value)) {
                                method.invoke(instance, dateFormat.parse(value));
                            }
                        }
                    }
                }

            }
            return instance;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
