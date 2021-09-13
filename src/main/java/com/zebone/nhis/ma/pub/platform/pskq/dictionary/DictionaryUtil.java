package com.zebone.nhis.ma.pub.platform.pskq.dictionary;

import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryUtil {

    public static String getCode(MultiValueMap<String,Object> multiValueMap,String key){

        try{
            List<Object> list = multiValueMap.get(key);
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            String code = "";
            for (Object o : list) {
                Matcher isNum = pattern.matcher((String)o);
                if (isNum.matches()) {
                    code = (String) o;
                    break;
                }
            }
            return code;
        }catch (Exception e){
            return "";
        }

    }

    public static String getName(MultiValueMap<String,Object> multiValueMap,String key){

        try{
            List<Object> list = multiValueMap.get(key);
            Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]");
            String name = "";
            for (Object o : list) {
                Matcher matcher = pattern.matcher((String)o);
                if (matcher.find()) {
                    name = (String) o;
                    break;
                }
            }
            return name;
        }catch (Exception e){
            return "";
        }
    }

    public static void main(String[] args) {
        String code = DictionaryUtil.getCode(Education.educationMap,"01");
        String name = DictionaryUtil.getName(Education.educationMap,"01");
        System.out.println(code);
        System.out.println(name);
    }
}
