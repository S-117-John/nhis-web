package com.zebone.nhis.webservice.lbzy.model.paydt;

import com.zebone.nhis.webservice.lbzy.model.QueryToPayItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryToPresList {

    @XmlElement(name = "Item")
    private List<QueryToPayPres> objectList;

    public List<QueryToPayPres> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<QueryToPayPres> objectList) {
        this.objectList = objectList;
    }
}
