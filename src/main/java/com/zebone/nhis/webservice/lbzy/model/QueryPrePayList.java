package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryPrePayList {

    @XmlElement(name = "Item")
    private List<QueryPrePayItem> objectList;

    public List<QueryPrePayItem> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<QueryPrePayItem> objectList) {
        this.objectList = objectList;
    }
}
