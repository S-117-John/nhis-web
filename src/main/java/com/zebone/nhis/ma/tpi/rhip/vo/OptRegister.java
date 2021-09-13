package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Opt_Register挂号表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_Register")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptRegister {
    @XmlElement(name = "JZLSH")
    protected String jzlsh;
    @XmlElement(name = "YYLSH")
    protected String yylsh;
    @XmlElement(name = "GHLSH", required = true)
    protected String ghlsh;
    @XmlElement(name = "KH", required = true)
    protected String kh;
    @XmlElement(name = "KLX", required = true)
    protected String klx;
    @XmlElement(name = "GHRQ", required = true)
    protected String ghrq;
    @XmlElement(name = "IDCardCode")
    protected String idCardCode;
    @XmlElement(name = "IDCard")
    protected String idCard;
    @XmlElement(name = "GHFS", required = true)
    protected String ghfs;
    @XmlElement(name = "BRLB", required = true)
    protected String brlb;
    @XmlElement(name = "THBZ", required = true)
    protected String thbz;
    @XmlElement(name = "GHLB", required = true)
    protected String ghlb;
    @XmlElement(name = "GHF", required = true)
    protected String ghf;
    @XmlElement(name = "ZLF", required = true)
    protected String zlf;
    @XmlElement(name = "QTF", required = true)
    protected String qtf;
    @XmlElement(name = "GHKSDM", required = true)
    protected String ghksdm;
    @XmlElement(name = "GHDJH")
    protected String ghdjh;
    @XmlElement(name = "FPHM")
    protected String fphm;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "SFYYBS")
    protected String sfyybs;
    @XmlElement(name = "YSXM")
    protected String ysxm;
    @XmlElement(name = "GHKSMC")
    protected String ghksmc;
    @XmlElement(name = "YSGH")
    protected String ysgh;
    
    
    @XmlAttribute(name = "Name")
    protected String name;
    
    
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getYylsh() {
		return yylsh;
	}
	public void setYylsh(String yylsh) {
		this.yylsh = yylsh;
	}
	public String getGhlsh() {
		return ghlsh;
	}
	public void setGhlsh(String ghlsh) {
		this.ghlsh = ghlsh;
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
	public String getGhrq() {
		return ghrq;
	}
	public void setGhrq(String ghrq) {
		this.ghrq = ghrq;
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
	public String getGhfs() {
		return ghfs;
	}
	public void setGhfs(String ghfs) {
		this.ghfs = ghfs;
	}
	public String getBrlb() {
		return brlb;
	}
	public void setBrlb(String brlb) {
		this.brlb = brlb;
	}
	public String getThbz() {
		return thbz;
	}
	public void setThbz(String thbz) {
		this.thbz = thbz;
	}
	public String getGhlb() {
		return ghlb;
	}
	public void setGhlb(String ghlb) {
		this.ghlb = ghlb;
	}
	public String getGhf() {
		return ghf;
	}
	public void setGhf(String ghf) {
		this.ghf = ghf;
	}
	public String getZlf() {
		return zlf;
	}
	public void setZlf(String zlf) {
		this.zlf = zlf;
	}
	public String getQtf() {
		return qtf;
	}
	public void setQtf(String qtf) {
		this.qtf = qtf;
	}
	public String getGhksdm() {
		return ghksdm;
	}
	public void setGhksdm(String ghksdm) {
		this.ghksdm = ghksdm;
	}
	public String getGhdjh() {
		return ghdjh;
	}
	public void setGhdjh(String ghdjh) {
		this.ghdjh = ghdjh;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
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
		return ghlsh;
	}
	public String getSfyybs() {
		return sfyybs;
	}
	public void setSfyybs(String sfyybs) {
		this.sfyybs = sfyybs;
	}
	public String getYsxm() {
		return ysxm;
	}
	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}
	public String getGhksmc() {
		return ghksmc;
	}
	public void setGhksmc(String ghksmc) {
		this.ghksmc = ghksmc;
	}
	public String getYsgh() {
		return ysgh;
	}
	public void setYsgh(String ysgh) {
		this.ysgh = ysgh;
	}
	
	
}
