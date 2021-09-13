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
@XmlRootElement(name="pv")
public class ReqEmrPv {

    @XmlElement(name="code_pv")
    private String codePv;

    @XmlElement(name="code_pi")
    private String codePi;

    @XmlElement(name="code_ipop")
    private String codeIp;

    @XmlElement(name="ipop_times")
    private String ipTimes;

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getIpTimes() {
        return ipTimes;
    }

    public void setIpTimes(String ipTimes) {
        this.ipTimes = ipTimes;
    }
}
