package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ConditionRecIdentifierDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

public class ConditionRecorder {
    public ConditionRecorder() {
    }

    public ConditionRecorder(String resourceType, Coding clas, List<Identifier> identifier, List<TextElement> name) {
        this.resourceType = resourceType;
        this.clas = clas;
        this.identifier = identifier;
        this.name = name;
    }

    public ConditionRecorder(String resourceType, Coding clas, Identifier identifier) {
        this.resourceType = resourceType;
        this.clas = clas;
        this.identifier = identifier;
    }

    private String resourceType;
    @JSONField(name = "class")
    private Coding clas;

    @JsonDeserialize(using = ConditionRecIdentifierDeserializer.class)
    private Object identifier;

    private List<TextElement> name;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Coding getClas() {
        return clas;
    }

    public void setClas(Coding clas) {
        this.clas = clas;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public List<TextElement> getName() {
        return name;
    }

    public void setName(List<TextElement> name) {
        this.name = name;
    }
}
