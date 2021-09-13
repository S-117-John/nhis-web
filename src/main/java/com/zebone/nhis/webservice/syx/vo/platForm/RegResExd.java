package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class RegResExd extends PlatFormRes<RegResResult> {

    @XmlElement(name="result")
    @Override
    public RegResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(RegResResult result) {
        super.result = result;
    }
}
