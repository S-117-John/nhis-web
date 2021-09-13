package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class CreateACardResExd extends PlatFormRes<CreateACardResResult> {

    @XmlElement(name="result")
    @Override
    public CreateACardResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(CreateACardResResult result) {
        super.result = result;
    }
}
