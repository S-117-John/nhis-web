package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 手术记录
 * @author chengjia
 *
 */
@XmlRootElement(name = "Pt_Operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtOperation {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "SSJLLSH", required = true)
    protected String ssjllsh;
    @XmlElement(name = "MZZYBZ", required = true)
    protected String mzzybz;
    @XmlElement(name = "SSQSSJ", required = true)
    protected String ssqssj;
    @XmlElement(name = "SSJSSJ", required = true)
    protected String ssjssj;
    @XmlElement(name = "SSQZD")
    protected String ssqzd;
    @XmlElement(name = "SSHZD")
    protected String sshzd;
    @XmlElement(name = "SSDM")
    protected String ssdm;
    @XmlElement(name = "SSMC", required = true)
    protected String ssmc;
    @XmlElement(name = "SSBWDM", required = true)
    protected String ssbwdm;
    @XmlElement(name = "SSLX", required = true)
    protected String sslx;
    @XmlElement(name = "OperationLevel", required = true)
    protected String operationLevel;
    @XmlElement(name = "SSJGMS")
    protected String ssjgms;
    @XmlElement(name = "MZFF")
    protected String mzff;
    @XmlElement(name = "MZFY")
    protected String mzfy;
    @XmlElement(name = "TCYHDJ", required = true)
    protected String tcyhdj;
    @XmlElement(name = "SSLY", required = true)
    protected String ssly;
    @XmlElement(name = "SSYSGH", required = true)
    protected String ssysgh;
    @XmlElement(name = "SSYSXM")
    protected String ssysxm;
    @XmlElement(name = "ZDYSKSDM", required = true)
    protected String zdysksdm;
    @XmlElement(name = "MZYSGH", required = true)
    protected String mzysgh;
    @XmlElement(name = "MZYSXM")
    protected String mzysxm;
    @XmlElement(name = "ZDYSGH")
    protected String zdysgh;
    @XmlElement(name = "SSYSZSIGH")
    protected String ssyszsigh;
    @XmlElement(name = "SSYSZSIXM")
    protected String ssyszsixm;
    @XmlElement(name = "SSYSZSIIGH")
    protected String ssyszsiigh;
    @XmlElement(name = "SSYSZSIIXM")
    protected String ssyszsiixm;
    @XmlElement(name = "SSQKLB")
    protected String ssqklb;
    @XmlElement(name = "SSQZDMC", required = true)
    protected String ssqzdmc;
    @XmlElement(name = "SSHZDMC", required = true)
    protected String sshzdmc;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "HYZK")
    protected String hyzk;
    @XmlElement(name = "ZY")
    protected String zy;
    @XmlElement(name = "SSZXKSBM")
    protected String sszxksbm;
    @XmlElement(name = "SSZXKSMC")
    protected String sszxksmc;
    @XmlElement(name = "ZRSSBZ")
    protected String zrssbz;
    @XmlElement(name = "SXL")
    protected String sxl;
    @XmlElement(name = "SHXL")
    protected String shxl;
    @XmlElement(name = "SSQKDJBM")
    protected String ssqkdjbm;
    @XmlElement(name = "SSQKDJMC")
    protected String ssqkdjmc;
    @XmlElement(name = "SSCZRQSJ")
    protected String ssczrqsj;
    @XmlElement(name = "SSYZMC")
    protected String ssyzmc;
    @XmlElement(name = "SSEZMC")
    protected String ssezmc;
    @XmlElement(name = "SFBSHM")
    protected String sfbshm;
    @XmlElement(name = "SFBS_DM")
    protected String sfbsdm;
    @XmlElement(name = "MZFFDM")
    protected String mzffdm;
    @XmlElement(name = "KLX")
    protected String klx;
    @XmlElement(name = "KH")
    protected String kh;
    @XmlElement(name = "JSWLBZ")
    protected String jswlbz;
    @XmlElement(name = "SSZSIGH")
    protected String sszsigh;
    @XmlElement(name = "SSZSIIGH")
    protected String sszsiigh;
    @XmlElement(name = "SSCZMC")
    protected String ssczmc;
    @XmlElement(name = "SSZLJCZDWBYDYL_DM")
    protected String sszljczdwbydyldm;
    @XmlElement(name = "SSQKYHDJ")
    protected String ssqkyhdj;

    
    @XmlAttribute(name = "Name")
    protected String name;
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getSsjllsh() {
		return ssjllsh;
	}
	public void setSsjllsh(String ssjllsh) {
		this.ssjllsh = ssjllsh;
	}
	public String getMzzybz() {
		return mzzybz;
	}
	public void setMzzybz(String mzzybz) {
		this.mzzybz = mzzybz;
	}
	public String getSsqssj() {
		return ssqssj;
	}
	public void setSsqssj(String ssqssj) {
		this.ssqssj = ssqssj;
	}
	public String getSsjssj() {
		return ssjssj;
	}
	public void setSsjssj(String ssjssj) {
		this.ssjssj = ssjssj;
	}
	public String getSsqzd() {
		return ssqzd;
	}
	public void setSsqzd(String ssqzd) {
		this.ssqzd = ssqzd;
	}
	public String getSshzd() {
		return sshzd;
	}
	public void setSshzd(String sshzd) {
		this.sshzd = sshzd;
	}
	public String getSsdm() {
		return ssdm;
	}
	public void setSsdm(String ssdm) {
		this.ssdm = ssdm;
	}
	public String getSsmc() {
		return ssmc;
	}
	public void setSsmc(String ssmc) {
		this.ssmc = ssmc;
	}
	public String getSsbwdm() {
		return ssbwdm;
	}
	public void setSsbwdm(String ssbwdm) {
		this.ssbwdm = ssbwdm;
	}
	public String getSslx() {
		return sslx;
	}
	public void setSslx(String sslx) {
		this.sslx = sslx;
	}
	public String getOperationLevel() {
		return operationLevel;
	}
	public void setOperationLevel(String operationLevel) {
		this.operationLevel = operationLevel;
	}
	public String getSsjgms() {
		return ssjgms;
	}
	public void setSsjgms(String ssjgms) {
		this.ssjgms = ssjgms;
	}
	public String getMzff() {
		return mzff;
	}
	public void setMzff(String mzff) {
		this.mzff = mzff;
	}
	public String getMzfy() {
		return mzfy;
	}
	public void setMzfy(String mzfy) {
		this.mzfy = mzfy;
	}
	public String getTcyhdj() {
		return tcyhdj;
	}
	public void setTcyhdj(String tcyhdj) {
		this.tcyhdj = tcyhdj;
	}
	public String getSsly() {
		return ssly;
	}
	public void setSsly(String ssly) {
		this.ssly = ssly;
	}
	public String getSsysgh() {
		return ssysgh;
	}
	public void setSsysgh(String ssysgh) {
		this.ssysgh = ssysgh;
	}
	public String getSsysxm() {
		return ssysxm;
	}
	public void setSsysxm(String ssysxm) {
		this.ssysxm = ssysxm;
	}
	public String getZdysksdm() {
		return zdysksdm;
	}
	public void setZdysksdm(String zdysksdm) {
		this.zdysksdm = zdysksdm;
	}
	public String getMzysgh() {
		return mzysgh;
	}
	public void setMzysgh(String mzysgh) {
		this.mzysgh = mzysgh;
	}
	public String getMzysxm() {
		return mzysxm;
	}
	public void setMzysxm(String mzysxm) {
		this.mzysxm = mzysxm;
	}
	public String getZdysgh() {
		return zdysgh;
	}
	public void setZdysgh(String zdysgh) {
		this.zdysgh = zdysgh;
	}
	public String getSsyszsigh() {
		return ssyszsigh;
	}
	public void setSsyszsigh(String ssyszsigh) {
		this.ssyszsigh = ssyszsigh;
	}
	public String getSsyszsixm() {
		return ssyszsixm;
	}
	public void setSsyszsixm(String ssyszsixm) {
		this.ssyszsixm = ssyszsixm;
	}
	public String getSsyszsiigh() {
		return ssyszsiigh;
	}
	public void setSsyszsiigh(String ssyszsiigh) {
		this.ssyszsiigh = ssyszsiigh;
	}
	public String getSsyszsiixm() {
		return ssyszsiixm;
	}
	public void setSsyszsiixm(String ssyszsiixm) {
		this.ssyszsiixm = ssyszsiixm;
	}
	public String getSsqklb() {
		return ssqklb;
	}
	public void setSsqklb(String ssqklb) {
		this.ssqklb = ssqklb;
	}
	public String getSsqzdmc() {
		return ssqzdmc;
	}
	public void setSsqzdmc(String ssqzdmc) {
		this.ssqzdmc = ssqzdmc;
	}
	public String getSshzdmc() {
		return sshzdmc;
	}
	public void setSshzdmc(String sshzdmc) {
		this.sshzdmc = sshzdmc;
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
	public String getHyzk() {
		return hyzk;
	}
	public void setHyzk(String hyzk) {
		this.hyzk = hyzk;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getSszxksbm() {
		return sszxksbm;
	}
	public void setSszxksbm(String sszxksbm) {
		this.sszxksbm = sszxksbm;
	}
	public String getSszxksmc() {
		return sszxksmc;
	}
	public void setSszxksmc(String sszxksmc) {
		this.sszxksmc = sszxksmc;
	}
	public String getZrssbz() {
		return zrssbz;
	}
	public void setZrssbz(String zrssbz) {
		this.zrssbz = zrssbz;
	}
	public String getSxl() {
		return sxl;
	}
	public void setSxl(String sxl) {
		this.sxl = sxl;
	}
	public String getShxl() {
		return shxl;
	}
	public void setShxl(String shxl) {
		this.shxl = shxl;
	}
	public String getSsqkdjbm() {
		return ssqkdjbm;
	}
	public void setSsqkdjbm(String ssqkdjbm) {
		this.ssqkdjbm = ssqkdjbm;
	}
	public String getSsqkdjmc() {
		return ssqkdjmc;
	}
	public void setSsqkdjmc(String ssqkdjmc) {
		this.ssqkdjmc = ssqkdjmc;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    
	public String getSourceId() {
		return ssjllsh;
	}
	public String getSsczrqsj() {
		return ssczrqsj;
	}
	public void setSsczrqsj(String ssczrqsj) {
		this.ssczrqsj = ssczrqsj;
	}
	public String getSsyzmc() {
		return ssyzmc;
	}
	public void setSsyzmc(String ssyzmc) {
		this.ssyzmc = ssyzmc;
	}
	public String getSsezmc() {
		return ssezmc;
	}
	public void setSsezmc(String ssezmc) {
		this.ssezmc = ssezmc;
	}
	public String getSfbshm() {
		return sfbshm;
	}
	public void setSfbshm(String sfbshm) {
		this.sfbshm = sfbshm;
	}
	public String getSfbsdm() {
		return sfbsdm;
	}
	public void setSfbsdm(String sfbsdm) {
		this.sfbsdm = sfbsdm;
	}
	public String getMzffdm() {
		return mzffdm;
	}
	public void setMzffdm(String mzffdm) {
		this.mzffdm = mzffdm;
	}
	public String getKlx() {
		return klx;
	}
	public void setKlx(String klx) {
		this.klx = klx;
	}
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getJswlbz() {
		return jswlbz;
	}
	public void setJswlbz(String jswlbz) {
		this.jswlbz = jswlbz;
	}
	public String getSszsigh() {
		return sszsigh;
	}
	public void setSszsigh(String sszsigh) {
		this.sszsigh = sszsigh;
	}
	public String getSszsiigh() {
		return sszsiigh;
	}
	public void setSszsiigh(String sszsiigh) {
		this.sszsiigh = sszsiigh;
	}
	public String getSsczmc() {
		return ssczmc;
	}
	public void setSsczmc(String ssczmc) {
		this.ssczmc = ssczmc;
	}

	public String getSszljczdwbydyldm() {
		return sszljczdwbydyldm;
	}

	public void setSszljczdwbydyldm(String sszljczdwbydyldm) {
		this.sszljczdwbydyldm = sszljczdwbydyldm;
	}

	public String getSsqkyhdj() {
		return ssqkyhdj;
	}

	public void setSsqkyhdj(String ssqkyhdj) {
		this.ssqkyhdj = ssqkyhdj;
	}
}
