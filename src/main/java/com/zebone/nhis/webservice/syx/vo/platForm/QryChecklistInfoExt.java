package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class QryChecklistInfoExt extends PlatFormRes<QryChecklistInfoResult> {

    @XmlElement(name="result")
    @Override
    public QryChecklistInfoResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(QryChecklistInfoResult result) {
        super.result=result;
    }
}
