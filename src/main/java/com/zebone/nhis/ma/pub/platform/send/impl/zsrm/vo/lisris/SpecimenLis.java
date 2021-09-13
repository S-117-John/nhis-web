package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;

public class SpecimenLis {

    private String resourceType;

    private CodeLis type;

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

    public CodeLis getType() {
        return type;
    }

    public void setType(CodeLis type) {
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