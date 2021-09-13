package com.zebone.nhis.ma.pub.platform.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DataElement implements Serializable {

    @JSONField(name = "DATA_ELEMENT_ID",ordinal = 1)
    private String id;

    @JSONField(name = "DATA_ELEMENT_NAME",ordinal = 2)
    private String name;

    @JSONField(name = "DATA_ELEMENT_EN_NAME",ordinal = 3)
    private String enName;

    @JSONField(name = "DATA_ELEMENT_VALUE",ordinal = 4)
    private Object value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
