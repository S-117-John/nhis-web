package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 住院体温单
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_SignsRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptSignsRecord {
	@XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "ZYBAH", required = true)
    protected String bzbah;
    @XmlElement(name = "JLSJ", required = true)
    protected String jlsj;
    @XmlElement(name = "TW")
    protected String tw;
    @XmlElement(name = "SSY")
    protected String ssy;
    @XmlElement(name = "SZY")
    protected String szy;
    @XmlElement(name = "KFXT")
    protected String kfxt;
    @XmlElement(name = "CHXT")
    protected String chxt;
    @XmlElement(name = "SG")
    protected String sg;
    @XmlElement(name = "TZ")
    protected String tz;
    @XmlElement(name = "HXPL")
    protected String hxpl;
    @XmlElement(name = "XL")
    protected String xl;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "TWDLSH", required = true)
    protected String twdlsh;
    @XmlElement(name = "RKSJ", required = true)
    protected String rksj;
    @XmlElement(name = "JLRGH", required = true)
    protected String jlzgh;
    @XmlElement(name = "JLRXM", required = true)
    protected String jlrxm;
    @XmlElement(name = "DJSJ", required = true)
    protected String djsj;
    @XmlElement(name = "CYRQ", required = true)
    protected String cyrq;
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getBzbah() {
		return bzbah;
	}
	public void setBzbah(String bzbah) {
		this.bzbah = bzbah;
	}
	public String getJlsj() {
		return jlsj;
	}
	public void setJlsj(String jlsj) {
		this.jlsj = jlsj;
	}
	public String getTw() {
		return tw;
	}
	public void setTw(String tw) {
		this.tw = tw;
	}
	public String getSsy() {
		return ssy;
	}
	public void setSsy(String ssy) {
		this.ssy = ssy;
	}
	public String getSzy() {
		return szy;
	}
	public void setSzy(String szy) {
		this.szy = szy;
	}
	public String getKfxt() {
		return kfxt;
	}
	public void setKfxt(String kfxt) {
		this.kfxt = kfxt;
	}
	public String getChxt() {
		return chxt;
	}
	public void setChxt(String chxt) {
		this.chxt = chxt;
	}
	public String getSg() {
		return sg;
	}
	public void setSg(String sg) {
		this.sg = sg;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getHxpl() {
		return hxpl;
	}
	public void setHxpl(String hxpl) {
		this.hxpl = hxpl;
	}
	public String getXl() {
		return xl;
	}
	public void setXl(String xl) {
		this.xl = xl;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getXbdm() {
		return xbdm;
	}
	public void setXbdm(String xbdm) {
		this.xbdm = xbdm;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSourceId() {
		return jzlsh;
	}
	public String getTwdlsh() {
		return twdlsh;
	}
	public void setTwdlsh(String twdlsh) {
		this.twdlsh = twdlsh;
	}
	public String getRksj() {
		return rksj;
	}
	public void setRksj(String rksj) {
		this.rksj = rksj;
	}
	public String getJlzgh() {
		return jlzgh;
	}
	public void setJlzgh(String jlzgh) {
		this.jlzgh = jlzgh;
	}
	public String getJlrxm() {
		return jlrxm;
	}
	public void setJlrxm(String jlrxm) {
		this.jlrxm = jlrxm;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getCyrq() {
		return cyrq;
	}
	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}
     
	
}
