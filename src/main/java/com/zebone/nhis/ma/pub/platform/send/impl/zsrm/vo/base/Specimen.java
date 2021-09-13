package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Code;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Collection;

import java.util.Date;
import java.util.List;

public class Specimen {

    private String resourceType;

    private Code type;

    private Collection collection;

    private List<Extension> extension;

    private Identifier identifier;

    private List<TextElement> name;

    public List<TextElement> getName() {
        return name;
    }

    public void setName(List<TextElement> name) {
        this.name = name;
    }


    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Code getType() {
        return type;
    }

    public void setType(Code type) {
        this.type = type;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }
}