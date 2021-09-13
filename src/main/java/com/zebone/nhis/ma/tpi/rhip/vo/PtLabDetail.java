package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author chengjia
 *
 */
@XmlRootElement(name = "Pt_LabDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtLabDetail {
    @XmlElement(name = "WJZBZ", required = true)
    protected String wjzbz;
    @XmlElement(name = "XGYZIDHCFXMMXBH")
    protected String xgyzidhcfxmmxbh;
    @XmlElement(name = "SFDM")
    protected String sfdm;
    @XmlElement(name = "SHRBM")
    protected String shrbm;
    @XmlElement(name = "JCRBM")
    protected String jcrbm;
    @XmlElement(name = "SHR")
    protected String shr;
    @XmlElement(name = "JCR")
    protected String jcr;
    @XmlElement(name = "JYXMMC", required = true)
    protected String jyxmmc;
    @XmlElement(name = "JYXMDM")
    protected String jyxmdm;
    @XmlElement(name = "JLDW")
    protected String jldw;
    @XmlElement(name = "JCFF")
    protected String jcff;
    @XmlElement(name = "XSSX")
    protected String xssx;
    @XmlElement(name = "TS")
    protected String ts;
    @XmlElement(name = "LCYY")
    protected String lcyy;
    @XmlElement(name = "CKDX")
    protected String ckdx;
    @XmlElement(name = "JYJG")
    protected String jyjg;
    @XmlElement(name = "JYXMYWMC")
    protected String jyxmywmc;
    @XmlElement(name = "SBM")
    protected String sbm;
    @XmlElement(name = "JYMXLSH")
    protected String jymxlsh;
    @XmlElement(name = "JYFF_DM")
    protected String jyff_dm;
    @XmlElement(name = "JYMXRQ")
    protected String jymxrq;
    @XmlElement(name = "JYBGRQ")
    protected String jybgrq;
    @XmlElement(name = "JYJGDX")
    protected String jyjgdx;
    @XmlElement(name = "JYJGDL")
    protected String jyjgdl;
    @XmlElement(name = "LOINDBM")
    protected String loindbm;
    @XmlElement(name = "YCTS_DM")
    protected String ycts_dm;
    @XmlElement(name = "MXYQBH")
    protected String mxyqbh;
    @XmlElement(name = "MXYQMC")
    protected String mxyqmc;
    @XmlElement(name = "JYZB_DM")
    protected String jyzb_dm;
    @XmlElement(name = "JYDYXH")
    protected String jydyxh;
    @XmlElement(name = "MXBZ")
    protected String mxbz;
    @XmlElement(name = "JYLB_DM")
    protected String jylb_dm;
    @XmlElement(name = "JYRQ1")
    protected String jyrq1;

	public String getWjzbz() {
		return wjzbz;
	}
	public void setWjzbz(String wjzbz) {
		this.wjzbz = wjzbz;
	}
	public String getXgyzidhcfxmmxbh() {
		return xgyzidhcfxmmxbh;
	}
	public void setXgyzidhcfxmmxbh(String xgyzidhcfxmmxbh) {
		this.xgyzidhcfxmmxbh = xgyzidhcfxmmxbh;
	}
	public String getSfdm() {
		return sfdm;
	}
	public void setSfdm(String sfdm) {
		this.sfdm = sfdm;
	}
	public String getShrbm() {
		return shrbm;
	}
	public void setShrbm(String shrbm) {
		this.shrbm = shrbm;
	}
	public String getJcrbm() {
		return jcrbm;
	}
	public void setJcrbm(String jcrbm) {
		this.jcrbm = jcrbm;
	}
	public String getShr() {
		return shr;
	}
	public void setShr(String shr) {
		this.shr = shr;
	}
	public String getJcr() {
		return jcr;
	}
	public void setJcr(String jcr) {
		this.jcr = jcr;
	}
	public String getJyxmmc() {
		return jyxmmc;
	}
	public void setJyxmmc(String jyxmmc) {
		this.jyxmmc = jyxmmc;
	}
	public String getJyxmdm() {
		return jyxmdm;
	}
	public void setJyxmdm(String jyxmdm) {
		this.jyxmdm = jyxmdm;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getJcff() {
		return jcff;
	}
	public void setJcff(String jcff) {
		this.jcff = jcff;
	}
	public String getXssx() {
		return xssx;
	}
	public void setXssx(String xssx) {
		this.xssx = xssx;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getLcyy() {
		return lcyy;
	}
	public void setLcyy(String lcyy) {
		this.lcyy = lcyy;
	}
	public String getCkdx() {
		return ckdx;
	}
	public void setCkdx(String ckdx) {
		this.ckdx = ckdx;
	}
	public String getJyjg() {
		return jyjg;
	}
	public void setJyjg(String jyjg) {
		this.jyjg = jyjg;
	}
	public String getJyxmywmc() {
		return jyxmywmc;
	}
	public void setJyxmywmc(String jyxmywmc) {
		this.jyxmywmc = jyxmywmc;
	}
	public String getSbm() {
		return sbm;
	}
	public void setSbm(String sbm) {
		this.sbm = sbm;
	}
	public String getJymxlsh() {
		return jymxlsh;
	}
	public void setJymxlsh(String jymxlsh) {
		this.jymxlsh = jymxlsh;
	}
	public String getJyff_dm() {
		return jyff_dm;
	}
	public void setJyff_dm(String jyff_dm) {
		this.jyff_dm = jyff_dm;
	}
	public String getJymxrq() {
		return jymxrq;
	}
	public void setJymxrq(String jymxrq) {
		this.jymxrq = jymxrq;
	}
	public String getJybgrq() {
		return jybgrq;
	}
	public void setJybgrq(String jybgrq) {
		this.jybgrq = jybgrq;
	}
	public String getJyjgdx() {
		return jyjgdx;
	}
	public void setJyjgdx(String jyjgdx) {
		this.jyjgdx = jyjgdx;
	}
	public String getJyjgdl() {
		return jyjgdl;
	}
	public void setJyjgdl(String jyjgdl) {
		this.jyjgdl = jyjgdl;
	}

	public String getLoindbm() {
		return loindbm;
	}

	public void setLoindbm(String loindbm) {
		this.loindbm = loindbm;
	}

	public String getYcts_dm() {
		return ycts_dm;
	}

	public void setYcts_dm(String ycts_dm) {
		this.ycts_dm = ycts_dm;
	}

	public String getMxyqbh() {
		return mxyqbh;
	}

	public void setMxyqbh(String mxyqbh) {
		this.mxyqbh = mxyqbh;
	}

	public String getMxyqmc() {
		return mxyqmc;
	}

	public void setMxyqmc(String mxyqmc) {
		this.mxyqmc = mxyqmc;
	}

	public String getJyzb_dm() {
		return jyzb_dm;
	}

	public void setJyzb_dm(String jyzb_dm) {
		this.jyzb_dm = jyzb_dm;
	}

	public String getJydyxh() {
		return jydyxh;
	}

	public void setJydyxh(String jydyxh) {
		this.jydyxh = jydyxh;
	}

	public String getMxbz() {
		return mxbz;
	}

	public void setMxbz(String mxbz) {
		this.mxbz = mxbz;
	}

	public String getJylb_dm() {
		return jylb_dm;
	}

	public void setJylb_dm(String jylb_dm) {
		this.jylb_dm = jylb_dm;
	}

	public String getJyrq1() {
		return jyrq1;
	}

	public void setJyrq1(String jyrq1) {
		this.jyrq1 = jyrq1;
	}
}
