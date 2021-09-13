package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class QryChecklistInfoReqVo extends PlatFormReq<QryChecklistInfoReqSub> {

//    @XmlElementWrapper(name = "subject")
    @XmlElement(name = "subject")
    @Override
    public List<QryChecklistInfoReqSub> getSubject() {
        return super.subject;
    }

    @Override
    public void setSubject(List<QryChecklistInfoReqSub> subject) {
        super.subject = subject;
    }
}
