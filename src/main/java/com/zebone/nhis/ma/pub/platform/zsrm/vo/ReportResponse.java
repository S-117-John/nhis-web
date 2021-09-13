package com.zebone.nhis.ma.pub.platform.zsrm.vo;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class ReportResponse implements Serializable {
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