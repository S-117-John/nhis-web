package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class StopRegResExd extends PlatFormRes<StopRegResResult> {

    @XmlElement(name="result")
    @Override
    public StopRegResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(StopRegResResult result) {
        super.result = result;
    }
}
