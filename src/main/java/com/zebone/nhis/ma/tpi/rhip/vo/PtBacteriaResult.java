package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  培养结果
 */
@XmlRootElement(name = "Pt_BacteriaResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtBacteriaResult {
    @XmlElement(name = "XJXH")
    protected String xjxh;
    @XmlElement(name = "ZJTS")
    protected String zjts;
    @XmlElement(name = "XJID", required = true)
    protected String xjid;
    @XmlElement(name = "XJJGLSH", required = true)
    protected String xjjglsh;
    @XmlElement(name = "XJMC", required = true)
    protected String xjmc;
    @XmlElement(name = "JLJS")
    protected String jljs;
    @XmlElement(name = "PYJ")
    protected String pyj;
    @XmlElement(name = "YQMC")
    protected String yqmc;
    @XmlElement(name = "YQBH")
    protected String yqbh;
    @XmlElement(name = "SBBM")
    protected String sbbm;
    @XmlElement(name = "XJLB")
    protected String xjlb;
    @XmlElement(name = "JCJGWZXS")
    protected String jcjgwzxs;
    @XmlElement(name = "JCJG")
    protected String jcjg;
    @XmlElement(name = "FXSF")
    protected String fxsf;
    @XmlElement(name = "PYTJ")
    protected String pytj;
    @XmlElement(name = "PYSJ")
    protected String pysj;
    @XmlElement(name = "XJBGRQ")
    protected String xjbgrq;
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


    @XmlElementWrapper(name="Pt_AllergyResults")
    @XmlElement(name = "Pt_AllergyResult")
    private List<PtAllergyResult> Pt_AllergyResults;

	public String getXjxh() {
		return xjxh;
	}

	public void setXjxh(String xjxh) {
		this.xjxh = xjxh;
	}

	public String getZjts() {
		return zjts;
	}

	public void setZjts(String zjts) {
		this.zjts = zjts;
	}

	public String getXjid() {
		return xjid;
	}

	public void setXjid(String xjid) {
		this.xjid = xjid;
	}

	public String getXjjglsh() {
		return xjjglsh;
	}

	public void setXjjglsh(String xjjglsh) {
		this.xjjglsh = xjjglsh;
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

	public String getPyj() {
		return pyj;
	}

	public void setPyj(String pyj) {
		this.pyj = pyj;
	}

	public String getYqmc() {
		return yqmc;
	}

	public void setYqmc(String yqmc) {
		this.yqmc = yqmc;
	}

	public String getYqbh() {
		return yqbh;
	}

	public void setYqbh(String yqbh) {
		this.yqbh = yqbh;
	}

	public String getSbbm() {
		return sbbm;
	}

	public void setSbbm(String sbbm) {
		this.sbbm = sbbm;
	}

	public String getXjlb() {
		return xjlb;
	}

	public void setXjlb(String xjlb) {
		this.xjlb = xjlb;
	}

	public String getJcjgwzxs() {
		return jcjgwzxs;
	}

	public void setJcjgwzxs(String jcjgwzxs) {
		this.jcjgwzxs = jcjgwzxs;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getFxsf() {
		return fxsf;
	}

	public void setFxsf(String fxsf) {
		this.fxsf = fxsf;
	}

	public String getPytj() {
		return pytj;
	}

	public void setPytj(String pytj) {
		this.pytj = pytj;
	}

	public String getPysj() {
		return pysj;
	}

	public void setPysj(String pysj) {
		this.pysj = pysj;
	}

	public List<PtAllergyResult> getPt_AllergyResults() {
		return Pt_AllergyResults;
	}

	public void setPt_AllergyResults(List<PtAllergyResult> pt_AllergyResults) {
		Pt_AllergyResults = pt_AllergyResults;
	}

	public String getXjbgrq() {
		return xjbgrq;
	}

	public void setXjbgrq(String xjbgrq) {
		this.xjbgrq = xjbgrq;
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
