package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPayList {

    @XmlElement(name = "Item")
    private List<QueryToPayItem> objectList;

    public List<QueryToPayItem> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<QueryToPayItem> objectList) {
        this.objectList = objectList;
    }
}
