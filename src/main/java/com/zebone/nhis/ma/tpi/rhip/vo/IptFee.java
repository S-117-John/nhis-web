package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 在/出院结算表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_Fee")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptFee {
	@XmlElement(name = "JZLSH")
    protected String jzlsh;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "TFBZ", required = true)
    protected String tfbz;
    @XmlElement(name = "FYID", required = true)
    protected String fyid;
    @XmlElement(name = "JSLY")
    protected String jsly;
    @XmlElement(name = "FPHM", required = true)
    protected String fphm;
    @XmlElement(name = "STFSJ", required = true)
    protected String stfsj;
    @XmlElement(name = "FYJE", required = true)
    protected String fyje;
//    @XmlElement(name = "JCF")
//    protected String jcf;
    @XmlElement(name = "ZLF")
    protected String zlf;
    @XmlElement(name = "QTF")
    protected String qtf;
    @XmlElement(name = "SSF")
    protected String ssf;
    @XmlElement(name = "FSF")
    protected String fsf;
    @XmlElement(name = "HYF")
    protected String hyf;
    @XmlElement(name = "ZCF")
    protected String zcf;
    @XmlElement(name = "ZCYF1")
    protected String zcyf1;
    @XmlElement(name = "ZCYF")
    protected String zcyf;
    @XmlElement(name = "GHF")
    protected String ghf;
    @XmlElement(name = "XYF")
    protected String xyf;
    @XmlElement(name = "HLF")
    protected String hlf;
    @XmlElement(name = "SXF")
    protected String sxf;
    @XmlElement(name = "MZF")
    protected String mzf;
    @XmlElement(name = "TSF")
    protected String tsf;
    @XmlElement(name = "SYF")
    protected String syf;
    @XmlElement(name = "TXYF")
    protected String txyf;
    @XmlElement(name = "SSCLF")
    protected String ssclf;
    @XmlElement(name = "CWF")
    protected String cwf;
    @XmlElement(name = "JSF")
    protected String jsf;
    @XmlElement(name = "PCF")
    protected String pcf;
    @XmlElement(name = "TXF")
    protected String txf;
    @XmlElement(name = "KTF")
    protected String ktf;
    @XmlElement(name = "ZJF")
    protected String zjf;
    @XmlElement(name = "XYF1")
    protected String xyf1;
    @XmlElement(name = "CLF")
    protected String clf;
    @XmlElement(name = "QJF")
    protected String qjf;
    @XmlElement(name = "JCEF")
    protected String jcef;
    @XmlElement(name = "YBLXBM")
    protected String yblxbm;
    @XmlElement(name = "YBLXMC")
    protected String yblxmc;
    @XmlElement(name = "ZLJE")
    protected String zlje;
    @XmlElement(name = "ZFJE")
    protected String zfje;
    @XmlElement(name = "FHYBFY")
    protected String fhybfy;
    @XmlElement(name = "YBJJ")
    protected String ybjj;
    @XmlElement(name = "YBQFX")
    protected String ybqfx;
    @XmlElement(name = "GRZF")
    protected String grzf;
    @XmlElement(name = "YYFD")
    protected String yyfd;
    @XmlElement(name = "ZZXZF")
    protected String zzxzf;
    @XmlElement(name = "TCFDZF")
    protected String tcfdzf;
    @XmlElement(name = "CFDXZF")
    protected String cfdxzf;
    @XmlElement(name = "TCJJZF")
    protected String tcjjzf;
    @XmlElement(name = "GWYJJZF")
    protected String gwyjjzf;
    @XmlElement(name = "DBJJZF")
    protected String dbjjzf;
    @XmlElement(name = "ZHZF")
    protected String zhzf;
    @XmlElement(name = "MZJZZF")
    protected String mzjzzf;
    @XmlElement(name = "QTJJZF")
    protected String qtjjzf;
    @XmlElement(name = "BCXJZF")
    protected String bcxjzf;
    @XmlElement(name = "JSRQ")
    protected String jsrq;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "YBJSLSH")
    protected String ybjslsh;
    @XmlElement(name = "SFYGH")
    protected String sfygh;
    @XmlElement(name = "SFYXM")
    protected String sfyxm;
    @XmlElement(name = "YLFYLYLB_DM")
    protected String ylfylylbDm;
    @XmlElement(name = "YLFYJSFS_DM")
    protected String ylfyjsfsDm;
    @XmlElement(name = "SFBS_DM")
    protected String sfbsDm;
    @XmlElement(name = "SFBSHM")
    protected String sfbshm;
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
    @XmlElement(name = "BCYLTC")
    protected String bcyltc;

	@XmlElementWrapper(name="Ipt_FeeCosts")
	@XmlElement(name = "Ipt_FeeCost")
	protected List<IptFeeCost> IPT_DrugRecords;



	public String getJzlsh() {
		return jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
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
	public String getTfbz() {
		return tfbz;
	}
	public void setTfbz(String tfbz) {
		this.tfbz = tfbz;
	}
	public String getFyid() {
		return fyid;
	}
	public void setFyid(String fyid) {
		this.fyid = fyid;
	}
	public String getJsly() {
		return jsly;
	}
	public void setJsly(String jsly) {
		this.jsly = jsly;
	}
	public String getFphm() {
		return fphm;
	}
	public void setFphm(String fphm) {
		this.fphm = fphm;
	}
	public String getStfsj() {
		return stfsj;
	}
	public void setStfsj(String stfsj) {
		this.stfsj = stfsj;
	}
	public String getFyje() {
		return fyje;
	}
	public void setFyje(String fyje) {
		this.fyje = fyje;
	}

	public String getZlf() {
		return zlf;
	}
	public void setZlf(String zlf) {
		this.zlf = zlf;
	}
	public String getQtf() {
		return qtf;
	}
	public void setQtf(String qtf) {
		this.qtf = qtf;
	}
	public String getSsf() {
		return ssf;
	}
	public void setSsf(String ssf) {
		this.ssf = ssf;
	}
	public String getFsf() {
		return fsf;
	}
	public void setFsf(String fsf) {
		this.fsf = fsf;
	}
	public String getHyf() {
		return hyf;
	}
	public void setHyf(String hyf) {
		this.hyf = hyf;
	}
	public String getZcf() {
		return zcf;
	}
	public void setZcf(String zcf) {
		this.zcf = zcf;
	}
	public String getZcyf1() {
		return zcyf1;
	}
	public void setZcyf1(String zcyf1) {
		this.zcyf1 = zcyf1;
	}
	public String getZcyf() {
		return zcyf;
	}
	public void setZcyf(String zcyf) {
		this.zcyf = zcyf;
	}
	public String getGhf() {
		return ghf;
	}
	public void setGhf(String ghf) {
		this.ghf = ghf;
	}
	public String getXyf() {
		return xyf;
	}
	public void setXyf(String xyf) {
		this.xyf = xyf;
	}
	public String getHlf() {
		return hlf;
	}
	public void setHlf(String hlf) {
		this.hlf = hlf;
	}
	public String getSxf() {
		return sxf;
	}
	public void setSxf(String sxf) {
		this.sxf = sxf;
	}
	public String getMzf() {
		return mzf;
	}
	public void setMzf(String mzf) {
		this.mzf = mzf;
	}
	public String getTsf() {
		return tsf;
	}
	public void setTsf(String tsf) {
		this.tsf = tsf;
	}
	public String getSyf() {
		return syf;
	}
	public void setSyf(String syf) {
		this.syf = syf;
	}
	public String getTxyf() {
		return txyf;
	}
	public void setTxyf(String txyf) {
		this.txyf = txyf;
	}
	public String getSsclf() {
		return ssclf;
	}
	public void setSsclf(String ssclf) {
		this.ssclf = ssclf;
	}
	public String getCwf() {
		return cwf;
	}
	public void setCwf(String cwf) {
		this.cwf = cwf;
	}
	public String getJsf() {
		return jsf;
	}
	public void setJsf(String jsf) {
		this.jsf = jsf;
	}
	public String getPcf() {
		return pcf;
	}
	public void setPcf(String pcf) {
		this.pcf = pcf;
	}
	public String getTxf() {
		return txf;
	}
	public void setTxf(String txf) {
		this.txf = txf;
	}
	public String getKtf() {
		return ktf;
	}
	public void setKtf(String ktf) {
		this.ktf = ktf;
	}
	public String getZjf() {
		return zjf;
	}
	public void setZjf(String zjf) {
		this.zjf = zjf;
	}
	public String getXyf1() {
		return xyf1;
	}
	public void setXyf1(String xyf1) {
		this.xyf1 = xyf1;
	}
	public String getClf() {
		return clf;
	}
	public void setClf(String clf) {
		this.clf = clf;
	}
	public String getQjf() {
		return qjf;
	}
	public void setQjf(String qjf) {
		this.qjf = qjf;
	}
	public String getJcef() {
		return jcef;
	}
	public void setJcef(String jcef) {
		this.jcef = jcef;
	}
	public String getYblxbm() {
		return yblxbm;
	}
	public void setYblxbm(String yblxbm) {
		this.yblxbm = yblxbm;
	}
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getZlje() {
		return zlje;
	}
	public void setZlje(String zlje) {
		this.zlje = zlje;
	}
	public String getZfje() {
		return zfje;
	}
	public void setZfje(String zfje) {
		this.zfje = zfje;
	}
	public String getFhybfy() {
		return fhybfy;
	}
	public void setFhybfy(String fhybfy) {
		this.fhybfy = fhybfy;
	}
	public String getYbjj() {
		return ybjj;
	}
	public void setYbjj(String ybjj) {
		this.ybjj = ybjj;
	}
	public String getYbqfx() {
		return ybqfx;
	}
	public void setYbqfx(String ybqfx) {
		this.ybqfx = ybqfx;
	}
	public String getGrzf() {
		return grzf;
	}
	public void setGrzf(String grzf) {
		this.grzf = grzf;
	}
	public String getYyfd() {
		return yyfd;
	}
	public void setYyfd(String yyfd) {
		this.yyfd = yyfd;
	}
	public String getZzxzf() {
		return zzxzf;
	}
	public void setZzxzf(String zzxzf) {
		this.zzxzf = zzxzf;
	}
	public String getTcfdzf() {
		return tcfdzf;
	}
	public void setTcfdzf(String tcfdzf) {
		this.tcfdzf = tcfdzf;
	}
	public String getCfdxzf() {
		return cfdxzf;
	}
	public void setCfdxzf(String cfdxzf) {
		this.cfdxzf = cfdxzf;
	}
	public String getTcjjzf() {
		return tcjjzf;
	}
	public void setTcjjzf(String tcjjzf) {
		this.tcjjzf = tcjjzf;
	}
	public String getGwyjjzf() {
		return gwyjjzf;
	}
	public void setGwyjjzf(String gwyjjzf) {
		this.gwyjjzf = gwyjjzf;
	}
	public String getDbjjzf() {
		return dbjjzf;
	}
	public void setDbjjzf(String dbjjzf) {
		this.dbjjzf = dbjjzf;
	}
	public String getZhzf() {
		return zhzf;
	}
	public void setZhzf(String zhzf) {
		this.zhzf = zhzf;
	}
	public String getMzjzzf() {
		return mzjzzf;
	}
	public void setMzjzzf(String mzjzzf) {
		this.mzjzzf = mzjzzf;
	}
	public String getQtjjzf() {
		return qtjjzf;
	}
	public void setQtjjzf(String qtjjzf) {
		this.qtjjzf = qtjjzf;
	}
	public String getBcxjzf() {
		return bcxjzf;
	}
	public void setBcxjzf(String bcxjzf) {
		this.bcxjzf = bcxjzf;
	}
	public String getJsrq() {
		return jsrq;
	}
	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getSourceId() {
		return fyid;
	}
	public String getYbjslsh() {
		return ybjslsh;
	}
	public void setYbjslsh(String ybjslsh) {
		this.ybjslsh = ybjslsh;
	}
	public String getSfygh() {
		return sfygh;
	}
	public void setSfygh(String sfygh) {
		this.sfygh = sfygh;
	}
	public String getSfyxm() {
		return sfyxm;
	}
	public void setSfyxm(String sfyxm) {
		this.sfyxm = sfyxm;
	}
	public String getYlfylylbDm() {
		return ylfylylbDm;
	}
	public void setYlfylylbDm(String ylfylylbDm) {
		this.ylfylylbDm = ylfylylbDm;
	}
	public String getYlfyjsfsDm() {
		return ylfyjsfsDm;
	}
	public void setYlfyjsfsDm(String ylfyjsfsDm) {
		this.ylfyjsfsDm = ylfyjsfsDm;
	}
	public String getSfbsDm() {
		return sfbsDm;
	}
	public void setSfbsDm(String sfbsDm) {
		this.sfbsDm = sfbsDm;
	}
	public String getSfbshm() {
		return sfbshm;
	}
	public void setSfbshm(String sfbshm) {
		this.sfbshm = sfbshm;
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
	public String getBcyltc() {
		return bcyltc;
	}
	public void setBcyltc(String bcyltc) {
		this.bcyltc = bcyltc;
	}

	public List<IptFeeCost> getIPT_DrugRecords() {
		return IPT_DrugRecords;
	}

	public void setIPT_DrugRecords(List<IptFeeCost> IPT_DrugRecords) {
		this.IPT_DrugRecords = IPT_DrugRecords;
	}
}
