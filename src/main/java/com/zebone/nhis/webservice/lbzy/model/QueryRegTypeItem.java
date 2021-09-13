package com.zebone.nhis.webservice.lbzy.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 卡卡西
 */
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryRegTypeItem {

    @XmlElement(name = "TypeID")
    private String typeID;

    @XmlElement(name = "TypeName")
    private String typeName;

    @XmlElement(name = "Cost")
    private Double cost;

    @XmlElement(name = "ReglevlValid")
    private String reglevlValid;

    public QueryRegTypeItem() {
    }

    public QueryRegTypeItem(String typeID, String typeName) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.reglevlValid="1";
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getReglevlValid() {
        return reglevlValid;
    }

    public void setReglevlValid(String reglevlValid) {
        this.reglevlValid = reglevlValid;
    }
}
