package com.zebone.nhis.webservice.lbzy.model.paydt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryPayRecordDetailList {

    @XmlElement(name = "Item")
    private List<QueryPayRecordDetail> objectList;

    public List<QueryPayRecordDetail> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<QueryPayRecordDetail> objectList) {
        this.objectList = objectList;
    }
}
