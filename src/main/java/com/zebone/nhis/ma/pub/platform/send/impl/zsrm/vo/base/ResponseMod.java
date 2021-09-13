package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;

public class ResponseMod {
    private String resourceType;
    private String id;
    private Identifier identifier;
    private List<Issue> issue;

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }

    public List<Issue> getIssue() {
        return issue;
    }
}