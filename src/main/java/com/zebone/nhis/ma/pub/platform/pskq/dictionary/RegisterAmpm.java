package com.zebone.nhis.ma.pub.platform.pskq.dictionary;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RegisterAmpm {

    public static MultiValueMap<String,Object> registerAmpmMap = new LinkedMultiValueMap<>();

    static {
        registerAmpmMap.add("0101","1");
        registerAmpmMap.add("0101","上午");

        registerAmpmMap.add("0102","2");
        registerAmpmMap.add("0102","下午");

        registerAmpmMap.add("0203","3");
        registerAmpmMap.add("0203","晚上");

        registerAmpmMap.add("0202","4");
        registerAmpmMap.add("0202","中午");

        registerAmpmMap.add("0301","9");
        registerAmpmMap.add("0301","全天");


    }
}
