package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 康仁堂药品处方获取请求入参vo
 */
@XmlRootElement(name="root")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresRequest {

    @XmlElement(name = "beginTime")
    private String dateStart;

    @XmlElement(name = "endTime")
    private String dateEnd;

    @XmlElement(name = "prescribeNO")
    private String presNo;

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }
}
