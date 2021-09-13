package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  PMC_DrugStock 药房库存
 * @author kong
 *
 */
@XmlRootElement(name = "PMC_DrugStock")
@XmlAccessorType(XmlAccessType.FIELD)
public class PMCDrugStock {

	@XmlElement(name = "LSH", required = true)
    protected String lsh;
    @XmlElement(name = "XM_BM", required = true)
    protected String xm_bm;
    @XmlElement(name = "XM_MC", required = true)
    protected String xm_mc;
    @XmlElement(name = "RKRQ", required = true)
    protected String rkrq;
    @XmlElement(name = "CKRQ")
    protected String ckrq;
    @XmlElement(name = "GG", required = true)
    protected String gg;
    @XmlElement(name = "ZXDW_DM")
    protected String zxdw_dm;
    @XmlElement(name = "ZXDW_MC")
    protected String zxdw_mc;
    @XmlElement(name = "YFCFXS")
    protected String yfcfxs;
    @XmlElement(name = "YKDW")
    protected String ykdw;
    @XmlElement(name = "SCPH")
    protected String scph;
    @XmlElement(name = "YPLX_DM")
    protected String yplx_dm;
    @XmlElement(name = "YPLX_MC", required = true)
    protected String yplx_mc;
    @XmlElement(name = "KCSL", required = true)
    protected String kcsl;
    @XmlElement(name = "JHJG", required = true)
    protected String jhjg;
    @XmlElement(name = "LSJ", required = true)
    protected String lsj;
    @XmlElement(name = "KCJE")
    protected String kcje;
    @XmlElement(name = "JBYPBZ", required = true)
    protected String jbypbz;
    @XmlElement(name = "YPSXRQ", required = true)
    protected String ypsxrq;
    @XmlElement(name = "PFYPJG", required = true)
    protected String pfypjg;
    @XmlElement(name = "YPBZBS")
    protected String ypbzbs;
    @XmlElement(name = "YPBZDW")
    protected String ypbzdw;
    @XmlElement(name = "YPCD")
    protected String ypcd;
    @XmlElement(name = "YPCKSL")
    protected String ypcksl;
    @XmlElement(name = "YPKCBH")
    protected String ypkcbh;
    @XmlElement(name = "YPKCDW")
    protected String ypkcdw;
    @XmlElement(name = "YPRKSL")
    protected String yprksl;
    @XmlElement(name = "YPSZKF_BH")
    protected String ypszkf_bh;
    @XmlElement(name = "YPSZKF_MC")
    protected String ypszkf_mc;
    @XmlElement(name = "YPDLFL_BM")
    protected String ypdlfl_bm;
    @XmlElement(name = "YPDLFL_MC")
    protected String ypdlfl_mc;
    @XmlElement(name = "YPLB_DM", required = true)
    protected String yplb_dm;
    @XmlElement(name = "YPLB_MC")
    protected String yplb_mc;
    @XmlElement(name = "YPGGDMS")
    protected String ypggdms;
    @XmlElement(name = "YPDJL")
    protected String ypdjl;
    @XmlElement(name = "YPDMC", required = true)
    protected String ypdmc;
    @XmlElement(name = "YPDPH", required = true)
    protected String ypdph;
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
	public String getCkrq() {
		return ckrq;
	}
	public void setCkrq(String ckrq) {
		this.ckrq = ckrq;
	}
	public String getGg() {
		return gg;
	}
	public void setGg(String gg) {
		this.gg = gg;
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
	public String getJbypbz() {
		return jbypbz;
	}
	public void setJbypbz(String jbypbz) {
		this.jbypbz = jbypbz;
	}
	public String getYpsxrq() {
		return ypsxrq;
	}
	public void setYpsxrq(String ypsxrq) {
		this.ypsxrq = ypsxrq;
	}
	public String getPfypjg() {
		return pfypjg;
	}
	public void setPfypjg(String pfypjg) {
		this.pfypjg = pfypjg;
	}
	public String getYpbzbs() {
		return ypbzbs;
	}
	public void setYpbzbs(String ypbzbs) {
		this.ypbzbs = ypbzbs;
	}
	public String getYpbzdw() {
		return ypbzdw;
	}
	public void setYpbzdw(String ypbzdw) {
		this.ypbzdw = ypbzdw;
	}
	public String getYpcd() {
		return ypcd;
	}
	public void setYpcd(String ypcd) {
		this.ypcd = ypcd;
	}
	public String getYpcksl() {
		return ypcksl;
	}
	public void setYpcksl(String ypcksl) {
		this.ypcksl = ypcksl;
	}
	public String getYpkcbh() {
		return ypkcbh;
	}
	public void setYpkcbh(String ypkcbh) {
		this.ypkcbh = ypkcbh;
	}
	public String getYpkcdw() {
		return ypkcdw;
	}
	public void setYpkcdw(String ypkcdw) {
		this.ypkcdw = ypkcdw;
	}
	public String getYprksl() {
		return yprksl;
	}
	public void setYprksl(String yprksl) {
		this.yprksl = yprksl;
	}
	public String getYpszkf_bh() {
		return ypszkf_bh;
	}
	public void setYpszkf_bh(String ypszkf_bh) {
		this.ypszkf_bh = ypszkf_bh;
	}
	public String getYpszkf_mc() {
		return ypszkf_mc;
	}
	public void setYpszkf_mc(String ypszkf_mc) {
		this.ypszkf_mc = ypszkf_mc;
	}
	public String getYpdlfl_bm() {
		return ypdlfl_bm;
	}
	public void setYpdlfl_bm(String ypdlfl_bm) {
		this.ypdlfl_bm = ypdlfl_bm;
	}
	public String getYpdlfl_mc() {
		return ypdlfl_mc;
	}
	public void setYpdlfl_mc(String ypdlfl_mc) {
		this.ypdlfl_mc = ypdlfl_mc;
	}
	public String getYplb_dm() {
		return yplb_dm;
	}
	public void setYplb_dm(String yplb_dm) {
		this.yplb_dm = yplb_dm;
	}
	public String getYplb_mc() {
		return yplb_mc;
	}
	public void setYplb_mc(String yplb_mc) {
		this.yplb_mc = yplb_mc;
	}
	public String getYpggdms() {
		return ypggdms;
	}
	public void setYpggdms(String ypggdms) {
		this.ypggdms = ypggdms;
	}
	public String getYpdjl() {
		return ypdjl;
	}
	public void setYpdjl(String ypdjl) {
		this.ypdjl = ypdjl;
	}
	public String getYpdmc() {
		return ypdmc;
	}
	public void setYpdmc(String ypdmc) {
		this.ypdmc = ypdmc;
	}
	public String getYpdph() {
		return ypdph;
	}
	public void setYpdph(String ypdph) {
		this.ypdph = ypdph;
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
