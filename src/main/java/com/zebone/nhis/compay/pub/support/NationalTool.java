package com.zebone.nhis.compay.pub.support;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.DynaBean;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class NationalTool {
    private static final Logger logger = LoggerFactory.getLogger("nhis.ZsrmQGLog");

    public static List<Map<String,Object>> fillData(NationalDict nationalDict,List<Object[]> dataList){
        List<Map<String,Object>> mapList = Lists.newArrayList();
        List<String> fields = nationalDict.getFileds();
        if(CollectionUtils.isEmpty(dataList)){
            return mapList;
        }
        for (Object[] objects : dataList) {
            if(objects == null)
                continue;
            //字段多，数据少时报错；数据多字段少、忽略
            if(fields.size() > objects.length){
                throw new BusException("数据长度和字段集合长度不一致");
            }
            Map<String,Object> map = new DynaBean();
            for (int i = 0; i < fields.size(); i++) {
                map.put(fields.get(i), objects[i]);
            }
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 使用下划线字段，给对象填充值，<b>只适合普通属性</b>
     * @param nationalDict
     * @param dataList
     * @param clas
     * @param <T>
     * @return
     */
    public static <T> List<T> fillData(NationalDict nationalDict, List<Object[]> dataList, Class<T> clas){
        List<T> list = Lists.newArrayList();
        try {
            List<String> fields = nationalDict.getFileds();
            Method[] methods = clas.getMethods();
            Map<String, Method> methodMap = Arrays.stream(methods).filter(md -> md.getName().startsWith("set")).collect(Collectors.toMap(md -> md.getName(), md -> md));
            for (Object[] objects : dataList) {
                if (objects == null)
                    continue;
                if(fields.size() > objects.length){
                    throw new BusException("数据长度和字段集合长度不一致");
                }
                T data = clas.newInstance();
                for (int i = 0; i < fields.size(); i++) {
                    Method method = methodMap.get(getName(fields.get(i)));
                    if (method != null) {
                        String simpleName = method.getParameterTypes()[0].getSimpleName();
                        Object object = objects[i];
                        try {
                            if("Double".equals(simpleName)){
                                object = object==null?0d:Double.valueOf(object.toString());
                            } else if("Integer".equals(simpleName)){
                                object = object==null?0:Integer.valueOf(object.toString());
                            }
                        } catch (NumberFormatException e) {
                            logger.error("医保返回数据异常，{},目标数据类型：{}，传入的值：{}",nationalDict.name(),simpleName,object);
                            object = "Double".equals(simpleName)?(Double.parseDouble(String.valueOf(Integer.MIN_VALUE))):Integer.MIN_VALUE;
                        }
                        method.invoke(data, object);
                    }
                }
                list.add(data);
            }
        } catch (Exception e) {
            logger.error("填充数据异常：",e);
            throw new BusException(e);
        }
        return list;
    }

    private static String getName(String field){
        return "set"+CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, field);
    }

    public static String getRsltDes(String stmtRslt){
        stmtRslt = Optional.ofNullable(stmtRslt).orElse("");
        switch (stmtRslt){
            case "0":
                return "对平";
            case "1":
                return "中心多";
            case "2":
                return "医药机构多";
            case "3":
                return "数据不一致";
        }
        return stmtRslt;
    }
}
