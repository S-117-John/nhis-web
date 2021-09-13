package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 门诊病历表(Opt_BL)
 * @author chengjia
 *
 */
@XmlRootElement(name = "Opt_BL")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptBL {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "BLID", required = true)
    protected String blid;
    @XmlElement(name = "YHBM")
    protected String yhbm;
    @XmlElement(name = "ZS", required = true)
    protected String zs;
    @XmlElement(name = "XBS")
    protected String xbs;
    @XmlElement(name = "JWS")
    protected String jws;
    @XmlElement(name = "GRS")
    protected String grs;
    @XmlElement(name = "JZS")
    protected String jzs;
    @XmlElement(name = "HYS")
    protected String hys;
    @XmlElement(name = "TGJC")
    protected String tgjc;
    @XmlElement(name = "ZKQK")
    protected String zkqk;
    @XmlElement(name = "FZJC")
    protected String fzjc;
    @XmlElement(name = "ZYZDBM", required = true)
    protected String zyzdbm;
    @XmlElement(name = "ZYZDMC", required = true)
    protected String zyzdmc;
    @XmlElement(name = "ZYZDBM1")
    protected String zyzdbm1;
    @XmlElement(name = "ZYZDMC1")
    protected String zyzdmc1;
    @XmlElement(name = "ZYZDBM2")
    protected String zyzdbm2;
    @XmlElement(name = "ZYZDMC2")
    protected String zyzdmc2;
    @XmlElement(name = "ZYZDBM3")
    protected String zyzdbm3;
    @XmlElement(name = "ZYZDMC3")
    protected String zyzdmc3;
    @XmlElement(name = "ZYZDBM4")
    protected String zyzdbm4;
    @XmlElement(name = "ZYZDMC4")
    protected String zyzdmc4;
    @XmlElement(name = "KSBM")
    protected String ksbm;
    @XmlElement(name = "KSMC")
    protected String ksmc;
    @XmlElement(name = "YSGH")
    protected String ysgh;
    @XmlElement(name = "YSXM")
    protected String ysxm;
    @XmlElement(name = "TXSJ")
    protected String txsj;
    @XmlElement(name = "YLJGBM")
    protected String yljgbm;
    @XmlElement(name = "YLJGMC")
    protected String yljgmc;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "NLS")
    protected String nls;
    @XmlElement(name = "GMSBZ")
    protected String gmsbz;
    @XmlElement(name = "GMS")
    protected String gms;
    @XmlElement(name = "JZRQSJ")
    protected String jzrqsj;
    @XmlElement(name = "CZBZDM")
    protected String czbzdm;
    @XmlElement(name = "ZYSZGCJG")
    protected String zyszgcjg;
    @XmlElement(name = "CBZDZYBMDM")
    protected String cbzdzybmdm;
    @XmlElement(name = "CBZDZYBMMC")
    protected String cbzdzybmmc;
    @XmlElement(name = "CBZDZYZHDM")
    protected String cbzdzyzhdm;
    @XmlElement(name = "CBZDZYZHMC")
    protected String cbzdzyzhmc;
    @XmlElement(name = "BZYJ")
    protected String bzyj;
    @XmlElement(name = "ZZZF")
    protected String zzzf;
    @XmlElement(name = "TW")
    protected String tw;
    @XmlElement(name = "XL")
    protected String xl;
    @XmlElement(name = "HXPL")
    protected String hxpl;
    @XmlElement(name = "SSY")
    protected String ssy;
    @XmlElement(name = "SZY")
    protected String szy;
    @XmlElement(name = "SG")
    protected String sg;
    @XmlElement(name = "TZ")
    protected String tz;
    @XmlElement(name = "JKWTPG")
    protected String jkwtpg;
    @XmlElement(name = "CYZDBM")
    protected String cyzdbm;
    @XmlElement(name = "CYZDMC")
    protected String cyzdmc;
    @XmlAttribute(name = "Name")
    protected String name;
    
    
    @XmlElementWrapper(name="SSLBs")
    @XmlElement(name = "SSLB")
    protected List<SSLB> sslb;
	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public String getBlid() {
		return blid;
	}
	public void setBlid(String blid) {
		this.blid = blid;
	}
	public String getYhbm() {
		return yhbm;
	}
	public void setYhbm(String yhbm) {
		this.yhbm = yhbm;
	}
	public String getZs() {
		return zs;
	}
	public void setZs(String zs) {
		this.zs = zs;
	}
	public String getXbs() {
		return xbs;
	}
	public void setXbs(String xbs) {
		this.xbs = xbs;
	}
	public String getJws() {
		return jws;
	}
	public void setJws(String jws) {
		this.jws = jws;
	}
	public String getGrs() {
		return grs;
	}
	public void setGrs(String grs) {
		this.grs = grs;
	}
	public String getJzs() {
		return jzs;
	}
	public void setJzs(String jzs) {
		this.jzs = jzs;
	}
	public String getHys() {
		return hys;
	}
	public void setHys(String hys) {
		this.hys = hys;
	}
	public String getTgjc() {
		return tgjc;
	}
	public void setTgjc(String tgjc) {
		this.tgjc = tgjc;
	}
	public String getZkqk() {
		return zkqk;
	}
	public void setZkqk(String zkqk) {
		this.zkqk = zkqk;
	}
	public String getFzjc() {
		return fzjc;
	}
	public void setFzjc(String fzjc) {
		this.fzjc = fzjc;
	}
	public String getZyzdbm() {
		return zyzdbm;
	}
	public void setZyzdbm(String zyzdbm) {
		this.zyzdbm = zyzdbm;
	}
	public String getZyzdmc() {
		return zyzdmc;
	}
	public void setZyzdmc(String zyzdmc) {
		this.zyzdmc = zyzdmc;
	}
	public String getZyzdbm1() {
		return zyzdbm1;
	}
	public void setZyzdbm1(String zyzdbm1) {
		this.zyzdbm1 = zyzdbm1;
	}
	public String getZyzdmc1() {
		return zyzdmc1;
	}
	public void setZyzdmc1(String zyzdmc1) {
		this.zyzdmc1 = zyzdmc1;
	}
	public String getZyzdbm2() {
		return zyzdbm2;
	}
	public void setZyzdbm2(String zyzdbm2) {
		this.zyzdbm2 = zyzdbm2;
	}
	public String getZyzdmc2() {
		return zyzdmc2;
	}
	public void setZyzdmc2(String zyzdmc2) {
		this.zyzdmc2 = zyzdmc2;
	}
	public String getZyzdbm3() {
		return zyzdbm3;
	}
	public void setZyzdbm3(String zyzdbm3) {
		this.zyzdbm3 = zyzdbm3;
	}
	public String getZyzdmc3() {
		return zyzdmc3;
	}
	public void setZyzdmc3(String zyzdmc3) {
		this.zyzdmc3 = zyzdmc3;
	}
	public String getZyzdbm4() {
		return zyzdbm4;
	}
	public void setZyzdbm4(String zyzdbm4) {
		this.zyzdbm4 = zyzdbm4;
	}
	public String getZyzdmc4() {
		return zyzdmc4;
	}
	public void setZyzdmc4(String zyzdmc4) {
		this.zyzdmc4 = zyzdmc4;
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
	public String getYsgh() {
		return ysgh;
	}
	public void setYsgh(String ysgh) {
		this.ysgh = ysgh;
	}
	public String getYsxm() {
		return ysxm;
	}
	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}
	public String getTxsj() {
		return txsj;
	}
	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}
	public String getYljgbm() {
		return yljgbm;
	}
	public void setYljgbm(String yljgbm) {
		this.yljgbm = yljgbm;
	}
	public String getYljgmc() {
		return yljgmc;
	}
	public void setYljgmc(String yljgmc) {
		this.yljgmc = yljgmc;
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
	public String getNls() {
		return nls;
	}
	public void setNls(String nls) {
		this.nls = nls;
	}
	public String getGmsbz() {
		return gmsbz;
	}
	public void setGmsbz(String gmsbz) {
		this.gmsbz = gmsbz;
	}
	public String getGms() {
		return gms;
	}
	public void setGms(String gms) {
		this.gms = gms;
	}
	public String getJzrqsj() {
		return jzrqsj;
	}
	public void setJzrqsj(String jzrqsj) {
		this.jzrqsj = jzrqsj;
	}
	public String getCzbzdm() {
		return czbzdm;
	}
	public void setCzbzdm(String czbzdm) {
		this.czbzdm = czbzdm;
	}
	public String getZyszgcjg() {
		return zyszgcjg;
	}
	public void setZyszgcjg(String zyszgcjg) {
		this.zyszgcjg = zyszgcjg;
	}
	public String getCbzdzybmdm() {
		return cbzdzybmdm;
	}
	public void setCbzdzybmdm(String cbzdzybmdm) {
		this.cbzdzybmdm = cbzdzybmdm;
	}
	public String getCbzdzybmmc() {
		return cbzdzybmmc;
	}
	public void setCbzdzybmmc(String cbzdzybmmc) {
		this.cbzdzybmmc = cbzdzybmmc;
	}
	public String getCbzdzyzhdm() {
		return cbzdzyzhdm;
	}
	public void setCbzdzyzhdm(String cbzdzyzhdm) {
		this.cbzdzyzhdm = cbzdzyzhdm;
	}
	public String getCbzdzyzhmc() {
		return cbzdzyzhmc;
	}
	public void setCbzdzyzhmc(String cbzdzyzhmc) {
		this.cbzdzyzhmc = cbzdzyzhmc;
	}
	public String getBzyj() {
		return bzyj;
	}
	public void setBzyj(String bzyj) {
		this.bzyj = bzyj;
	}
	public String getZzzf() {
		return zzzf;
	}
	public void setZzzf(String zzzf) {
		this.zzzf = zzzf;
	}
	
	public List<SSLB> getSslb() {
		return sslb;
	}
	public void setSslb(List<SSLB> sslb) {
		this.sslb = sslb;
	}
	
	public String getTw() {
		return tw;
	}
	public void setTw(String tw) {
		this.tw = tw;
	}
	public String getXl() {
		return xl;
	}
	public void setXl(String xl) {
		this.xl = xl;
	}
	public String getHxpl() {
		return hxpl;
	}
	public void setHxpl(String hxpl) {
		this.hxpl = hxpl;
	}
	public String getSsy() {
		return ssy;
	}
	public void setSsy(String ssy) {
		this.ssy = ssy;
	}
	public String getSzy() {
		return szy;
	}
	public void setSzy(String szy) {
		this.szy = szy;
	}
	public String getSg() {
		return sg;
	}
	public void setSg(String sg) {
		this.sg = sg;
	}
	public String getTz() {
		return tz;
	}
	public void setTz(String tz) {
		this.tz = tz;
	}
	public String getJkwtpg() {
		return jkwtpg;
	}
	public void setJkwtpg(String jkwtpg) {
		this.jkwtpg = jkwtpg;
	}
	public String getCyzdbm() {
		return cyzdbm;
	}
	public void setCyzdbm(String cyzdbm) {
		this.cyzdbm = cyzdbm;
	}
	public String getCyzdmc() {
		return cyzdmc;
	}
	public void setCyzdmc(String cyzdmc) {
		this.cyzdmc = cyzdmc;
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
