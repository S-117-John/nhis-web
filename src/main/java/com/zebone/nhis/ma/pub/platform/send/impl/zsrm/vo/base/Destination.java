package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Destination {

    public Destination(){

    }
    public Destination(String endpoint){
        this.endpoint = endpoint;
    }
    private String endpoint;

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

}