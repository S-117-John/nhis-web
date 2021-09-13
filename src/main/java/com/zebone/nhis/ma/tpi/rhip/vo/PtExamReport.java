package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 检查报告
 */
@XmlRootElement(name = "Pt_ExamReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtExamReport {
    @XmlElement(name = "BCKSMC")
    protected String bcksmc;
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "JCBW")
    protected String jcbw;
    @XmlElement(name = "JCLSH", required = true)
    protected String jclsh;
    @XmlElement(name = "BCKGSJ")
    protected String bckgsj;
    @XmlElement(name = "BRBSLB", required = true)
    protected String brbslb;
    @XmlElement(name = "BCZGTS")
    protected String bczgts;
    @XmlElement(name = "SQDH", required = true)
    protected String sqdh;
    @XmlElement(name = "SQKSBM", required = true)
    protected String sqksbm;
    @XmlElement(name = "SQKSMC", required = true)
    protected String sqksmc;
    @XmlElement(name = "JCXMDM", required = true)
    protected String jcxmdm;
    @XmlElement(name = "JCXMMC")
    protected String jcxmmc;
    @XmlElement(name = "JCLX")
    protected String jclx;
    @XmlElement(name = "JCFF")
    protected String jcff;
    @XmlElement(name = "JCSBYQMC", required = true)
    protected String jcsbyqmc;
    @XmlElement(name = "JCSBYQH", required = true)
    protected String jcsbyqh;
    @XmlElement(name = "JCZDHTS")
    protected String jczdhts;
    @XmlElement(name = "JCRQ", required = true)
    protected String jcrq;
    @XmlElement(name = "YYX")
    protected String yyx;
    @XmlElement(name = "SFYYXBS_DM", required = true)
    protected String sfyyx;
    @XmlElement(name = "SFFSX", required = true)
    protected String sffsx;
    @XmlElement(name = "BGSJ", required = true)
    protected String bgsj;
    @XmlElement(name = "SHRQ", required = true)
    protected String shrq;
    @XmlElement(name = "BGBZ")
    protected String bgbz;
    @XmlElement(name = "YYKSSJ")
    protected String yykssj;
    @XmlElement(name = "YYJSSJ")
    protected String yyjssj;
    @XmlElement(name = "SQYSXM", required = true)
    protected String sqysxm;
    @XmlElement(name = "KDSJ")
    protected String kdsj;
    @XmlElement(name = "ZFPB")
    protected String zfpb;
    @XmlElement(name = "YXSL")
    protected String yxsl;
    @XmlElement(name = "BGYSXM")
    protected String bgysxm;
    @XmlElement(name = "SHYSSFZH")
    protected String shyssfzh;
    @XmlElement(name = "SHYSXM")
    protected String shysxm;
    @XmlElement(name = "MXHM", required = true)
    protected String mxhm;
    @XmlElement(name = "DXYQBM")
    protected String dxyqbm;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
	@XmlAttribute(name = "Name")
    protected String name;
	@XmlElement(name = "YZID")
    protected String yzid;
	@XmlElement(name = "SQYSGH")
    protected String sqysgh;	
	@XmlElement(name = "SQRQ")
    protected String sqrq;	
	@XmlElement(name = "JCKSMC")
    protected String jcksmc;
	@XmlElement(name = "JCYSGH")
    protected String jcysgh;	
	@XmlElement(name = "JCJG_DM")
    protected String jcjgDm;	
	@XmlElement(name = "BGKSMC")
    protected String bgksmc;
	@XmlElement(name = "BGYSGH")
    protected String bgysgh;	
	@XmlElement(name = "SHYSGH")
    protected String shysgh;	
	@XmlElement(name = "IDCardCode")
    protected String idCardCode;
	@XmlElement(name = "IDCard")
    protected String idCard;
	@XmlElement(name = "KH")
    protected String kh;
	@XmlElement(name = "KLX")
    protected String klx;
	@XmlElement(name = "JCJLDW")
    protected String jcjldw;
	@XmlElement(name = "JCYSCXM")
    protected String jcyscxm;
	@XmlElement(name = "BGKS_BM")
    protected String bgks_bm;
	@XmlElement(name = "SQYY_BM")
    protected String sqyy_bm;
	@XmlElement(name = "SBLB_BM")
    protected String sblb_bm;
	@XmlElement(name = "SBLB_MC")
    protected String sblb_mc;
	@XmlElement(name = "JCBWACRBM")
    protected String jcbwacrbm;
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

	
    @XmlElementWrapper(name="PT_ExamPACSs")
    @XmlElement(name = "PT_ExamPACS")
    private List<PTExamPACS> ptExamPACSs;

    
    public String getBcksmc() {
		return bcksmc;
	}


	public void setBcksmc(String bcksmc) {
		this.bcksmc = bcksmc;
	}


	public String getJzlsh() {
		return jzlsh;
	}


	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}


	public String getJcbw() {
		return jcbw;
	}


	public void setJcbw(String jcbw) {
		this.jcbw = jcbw;
	}


	public String getJclsh() {
		return jclsh;
	}


	public void setJclsh(String jclsh) {
		this.jclsh = jclsh;
	}


	public String getBckgsj() {
		return bckgsj;
	}


	public void setBckgsj(String bckgsj) {
		this.bckgsj = bckgsj;
	}


	public String getBrbslb() {
		return brbslb;
	}


	public void setBrbslb(String brbslb) {
		this.brbslb = brbslb;
	}


	public String getBczgts() {
		return bczgts;
	}


	public void setBczgts(String bczgts) {
		this.bczgts = bczgts;
	}


	public String getSqdh() {
		return sqdh;
	}


	public void setSqdh(String sqdh) {
		this.sqdh = sqdh;
	}


	public String getSqksbm() {
		return sqksbm;
	}


	public void setSqksbm(String sqksbm) {
		this.sqksbm = sqksbm;
	}


	public String getSqksmc() {
		return sqksmc;
	}


	public void setSqksmc(String sqksmc) {
		this.sqksmc = sqksmc;
	}


	public String getJcxmdm() {
		return jcxmdm;
	}


	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}


	public String getJcxmmc() {
		return jcxmmc;
	}


	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}


	public String getJclx() {
		return jclx;
	}


	public void setJclx(String jclx) {
		this.jclx = jclx;
	}


	public String getJcff() {
		return jcff;
	}


	public void setJcff(String jcff) {
		this.jcff = jcff;
	}


	public String getJcsbyqmc() {
		return jcsbyqmc;
	}


	public void setJcsbyqmc(String jcsbyqmc) {
		this.jcsbyqmc = jcsbyqmc;
	}


	public String getJcsbyqh() {
		return jcsbyqh;
	}


	public void setJcsbyqh(String jcsbyqh) {
		this.jcsbyqh = jcsbyqh;
	}


	public String getJczdhts() {
		return jczdhts;
	}


	public void setJczdhts(String jczdhts) {
		this.jczdhts = jczdhts;
	}


	public String getJcrq() {
		return jcrq;
	}


	public void setJcrq(String jcrq) {
		this.jcrq = jcrq;
	}


	public String getYyx() {
		return yyx;
	}


	public void setYyx(String yyx) {
		this.yyx = yyx;
	}


	public String getSfyyx() {
		return sfyyx;
	}


	public void setSfyyx(String sfyyx) {
		this.sfyyx = sfyyx;
	}


	public String getSffsx() {
		return sffsx;
	}


	public void setSffsx(String sffsx) {
		this.sffsx = sffsx;
	}


	public String getBgsj() {
		return bgsj;
	}


	public void setBgsj(String bgsj) {
		this.bgsj = bgsj;
	}


	public String getShrq() {
		return shrq;
	}


	public void setShrq(String shrq) {
		this.shrq = shrq;
	}


	public String getBgbz() {
		return bgbz;
	}


	public void setBgbz(String bgbz) {
		this.bgbz = bgbz;
	}


	public String getYykssj() {
		return yykssj;
	}


	public void setYykssj(String yykssj) {
		this.yykssj = yykssj;
	}


	public String getYyjssj() {
		return yyjssj;
	}


	public void setYyjssj(String yyjssj) {
		this.yyjssj = yyjssj;
	}


	public String getSqysxm() {
		return sqysxm;
	}


	public void setSqysxm(String sqysxm) {
		this.sqysxm = sqysxm;
	}


	public String getKdsj() {
		return kdsj;
	}


	public void setKdsj(String kdsj) {
		this.kdsj = kdsj;
	}

	public String getZfpb() {
		return zfpb;
	}


	public void setZfpb(String zfpb) {
		this.zfpb = zfpb;
	}


	public String getYxsl() {
		return yxsl;
	}


	public void setYxsl(String yxsl) {
		this.yxsl = yxsl;
	}


	public String getBgysxm() {
		return bgysxm;
	}


	public void setBgysxm(String bgysxm) {
		this.bgysxm = bgysxm;
	}


	public String getShyssfzh() {
		return shyssfzh;
	}


	public void setShyssfzh(String shyssfzh) {
		this.shyssfzh = shyssfzh;
	}


	public String getShysxm() {
		return shysxm;
	}


	public void setShysxm(String shysxm) {
		this.shysxm = shysxm;
	}


	public String getMxhm() {
		return mxhm;
	}


	public void setMxhm(String mxhm) {
		this.mxhm = mxhm;
	}


	public String getDxyqbm() {
		return dxyqbm;
	}


	public void setDxyqbm(String dxyqbm) {
		this.dxyqbm = dxyqbm;
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


	public List<PTExamPACS> getPtExamPACSs() {
		return ptExamPACSs;
	}


	public void setPtExamPACSs(List<PTExamPACS> ptExamPACSs) {
		this.ptExamPACSs = ptExamPACSs;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getSourceId() {
		return jclsh;
	}


	public String getYzid() {
		return yzid;
	}


	public void setYzid(String yzid) {
		this.yzid = yzid;
	}


	public String getSqysgh() {
		return sqysgh;
	}


	public void setSqysgh(String sqysgh) {
		this.sqysgh = sqysgh;
	}


	public String getSqrq() {
		return sqrq;
	}


	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}


	public String getJcksmc() {
		return jcksmc;
	}


	public void setJcksmc(String jcksmc) {
		this.jcksmc = jcksmc;
	}


	public String getJcjgDm() {
		return jcjgDm;
	}


	public void setJcjgDm(String jcjgDm) {
		this.jcjgDm = jcjgDm;
	}


	public String getJcysgh() {
		return jcysgh;
	}


	public void setJcysgh(String jcysgh) {
		this.jcysgh = jcysgh;
	}

	public String getBgksmc() {
		return bgksmc;
	}


	public void setBgksmc(String bgksmc) {
		this.bgksmc = bgksmc;
	}


	public String getBgysgh() {
		return bgysgh;
	}


	public void setBgysgh(String bgysgh) {
		this.bgysgh = bgysgh;
	}


	public String getShysgh() {
		return shysgh;
	}


	public void setShysgh(String shysgh) {
		this.shysgh = shysgh;
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

	public String getJcjldw() {
		return jcjldw;
	}

	public void setJcjldw(String jcjldw) {
		this.jcjldw = jcjldw;
	}

	public String getJcyscxm() {
		return jcyscxm;
	}

	public void setJcyscxm(String jcyscxm) {
		this.jcyscxm = jcyscxm;
	}

	public String getBgks_bm() {
		return bgks_bm;
	}

	public void setBgks_bm(String bgks_bm) {
		this.bgks_bm = bgks_bm;
	}

	public String getSqyy_bm() {
		return sqyy_bm;
	}

	public void setSqyy_bm(String sqyy_bm) {
		this.sqyy_bm = sqyy_bm;
	}

	public String getSblb_bm() {
		return sblb_bm;
	}

	public void setSblb_bm(String sblb_bm) {
		this.sblb_bm = sblb_bm;
	}

	public String getSblb_mc() {
		return sblb_mc;
	}

	public void setSblb_mc(String sblb_mc) {
		this.sblb_mc = sblb_mc;
	}

	public String getJcbwacrbm() {
		return jcbwacrbm;
	}

	public void setJcbwacrbm(String jcbwacrbm) {
		this.jcbwacrbm = jcbwacrbm;
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
