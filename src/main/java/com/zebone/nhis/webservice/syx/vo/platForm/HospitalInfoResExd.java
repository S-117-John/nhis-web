package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class HospitalInfoResExd extends PlatFormRes<HospitalInfoResResult> {

    @XmlElement(name="result")
    @Override
    public HospitalInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(HospitalInfoResResult result) {
        super.result = result;
    }
}
