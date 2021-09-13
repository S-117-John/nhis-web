package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeptItem {

    @XmlElement(name = "DepartmentID")
    private String departmentID;

    @XmlElement(name = "DepartmentName")
    private String departmentName;

    @XmlElement(name = "Cost")
    private String cost;

    @XmlElement(name = "IsEndPoint")
    private String isEndPoint;

    @XmlElement(name = "IsChooseDoctor")
    private String isChooseDoctor;

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getIsEndPoint() {
        return isEndPoint;
    }

    public void setIsEndPoint(String isEndPoint) {
        this.isEndPoint = isEndPoint;
    }

    public String getIsChooseDoctor() {
        return isChooseDoctor;
    }

    public void setIsChooseDoctor(String isChooseDoctor) {
        this.isChooseDoctor = isChooseDoctor;
    }
}
