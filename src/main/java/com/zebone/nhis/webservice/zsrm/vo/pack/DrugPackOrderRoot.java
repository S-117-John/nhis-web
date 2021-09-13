package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
public class DrugPackOrderRoot {
    @XmlElement(name = "DataTable")
    private List<DrugPackOrdervo> ordList;

    public List<DrugPackOrdervo> getOrdList() {
        return ordList;
    }

    public void setOrdList(List<DrugPackOrdervo> ordList) {
        this.ordList = ordList;
    }
}
