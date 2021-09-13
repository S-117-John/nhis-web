package com.zebone.nhis.ma.pub.platform.pskq.utils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

public class Value {

    @JsonProperty("message")
    private Map<String, Object> message;

    @JsonAnySetter
    public void put(String key, Object value) {
        message = Collections.singletonMap(key, value);
    }


    @JsonAnyGetter
    public Map<String, Object> getValues() {
        return message;
    }

    @Override
    public String toString() {
        return message.toString();
    }
}
