package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 检验报告
 * @author chengjia
 *
 */
@XmlRootElement(name = "Pt_LabReport")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtLabReport {
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "JYBGDH", required = true)
    protected String jybgdh;
    @XmlElement(name = "BRBSLB", required = true)
    protected String brbslb;
    @XmlElement(name = "JYBGDDM")
    protected String jybgddm;
    @XmlElement(name = "JYBGDMC", required = true)
    protected String jybgdmc;
    @XmlElement(name = "SHYSXM")
    protected String shysxm;
    @XmlElement(name = "SHYSGH", required = true)
    protected String shysgh;
    @XmlElement(name = "BGYSXM", required = true)
    protected String bgysxm;
    @XmlElement(name = "BGYSGH", required = true)
    protected String bgysgh;
    @XmlElement(name = "SQYSXM")
    protected String sqysxm;
    @XmlElement(name = "SQYSGH")
    protected String sqysgh;
    @XmlElement(name = "SQRQ")
    protected String sqrq;
    @XmlElement(name = "JYRQ", required = true)
    protected String jyrq;
    @XmlElement(name = "BGRQ", required = true)
    protected String bgrq;
    @XmlElement(name = "BBLBDM")
    protected String bblbdm;
    @XmlElement(name = "SHYSGH2")
    protected String shysgh2;
    @XmlElement(name = "JCSBYQMC")
    protected String jcsbyqmc;
    @XmlElement(name = "YBZT")
    protected String ybzt;
    @XmlElement(name = "YEBZ")
    protected String yebz;
    @XmlElement(name = "JZBZ", required = true)
    protected String jzbz;
    @XmlElement(name = "CJSJ")
    protected String cjsj;
    @XmlElement(name = "YBJSSJ", required = true)
    protected String ybjssj;
    @XmlElement(name = "CJBW")
    protected String cjbw;
    @XmlElement(name = "TXDM")
    protected String txdm;
    @XmlElement(name = "SQKSDM")
    protected String sqksdm;
    @XmlElement(name = "JYBGDJG", required = true)
    protected String jybgdjg;
    @XmlElement(name = "BZ")
    protected String bz;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "JYID")
    protected String jyid;
    @XmlElement(name = "YZID")
    protected String yzid;
    @XmlElement(name = "KLX")
    protected String klx;
    @XmlElement(name = "SFBSLB_DM")
    protected String sfbslb_dm;
    @XmlElement(name = "SFBSHM")
    protected String sfbshm;
    @XmlElement(name = "KH")
    protected String kh;
    @XmlElement(name = "BGDLBD_BM")
    protected String bgdlbd_bm;
    @XmlElement(name = "JYXM_DM")
    protected String jyxm_dm;
    @XmlElement(name = "JYXM_MC")
    protected String jyxm_mc;
    @XmlElement(name = "SHRQ")
    protected String shrq;
    @XmlElement(name = "SQYY_BM")
    protected String sqyy_bm;
    @XmlElement(name = "SQYY_MC")
    protected String sqyy_mc;
    @XmlElement(name = "SQKSBM")
    protected String sqksbm;
    @XmlElement(name = "SQKSMC")
    protected String sqksmc;
    @XmlElement(name = "JYBGDJGDM")
    protected String jybgdjgdm;
    @XmlElement(name = "JCYSGH")
    protected String jcysgh;
    @XmlElement(name = "JCYSXM")
    protected String jcysxm;
    @XmlElement(name = "BQ")
    protected String bq;
    @XmlElement(name = "CH")
    protected String ch;
    @XmlElement(name = "JBZD_BM")
    protected String jbzd_bm;
    @XmlElement(name = "BB_MC")
    protected String bb_mc;
    @XmlElement(name = "DYRQ")
    protected String dyrq;
    @XmlElement(name = "BGBZ")
    protected String bgbz;
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

    @XmlElement(name = "CFID")
    protected String cfid;


    @XmlElementWrapper(name="Pt_BacteriaResults")
    @XmlElement(name = "Pt_BacteriaResult")
    private List<PtBacteriaResult> ptBacteriaResults;

    @XmlElementWrapper(name="Pt_AllergyResults")
    @XmlElement(name = "Pt_AllergyResult")
    private List<PtBacteriaResult> ptAllergyResults;



    @XmlElementWrapper(name="Pt_TrainResults")
    @XmlElement(name = "Pt_TrainResult")
    private List<PtTrainResult> ptTrainResults;


    @XmlElementWrapper(name="Pt_LabDetails")
    @XmlElement(name = "Pt_LabDetail")
    private List<PtLabDetail> ptLabDetails;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJzlsh() {
		return jzlsh;
	}

	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}

	public String getJybgdh() {
		return jybgdh;
	}

	public void setJybgdh(String jybgdh) {
		this.jybgdh = jybgdh;
	}

	public String getBrbslb() {
		return brbslb;
	}

	public void setBrbslb(String brbslb) {
		this.brbslb = brbslb;
	}

	public String getJybgddm() {
		return jybgddm;
	}

	public void setJybgddm(String jybgddm) {
		this.jybgddm = jybgddm;
	}

	public String getJybgdmc() {
		return jybgdmc;
	}

	public void setJybgdmc(String jybgdmc) {
		this.jybgdmc = jybgdmc;
	}

	public String getShysxm() {
		return shysxm;
	}

	public void setShysxm(String shysxm) {
		this.shysxm = shysxm;
	}

	public String getShysgh() {
		return shysgh;
	}

	public void setShysgh(String shysgh) {
		this.shysgh = shysgh;
	}

	public String getBgysxm() {
		return bgysxm;
	}

	public void setBgysxm(String bgysxm) {
		this.bgysxm = bgysxm;
	}

	public String getBgysgh() {
		return bgysgh;
	}

	public void setBgysgh(String bgysgh) {
		this.bgysgh = bgysgh;
	}

	public String getSqysxm() {
		return sqysxm;
	}

	public void setSqysxm(String sqysxm) {
		this.sqysxm = sqysxm;
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

	public String getJyrq() {
		return jyrq;
	}

	public void setJyrq(String jyrq) {
		this.jyrq = jyrq;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getBblbdm() {
		return bblbdm;
	}

	public void setBblbdm(String bblbdm) {
		this.bblbdm = bblbdm;
	}

	public String getShysgh2() {
		return shysgh2;
	}

	public void setShysgh2(String shysgh2) {
		this.shysgh2 = shysgh2;
	}

	public String getJcsbyqmc() {
		return jcsbyqmc;
	}

	public void setJcsbyqmc(String jcsbyqmc) {
		this.jcsbyqmc = jcsbyqmc;
	}

	public String getYbzt() {
		return ybzt;
	}

	public void setYbzt(String ybzt) {
		this.ybzt = ybzt;
	}

	public String getYebz() {
		return yebz;
	}

	public void setYebz(String yebz) {
		this.yebz = yebz;
	}

	public String getJzbz() {
		return jzbz;
	}

	public void setJzbz(String jzbz) {
		this.jzbz = jzbz;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getYbjssj() {
		return ybjssj;
	}

	public void setYbjssj(String ybjssj) {
		this.ybjssj = ybjssj;
	}

	public String getCjbw() {
		return cjbw;
	}

	public void setCjbw(String cjbw) {
		this.cjbw = cjbw;
	}

	public String getTxdm() {
		return txdm;
	}

	public void setTxdm(String txdm) {
		this.txdm = txdm;
	}

	public String getSqksdm() {
		return sqksdm;
	}

	public void setSqksdm(String sqksdm) {
		this.sqksdm = sqksdm;
	}

	public String getJybgdjg() {
		return jybgdjg;
	}

	public void setJybgdjg(String jybgdjg) {
		this.jybgdjg = jybgdjg;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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

	public List<PtBacteriaResult> getPtBacteriaResults() {
		return ptBacteriaResults;
	}

	public void setPtBacteriaResults(List<PtBacteriaResult> ptBacteriaResults) {
		this.ptBacteriaResults = ptBacteriaResults;
	}

	public List<PtTrainResult> getPtTrainResults() {
		return ptTrainResults;
	}

	public void setPtTrainResults(List<PtTrainResult> ptTrainResults) {
		this.ptTrainResults = ptTrainResults;
	}

	public List<PtLabDetail> getPtLabDetails() {
		return ptLabDetails;
	}

	public void setPtLabDetails(List<PtLabDetail> ptLabDetails) {
		this.ptLabDetails = ptLabDetails;
	}

	public String getSourceId() {
		return jybgdh;
	}

	public String getJyid() {
		return jyid;
	}

	public void setJyid(String jyid) {
		this.jyid = jyid;
	}

	public String getYzid() {
		return yzid;
	}

	public void setYzid(String yzid) {
		this.yzid = yzid;
	}

	public String getKlx() {
		return klx;
	}

	public void setKlx(String klx) {
		this.klx = klx;
	}

	public String getSfbslb_dm() {
		return sfbslb_dm;
	}

	public void setSfbslb_dm(String sfbslb_dm) {
		this.sfbslb_dm = sfbslb_dm;
	}

	public String getSfbshm() {
		return sfbshm;
	}

	public void setSfbshm(String sfbshm) {
		this.sfbshm = sfbshm;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getBgdlbd_bm() {
		return bgdlbd_bm;
	}

	public void setBgdlbd_bm(String bgdlbd_bm) {
		this.bgdlbd_bm = bgdlbd_bm;
	}

	public String getJyxm_dm() {
		return jyxm_dm;
	}

	public void setJyxm_dm(String jyxm_dm) {
		this.jyxm_dm = jyxm_dm;
	}

	public String getJyxm_mc() {
		return jyxm_mc;
	}

	public void setJyxm_mc(String jyxm_mc) {
		this.jyxm_mc = jyxm_mc;
	}

	public String getShrq() {
		return shrq;
	}

	public void setShrq(String shrq) {
		this.shrq = shrq;
	}

	public String getSqyy_bm() {
		return sqyy_bm;
	}

	public void setSqyy_bm(String sqyy_bm) {
		this.sqyy_bm = sqyy_bm;
	}

	public String getSqyy_mc() {
		return sqyy_mc;
	}

	public void setSqyy_mc(String sqyy_mc) {
		this.sqyy_mc = sqyy_mc;
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

	public String getJybgdjgdm() {
		return jybgdjgdm;
	}

	public void setJybgdjgdm(String jybgdjgdm) {
		this.jybgdjgdm = jybgdjgdm;
	}

	public String getJcysgh() {
		return jcysgh;
	}

	public void setJcysgh(String jcysgh) {
		this.jcysgh = jcysgh;
	}

	public String getJcysxm() {
		return jcysxm;
	}

	public void setJcysxm(String jcysxm) {
		this.jcysxm = jcysxm;
	}

	public String getBq() {
		return bq;
	}

	public void setBq(String bq) {
		this.bq = bq;
	}

	public List<PtBacteriaResult> getPtAllergyResults() {
		return ptAllergyResults;
	}

	public void setPtAllergyResults(List<PtBacteriaResult> ptAllergyResults) {
		this.ptAllergyResults = ptAllergyResults;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getJbzd_bm() {
		return jbzd_bm;
	}

	public void setJbzd_bm(String jbzd_bm) {
		this.jbzd_bm = jbzd_bm;
	}

	public String getBb_mc() {
		return bb_mc;
	}

	public void setBb_mc(String bb_mc) {
		this.bb_mc = bb_mc;
	}

	public String getDyrq() {
		return dyrq;
	}

	public void setDyrq(String dyrq) {
		this.dyrq = dyrq;
	}

	public String getBgbz() {
		return bgbz;
	}

	public void setBgbz(String bgbz) {
		this.bgbz = bgbz;
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

	public String getCfid() {
		return cfid;
	}

	public void setCfid(String cfid) {
		this.cfid = cfid;
	}
}

