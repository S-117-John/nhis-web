package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class RegisterInfoResExd extends PlatFormRes<RegisterInfoResResult> {

    @XmlElement(name="result")
    @Override
    public RegisterInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(RegisterInfoResResult result) {
        super.result = result;
    }
}
