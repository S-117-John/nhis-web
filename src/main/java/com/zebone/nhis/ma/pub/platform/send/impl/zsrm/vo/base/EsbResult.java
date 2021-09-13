package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;
import java.util.Map;

public class EsbResult {
    private String resourcetype;
    private String id;
    private String type;
    private String timestamp;
    private List<Map<String,Object>> entry;

    public String getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(String resourcetype) {
        this.resourcetype = resourcetype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Map<String,Object>> getEntry() {
        return entry;
    }

    public void setEntry(List<Map<String,Object>> entry) {
        this.entry = entry;
    }
}
