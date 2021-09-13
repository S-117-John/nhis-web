package com.zebone.nhis.ma.pub.platform.sd.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取身份证性别以及年龄
 * @author bat
 * @date 2020/9/27
 */
public class IDCardUtil {

    /**
     * 获取身份证年龄以及性别
     * @param idCard
     * @return
     */
    public static Map<String, Object> getBirthAgeSex(String idCard) {
        String birthday = "";
        String age = "";
        String sex = "";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        char[] number = idCard.toCharArray();
        boolean flag = true;
        if (number.length == 15) {
            for (int x = 0; x < number.length; x++) {
                if (!flag)
                    return new HashMap<String, Object>();
                flag = Character.isDigit(number[x]);
            }
        } else if (number.length == 18) {
            for (int x = 0; x < number.length - 1; x++) {
                if (!flag)
                    return new HashMap<String, Object>();
                flag = Character.isDigit(number[x]);
            }
        }
        if (flag && idCard.length() == 15) {
            birthday = "19" + idCard.substring(6, 8) +  idCard.substring(8, 10) + idCard.substring(10, 12);
            sex = Integer.parseInt(idCard.substring(idCard.length() - 3, idCard.length()))
                    % 2 == 0 ? "F" : "M";
            age = (year - Integer.parseInt("19" + idCard.substring(6, 8))) + "";
        } else if (flag && idCard.length() == 18) {
            birthday = idCard.substring(6, 10) +  idCard.substring(10, 12) + idCard.substring(12, 14);
            sex = Integer.parseInt(idCard.substring(idCard.length() - 4, idCard.length() - 1))
                    % 2 == 0 ? "F" : "M";
            age = (year - Integer.parseInt(idCard.substring(6, 10))) + "";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("birthday", birthday+"000000");
        map.put("age", age);
        map.put("sex", sex);
        return map;
    }
}
