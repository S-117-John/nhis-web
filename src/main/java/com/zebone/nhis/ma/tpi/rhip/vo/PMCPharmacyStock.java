package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  PMC_PharmacyStock 药库库存
 * @author kong
 *
 */
@XmlRootElement(name = "PMC_PharmacyStock")
@XmlAccessorType(XmlAccessType.FIELD)
public class PMCPharmacyStock {

	@XmlElement(name = "LSH", required = true)
    protected String lsh;
	@XmlElement(name = "LX_DM", required = true)
    protected String lx_dm;
	@XmlElement(name = "LX_MC", required = true)
    protected String lx_mc;
    @XmlElement(name = "XM_BM", required = true)
    protected String xm_bm;
    @XmlElement(name = "XM_MC", required = true)
    protected String xm_mc;
    @XmlElement(name = "GG", required = true)
    protected String gg;
    @XmlElement(name = "RKRQ", required = true)
    protected String rkrq;
    @XmlElement(name = "CKRQ")
    protected String ckrq;
    @XmlElement(name = "ZXDW_DM")
    protected String zxdw_dm;
    @XmlElement(name = "ZXDW_MC")
    protected String zxdw_mc;
    @XmlElement(name = "YFCFXS")
    protected String yfcfxs;
    @XmlElement(name = "YKDW")
    protected String ykdw;
    @XmlElement(name = "YPLX_DM", required = true)
    protected String yplx_dm;
    @XmlElement(name = "YPLX_MC", required = true)
    protected String yplx_mc;
    @XmlElement(name = "KCSL", required = true)
    protected String kcsl;
    @XmlElement(name = "JHJG", required = true)
    protected String jhjg;
    @XmlElement(name = "LSJ", required = true)
    protected String lsj;
    @XmlElement(name = "KCJE", required = true)
    protected String kcje;
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
	public String getLx_dm() {
		return lx_dm;
	}
	public void setLx_dm(String lx_dm) {
		this.lx_dm = lx_dm;
	}
	public String getLx_mc() {
		return lx_mc;
	}
	public void setLx_mc(String lx_mc) {
		this.lx_mc = lx_mc;
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
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
	}
	public String getRkrq() {
		return rkrq;
	}
	public void setRkrq(String rkrq) {
		this.rkrq = rkrq;
	}
	public String getCkrq() {
		return ckrq;
	}
	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
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
	public String getYfcfxs() {
		return yfcfxs;
	}
	public void setYfcfxs(String yfcfxs) {
		this.yfcfxs = yfcfxs;
	}
	public String getYkdw() {
		return ykdw;
	}
	public void setYkdw(String ykdw) {
		this.ykdw = ykdw;
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
	public String getKcsl() {
		return kcsl;
	}
	public void setKcsl(String kcsl) {
		this.kcsl = kcsl;
	}
	public String getJhjg() {
		return jhjg;
	}
	public void setJhjg(String jhjg) {
		this.jhjg = jhjg;
	}
	public String getLsj() {
		return lsj;
	}
	public void setLsj(String lsj) {
		this.lsj = lsj;
	}
	public String getKcje() {
		return kcje;
	}
	public void setKcje(String kcje) {
		this.kcje = kcje;
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
