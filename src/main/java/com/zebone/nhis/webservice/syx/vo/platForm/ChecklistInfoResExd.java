package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class ChecklistInfoResExd extends PlatFormRes<ChecklistInfoResResult> {

    @XmlElement(name="result")
    @Override
    public ChecklistInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(ChecklistInfoResResult result) {
        super.result = result;
    }
}
