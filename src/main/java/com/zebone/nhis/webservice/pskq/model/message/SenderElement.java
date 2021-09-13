package com.zebone.nhis.webservice.pskq.model.message;

public class SenderElement {

    public SenderElement() {
    }

    public SenderElement(String id, SoftwareNameElement softwareName, SoftwareProviderElement softwareProvider, OrganizationElement organization) {
        this.id = id;
        this.softwareName = softwareName;
        this.softwareProvider = softwareProvider;
        this.organization = organization;
    }


    private String id;

    private SoftwareNameElement softwareName;

    private SoftwareProviderElement softwareProvider;

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
