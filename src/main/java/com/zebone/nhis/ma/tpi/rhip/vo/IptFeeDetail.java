package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.*;

/**
 * 在/出院费用明细表
 *
 * @author chengjia
 */
@XmlRootElement(name = "Ipt_FeeDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptFeeDetail {

    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "GG")
    protected String gg;
    @XmlElement(name = "FYID", required = true)
    protected String fyid;
    @XmlElement(name = "SFMLLB")
    protected String sfmllb;
    @XmlElement(name = "YBSFXMBM")
    protected String ybsfxmbm;
    @XmlElement(name = "YBSFXMMC")
    protected String ybsfxmmc;
    @XmlElement(name = "YLXMLB", required = true)
    protected String ylxmlb;
    @XmlElement(name = "YLXMLBMC", required = true)
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
    @XmlElement(name = "YBJSFWFYZJE")
    protected String ybjsfwfyzje;
    @XmlElement(name = "GRZF")
    protected String grzf;
    @XmlElement(name = "ZFBL")
    protected String zfbl;
    @XmlElement(name = "SFXMBM")
    protected String sfxmbm;
    @XmlElement(name = "YZID")
    protected String yzid;
    @XmlElement(name = "LSH")
    protected String lsh;
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
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "ZYSFFSRQ")
    protected String zysffsrq;
    @XmlElement(name = "STBZ_DM")
    protected String stbzDm;
    @XmlElement(name = "ZYFYFL_DM")
    protected String zyfyflDm;
    @XmlElement(name = "MXXMYBFNFLZFJE")
    protected String mxxmybfnflzfje;
    @XmlElement(name = "YZXMMXID")
    protected String yzxmmxid;
    @XmlElement(name = "DW")
    protected String dw;
    @XmlElement(name = "XMDM")
    protected String xmdm;
    @XmlElement(name = "XMMC")
    protected String xmmc;
    @XmlElement(name = "MXXMYBFWWZFZE")
    protected String mxxmybfwwzfze;
    @XmlElement(name = "YBXE")
    protected String ybxe;
    @XmlElement(name = "SFYP_BS")
    protected String sfyp_bs;
    @XmlElement(name = "YPDM")
    protected String ypdm;
    @XmlElement(name = "YPMC")
    protected String ypmc;
    @XmlElement(name = "FWXMDM")
    protected String fwxmdm;
    @XmlElement(name = "FWXMMC")
    protected String fwxmmc;
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


    public String getJzlsh() {
        return jzlsh;
    }

    public void setJzlsh(String jzlsh) {
        this.jzlsh = jzlsh;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
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

    public String getSfxmbm() {
        return sfxmbm;
    }

    public void setSfxmbm(String sfxmbm) {
        this.sfxmbm = sfxmbm;
    }

    public String getYzid() {
        return yzid;
    }

    public void setYzid(String yzid) {
        this.yzid = yzid;
    }

    public String getLsh() {
        return lsh;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceId() {
        return fyid;
    }

    public String getZysffsrq() {
        return zysffsrq;
    }

    public void setZysffsrq(String zysffsrq) {
        this.zysffsrq = zysffsrq;
    }

    public String getStbzDm() {
        return stbzDm;
    }

    public void setStbzDm(String stbzDm) {
        this.stbzDm = stbzDm;
    }

    public String getZyfyflDm() {
        return zyfyflDm;
    }

    public void setZyfyflDm(String zyfyflDm) {
        this.zyfyflDm = zyfyflDm;
    }

    public String getMxxmybfnflzfje() {
        return mxxmybfnflzfje;
    }

    public void setMxxmybfnflzfje(String mxxmybfnflzfje) {
        this.mxxmybfnflzfje = mxxmybfnflzfje;
    }

    public String getYzxmmxid() {
        return yzxmmxid;
    }

    public void setYzxmmxid(String yzxmmxid) {
        this.yzxmmxid = yzxmmxid;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getXmdm() {
        return xmdm;
    }

    public void setXmdm(String xmdm) {
        this.xmdm = xmdm;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getMxxmybfwwzfze() {
        return mxxmybfwwzfze;
    }

    public void setMxxmybfwwzfze(String mxxmybfwwzfze) {
        this.mxxmybfwwzfze = mxxmybfwwzfze;
    }

    public String getYbxe() {
        return ybxe;
    }

    public void setYbxe(String ybxe) {
        this.ybxe = ybxe;
    }

    public String getSfyp_bs() {
        return sfyp_bs;
    }

    public void setSfyp_bs(String sfyp_bs) {
        this.sfyp_bs = sfyp_bs;
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
