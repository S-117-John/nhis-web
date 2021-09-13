package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;

/**
 * 科室消息体
 */
public class DeptOutLocation extends Outcome {
    private String status;
    private String description;
    private List<Identifier> telecom;
    private Address address;
    private Address partOf;
    private List<BdExtension> extension;
    private String hospitalId;
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getPartOf() {
        return partOf;
    }

    public void setPartOf(Address partOf) {
        this.partOf = partOf;
    }

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
