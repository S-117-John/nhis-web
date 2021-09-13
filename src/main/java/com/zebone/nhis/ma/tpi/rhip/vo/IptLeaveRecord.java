package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 住院出院记录
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_LeaveRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptLeaveRecord {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "ZYBAH", required = true)
    protected String bzbah;
    @XmlElement(name = "MZZD")
    protected String mzzd;
    @XmlElement(name = "RYZD")
    protected String ryzd;
    @XmlElement(name = "CYZD", required = true)
    protected String cyzd;
    @XmlElement(name = "CYYZ")
    protected String cyyz;
    @XmlElement(name = "ZZJG", required = true)
    protected String zzjg;
    @XmlElement(name = "KSBM", required = true)
    protected String ksbm;
    @XmlElement(name = "ZYTS", required = true)
    protected String zyts;
    @XmlElement(name = "ZYYSGH")
    protected String zyysgh;
    @XmlElement(name = "ZYYSXM", required = true)
    protected String zyysxm;
    @XmlElement(name = "ZZYSXM", required = true)
    protected String zzysxm;
    @XmlElement(name = "ZZYSGH", required = true)
    protected String zzysgh;
    @XmlElement(name = "ZRYSXM", required = true)
    protected String zrysxm;
//    @XmlElement(name = "ZRYSGH", required = true)
//    protected String zrysgh;
//    @XmlElement(name = "JZYSXM", required = true)
//    protected String jzysxm;
//    @XmlElement(name = "JZYSGH", required = true)
//    protected String jzysgh;
    @XmlElement(name = "RYQK", required = true)
    protected String ryqk;
    @XmlElement(name = "CYQK", required = true)
    protected String cyqk;
    @XmlElement(name = "CYSJ")
    protected String cysj;
    @XmlElement(name = "JLSJ")
    protected String jlsj;
    @XmlElement(name = "RYSJ", required = true)
    protected String rysj;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    /*@XmlElement(name = "HYZK")
    protected String hyzk;*/
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "XZZ_DM")
    protected String xzzDm;
    @XmlElement(name = "MasterDiseaseCode")
    protected String masterDiseaseCode;
    @XmlElement(name = "MasterDiseaseName")
    protected String masterDiseaseName;
    //出院诊断-主要诊断-入院病情/CC09_00_104_00/1有明确诊断2临床未确定3情况不明4无法确定
    @XmlElement(name = "MasterAdmissionCondition")
    protected String masterAdmissionCondition;
    //离院方式/CVX_LeaveHospitalType/1医嘱离院2医嘱转院3医嘱转社区卫生服务机构/乡镇卫生院4非医嘱离院5死亡9其他
    @XmlElement(name = "DischargeMethods")
    protected String dischargeMethods;
    
    @XmlElement(name = "DischargeSickRoom")
    protected String dischargeSickRoom;
    @XmlElement(name = "KSMC")
    protected String ksmc;
    
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
	public String getMzzd() {
		return mzzd;
	}
	public void setMzzd(String mzzd) {
		this.mzzd = mzzd;
	}
	public String getRyzd() {
		return ryzd;
	}
	public void setRyzd(String ryzd) {
		this.ryzd = ryzd;
	}
	public String getCyzd() {
		return cyzd;
	}
	public void setCyzd(String cyzd) {
		this.cyzd = cyzd;
	}
	public String getCyyz() {
		return cyyz;
	}
	public void setCyyz(String cyyz) {
		this.cyyz = cyyz;
	}
	public String getZzjg() {
		return zzjg;
	}
	public void setZzjg(String zzjg) {
		this.zzjg = zzjg;
	}
	public String getKsbm() {
		return ksbm;
	}
	public void setKsbm(String ksbm) {
		this.ksbm = ksbm;
	}
	public String getZyts() {
		return zyts;
	}
	public void setZyts(String zyts) {
		this.zyts = zyts;
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
	public String getZzysxm() {
		return zzysxm;
	}
	public void setZzysxm(String zzysxm) {
		this.zzysxm = zzysxm;
	}
	public String getRyqk() {
		return ryqk;
	}
	public void setRyqk(String ryqk) {
		this.ryqk = ryqk;
	}
	public String getCyqk() {
		return cyqk;
	}
	public void setCyqk(String cyqk) {
		this.cyqk = cyqk;
	}
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj) {
		this.cysj = cysj;
	}
	public String getJlsj() {
		return jlsj;
	}
	public void setJlsj(String jlsj) {
		this.jlsj = jlsj;
	}
	public String getRysj() {
		return rysj;
	}
	public void setRysj(String rysj) {
		this.rysj = rysj;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
	public String getSourceId() {
		return jzlsh;
	}
	public String getZzysgh() {
		return zzysgh;
	}
	public void setZzysgh(String zzysgh) {
		this.zzysgh = zzysgh;
	}
	public String getZrysxm() {
		return zrysxm;
	}
	public void setZrysxm(String zrysxm) {
		this.zrysxm = zrysxm;
	}
	public String getXzzDm() {
		return xzzDm;
	}
	public void setXzzDm(String xzzDm) {
		this.xzzDm = xzzDm;
	}
	public String getMasterDiseaseCode() {
		return masterDiseaseCode;
	}
	public void setMasterDiseaseCode(String masterDiseaseCode) {
		this.masterDiseaseCode = masterDiseaseCode;
	}
	public String getMasterDiseaseName() {
		return masterDiseaseName;
	}
	public void setMasterDiseaseName(String masterDiseaseName) {
		this.masterDiseaseName = masterDiseaseName;
	}
	public String getMasterAdmissionCondition() {
		return masterAdmissionCondition;
	}
	public void setMasterAdmissionCondition(String masterAdmissionCondition) {
		this.masterAdmissionCondition = masterAdmissionCondition;
	}
	public String getDischargeMethods() {
		return dischargeMethods;
	}
	public void setDischargeMethods(String dischargeMethods) {
		this.dischargeMethods = dischargeMethods;
	}
	public String getDischargeSickRoom() {
		return dischargeSickRoom;
	}
	public void setDischargeSickRoom(String dischargeSickRoom) {
		this.dischargeSickRoom = dischargeSickRoom;
	}
	public String getKsmc() {
		return ksmc;
	}
	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}
	
}
