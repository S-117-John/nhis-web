package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.dict;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

public class BedResource extends PhResource {

    private String status;

    private String name;

    private CodeableConcept physicalType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CodeableConcept getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(CodeableConcept physicalType) {
        this.physicalType = physicalType;
    }
}
