package com.zebone.nhis.ma.pub.platform.emr.vo.sendvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * create by: gao shiheng
 * @Param: null
 * @return
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="subject")
public class ReqEmrSubject {
    @XmlElement(name="eu_pvtype")
    private String euPvtype;

    @XmlElement(name="pv_list")
    private ReqEmrPvList reqEmrPvList;

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public ReqEmrPvList getReqEmrPvList() {
        return reqEmrPvList;
    }

    public void setReqEmrPvList(ReqEmrPvList reqEmrPvList) {
        this.reqEmrPvList = reqEmrPvList;
    }
}
