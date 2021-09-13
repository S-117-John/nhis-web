package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PayOrderStatusResExd extends PlatFormRes<PayOrderStatusResResult> {

    @XmlElement(name="result")
    @Override
    public PayOrderStatusResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(PayOrderStatusResResult result) {
        super.result = result;
    }
}
