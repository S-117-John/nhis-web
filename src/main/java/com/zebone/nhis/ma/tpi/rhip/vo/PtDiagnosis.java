package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 诊断明细报告
 * @author chengjia
 *
 */
@XmlRootElement(name = "Pt_Diagnosis")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtDiagnosis {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "ZDLSH", required = true)
    protected String zdlsh;
    @XmlElement(name = "BRBSLB", required = true)
    protected String brbslb;
    @XmlElement(name = "SFBS_DM")
    protected String sfbsDm;
    @XmlElement(name = "SFBSHM")
    protected String sfbshm;
    @XmlElement(name = "KLX")
    protected String klx;
    @XmlElement(name = "KH")
    protected String kh;
    @XmlElement(name = "ZYXYZDBZ_DM")
    protected String zyxyzdbzDm;
    @XmlElement(name = "JBDM")
    protected String jbdm;
    @XmlElement(name = "JBMC", required = true)
    protected String jbmc;
    @XmlElement(name = "ZYZDLBDM")
    protected String zyzdlbdm;
    @XmlElement(name = "XYZDLBDM")
    protected String xyzdlbdm;
    @XmlElement(name = "ZYZDBZ")
    protected String zyzdbz;
    @XmlElement(name = "YSZDBZ")
    protected String yszdbz;
    @XmlElement(name = "ZDYJDM")
    protected String zdyjdm;
    @XmlElement(name = "ZDRQ", required = true)
    protected String zdrq;
    @XmlElement(name = "ZGQK")
    protected String zgqk;
    @XmlElement(name = "ZDLXBM")
    protected String zdlxbm;
    @XmlElement(name = "ZDGCMS")
    protected String zdgcms;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "ZDYSGH")
    protected String zdysgh;
    @XmlElement(name = "ZDYSXM")
    protected String zdysxm;
    @XmlElement(name = "ZLJG_DM")
    protected String zljgDm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "QTZLJGQKXS")
    protected String qtzljgqkxs;
    @XmlElement(name = "JBZDBMBBH")
    protected String jbzdbmbbh;
    
    
    @XmlAttribute(name = "Name")
    protected String name;
    
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getZdlsh() {
		return zdlsh;
	}
	public void setZdlsh(String zdlsh) {
		this.zdlsh = zdlsh;
	}
	public String getBrbslb() {
		return brbslb;
	}
	public void setBrbslb(String brbslb) {
		this.brbslb = brbslb;
	}
	public String getJbdm() {
		return jbdm;
	}
	public void setJbdm(String jbdm) {
		this.jbdm = jbdm;
	}
	public String getJbmc() {
		return jbmc;
	}
	public void setJbmc(String jbmc) {
		this.jbmc = jbmc;
	}
	public String getZyzdlbdm() {
		return zyzdlbdm;
	}
	public void setZyzdlbdm(String zyzdlbdm) {
		this.zyzdlbdm = zyzdlbdm;
	}
	public String getXyzdlbdm() {
		return xyzdlbdm;
	}
	public void setXyzdlbdm(String xyzdlbdm) {
		this.xyzdlbdm = xyzdlbdm;
	}
	public String getZyzdbz() {
		return zyzdbz;
	}
	public void setZyzdbz(String zyzdbz) {
		this.zyzdbz = zyzdbz;
	}
	public String getYszdbz() {
		return yszdbz;
	}
	public void setYszdbz(String yszdbz) {
		this.yszdbz = yszdbz;
	}
	public String getZdyjdm() {
		return zdyjdm;
	}
	public void setZdyjdm(String zdyjdm) {
		this.zdyjdm = zdyjdm;
	}
	public String getZdrq() {
		return zdrq;
	}
	public void setZdrq(String zdrq) {
		this.zdrq = zdrq;
	}
	public String getZgqk() {
		return zgqk;
	}
	public void setZgqk(String zgqk) {
		this.zgqk = zgqk;
	}
	public String getZdlxbm() {
		return zdlxbm;
	}
	public void setZdlxbm(String zdlxbm) {
		this.zdlxbm = zdlxbm;
	}
	public String getZdgcms() {
		return zdgcms;
	}
	public void setZdgcms(String zdgcms) {
		this.zdgcms = zdgcms;
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
		return zdlsh;
	}

	public String getKlx() {
		return klx;
	}
	public void setKlx(String klx) {
		this.klx = klx;
	}
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getSfbsDm() {
		return sfbsDm;
	}
	public void setSfbsDm(String sfbsDm) {
		this.sfbsDm = sfbsDm;
	}
	public String getSfbshm() {
		return sfbshm;
	}
	public void setSfbshm(String sfbshm) {
		this.sfbshm = sfbshm;
	}
	public String getZyxyzdbzDm() {
		return zyxyzdbzDm;
	}
	public void setZyxyzdbzDm(String zyxyzdbzDm) {
		this.zyxyzdbzDm = zyxyzdbzDm;
	}
	public String getZdysgh() {
		return zdysgh;
	}
	public void setZdysgh(String zdysgh) {
		this.zdysgh = zdysgh;
	}
	public String getZdysxm() {
		return zdysxm;
	}
	public void setZdysxm(String zdysxm) {
		this.zdysxm = zdysxm;
	}
	public String getZljgDm() {
		return zljgDm;
	}
	public void setZljgDm(String zljgDm) {
		this.zljgDm = zljgDm;
	}
	public String getQtzljgqkxs() {
		return qtzljgqkxs;
	}
	public void setQtzljgqkxs(String qtzljgqkxs) {
		this.qtzljgqkxs = qtzljgqkxs;
	}
	public String getJbzdbmbbh() {
		return jbzdbmbbh;
	}
	public void setJbzdbmbbh(String jbzdbmbbh) {
		this.jbzdbmbbh = jbzdbmbbh;
	}

	
}
