package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_RecipeDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptRecipeDetail {
    @XmlElement(name = "CFMXHM")
    protected String cfmxhm;
    @XmlElement(name = "XMBH")
    protected String xmbh;
    @XmlElement(name = "XMMC", required = true)
    protected String xmmc;
    @XmlElement(name = "SFYP", required = true)
    protected String sfyp;
    @XmlElement(name = "CDID")
    protected String cdid;
    @XmlElement(name = "CDMC")
    protected String cdmc;
    @XmlElement(name = "ZH")
    protected String zh;
    @XmlElement(name = "ZYBZ", required = true)
    protected String zybz;
     @XmlElement(name = "JXDM")
    protected String jxdm;
    @XmlElement(name = "YPTYM")
    protected String yptym;
    @XmlElement(name = "YPSPM")
    protected String ypspm;
    @XmlElement(name = "YPGG")
    protected String ypgg;
    @XmlElement(name = "YPGGDW")
    protected String ypggdw;
    @XmlElement(name = "YPGGXS")
    protected String ypggxs;
    @XmlElement(name = "FYSL", required = true)
    protected String fysl;
    @XmlElement(name = "YYPD")
    protected String yypd;
    @XmlElement(name = "TJMC")
    protected String tjmc;
    @XmlElement(name = "TJDM")
    protected String tjdm;
    @XmlElement(name = "YYTS")
    protected String yyts;
    @XmlElement(name = "JL")
    protected String jl;
    @XmlElement(name = "DW")
    protected String dw;
    @XmlElement(name = "MCSL")
    protected String mcsl;
    @XmlElement(name = "MCDW")
    protected String mcdw;
    @XmlElement(name = "TS")
    protected String ts;
    @XmlElement(name = "ZE")
    protected String ze;
    @XmlElement(name = "DJ")
    protected String dj;
    @XmlElement(name = "YPTYBM")
    protected String yptybm;
    @XmlElement(name = "YPPZWH")
    protected String yppzwh;
    @XmlElement(name = "YPYXQ")
    protected String ypyxq;
    @XmlElement(name = "YWLX_DM")
    protected String ywlxdm;
    @XmlElement(name = "YWLX")
    protected String ywlx;
    @XmlElement(name = "HDYSGH")
    protected String hdysgh;
    @XmlElement(name = "HDYSXM")
    protected String hdysxm;
    @XmlElement(name = "FYYSGH")
    protected String fyysgh;
    @XmlElement(name = "FYYSXM")
    protected String fyysxm;
    @XmlElement(name = "YZZXKSBM")
    protected String yzzxksbm;
    @XmlElement(name = "YZZXKSMC")
    protected String yzzxksmc;
    @XmlElement(name = "YZZXRGH")
    protected String yzzxrgh;
    @XmlElement(name = "YZZXRXM")
    protected String yzzxrxm;
    @XmlElement(name = "YZZZSJ")
    protected String yzzzsj;
    @XmlElement(name = "ZYLBDM")
    protected String zylbdm;
    @XmlElement(name = "YZXMLXBM")
    protected String yzxmlxbm;
    @XmlElement(name = "YPDM")
    protected String ypdm;
    @XmlElement(name = "YPMC")
    protected String ypmc;
    @XmlElement(name = "FWXMDM")
    protected String fwxmdm;    
    @XmlElement(name = "FWXMMC")
    protected String fwxmmc;
    @XmlElement(name = "ZYJS")
    protected String zyjs;
    @XmlElement(name = "FYSLDW_DM")
    protected String fysldwDm;
    @XmlElement(name = "YPYF")
    protected String ypyf;
    @XmlElement(name = "ZJL")
    protected String zjl;
    @XmlElement(name = "ZJLDW")
    protected String zjldw;
    @XmlElement(name = "SFPSBS")
    protected String sfpsbs;
    @XmlElement(name = "YPSL")
    protected String ypsl;
    @XmlElement(name = "ZYJZF")
    protected String zyjzf;
    @XmlElement(name = "JCBW")
    protected String jcbw;
    @XmlElement(name = "BZXX")
    protected String bzxx;
    @XmlElement(name = "TPYSGH")
    protected String tpysgh;
    @XmlElement(name = "TPYSXM")
    protected String tpysxm;
    @XmlElement(name = "YWFZY")
    protected String ywfzy;
    
    
	public String getCfmxhm() {
		return cfmxhm;
	}
	public void setCfmxhm(String cfmxhm) {
		this.cfmxhm = cfmxhm;
	}
	public String getXmbh() {
		return xmbh;
	}
	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getSfyp() {
		return sfyp;
	}
	public void setSfyp(String sfyp) {
		this.sfyp = sfyp;
	}
	public String getCdid() {
		return cdid;
	}
	public void setCdid(String cdid) {
		this.cdid = cdid;
	}
	public String getCdmc() {
		return cdmc;
	}
	public void setCdmc(String cdmc) {
		this.cdmc = cdmc;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getZybz() {
		return zybz;
	}
	public void setZybz(String zybz) {
		this.zybz = zybz;
	}
	public String getJxdm() {
		return jxdm;
	}
	public void setJxdm(String jxdm) {
		this.jxdm = jxdm;
	}
	public String getYptym() {
		return yptym;
	}
	public void setYptym(String yptym) {
		this.yptym = yptym;
	}
	public String getYpspm() {
		return ypspm;
	}
	public void setYpspm(String ypspm) {
		this.ypspm = ypspm;
	}
	public String getYpgg() {
		return ypgg;
	}
	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}
	public String getYpggdw() {
		return ypggdw;
	}
	public void setYpggdw(String ypggdw) {
		this.ypggdw = ypggdw;
	}
	public String getYpggxs() {
		return ypggxs;
	}
	public void setYpggxs(String ypggxs) {
		this.ypggxs = ypggxs;
	}
	public String getFysl() {
		return fysl;
	}
	public void setFysl(String fysl) {
		this.fysl = fysl;
	}
	public String getYypd() {
		return yypd;
	}
	public void setYypd(String yypd) {
		this.yypd = yypd;
	}
	public String getTjmc() {
		return tjmc;
	}
	public void setTjmc(String tjmc) {
		this.tjmc = tjmc;
	}
	public String getTjdm() {
		return tjdm;
	}
	public void setTjdm(String tjdm) {
		this.tjdm = tjdm;
	}
	public String getYyts() {
		return yyts;
	}
	public void setYyts(String yyts) {
		this.yyts = yyts;
	}
	public String getJl() {
		return jl;
	}
	public void setJl(String jl) {
		this.jl = jl;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getMcsl() {
		return mcsl;
	}
	public void setMcsl(String mcsl) {
		this.mcsl = mcsl;
	}
	public String getMcdw() {
		return mcdw;
	}
	public void setMcdw(String mcdw) {
		this.mcdw = mcdw;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getZe() {
		return ze;
	}
	public void setZe(String ze) {
		this.ze = ze;
	}
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getYptybm() {
		return yptybm;
	}
	public void setYptybm(String yptybm) {
		this.yptybm = yptybm;
	}
	public String getYppzwh() {
		return yppzwh;
	}
	public void setYppzwh(String yppzwh) {
		this.yppzwh = yppzwh;
	}
	public String getYpyxq() {
		return ypyxq;
	}
	public void setYpyxq(String ypyxq) {
		this.ypyxq = ypyxq;
	}
	public String getYwlxdm() {
		return ywlxdm;
	}
	public void setYwlxdm(String ywlxdm) {
		this.ywlxdm = ywlxdm;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getHdysgh() {
		return hdysgh;
	}
	public void setHdysgh(String hdysgh) {
		this.hdysgh = hdysgh;
	}
	public String getHdysxm() {
		return hdysxm;
	}
	public void setHdysxm(String hdysxm) {
		this.hdysxm = hdysxm;
	}
	public String getFyysgh() {
		return fyysgh;
	}
	public void setFyysgh(String fyysgh) {
		this.fyysgh = fyysgh;
	}
	public String getYzzxksbm() {
		return yzzxksbm;
	}
	public void setYzzxksbm(String yzzxksbm) {
		this.yzzxksbm = yzzxksbm;
	}
	public String getYzzxksmc() {
		return yzzxksmc;
	}
	public void setYzzxksmc(String yzzxksmc) {
		this.yzzxksmc = yzzxksmc;
	}
	public String getYzzxrgh() {
		return yzzxrgh;
	}
	public void setYzzxrgh(String yzzxrgh) {
		this.yzzxrgh = yzzxrgh;
	}
	public String getYzzxrxm() {
		return yzzxrxm;
	}
	public void setYzzxrxm(String yzzxrxm) {
		this.yzzxrxm = yzzxrxm;
	}
	public String getFyysxm() {
		return fyysxm;
	}
	public void setFyysxm(String fyysxm) {
		this.fyysxm = fyysxm;
	}
	public String getYzzzsj() {
		return yzzzsj;
	}
	public void setYzzzsj(String yzzzsj) {
		this.yzzzsj = yzzzsj;
	}
	public String getZylbdm() {
		return zylbdm;
	}
	public void setZylbdm(String zylbdm) {
		this.zylbdm = zylbdm;
	}
	public String getYzxmlxbm() {
		return yzxmlxbm;
	}
	public void setYzxmlxbm(String yzxmlxbm) {
		this.yzxmlxbm = yzxmlxbm;
	}
	public String getYpdm() {
		return ypdm;
	}
	public void setYpdm(String ypdm) {
		this.ypdm = ypdm;
	}
	public String getYpmc() {
		return ypmc;
	}
	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}
	public String getFwxmdm() {
		return fwxmdm;
	}
	public void setFwxmdm(String fwxmdm) {
		this.fwxmdm = fwxmdm;
	}
	public String getFwxmmc() {
		return fwxmmc;
	}
	public void setFwxmmc(String fwxmmc) {
		this.fwxmmc = fwxmmc;
	}
	public String getZyjs() {
		return zyjs;
	}
	public void setZyjs(String zyjs) {
		this.zyjs = zyjs;
	}
	public String getFysldwDm() {
		return fysldwDm;
	}
	public void setFysldwDm(String fysldwDm) {
		this.fysldwDm = fysldwDm;
	}
	public String getYpyf() {
		return ypyf;
	}
	public void setYpyf(String ypyf) {
		this.ypyf = ypyf;
	}
	public String getZjl() {
		return zjl;
	}
	public void setZjl(String zjl) {
		this.zjl = zjl;
	}
	public String getZjldw() {
		return zjldw;
	}
	public void setZjldw(String zjldw) {
		this.zjldw = zjldw;
	}
	public String getSfpsbs() {
		return sfpsbs;
	}
	public void setSfpsbs(String sfpsbs) {
		this.sfpsbs = sfpsbs;
	}
	public String getYpsl() {
		return ypsl;
	}
	public void setYpsl(String ypsl) {
		this.ypsl = ypsl;
	}
	public String getZyjzf() {
		return zyjzf;
	}
	public void setZyjzf(String zyjzf) {
		this.zyjzf = zyjzf;
	}
	public String getJcbw() {
		return jcbw;
	}
	public void setJcbw(String jcbw) {
		this.jcbw = jcbw;
	}
	public String getBzxx() {
		return bzxx;
	}
	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}
	public String getTpysgh() {
		return tpysgh;
	}
	public void setTpysgh(String tpysgh) {
		this.tpysgh = tpysgh;
	}
	public String getTpysxm() {
		return tpysxm;
	}
	public void setTpysxm(String tpysxm) {
		this.tpysxm = tpysxm;
	}
	public String getYwfzy() {
		return ywfzy;
	}
	public void setYwfzy(String ywfzy) {
		this.ywfzy = ywfzy;
	}
    
    
}
