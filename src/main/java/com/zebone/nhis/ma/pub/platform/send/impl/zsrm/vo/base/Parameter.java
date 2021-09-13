package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;
/**
 * 查询主数据通用参数信息
 */
public class Parameter {

    private String name;
    private String type;
    private TextElement code;
    private String valueString;
    private String pyCode;
    private String dCode;

    public Parameter() {
    }

    public Parameter(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getPyCode() {
        return pyCode;
    }

    public void setPyCode(String pyCode) {
        this.pyCode = pyCode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public TextElement getCode() {
        return code;
    }

    public void setCode(TextElement code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }
}