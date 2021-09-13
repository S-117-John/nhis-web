package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresRoot {

    @XmlElementWrapper(name = "PRESCRIBERECORDS")
    @XmlElement(name = "PRESCRIBERECORD")
    private List<ZsrmHerbPresVo> presVoList;

    public List<ZsrmHerbPresVo> getPresVoList() {
        return presVoList;
    }

    public void setPresVoList(List<ZsrmHerbPresVo> presVoList) {
        this.presVoList = presVoList;
    }
}
