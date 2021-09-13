package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 住院就诊记录表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_Record")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptRecord {

    @XmlElement(name = "KH", required = true)
    protected String kh;
    @XmlElement(name = "KLX", required = true)
    protected String klx;
    @XmlElement(name = "IDCardCode")
    protected String idCardCode;
    @XmlElement(name = "IDCard")
    protected String idCard;
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "MZLSH")
    protected String mzlsh;
    @XmlElement(name = "ZYBAH", required = true)
    protected String bzbah;//BZBAH
    @XmlElement(name = "ZYJZLX")
    protected String zyjzlx;
    @XmlElement(name = "RYKSBM", required = true)
    protected String ryksbm;
    @XmlElement(name = "CYKSBM", required = true)
    protected String cyksbm;
    @XmlElement(name = "RYSJ", required = true)
    protected String rysj;
    @XmlElement(name = "CYSJ", required = true)
    protected String cysj;
    @XmlElement(name = "CYZD")
    protected String cyzd;
    @XmlElement(name = "ZDMC")
    protected String zdmc;
    @XmlElement(name = "BRXM")
    protected String brxm;
    @XmlElement(name = "BQBM")
    protected String bqbm;
    @XmlElement(name = "BQMC")
    protected String bqmc;
    @XmlElement(name = "XB_DM")
    protected String xb;
//    @XmlElement(name = "HYZK")
//    protected String hyzk;
    @XmlElement(name = "ZY")
    protected String zy;
