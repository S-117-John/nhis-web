package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class OrganizationResultResponse {

    @JSONField(name = "ORGANIZATION")
    private List<DataElement> organization;

    public List<DataElement> getOrganization() {
        return organization;
    }

    public void setOrganization(List<DataElement> organization) {
        this.organization = organization;
    }
}
