package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresDtRoot {
    @XmlElementWrapper(name = "PRESCRIBEDETAILS")
    @XmlElement(name = "PRESCRIBEDETAIL")
    private List<ZsrmHerbPresDtVo> presDtVoList;

    public List<ZsrmHerbPresDtVo> getPresDtVoList() {
        return presDtVoList;
    }

    public void setPresDtVoList(List<ZsrmHerbPresDtVo> presDtVoList) {
        this.presDtVoList = presDtVoList;
    }
}
