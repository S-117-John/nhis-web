package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Entry {

    private PhResource resource;

    private Response response;

    public Entry(){}
    public Entry(Response response) {
        this.response = response;
    }
    public Entry(PhResource resource) {
        this.resource = resource;
    }
    public void setResource(PhResource resource) {
        this.resource = resource;
    }

    public PhResource getResource() {
        return resource;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}