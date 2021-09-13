package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class QryChecklistInfoReqSub {
    @XmlElement(name="req")
    private QryCheckListInfo CheckListInfo;

    public QryCheckListInfo getCheckListInfo() {
        return CheckListInfo;
    }

    public void setCheckListInfo(QryCheckListInfo checkListInfo) {
        CheckListInfo = checkListInfo;
    }
}
