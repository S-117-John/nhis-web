package com.zebone.nhis.ma.pub.platform.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.BusinessBase;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Entry;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Issue;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class ReportBusinessBase implements Serializable {
    private String resourceType;
    private String id;
    private Object type;
    //@JsonDeserialize(using = ZsphJsonDateDeserializer.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private Date timestamp;
    private List<RrportEntry> entry;

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

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<RrportEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<RrportEntry> entry) {
        this.entry = entry;
    }
}
