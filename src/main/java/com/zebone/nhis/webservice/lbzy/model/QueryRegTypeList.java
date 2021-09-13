package com.zebone.nhis.webservice.lbzy.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author 卡卡西
 */
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryRegTypeList {

    @XmlElement(name = "Item")
    private List<QueryRegTypeItem> objectList;


    public List<QueryRegTypeItem> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<QueryRegTypeItem> objectList) {
        this.objectList = objectList;
    }
}
