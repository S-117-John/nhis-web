package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class QryChecklistInfoResSub {

    @XmlElementWrapper(name = "res")
    @XmlElement(name="item")
    private List<QryChecklistInfoResData> data;

    public List<QryChecklistInfoResData> getData() {
        return data;
    }

    public void setData(List<QryChecklistInfoResData> data) {
        this.data = data;
    }
}
