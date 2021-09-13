package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *  PMC_DrugEntry 药房入库
 * @author kong
 *
 */
@XmlRootElement(name = "PMC_DrugEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class PMCDrugEntry {

	@XmlElement(name = "LSH", required = true)
    protected String lsh;
    @XmlElement(name = "KSBM")
    protected String ksbm;
    @XmlElement(name = "KSMC")
    protected String ksmc;
    @XmlElement(name = "XMLX_DM", required = true)
    protected String xmlx_dm;
    @XmlElement(name = "XMLX_MC", required = true)
    protected String xmlx_mc;
    @XmlElement(name = "XM_BM", required = true)
    protected String xm_bm;
    @XmlElement(name = "XM_MC", required = true)
    protected String xm_mc;
    @XmlElement(name = "RKRQ", required = true)
    protected String rkrq;
    @XmlElement(name = "RKLY", required = true)
    protected String rkly;
    @XmlElement(name = "RKSL", required = true)
    protected String rksl;
    @XmlElement(name = "ZXDW_DM")
    protected String zxdw_dm;
    @XmlElement(name = "ZXDW_MC")
    protected String zxdw_mc;
    @XmlElement(name = "JHJG", required = true)
    protected String jhjg;
    @XmlElement(name = "PFJG", required = true)
    protected String pfjg;
    @XmlElement(name = "PFJE", required = true)
    protected String pfje;
    @XmlElement(name = "LSJ", required = true)
    protected String lsj;
    @XmlElement(name = "XSJE", required = true)
    protected String xsje;
    @XmlElement(name = "KFDM", required = true)
    protected String kfdm;
    @XmlElement(name = "KFMC", required = true)
    protected String kfmc;
    @XmlElement(name = "RKDH", required = true)
    protected String rkdh;
    @XmlElement(name = "SCRQ", required = true)
    protected String scrq;
    @XmlElement(name = "SXRQ", required = true)
    protected String sxrq;
    @XmlElement(name = "SCPH", required = true)
    protected String scph;
    @XmlElement(name = "YPLX_DM", required = true)
    protected String yplx_dm;
    @XmlElement(name = "YPLX_MC", required = true)
    protected String yplx_mc;
    @XmlElement(name = "ZDDW_DM")
    protected String zddw_dm;
    @XmlElement(name = "ZDDW_MC")
    protected String zddw_mc;
    @XmlElement(name = "ZDDW")
    protected String zddw;
    @XmlElement(name = "CFXS")
    protected String cfxs;
    @XmlElement(name = "SFJY_DM", required = true)
    protected String sfjy_dm;
    @XmlElement(name = "SFJY_MC", required = true)
    protected String sfjy_mc;
    @XmlElement(name = "SCCS", required = true)
    protected String sccs;
    @XmlElement(name = "DJSJ", required = true)
    protected String djsj;
    @XmlElement(name = "DJRYGH")
    protected String djrygh;
    @XmlElement(name = "DJRYMC")
    protected String djrymc;
    @XmlElement(name = "ZHXGSJ", required = true)
    protected String zhxgsj;
    @XmlElement(name = "ZHXGRYGH")
    protected String zhxgrygh;
    @XmlElement(name = "ZHXGRYMC")
    protected String zhxgrymc;
	public String getLsh() {
		return lsh;
	}
	public void setLsh(String lsh) {
		this.lsh = lsh;
	}
	public String getKsbm() {
		return ksbm;
	}
	public void setKsbm(String ksbm) {
		this.ksbm = ksbm;
	}
	public String getKsmc() {
		return ksmc;
	}
	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}
	public String getXmlx_dm() {
		return xmlx_dm;
	}
	public void setXmlx_dm(String xmlx_dm) {
		this.xmlx_dm = xmlx_dm;
	}
	public String getXmlx_mc() {
		return xmlx_mc;
	}
	public void setXmlx_mc(String xmlx_mc) {
		this.xmlx_mc = xmlx_mc;
	}
	public String getXm_bm() {
		return xm_bm;
	}
	public void setXm_bm(String xm_bm) {
		this.xm_bm = xm_bm;
	}
	public String getXm_mc() {
		return xm_mc;
	}
	public void setXm_mc(String xm_mc) {
		this.xm_mc = xm_mc;
	}
	public String getRkrq() {
		return rkrq;
	}
	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}
	public String getRkly() {
		return rkly;
	}
	public void setRkly(String rkly) {
		this.rkly = rkly;
	}
	public String getRksl() {
		return rksl;
	}
	public void setRksl(String rksl) {
		this.rksl = rksl;
	}
	public String getZxdw_dm() {
		return zxdw_dm;
	}
	public void setZxdw_dm(String zxdw_dm) {
		this.zxdw_dm = zxdw_dm;
	}
	public String getZxdw_mc() {
		return zxdw_mc;
	}
	public void setZxdw_mc(String zxdw_mc) {
		this.zxdw_mc = zxdw_mc;
	}
	public String getJhjg() {
		return jhjg;
	}
	public void setJhjg(String jhjg) {
		this.jhjg = jhjg;
	}
	public String getPfjg() {
		return pfjg;
	}
	public void setPfjg(String pfjg) {
		this.pfjg = pfjg;
	}
	public String getPfje() {
		return pfje;
	}
	public void setPfje(String pfje) {
		this.pfje = pfje;
	}
	public String getLsj() {
		return lsj;
	}
	public void setLsj(String lsj) {
		this.lsj = lsj;
	}
	public String getXsje() {
		return xsje;
	}
	public void setXsje(String xsje) {
		this.xsje = xsje;
	}
	public String getKfdm() {
		return kfdm;
	}
	public void setKfdm(String kfdm) {
		this.kfdm = kfdm;
	}
	public String getKfmc() {
		return kfmc;
	}
	public void setKfmc(String kfmc) {
		this.kfmc = kfmc;
	}
	public String getRkdh() {
		return rkdh;
	}
	public void setRkdh(String rkdh) {
		this.rkdh = rkdh;
	}
	public String getScrq() {
		return scrq;
	}
	public void setScrq(String scrq) {
		this.scrq = scrq;
	}
	public String getSxrq() {
		return sxrq;
	}
	public void setSxrq(String sxrq) {
		this.sxrq = sxrq;
	}
	public String getScph() {
		return scph;
	}
	public void setScph(String scph) {
		this.scph = scph;
	}
	public String getYplx_dm() {
		return yplx_dm;
	}
	public void setYplx_dm(String yplx_dm) {
		this.yplx_dm = yplx_dm;
	}
	public String getYplx_mc() {
		return yplx_mc;
	}
	public void setYplx_mc(String yplx_mc) {
		this.yplx_mc = yplx_mc;
	}
	public String getZddw_dm() {
		return zddw_dm;
	}
	public void setZddw_dm(String zddw_dm) {
		this.zddw_dm = zddw_dm;
	}
	public String getZddw_mc() {
		return zddw_mc;
	}
	public void setZddw_mc(String zddw_mc) {
		this.zddw_mc = zddw_mc;
	}
	public String getZddw() {
		return zddw;
	}
	public void setZddw(String zddw) {
		this.zddw = zddw;
	}
	public String getCfxs() {
		return cfxs;
	}
	public void setCfxs(String cfxs) {
		this.cfxs = cfxs;
	}
	public String getSfjy_dm() {
		return sfjy_dm;
	}
	public void setSfjy_dm(String sfjy_dm) {
		this.sfjy_dm = sfjy_dm;
	}
	public String getSfjy_mc() {
		return sfjy_mc;
	}
	public void setSfjy_mc(String sfjy_mc) {
		this.sfjy_mc = sfjy_mc;
	}
	public String getSccs() {
		return sccs;
	}
	public void setSccs(String sccs) {
		this.sccs = sccs;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public String getDjrygh() {
		return djrygh;
	}
	public void setDjrygh(String djrygh) {
		this.djrygh = djrygh;
	}
	public String getDjrymc() {
		return djrymc;
	}
	public void setDjrymc(String djrymc) {
		this.djrymc = djrymc;
	}
	public String getZhxgsj() {
		return zhxgsj;
	}
	public void setZhxgsj(String zhxgsj) {
		this.zhxgsj = zhxgsj;
	}
	public String getZhxgrygh() {
		return zhxgrygh;
	}
	public void setZhxgrygh(String zhxgrygh) {
		this.zhxgrygh = zhxgrygh;
	}
	public String getZhxgrymc() {
		return zhxgrymc;
	}
	public void setZhxgrymc(String zhxgrymc) {
		this.zhxgrymc = zhxgrymc;
	}
    
    

}
