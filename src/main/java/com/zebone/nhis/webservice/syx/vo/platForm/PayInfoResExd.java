package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PayInfoResExd extends PlatFormRes<PayInfoResResult> {

    @XmlElement(name="result")
    @Override
    public PayInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(PayInfoResResult result) {
        super.result = result;
    }
}
