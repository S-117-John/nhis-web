package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PayDetailInfoResExd extends PlatFormRes<PayDetailInfoResResult> {

    @XmlElement(name="result")
    @Override
    public PayDetailInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(PayDetailInfoResResult result) {
        super.result = result;
    }
}
