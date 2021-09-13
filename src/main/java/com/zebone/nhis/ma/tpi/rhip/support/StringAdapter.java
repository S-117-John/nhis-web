package com.zebone.nhis.ma.tpi.rhip.support;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<StringAdapter.AdaptedString, String>{

    @Override
    public String unmarshal(AdaptedString adaptedString) throws Exception {
        if(null == adaptedString) {
            return null;
        }
        String string = adaptedString.value;
        if("".equals(string)) {
            return null;
        }
        return string;
    }

    @Override
    public AdaptedString marshal(String string) throws Exception {
        AdaptedString adaptedString = new AdaptedString();
        adaptedString.value = string;
        return adaptedString;
    }

    public static class AdaptedString {
        @XmlValue public String value;
    }

}