package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  PMC_DrugStore 药房出库
 * @author kong
 *
 */
@XmlRootElement(name = "PMC_DrugStore")
@XmlAccessorType(XmlAccessType.FIELD)
public class PMCDrugStore {

	
	@XmlElement(name = "LSH", required = true)
    protected String lsh;
    @XmlElement(name = "CKRQ", required = true)
    protected String ckrq;
    @XmlElement(name = "KSBM")
    protected String ksbm;
    @XmlElement(name = "KSMC")
    protected String ksmc;
    @XmlElement(name = "XMLX_DM")
    protected String xmlx_dm;
    @XmlElement(name = "XMLX_MC")
    protected String xmlx_mc;
    @XmlElement(name = "XM_BM")
    protected String xm_bm;
    @XmlElement(name = "XM_MC")
    protected String xm_mc;
    @XmlElement(name = "ZXDW_DM")
    protected String zxdw_dm;
    @XmlElement(name = "ZXDW_MC")
    protected String zxdw_mc;
    @XmlElement(name = "CKSL")
    protected String cksl;
    @XmlElement(name = "JKJG")
    protected String jkjg;
    @XmlElement(name = "LSJ")
    protected String lsj;
    @XmlElement(name = "CKJE")
    protected String ckje;
    @XmlElement(name = "ZXJJ")
    protected String zxjj;
    @XmlElement(name = "CKDH")
    protected String ckdh;
    @XmlElement(name = "DJSJ")
    protected String djsj;
    @XmlElement(name = "DJRYGH")
    protected String djrygh;
    @XmlElement(name = "DJRYMC")
    protected String djrymc;
    @XmlElement(name = "ZHXGSJ")
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
	public String getCkrq() {
		return ckrq;
	}
	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
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
	public String getCksl() {
		return cksl;
	}
	public void setCksl(String cksl) {
		this.cksl = cksl;
	}
	public String getJkjg() {
		return jkjg;
	}
	public void setJkjg(String jkjg) {
		this.jkjg = jkjg;
	}
	public String getLsj() {
		return lsj;
	}
	public void setLsj(String lsj) {
		this.lsj = lsj;
	}
	public String getCkje() {
		return ckje;
	}
	public void setCkje(String ckje) {
		this.ckje = ckje;
	}
	public String getZxjj() {
		return zxjj;
	}
	public void setZxjj(String zxjj) {
		this.zxjj = zxjj;
	}
	public String getCkdh() {
		return ckdh;
	}
	public void setCkdh(String ckdh) {
		this.ckdh = ckdh;
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
