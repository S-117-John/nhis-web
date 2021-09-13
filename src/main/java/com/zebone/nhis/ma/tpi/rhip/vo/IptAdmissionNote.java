package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 住院入院记录
 * @author chengjia
 *
 */
@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptAdmissionNote {
	
	 @XmlAttribute(name = "Name", required = true)
	 private String name;
     @XmlElement(name = "JZLSH", required = true)
     protected String jzlsh;
     @XmlElement(name = "ZYBAH", required = true)
     protected String bzbah;
     @XmlElement(name = "BZBSCSZ", required = true)
     protected String bzbscsz;
     @XmlElement(name = "BZZS", required = true)
     protected String bzzs;
     @XmlElement(name = "BZXBS", required = true)
     protected String bzxbs;
     @XmlElement(name = "BZJWS", required = true)
     protected String bzjws;
     @XmlElement(name = "BZGRS", required = true)
     protected String bzgrs;
     @XmlElement(name = "BZHYS")
     protected String bzhys;
     @XmlElement(name = "BZJZS", required = true)
     protected String bzjzs;
     @XmlElement(name = "BZXTHG")
     protected String bzxthg;
     @XmlElement(name = "BZWYS")
     protected String bzwys;
     @XmlElement(name = "BZCSS")
     protected String bzcss;
     @XmlElement(name = "BZYJS")
     protected String bzyjs;
     @XmlElement(name = "BZBSZY")
     protected String bzbszy;
     @XmlElement(name = "BZCBZD")
     protected String bzcbzd;
     @XmlElement(name = "BZXZZD")
     protected String bzxzzd;
     @XmlElement(name = "BZFZJC_SYSJC")
     protected String bzfzjcsysjc;
     @XmlElement(name = "BZFZJC_TSJC")
     protected String bzfzjctsjc;
     @XmlElement(name = "BZFZJC_BLJC")
     protected String bzfzjcbljc;
     @XmlElement(name = "BZFZJC_QT")
     protected String bzfzjcqt;
     @XmlElement(name = "BZYHZGX")
     protected String bzyhzgx;
     @XmlElement(name = "ZYYSGH")
     protected String zyysgh;
     @XmlElement(name = "ZYYSXM")
     protected String zyysxm;
     @XmlElement(name = "ZRYSXM")
     protected String zrysxm;
     @XmlElement(name = "ZRYSGH")
     protected String zrysgh;
     @XmlElement(name = "ZZYSXM")
     protected String zzysxm;
     @XmlElement(name = "JZYSGH")
     protected String jzysgh;
     @XmlElement(name = "ZZYSGH")
     protected String zzysgh;
     @XmlElement(name = "JZYSXM")
     protected String jzysxm;
     @XmlElement(name = "RYRQSJ", required = true)
     protected String ryrqsj;
     @XmlElement(name = "HZXM")
     protected String hzxm;
     @XmlElement(name = "YBLX")
     protected String yblx;
     @XmlElement(name = "XBDM")
     protected String xbdm;
     @XmlElement(name = "CSRQ")
     protected String csrq;
//     @XmlElement(name = "HYZK")
//     protected String hyzk;
     @XmlElement(name = "ZY")
     protected String zy;
//     @XmlElement(name = "MZ")
//     protected String mz;
     @XmlElement(name = "GMS")
     protected String gms;
     @XmlElement(name = "Day31InpatientMk")
     protected String day31InpatientMk;
     @XmlElement(name = "Day31InpatientAim")
     protected String day31InpatientAim;
     @XmlElement(name = "TGJC")
     protected String tgjc;
     //体温（℃）
     @XmlElement(name = "TW")
     protected String tw;
 	//心率（次/min）    
     @XmlElement(name = "XL")
     protected String xl;
     //呼吸频率（次/min）
     @XmlElement(name = "HXPL")
     protected String hxpl;
     //收缩压（mmHg）
     @XmlElement(name = "SSY")
     protected String ssy;
     //舒张压（mmHg）
     @XmlElement(name = "SZY")
     protected String szy;
     
//    @XmlElementWrapper(name="Ipt_PhysiqueExams")
//    @XmlElement(name = "Ipt_PhysiqueExam")  
//    private List<IptPhysiqueExam> Ipt_PhysiqueExams;


	public void setName(String name) {
		this.name = name;
	}

	
	
	
	public String getJzlsh() {
		return jzlsh;
	}


	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}


	public String getBzbah() {
		return bzbah;
	}


	public void setBzbah(String bzbah) {
		this.bzbah = bzbah;
	}


	public String getBzbscsz() {
		return bzbscsz;
	}


	public void setBzbscsz(String bzbscsz) {
		this.bzbscsz = bzbscsz;
	}


	public String getBzzs() {
		return bzzs;
	}


	public void setBzzs(String bzzs) {
		this.bzzs = bzzs;
	}


	public String getBzxbs() {
		return bzxbs;
	}


	public void setBzxbs(String bzxbs) {
		this.bzxbs = bzxbs;
	}


	public String getBzjws() {
		return bzjws;
	}


	public void setBzjws(String bzjws) {
		this.bzjws = bzjws;
	}


	public String getBzgrs() {
		return bzgrs;
	}


	public void setBzgrs(String bzgrs) {
		this.bzgrs = bzgrs;
	}


	public String getBzhys() {
		return bzhys;
	}


	public void setBzhys(String bzhys) {
		this.bzhys = bzhys;
	}


	public String getBzjzs() {
		return bzjzs;
	}


	public void setBzjzs(String bzjzs) {
		this.bzjzs = bzjzs;
	}


	public String getBzxthg() {
		return bzxthg;
	}


	public void setBzxthg(String bzxthg) {
		this.bzxthg = bzxthg;
	}


	public String getBzwys() {
		return bzwys;
	}


	public void setBzwys(String bzwys) {
		this.bzwys = bzwys;
	}


	public String getBzcss() {
		return bzcss;
	}


	public void setBzcss(String bzcss) {
		this.bzcss = bzcss;
	}


	public String getBzyjs() {
		return bzyjs;
	}


	public void setBzyjs(String bzyjs) {
		this.bzyjs = bzyjs;
	}


	public String getBzbszy() {
		return bzbszy;
	}


	public void setBzbszy(String bzbszy) {
		this.bzbszy = bzbszy;
	}


	public String getBzcbzd() {
		return bzcbzd;
	}


	public void setBzcbzd(String bzcbzd) {
		this.bzcbzd = bzcbzd;
	}


	public String getBzxzzd() {
		return bzxzzd;
	}


	public void setBzxzzd(String bzxzzd) {
		this.bzxzzd = bzxzzd;
	}


	public String getBzfzjcsysjc() {
		return bzfzjcsysjc;
	}


	public void setBzfzjcsysjc(String bzfzjcsysjc) {
		this.bzfzjcsysjc = bzfzjcsysjc;
	}


	public String getBzfzjctsjc() {
		return bzfzjctsjc;
	}


	public void setBzfzjctsjc(String bzfzjctsjc) {
		this.bzfzjctsjc = bzfzjctsjc;
	}


	public String getBzfzjcbljc() {
		return bzfzjcbljc;
	}


	public void setBzfzjcbljc(String bzfzjcbljc) {
		this.bzfzjcbljc = bzfzjcbljc;
	}


	public String getBzfzjcqt() {
		return bzfzjcqt;
	}


	public void setBzfzjcqt(String bzfzjcqt) {
		this.bzfzjcqt = bzfzjcqt;
	}


	public String getBzyhzgx() {
		return bzyhzgx;
	}


	public void setBzyhzgx(String bzyhzgx) {
		this.bzyhzgx = bzyhzgx;
	}


	public String getZyysgh() {
		return zyysgh;
	}


	public void setZyysgh(String zyysgh) {
		this.zyysgh = zyysgh;
	}


	public String getZyysxm() {
		return zyysxm;
	}


	public void setZyysxm(String zyysxm) {
		this.zyysxm = zyysxm;
	}


	public String getZrysxm() {
		return zrysxm;
	}


	public void setZrysxm(String zrysxm) {
		this.zrysxm = zrysxm;
	}


	public String getZrysgh() {
		return zrysgh;
	}


	public void setZrysgh(String zrysgh) {
		this.zrysgh = zrysgh;
	}


	public String getZzysxm() {
		return zzysxm;
	}


	public void setZzysxm(String zzysxm) {
		this.zzysxm = zzysxm;
	}


	public String getJzysgh() {
		return jzysgh;
	}


	public void setJzysgh(String jzysgh) {
		this.jzysgh = jzysgh;
	}


	public String getZzysgh() {
		return zzysgh;
	}


	public void setZzysgh(String zzysgh) {
		this.zzysgh = zzysgh;
	}


	public String getJzysxm() {
		return jzysxm;
	}


	public void setJzysxm(String jzysxm) {
		this.jzysxm = jzysxm;
	}


	public String getRyrqsj() {
		return ryrqsj;
	}


	public void setRyrqsj(String ryrqsj) {
		this.ryrqsj = ryrqsj;
	}


	public String getHzxm() {
		return hzxm;
	}


	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}


	public String getYblx() {
		return yblx;
	}


	public void setYblx(String yblx) {
		this.yblx = yblx;
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


	public String getZy() {
		return zy;
	}


	public void setZy(String zy) {
		this.zy = zy;
	}


	public String getGms() {
		return gms;
	}


	public void setGms(String gms) {
		this.gms = gms;
	}


	public String getName() {
		return name;
	}


	public String getSourceId() {
		return jzlsh;
	}


	public String getDay31InpatientMk() {
		return day31InpatientMk;
	}


	public void setDay31InpatientMk(String day31InpatientMk) {
		this.day31InpatientMk = day31InpatientMk;
	}


	public String getTgjc() {
		return tgjc;
	}


	public void setTgjc(String tgjc) {
		this.tgjc = tgjc;
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




	public String getDay31InpatientAim() {
		return day31InpatientAim;
	}




	public void setDay31InpatientAim(String day31InpatientAim) {
		this.day31InpatientAim = day31InpatientAim;
	}




	


}
