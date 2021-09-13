package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

public class Response {
    @JsonProperty("status")
    private String staus;

    private Map<String,Object> outcome;

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getStaus() {
        return staus;
    }

    public void setOutcome(Map<String,Object> outcome) {
        this.outcome = outcome;
    }

    public Map<String,Object> getOutcome() {
        return outcome;
    }

}