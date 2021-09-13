package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 门诊收费明细表(Opt_FeeDetail)
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_FeeDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptFeeDetail {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "FYID", required = true)
    protected String fyid;
    @XmlElement(name = "SFMLLB")
    protected String sfmllb;
    @XmlElement(name = "SFXMBM")
    protected String sfxmbm;
    @XmlElement(name = "YBSFXMBM")
    protected String ybsfxmbm;
    @XmlElement(name = "YBSFXMMC")
    protected String ybsfxmmc;
    @XmlElement(name = "YLXMLB")
    protected String ylxmlb;
    @XmlElement(name = "YLXMLBMC")
    protected String ylxmlbmc;
    @XmlElement(name = "DJ")
    protected String dj;
    @XmlElement(name = "SL")
    protected String sl;
    @XmlElement(name = "ZJE")
    protected String zje;
    @XmlElement(name = "JX")
    protected String jx;
    @XmlElement(name = "SYPC")
    protected String sypc;
    @XmlElement(name = "TJMC")
    protected String tjmc;
    @XmlElement(name = "YYTS")
    protected String yyts;
    @XmlElement(name = "SYFF")
    protected String syff;
    @XmlElement(name = "JLDW")
    protected String jldw;
    @XmlElement(name = "GG")
    protected String gg;
    @XmlElement(name = "YBJSFWFYZJE")
    protected String ybjsfwfyzje;
    @XmlElement(name = "GRZF")
    protected String grzf;
    @XmlElement(name = "ZFBL")
    protected String zfbl;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "FYMXLSH")
    protected String fymxlsh;
    @XmlElement(name = "YZH")
    protected String yzh;
    @XmlElement(name = "YZNR")
    protected String yznr;
    @XmlElement(name = "ZXCS")
    protected String zxcs;
    @XmlElement(name = "YLBXLX")
    protected String ylbxlx;
    @XmlElement(name = "YEBZ")
    protected String yebz;
    @XmlElement(name = "CFLSH")
    protected String cflsh;
    @XmlElement(name = "MZFYFL_DM")
    protected String mzfyflDm;
    @XmlElement(name = "XMMC")
    protected String xmmc;
    @XmlElement(name = "XMDM")
    protected String xmdm;
    @XmlElement(name = "MXXMDW")
    protected String mxxmdw;
    @XmlElement(name = "MXXMDJ")
    protected String mxxmdj;
    @XmlElement(name = "MXXMSL")
    protected String mxxmsl;
    @XmlElement(name = "MXXMJE")
    protected String mxxmje;
    @XmlElement(name = "MXXMYBFWWZFZE")
    protected String mxxmybfwwzfze;
    @XmlElement(name = "MXXMYBFNFLZFJE")
    protected String mxxmybfnflzfje;
    @XmlElement(name = "STBZ_DM")
    protected String stbzDm;
    @XmlElement(name = "CFMXHM")
    protected String cfmxhm;
    @XmlElement(name = "YBXE")
    protected String ybxe;
    
    @XmlAttribute(name = "Name")
    protected String name;
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getFyid() {
		return fyid;
	}
	public void setFyid(String fyid) {
		this.fyid = fyid;
	}
	public String getSfmllb() {
		return sfmllb;
	}
	public void setSfmllb(String sfmllb) {
		this.sfmllb = sfmllb;
	}
	public String getSfxmbm() {
		return sfxmbm;
	}
	public void setSfxmbm(String sfxmbm) {
		this.sfxmbm = sfxmbm;
	}
	public String getYbsfxmbm() {
		return ybsfxmbm;
	}
	public void setYbsfxmbm(String ybsfxmbm) {
		this.ybsfxmbm = ybsfxmbm;
	}
	public String getYbsfxmmc() {
		return ybsfxmmc;
	}
	public void setYbsfxmmc(String ybsfxmmc) {
		this.ybsfxmmc = ybsfxmmc;
	}
	public String getYlxmlb() {
		return ylxmlb;
	}
	public void setYlxmlb(String ylxmlb) {
		this.ylxmlb = ylxmlb;
	}
	public String getYlxmlbmc() {
		return ylxmlbmc;
	}
	public void setYlxmlbmc(String ylxmlbmc) {
		this.ylxmlbmc = ylxmlbmc;
	}
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
	}
	public String getZje() {
		return zje;
	}
	public void setZje(String zje) {
		this.zje = zje;
	}
	public String getJx() {
		return jx;
	}
	public void setJx(String jx) {
		this.jx = jx;
	}
	public String getSypc() {
		return sypc;
	}
	public void setSypc(String sypc) {
		this.sypc = sypc;
	}
	public String getTjmc() {
		return tjmc;
	}
	public void setTjmc(String tjmc) {
		this.tjmc = tjmc;
	}
	public String getYyts() {
		return yyts;
	}
	public void setYyts(String yyts) {
		this.yyts = yyts;
	}
	public String getSyff() {
		return syff;
	}
	public void setSyff(String syff) {
		this.syff = syff;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public String getYbjsfwfyzje() {
		return ybjsfwfyzje;
	}
	public void setYbjsfwfyzje(String ybjsfwfyzje) {
		this.ybjsfwfyzje = ybjsfwfyzje;
	}
	public String getGrzf() {
		return grzf;
	}
	public void setGrzf(String grzf) {
		this.grzf = grzf;
	}
	public String getZfbl() {
		return zfbl;
	}
	public void setZfbl(String zfbl) {
		this.zfbl = zfbl;
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
	public String getFymxlsh() {
		return fymxlsh;
	}
	public void setFymxlsh(String fymxlsh) {
		this.fymxlsh = fymxlsh;
	}
	public String getYzh() {
		return yzh;
	}
	public void setYzh(String yzh) {
		this.yzh = yzh;
	}
	public String getYznr() {
		return yznr;
	}
	public void setYznr(String yznr) {
		this.yznr = yznr;
	}
	public String getZxcs() {
		return zxcs;
	}
	public void setZxcs(String zxcs) {
		this.zxcs = zxcs;
	}
	public String getYlbxlx() {
		return ylbxlx;
	}
	public void setYlbxlx(String ylbxlx) {
		this.ylbxlx = ylbxlx;
	}
	public String getYebz() {
		return yebz;
	}
	public void setYebz(String yebz) {
		this.yebz = yebz;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
	public String getSourceId() {
		return fyid;
	}
	public String getCflsh() {
		return cflsh;
	}
	public void setCflsh(String cflsh) {
		this.cflsh = cflsh;
	}
	public String getMzfyflDm() {
		return mzfyflDm;
	}
	public void setMzfyflDm(String mzfyflDm) {
		this.mzfyflDm = mzfyflDm;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getXmdm() {
		return xmdm;
	}
	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}
	public String getMxxmdw() {
		return mxxmdw;
	}
	public void setMxxmdw(String mxxmdw) {
		this.mxxmdw = mxxmdw;
	}
	public String getMxxmdj() {
		return mxxmdj;
	}
	public void setMxxmdj(String mxxmdj) {
		this.mxxmdj = mxxmdj;
	}
	public String getMxxmsl() {
		return mxxmsl;
	}
	public void setMxxmsl(String mxxmsl) {
		this.mxxmsl = mxxmsl;
	}
	public String getMxxmje() {
		return mxxmje;
	}
	public void setMxxmje(String mxxmje) {
		this.mxxmje = mxxmje;
	}
	public String getMxxmybfwwzfze() {
		return mxxmybfwwzfze;
	}
	public void setMxxmybfwwzfze(String mxxmybfwwzfze) {
		this.mxxmybfwwzfze = mxxmybfwwzfze;
	}
	public String getMxxmybfnflzfje() {
		return mxxmybfnflzfje;
	}
	public void setMxxmybfnflzfje(String mxxmybfnflzfje) {
		this.mxxmybfnflzfje = mxxmybfnflzfje;
	}
	public String getStbzDm() {
		return stbzDm;
	}
	public void setStbzDm(String stbzDm) {
		this.stbzDm = stbzDm;
	}
	public String getCfmxhm() {
		return cfmxhm;
	}
	public void setCfmxhm(String cfmxhm) {
		this.cfmxhm = cfmxhm;
	}
	public String getYbxe() {
		return ybxe;
	}
	public void setYbxe(String ybxe) {
		this.ybxe = ybxe;
	}
	
}
