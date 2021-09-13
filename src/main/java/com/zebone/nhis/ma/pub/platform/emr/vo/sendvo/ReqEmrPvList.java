package com.zebone.nhis.ma.pub.platform.emr.vo.sendvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * create by: gao shiheng
 *
 * @Param: null
 * @return 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="pv_list")
public class ReqEmrPvList {

    @XmlElement(name="pv")
    private ReqEmrPv reqEmrPv;

    public ReqEmrPv getReqEmrPv() {
        return reqEmrPv;
    }

    public void setReqEmrPv(ReqEmrPv reqEmrPv) {
        this.reqEmrPv = reqEmrPv;
    }
}
