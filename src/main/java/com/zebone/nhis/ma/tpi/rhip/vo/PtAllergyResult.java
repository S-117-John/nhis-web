package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 药敏结果
 */
@XmlRootElement(name = "Pt_AllergyResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtAllergyResult {
    @XmlElement(name = "DYXH")
    protected String dyxh;
    @XmlElement(name = "YMBGRQ", required = true)
    protected String ymbgrq;
    @XmlElement(name = "CKFW", required = true)
    protected String ckfw;
    @XmlElement(name = "YMJGLSH", required = true)
    protected String ymjglsh;
    @XmlElement(name = "JCFF", required = true)
    protected String jcff;
    @XmlElement(name = "YHHZJ")
    protected String yhhzj;
    @XmlElement(name = "JYND")
    protected String jynd;
    @XmlElement(name = "ZPHYL")
    protected String zphyl;
    @XmlElement(name = "JCJGMS", required = true)
    protected String jcjgms;
    @XmlElement(name = "YMDM", required = true)
    protected String ymdm;
    @XmlElement(name = "YMMC", required = true)
    protected String ymmc;
    @XmlElement(name = "XJDH", required = true)
    protected String xjdh;
    @XmlElement(name = "XJJGLSH", required = true)
    protected String xjjglsh;
    @XmlElement(name = "ZPHYLDW")
    protected String zphyldw;
    @XmlElement(name = "SYBXH")
    protected String sybxh;
    @XmlElement(name = "XJID")
    protected String xjid;
    @XmlElement(name = "XJBGRQ")
    protected String xjbgrq;
    @XmlElement(name = "ZJTS")
    protected String zjts;
    @XmlElement(name = "XJMC")
    protected String xjmc;
    @XmlElement(name = "JLJS")
    protected String jljs;
    @XmlElement(name = "XJXH")
    protected String xjxh;
    @XmlElement(name = "PYSJ")
    protected String pysj;
    @XmlElement(name = "PYTJ")
    protected String pytj;
    @XmlElement(name = "FXSF")
    protected String fxsf;
    @XmlElement(name = "YQBH")
    protected String yqbh;
    @XmlElement(name = "JCJG")
    protected String jcjg;
    @XmlElement(name = "YQMC")
    protected String yqmc;
    @XmlElement(name = "JCJGWZXS")
    protected String jcjgwzxs;
    @XmlElement(name = "XJLB")
    protected String xjlb;
    @XmlElement(name = "SBBM")
    protected String sbbm;
    @XmlElement(name = "PYJ")
    protected String pyj;
    @XmlElement(name = "XJSYBXH")
    protected String xjsybxh;
    @XmlElement(name = "SYBMC")
    protected String sybmc;
    @XmlElement(name = "PYJYXMMC")
    protected String pyjyxmmc;
    @XmlElement(name = "PYJYXMDM")
    protected String pyjyxmdm;
    @XmlElement(name = "PYJGLSH")
    protected String pyjglsh;
    @XmlElement(name = "XJPYLX")
    protected String xjpylx;
    @XmlElement(name = "XJPYJG")
    protected String xjpyjg;
    @XmlElement(name = "JGLX")
    protected String jglx;

    public String getDyxh() {
        return dyxh;
    }

    public void setDyxh(String dyxh) {
        this.dyxh = dyxh;
    }

    public String getYmbgrq() {
        return ymbgrq;
    }

    public void setYmbgrq(String ymbgrq) {
        this.ymbgrq = ymbgrq;
    }

    public String getCkfw() {
        return ckfw;
    }

    public void setCkfw(String ckfw) {
        this.ckfw = ckfw;
    }

    public String getYmjglsh() {
        return ymjglsh;
    }

    public void setYmjglsh(String ymjglsh) {
        this.ymjglsh = ymjglsh;
    }

    public String getJcff() {
        return jcff;
    }

    public void setJcff(String jcff) {
        this.jcff = jcff;
    }

    public String getYhhzj() {
        return yhhzj;
    }

    public void setYhhzj(String yhhzj) {
        this.yhhzj = yhhzj;
    }

    public String getJynd() {
        return jynd;
    }

    public void setJynd(String jynd) {
        this.jynd = jynd;
    }

    public String getZphyl() {
        return zphyl;
    }

    public void setZphyl(String zphyl) {
        this.zphyl = zphyl;
    }

    public String getJcjgms() {
        return jcjgms;
    }

    public void setJcjgms(String jcjgms) {
        this.jcjgms = jcjgms;
    }

    public String getYmdm() {
        return ymdm;
    }

    public void setYmdm(String ymdm) {
        this.ymdm = ymdm;
    }

    public String getYmmc() {
        return ymmc;
    }

    public void setYmmc(String ymmc) {
        this.ymmc = ymmc;
    }

    public String getXjdh() {
        return xjdh;
    }

    public void setXjdh(String xjdh) {
        this.xjdh = xjdh;
    }

    public String getXjjglsh() {
        return xjjglsh;
    }

    public void setXjjglsh(String xjjglsh) {
        this.xjjglsh = xjjglsh;
    }

    public String getZphyldw() {
        return zphyldw;
    }

    public void setZphyldw(String zphyldw) {
        this.zphyldw = zphyldw;
    }

    public String getSybxh() {
        return sybxh;
    }

    public void setSybxh(String sybxh) {
        this.sybxh = sybxh;
    }

    public String getXjid() {
        return xjid;
    }

    public void setXjid(String xjid) {
        this.xjid = xjid;
    }

    public String getXjbgrq() {
        return xjbgrq;
    }

    public void setXjbgrq(String xjbgrq) {
        this.xjbgrq = xjbgrq;
    }

    public String getZjts() {
        return zjts;
    }

    public void setZjts(String zjts) {
        this.zjts = zjts;
    }

    public String getXjmc() {
        return xjmc;
    }

    public void setXjmc(String xjmc) {
        this.xjmc = xjmc;
    }

    public String getJljs() {
        return jljs;
    }

    public void setJljs(String jljs) {
        this.jljs = jljs;
    }

    public String getXjxh() {
        return xjxh;
    }

    public void setXjxh(String xjxh) {
        this.xjxh = xjxh;
    }

    public String getPysj() {
        return pysj;
    }

    public void setPysj(String pysj) {
        this.pysj = pysj;
    }

    public String getPytj() {
        return pytj;
    }

    public void setPytj(String pytj) {
        this.pytj = pytj;
    }

    public String getFxsf() {
        return fxsf;
    }

    public void setFxsf(String fxsf) {
        this.fxsf = fxsf;
    }

    public String getYqbh() {
        return yqbh;
    }

    public void setYqbh(String yqbh) {
        this.yqbh = yqbh;
    }

    public String getJcjg() {
        return jcjg;
    }

    public void setJcjg(String jcjg) {
        this.jcjg = jcjg;
    }

    public String getYqmc() {
        return yqmc;
    }

    public void setYqmc(String yqmc) {
        this.yqmc = yqmc;
    }

    public String getJcjgwzxs() {
        return jcjgwzxs;
    }

    public void setJcjgwzxs(String jcjgwzxs) {
        this.jcjgwzxs = jcjgwzxs;
    }

    public String getXjlb() {
        return xjlb;
    }

    public void setXjlb(String xjlb) {
        this.xjlb = xjlb;
    }

    public String getSbbm() {
        return sbbm;
    }

    public void setSbbm(String sbbm) {
        this.sbbm = sbbm;
    }

    public String getPyj() {
        return pyj;
    }

    public void setPyj(String pyj) {
        this.pyj = pyj;
    }

    public String getXjsybxh() {
        return xjsybxh;
    }

    public void setXjsybxh(String xjsybxh) {
        this.xjsybxh = xjsybxh;
    }

    public String getSybmc() {
        return sybmc;
    }

    public void setSybmc(String sybmc) {
        this.sybmc = sybmc;
    }

    public String getPyjyxmmc() {
        return pyjyxmmc;
    }

    public void setPyjyxmmc(String pyjyxmmc) {
        this.pyjyxmmc = pyjyxmmc;
    }

    public String getPyjyxmdm() {
        return pyjyxmdm;
    }

    public void setPyjyxmdm(String pyjyxmdm) {
        this.pyjyxmdm = pyjyxmdm;
    }

    public String getPyjglsh() {
        return pyjglsh;
    }

    public void setPyjglsh(String pyjglsh) {
        this.pyjglsh = pyjglsh;
    }

    public String getXjpylx() {
        return xjpylx;
    }

    public void setXjpylx(String xjpylx) {
        this.xjpylx = xjpylx;
    }

    public String getXjpyjg() {
        return xjpyjg;
    }

    public void setXjpyjg(String xjpyjg) {
        this.xjpyjg = xjpyjg;
    }

    public String getJglx() {
        return jglx;
    }

    public void setJglx(String jglx) {
        this.jglx = jglx;
    }

}
