package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

public class SenderElement {

    public SenderElement() {
    }

    public SenderElement(String id, SoftwareNameElement softwareName, SoftwareProviderElement softwareProvider, OrganizationElement organization) {
        this.id = id;
        this.softwareName = softwareName;
        this.softwareProvider = softwareProvider;
        this.organization = organization;
    }

    @JSONField(ordinal = 1)
    private String id;

    @JSONField(ordinal = 2)
    private SoftwareNameElement softwareName;

    @JSONField(ordinal = 3)
    private SoftwareProviderElement softwareProvider;

    @JSONField(ordinal = 4)
    private OrganizationElement organization;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SoftwareNameElement getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(SoftwareNameElement softwareName) {
        this.softwareName = softwareName;
    }

    public SoftwareProviderElement getSoftwareProvider() {
        return softwareProvider;
    }

    public void setSoftwareProvider(SoftwareProviderElement softwareProvider) {
        this.softwareProvider = softwareProvider;
    }

    public OrganizationElement getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationElement organization) {
        this.organization = organization;
    }
}
