package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;


public class Identifier {
    public Identifier(){}
    public Identifier(String system, String value) {
        this.system = system;
        this.value = value;
    }

    public Identifier(CodeableConcept type, String system, String value) {
        this.type = type;
        this.system = system;
        this.value = value;
    }
    public Identifier(CodeableConcept type, String system, String value,String display) {
        this.type = type;
        this.system = system;
        this.value = value;
        this.display = display;
    }
    public Identifier(CodeableConcept type, String system, String value, String use, String text) {
        this.type = type;
        this.system = system;
        this.value = value;
        this.use = use;
        this.text = text;
    }

    public Identifier(CodeableConcept type, String system, TextElement patientType, String value, String display, String use, String text) {
        this.system = system;
        this.type = patientType!=null?patientType:type;
        this.value = value;
        this.display = display;
        this.use = use;
        this.text = text;
    }

    public Identifier(Object type, Mumerator doseQuantity) {
        this.type = type;
        this.doseQuantity = doseQuantity;
    }

    //反序列化，如果是普通对象，会是LikedHashMap
    private Object type;
    private String system;
    private String value;
    private String display;

    private String use;
    private String text;

    private Mumerator doseQuantity;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Mumerator getDoseQuantity() {
        return doseQuantity;
    }

    public void setDoseQuantity(Mumerator doseQuantity) {
        this.doseQuantity = doseQuantity;
    }
}