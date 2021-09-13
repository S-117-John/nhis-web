package com.zebone.nhis.ma.pub.platform.zsrm.support;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphConstant;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Issue;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.zsrm.model.message.RequestBody;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.exception.JonException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.cglib.beans.BeanMap;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ZsphMsgUtils {

    private static ObjectMapper objectMapper = getObjectMapper("yyyy-MM-dd HH:mm:ss");

    private static ObjectMapper getObjectMapper(String dateStyle) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(dateStyle));
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);//空节点不参与序列化
        return objectMapper;
    }

    /**
     * 生成失败时的内容
     * @return
     */
    public static Response createErrResponse(String errMsg){
        Response rs = new Response();
        rs.setStaus(ZsphConstant.RES_ERR_OTHER);
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        Issue issue = new Issue();
        issue.setSeverity(ZsphConstant.RES_ERR_CODE);
        issue.setCode("err");//我们目前没有错误编码，先放着
        issue.setDiagnostics(errMsg);
        outcome.setIssue(Arrays.asList(issue));
        rs.setOutcome(BeanMap.create(outcome));
        return rs;
    }

    public static Response createErrResponse(Exception e){
        String errMsg = null;
        if(e != null) {
            if(e instanceof BusException) {
                errMsg = e.getMessage();
            } else {
                Throwable cause = e.getCause();
                if(cause!=null && cause instanceof BusException){
                    errMsg = cause.getMessage();
                } else {
                    errMsg = ExceptionUtils.getRootCauseMessage(e);
                    errMsg = (StringUtils.length(errMsg)>50?errMsg.substring(0,48)+"..":errMsg);
                }
            }
        }
        return createErrResponse(errMsg);
    }

    public static Response createSimpleSuccess(String msg){
        Response response = new Response();
        response.setStaus(ZsphConstant.RES_SUS_OTHER);
        Outcome outcome = new Outcome();
        outcome.setResourceType("OperationOutcome");
        Issue issueSuccess = new Issue();
        issueSuccess.setCode("informational");
        issueSuccess.setSeverity("informational");
        issueSuccess.setDiagnostics(msg);
        outcome.setIssue(Arrays.asList(issueSuccess));
        response.setOutcome(BeanMap.create(outcome));
        return response;
    }

    public static <T> T fromJson(String jsonStr,Class<T> clas){
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.fromJson(jsonStr,clas);
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception var3) {
            throw new JonException(var3);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (Exception var3) {
            throw new JonException(var3);
        }
    }

    /**
     * 使用fastJson转换
     * @param object
     * @return
     */
    public static String getJsonStr(Object object){
        return JSON.toJSONString(object);
    }

    /** 使用jackson转换*/
    public static String writeValueAsString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception var2) {
            throw new JonException(var2);
        }
    }
    /** 使用jackson转换*/
    public static String writeValueAsString(Object obj, String dateStyle) {
        ObjectMapper objectMapper = getObjectMapper(dateStyle);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception var4) {
            throw new JonException(var4);
        }
    }

    /**
     * business中查询的Parameter
     * @param requestBody
     */
    public static List<Parameter> createParameterList(RequestBody requestBody){
        return requestBody.getEntry()
                .stream().filter(et ->{return et!=null && et.getResource()!=null && "Parameters".equals(et.getResource().getResourceType());})
                .findFirst().get().getResource().getParameter();
    }
    /**
     * 取文本内容
     * @param map
     * @return
     */
    public static String getPropValueStr(Map<String, Object> map,String key) {
        String value="" ;
        if(key==null||"".equals(key)||map==null||map.size()<=0){
            return "";
        }
        if(map.containsKey(key)){
            Object obj=map.get(key);
            value=obj==null?"":obj.toString();
        }
        return value;
    }
    public static Map<String,Object> parameterListToMap(List<Parameter> parameterList){
        Map<String,Object> map = new HashMap<>();
        parameterList.stream().forEach(parameter -> {
            if(parameter!=null && StringUtils.isNotBlank(parameter.getName())){
                map.put(parameter.getName(),parameter.getValueString());
            }
        });
        return map;
    }

    /**
     *
     * @param before  改变前的对象
     * @param after  改变后的对像
     * @param ignoreArr 不需要比较的属性
     * @return
     */
    public static <T> T compareFields(T before, T after, String[] ignoreArr) {
        T rs = null;
        try{
            if(before == null){
                return after;
            }
            List<String> ignoreList = null;
            if(ignoreArr != null && ignoreArr.length > 0){
                // array转化为list
                ignoreList = Arrays.asList(ignoreArr);
            }
            boolean flagChange = false;
            if (before.getClass() == after.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = before.getClass();
                rs = (T) clazz.newInstance();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    if(ignoreList != null && ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(before);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(after);
                    if(o1 instanceof Timestamp){
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if(o2 instanceof Timestamp){
                        o2 = new Date(((Timestamp) o2).getTime());
                    }
                    if(o1 == null && o2 == null){
                        continue;
                    }else if(o1 == null && o2 != null){
                        pd.getWriteMethod().invoke(rs,o2);
                        flagChange = true;
                        continue;
                    }
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                        if(o1 instanceof Timestamp){
                            flagChange = true;
                            pd.getWriteMethod().invoke(rs,readMethod.invoke(after));
                        } else {
                            flagChange = true;
                            pd.getWriteMethod().invoke(rs,o2);
                        }
                    }
                }
            }
            if (!flagChange) {
                return null;
            }
        } catch (Exception e) {
            throw new BusException("对象内容比较异常：", e);
        }
        return rs;
    }

    public static Object beanToMap(Object obj) {
        String stringBean = JsonUtil.writeValueAsString(obj);
        return JsonUtil.readValue(stringBean, Map.class);
    }

}
