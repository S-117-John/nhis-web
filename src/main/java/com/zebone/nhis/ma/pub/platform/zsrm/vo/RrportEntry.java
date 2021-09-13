package com.zebone.nhis.ma.pub.platform.zsrm.vo;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;

import java.io.Serializable;

public class RrportEntry implements Serializable {

    private PhResource resource;

    private ReportResponse response;

    public RrportEntry(){}
    public RrportEntry(ReportResponse response) {
        this.response = response;
    }
    public RrportEntry(PhResource resource) {
        this.resource = resource;
    }
    public void setResource(PhResource resource) {
        this.resource = resource;
    }

    public PhResource getResource() {
        return resource;
    }

    public ReportResponse getResponse() {
        return response;
    }

    public void setResponse(ReportResponse response) {
        this.response = response;
    }
}