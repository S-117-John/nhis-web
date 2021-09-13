package com.zebone.nhis.ma.pub.platform.pskq.dictionary;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Education {

    public static MultiValueMap<String,Object> educationMap = new LinkedMultiValueMap<>();

    static {
        //研究生
        educationMap.add("01","10");
        educationMap.add("01","研究生教育");

        educationMap.add("03","20");
        educationMap.add("03","大学本科教育");
    }
}