//    @XmlElement(name = "MZ")
//    protected String mz;
//    @XmlElement(name = "GJ")
//    protected String gj;
    @XmlElement(name = "YBLX")
    protected String yblx;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "CH")
    protected String ch;
    @XmlElement(name = "CYBQBM")
    protected String cybqbm;
    @XmlElement(name = "BRZT")
    protected String brzt;
    @XmlElement(name = "XSEBZ")
    protected String xsebz;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "YLFYLYLB_DM", required = true)
    protected String yblb;//医疗费用来源类别代码
    @XmlElement(name = "RYZD_DM", required = true)
    protected String ryzdDm;//入院诊断代码
    @XmlElement(name = "RYZD_MC", required = true)
    protected String ryzdMc;//入院诊断名称
    @XmlElement(name = "ZYCRBS_DM", required = true)
    protected String zycrbs;//1:否,2:是,9:不详
    @XmlElement(name = "ZYJBZT_DM", required = true)
    protected String zyjbzt;//CV05_01_001住院疾病状态代码	1危急2严重3一般4不适用9其他	
    @XmlElement(name = "ZYFBSJ", required = true)
    protected String zyfbsj;//住院发病时间
    @XmlElement(name = "ZYQZRQ", required = true)
    protected String zyqzrq;//住院确诊时间
    @XmlElement(name = "ZYZLJG_DM", required = true)
    protected String zyzljg;//住院治疗结果代码CV05_10_010 1治愈2好转3稳定4恶化5死亡9其他
    @XmlElement(name = "ZYYY_DM", required = true)
    protected String zyyyDm;//住院原因代码HIS18_01_010       1病伤2体检3分娩9其他
    @XmlElement(name = "SFHZ", required = true)
    protected String sfhz;//是否会诊 1：否；2：是；9：不详
    @XmlElement(name = "RYZZ_MC")
    protected String ryzzMc;//入院症状名称
    //住院根本死因代码
    @XmlElement(name = "ZYGBSY_DM")
    protected String zygbsyDm;
    //住院死亡时间
    @XmlElement(name = "ZYSWSJ")
    protected String zyswsj;
    //会诊原因
    @XmlElement(name = "HZYY")
    protected String hzyy;
    //会诊意见
    @XmlElement(name = "HZYJ")
    protected String hzyj;
    //体格检查记录
    @XmlElement(name = "TGJCJL")
    protected String tgjcjl;
    //专科情况
    @XmlElement(name = "ZKQK")
    protected String zkqk;
    //最后修改人员代码
    @XmlElement(name = "ZHXGRYDM")
    protected String zhxgrydm;
    //最后修改人员名称
    @XmlElement(name = "ZHXGRYMC")
    protected String zhxgrymc;
    //记录人
    @XmlElement(name = "JLR")
    protected String jlr;
    //记录时间
    @XmlElement(name = "JLSJ")
    protected String jlsj;
    
	public String getKh() {
		return kh;
	}
	public void setKh(String kh) {
		this.kh = kh;
	}
	public String getKlx() {
		return klx;
	}
	public void setKlx(String klx) {
		this.klx = klx;
	}
	public String getIdCardCode() {
		return idCardCode;
	}
	public void setIdCardCode(String idCardCode) {
		this.idCardCode = idCardCode;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
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
	public String getZyjzlx() {
		return zyjzlx;
	}
	public void setZyjzlx(String zyjzlx) {
		this.zyjzlx = zyjzlx;
	}
	public String getRyksbm() {
		return ryksbm;
	}
	public void setRyksbm(String ryksbm) {
		this.ryksbm = ryksbm;
	}
	public String getCyksbm() {
		return cyksbm;
	}
	public void setCyksbm(String cyksbm) {
		this.cyksbm = cyksbm;
	}
	public String getRysj() {
		return rysj;
	}
	public void setRysj(String rysj) {
		this.rysj = rysj;
	}
	public String getCysj() {
		return cysj;
	}
	public void setCysj(String cysj) {
		this.cysj = cysj;
	}
	public String getCyzd() {
		return cyzd;
	}
	public void setCyzd(String cyzd) {
		this.cyzd = cyzd;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getBrxm() {
		return brxm;
	}
	public void setBrxm(String brxm) {
		this.brxm = brxm;
	}
	public String getBqbm() {
		return bqbm;
	}
	public void setBqbm(String bqbm) {
		this.bqbm = bqbm;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getXb() {
		return xb;
	}
	public void setXb(String xb) {
		this.xb = xb;
	}
	public String getZy() {
		return zy;
	}
	public void setZy(String zy) {
		this.zy = zy;
	}
	public String getYblx() {
		return yblx;
	}
	public void setYblx(String yblx) {
		this.yblx = yblx;
	}
	public String getCsrq() {
		return csrq;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getCybqbm() {
		return cybqbm;
	}
	public void setCybqbm(String cybqbm) {
		this.cybqbm = cybqbm;
	}
	public String getBrzt() {
		return brzt;
	}
	public void setBrzt(String brzt) {
		this.brzt = brzt;
	}
	public String getXsebz() {
		return xsebz;
	}
	public void setXsebz(String xsebz) {
		this.xsebz = xsebz;
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
	public String getMzlsh() {
		return mzlsh;
	}
	public void setMzlsh(String mzlsh) {
		this.mzlsh = mzlsh;
	}
	public String getYblb() {
		return yblb;
	}
	public void setYblb(String yblb) {
		this.yblb = yblb;
	}
	public String getRyzdDm() {
		return ryzdDm;
	}
	public void setRyzdDm(String ryzdDm) {
		this.ryzdDm = ryzdDm;
	}
	public String getRyzdMc() {
		return ryzdMc;
	}
	public void setRyzdMc(String ryzdMc) {
		this.ryzdMc = ryzdMc;
	}
	public String getZycrbs() {
		return zycrbs;
	}
	public void setZycrbs(String zycrbs) {
		this.zycrbs = zycrbs;
	}
	public String getZyjbzt() {
		return zyjbzt;
	}
	public void setZyjbzt(String zyjbzt) {
		this.zyjbzt = zyjbzt;
	}
	public String getZyfbsj() {
		return zyfbsj;
	}
	public void setZyfbsj(String zyfbsj) {
		this.zyfbsj = zyfbsj;
	}
	public String getZyqzrq() {
		return zyqzrq;
	}
	public void setZyqzrq(String zyqzrq) {
		this.zyqzrq = zyqzrq;
	}
	public String getZyzljg() {
		return zyzljg;
	}
	public void setZyzljg(String zyzljg) {
		this.zyzljg = zyzljg;
	}
	public String getZyyyDm() {
		return zyyyDm;
	}
	public void setZyyyDm(String zyyyDm) {
		this.zyyyDm = zyyyDm;
	}
	public String getSfhz() {
		return sfhz;
	}
	public void setSfhz(String sfhz) {
		this.sfhz = sfhz;
	}
	public String getRyzzMc() {
		return ryzzMc;
	}
	public void setRyzzMc(String ryzzMc) {
		this.ryzzMc = ryzzMc;
	}
	public String getZygbsyDm() {
		return zygbsyDm;
	}
	public void setZygbsyDm(String zygbsyDm) {
		this.zygbsyDm = zygbsyDm;
	}
	public String getZyswsj() {
		return zyswsj;
	}
	public void setZyswsj(String zyswsj) {
		this.zyswsj = zyswsj;
	}
	public String getHzyy() {
		return hzyy;
	}
	public void setHzyy(String hzyy) {
		this.hzyy = hzyy;
	}
	public String getHzyj() {
		return hzyj;
	}
	public void setHzyj(String hzyj) {
		this.hzyj = hzyj;
	}
	public String getTgjcjl() {
		return tgjcjl;
	}
	public void setTgjcjl(String tgjcjl) {
		this.tgjcjl = tgjcjl;
	}
	public String getZkqk() {
		return zkqk;
	}
	public void setZkqk(String zkqk) {
		this.zkqk = zkqk;
	}
	public String getZhxgrydm() {
		return zhxgrydm;
	}
	public void setZhxgrydm(String zhxgrydm) {
		this.zhxgrydm = zhxgrydm;
	}
	public String getZhxgrymc() {
		return zhxgrymc;
	}
	public void setZhxgrymc(String zhxgrymc) {
		this.zhxgrymc = zhxgrymc;
	}
	public String getJlr() {
		return jlr;
	}
	public void setJlr(String jlr) {
		this.jlr = jlr;
	}
	public String getJlsj() {
		return jlsj;
	}
	public void setJlsj(String jlsj) {
		this.jlsj = jlsj;
	}

	
}
