package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class BusinessBase {

    private String resourceType;
    private String id;
    private Object type;
    @JsonDeserialize(using = ZsphJsonDateDeserializer.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private List<Entry> entry;

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

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public List<Issue> getIssue() {
        return issue;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }
}