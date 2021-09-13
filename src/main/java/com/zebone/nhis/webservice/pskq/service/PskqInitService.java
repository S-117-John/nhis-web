package com.zebone.nhis.webservice.pskq.service;

import java.util.HashMap;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

public class PskqInitService {

    private static class SingletonHolder{
        private static PskqInitService instance = new PskqInitService();
    }


    private PskqInitService(){

    }

    public static PskqInitService getInstance(){
        return SingletonHolder.instance;
    }

    public static Map<String,Object> marryMap = new HashMap<>(16);

    public static Map<String,Object> sexMap = new HashMap<>(16);
    
    public static Map<String,Object> typeCodeMap = new HashMap<>(16);

    public static Map<String,Object> typeNameMap = new HashMap<>(16);


    static {
        //未婚
        marryMap.put("10","1");
        //已婚
        marryMap.put("20","2");
        //初婚
        marryMap.put("21","");
        //再婚
        marryMap.put("22","6");
        //复婚
        marryMap.put("23","");
        //丧偶
        marryMap.put("30","3");
        //离婚
        marryMap.put("40","4");
        //未说明的婚姻状况
        marryMap.put("90","9");

        //未知的性别
        sexMap.put("0","04");

        //男性
        sexMap.put("1","02");

        //女性
        sexMap.put("2","03");

        sexMap.put("9","04");
        
        //微信
        typeCodeMap.put("2", "15");
        //支付宝
        typeCodeMap.put("3", "16");
        //银行卡
        typeCodeMap.put("1", "3");
        //现金
        typeCodeMap.put("0", "1");
        //其他
        typeCodeMap.put("5", "99");
    }
}
