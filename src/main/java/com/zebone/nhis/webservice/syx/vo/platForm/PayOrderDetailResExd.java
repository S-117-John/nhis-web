package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PayOrderDetailResExd extends PlatFormRes<PayOrderDetailResResult> {

    @XmlElement(name="result")
    @Override
    public PayOrderDetailResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(PayOrderDetailResResult result) {
        super.result = result;
    }
}
