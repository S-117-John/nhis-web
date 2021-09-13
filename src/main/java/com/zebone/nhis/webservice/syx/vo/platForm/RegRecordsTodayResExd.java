package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class RegRecordsTodayResExd extends PlatFormRes<RegRecordsTodayResResult> {

    @XmlElement(name="result")
    @Override
    public RegRecordsTodayResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(RegRecordsTodayResResult result) {
        super.result = result;
    }
}
