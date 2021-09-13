package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import java.util.List;

public class BdDataVo {
    private String resourceType;
    private String id;
    private String implicitRules;
    private List<Concept> concept;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImplicitRules() {
        return implicitRules;
    }

    public void setImplicitRules(String implicitRules) {
        this.implicitRules = implicitRules;
    }

    public List<Concept> getConcept() {
        return concept;
    }

    public void setConcept(List<Concept> concept) {
        this.concept = concept;
    }
}
