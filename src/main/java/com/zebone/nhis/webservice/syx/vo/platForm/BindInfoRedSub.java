package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class BindInfoRedSub extends PlatFormReq<BindInfoVo> {

    @XmlElementWrapper(name="subject")
    @XmlElement(name="req")
    @Override
    public List<BindInfoVo> getSubject() {
        return super.subject;
    }

    @Override
    public void setSubject(List<BindInfoVo> subject) {
        super.subject = subject;
    }
}
