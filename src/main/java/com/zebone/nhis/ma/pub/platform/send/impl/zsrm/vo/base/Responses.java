package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Responses {
    private String staus;
    private Outcome outcome;
    private String etag;
    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Responses() {
    }

    public Responses(String staus, Outcome outcome, String etag) {
        this.staus = staus;
        this.outcome = outcome;
        this.etag = etag;
    }
}
