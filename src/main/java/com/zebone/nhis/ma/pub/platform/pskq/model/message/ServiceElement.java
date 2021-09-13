package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import javax.validation.constraints.NotNull;

public class ServiceElement {


    public ServiceElement() {
    }

    public ServiceElement(String serviceCode, String serviceName) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }

    private String serviceCode;

    private String serviceName;


    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
