package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Opt_Record就诊表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_Record")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptRecord {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "GHLSH")
    protected String ghlsh;
    @XmlElement(name = "SFYY", required = true)
    protected String sfyy;
    @XmlElement(name = "KH")
    protected String kh;
    @XmlElement(name = "KLX")
    protected String klx;
    @XmlElement(name = "IDCardCode")
    protected String idCardCode;
    @XmlElement(name = "IDCard")
    protected String idCard;
    @XmlElement(name = "MZLB", required = true)
    protected String mzlb;
    @XmlElement(name = "JZKSBM", required = true)
    protected String jzksbm;
    @XmlElement(name = "ZDBM")
    protected String zdbm;
    @XmlElement(name = "ZDMC", required = true)
    protected String zdmc;
    @XmlElement(name = "ZS")
    protected String zs;
    @XmlElement(name = "ZZMS")
    protected String zzms;
    @XmlElement(name = "XBS")
    protected String xbs;
    @XmlElement(name = "JZRQ", required = true)
    protected String jzrq;
    @XmlElement(name = "ZXWT")
    protected String zxwt;
    @XmlElement(name = "WSFWYQ")
    protected String wsfwyq;
    @XmlElement(name = "FBRQSJ")
    protected String fbrqsj;
    @XmlElement(name = "ZZCXSJ")
    protected String zzcxsj;
    @XmlElement(name = "JKWTPG")
    protected String jkwtpg;
    @XmlElement(name = "CZJH")
    protected String czjh;
    @XmlElement(name = "ZYLBDM")
    protected String zylbdm;
    @XmlElement(name = "YYTZRQSJ")
    protected String yytzrqsj;
    @XmlElement(name = "ZRYSXM")
    protected String zrysxm;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "YBLX")
    protected String yblx;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "HYZK")
    protected String hyzk;
    @XmlElement(name = "ZY")
    protected String zy;
    @XmlElement(name = "MZ")
    protected String mz;
    @XmlElement(name = "GJ")
    protected String gj;
    @XmlElement(name = "XSRBZ")
    protected String xsrbz;
    @XmlElement(name = "ZRYSGH")
    protected String zrysgh;
    @XmlElement(name = "XSEBZ")
    protected String xsebz;
    @XmlElement(name = "ZDLXQF_DM")
    protected String zdlxqfDm;
    @XmlElement(name = "ZDLB_DM")
    protected String zdlbDm;
    @XmlElement(name = "CLCS")
    protected String clcs;
    @XmlElement(name = "ZLFA")
    protected String zlfa;
    @XmlElement(name = "JZSJ")
    protected String jzsj;
    
    
    @XmlAttribute(name = "Name")
    protected String name;
    
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getGhlsh() {
		return ghlsh;
	}
	public void setGhlsh(String ghlsh) {
		this.ghlsh = ghlsh;
	}
	public String getSfyy() {
		return sfyy;
	}
	public void setSfyy(String sfyy) {
		this.sfyy = sfyy;
	}
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getKlx() {
		return klx;
	}
	public void setKlx(String klx) {
		this.klx = klx;
	}
	public String getIdCardCode() {
		return idCardCode;
	}
	public void setIdCardCode(String idCardCode) {
		this.idCardCode = idCardCode;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMzlb() {
		return mzlb;
	}
	public void setMzlb(String mzlb) {
		this.mzlb = mzlb;
	}
	public String getJzksbm() {
		return jzksbm;
	}
	public void setJzksbm(String jzksbm) {
		this.jzksbm = jzksbm;
	}
	public String getZdbm() {
		return zdbm;
	}
	public void setZdbm(String zdbm) {
		this.zdbm = zdbm;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getZs() {
		return zs;
	}
	public void setZs(String zs) {
		this.zs = zs;
	}
	public String getZzms() {
		return zzms;
	}
	public void setZzms(String zzms) {
		this.zzms = zzms;
	}
	public String getXbs() {
		return xbs;
	}
	public void setXbs(String xbs) {
		this.xbs = xbs;
	}
	public String getJzrq() {
		return jzrq;
	}
	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}
	public String getZxwt() {
		return zxwt;
	}
	public void setZxwt(String zxwt) {
		this.zxwt = zxwt;
	}
	public String getWsfwyq() {
		return wsfwyq;
	}
	public void setWsfwyq(String wsfwyq) {
		this.wsfwyq = wsfwyq;
	}
	public String getFbrqsj() {
		return fbrqsj;
	}
	public void setFbrqsj(String fbrqsj) {
		this.fbrqsj = fbrqsj;
	}
	public String getZzcxsj() {
		return zzcxsj;
	}
	public void setZzcxsj(String zzcxsj) {
		this.zzcxsj = zzcxsj;
	}
	public String getJkwtpg() {
		return jkwtpg;
	}
	public void setJkwtpg(String jkwtpg) {
		this.jkwtpg = jkwtpg;
	}
	public String getCzjh() {
		return czjh;
	}
	public void setCzjh(String czjh) {
		this.czjh = czjh;
	}
	public String getZylbdm() {
		return zylbdm;
	}
	public void setZylbdm(String zylbdm) {
		this.zylbdm = zylbdm;
	}
	public String getYytzrqsj() {
		return yytzrqsj;
	}
	public void setYytzrqsj(String yytzrqsj) {
		this.yytzrqsj = yytzrqsj;
	}
	public String getZrysxm() {
		return zrysxm;
	}
	public void setZrysxm(String zrysxm) {
		this.zrysxm = zrysxm;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
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
	public String getHyzk() {
		return hyzk;
	}
	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getMz() {
		return mz;
	}
	public void setMz(String mz) {
		this.mz = mz;
	}
	public String getGj() {
		return gj;
	}
	public void setGj(String gj) {
		this.gj = gj;
	}
	public String getXsrbz() {
		return xsrbz;
	}
	public void setXsrbz(String xsrbz) {
		this.xsrbz = xsrbz;
	}
	public String getZrysgh() {
		return zrysgh;
	}
	public void setZrysgh(String zrysgh) {
		this.zrysgh = zrysgh;
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
	public String getXsebz() {
		return xsebz;
	}
	public String getZlfa() {
		return zlfa;
	}
	public void setZlfa(String zlfa) {
		this.zlfa = zlfa;
	}
	public void setXsebz(String xsebz) {
		this.xsebz = xsebz;
	}
	public String getZdlxqfDm() {
		return zdlxqfDm;
	}
	public void setZdlxqfDm(String zdlxqfDm) {
		this.zdlxqfDm = zdlxqfDm;
	}
	public String getZdlbDm() {
		return zdlbDm;
	}
	public void setZdlbDm(String zdlbDm) {
		this.zdlbDm = zdlbDm;
	}
	public String getClcs() {
		return clcs;
	}
	public void setClcs(String clcs) {
		this.clcs = clcs;
	}
	public String getJzsj() {
		return jzsj;
	}
	public void setJzsj(String jzsj) {
		this.jzsj = jzsj;
	}
	
	
}
