package com.zebone.nhis.webservice.lbzy.model.reg;

import com.zebone.nhis.webservice.lbzy.model.paydt.QueryPayRecordDetail;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApptTimeItemlList {
    public ApptTimeItemlList() {
    }

    public ApptTimeItemlList(List<ApptTimeItem> objectList) {
        this.objectList = objectList;
    }

    @XmlElement(name = "Item")
    private List<ApptTimeItem> objectList;

    public List<ApptTimeItem> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<ApptTimeItem> objectList) {
        this.objectList = objectList;
    }
}
