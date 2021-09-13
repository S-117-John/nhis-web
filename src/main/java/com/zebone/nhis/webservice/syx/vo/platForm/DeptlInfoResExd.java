package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class DeptlInfoResExd extends PlatFormRes<DeptInfoResResult> {
	
	@XmlElement(name="result")
    @Override
    public DeptInfoResResult getResult() {
        return super.result;
    }

    @Override
    public void setResult(DeptInfoResResult result) {
        super.result = result;
    }

}
