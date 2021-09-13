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
public class PreRegRecordList {

    @XmlElement(name = "Item")
    private List<PreRegRecordItem> preRegRecordItemList;

    public PreRegRecordList(List<PreRegRecordItem> preRegRecordItem) {
        this.preRegRecordItemList = preRegRecordItem;
    }

    public PreRegRecordList() {
    }
}
