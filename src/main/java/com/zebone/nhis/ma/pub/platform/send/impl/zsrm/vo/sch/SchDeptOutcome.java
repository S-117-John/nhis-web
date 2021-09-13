package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Location;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;

import java.util.List;

public class SchDeptOutcome extends Outcome {

    /**专科介绍说明*/
    private String description;
    /**专专科所属诊区*/
    private CodeableConcept physicalType;
    /**专科介绍说明*/
    private Outcome managingOrganization;
    /** 下级专科*/
    private Location partOf;
    /**专科特长介绍*/
    private String availabilityExceptions;

    private List<Extension> extension;

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CodeableConcept getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(CodeableConcept physicalType) {
        this.physicalType = physicalType;
    }

    public Outcome getManagingOrganization() {
        return managingOrganization;
    }

    public void setManagingOrganization(Outcome managingOrganization) {
        this.managingOrganization = managingOrganization;
    }

    public Location getPartOf() {
        return partOf;
    }

    public void setPartOf(Location partOf) {
        this.partOf = partOf;
    }

    public String getAvailabilityExceptions() {
        return availabilityExceptions;
    }

    public void setAvailabilityExceptions(String availabilityExceptions) {
        this.availabilityExceptions = availabilityExceptions;
    }
}
