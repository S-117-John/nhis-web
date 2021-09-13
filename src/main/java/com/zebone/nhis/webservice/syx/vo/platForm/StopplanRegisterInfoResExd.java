package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class StopplanRegisterInfoResExd extends PlatFormRes<StopplanRegisterInfoResResult> {

    @XmlElement(name="result")
    @Override
    public StopplanRegisterInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(StopplanRegisterInfoResResult result) {
        super.result = result;
    }
}
