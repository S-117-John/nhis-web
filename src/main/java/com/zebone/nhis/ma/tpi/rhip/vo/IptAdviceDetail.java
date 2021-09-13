package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 住院医嘱明细表
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_AdviceDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class IptAdviceDetail {
    @XmlElement(name = "ZXSJ")
    protected String zxsj;
    @XmlElement(name = "YZMXMC")
    protected String yzmxmc;
    @XmlElement(name = "YPDW")
    protected String ypdw;
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "XSYPDW")
    protected String xsypdw;
    @XmlElement(name = "YZID")
    protected String yzid;
    @XmlElement(name = "YZZH", required = true)
    protected String yzzh;
    @XmlElement(name = "YZBS", required = true)
    protected String yzbs;
    @XmlElement(name = "YZMXBM")
    protected String yzmxbm;
    @XmlElement(name = "YPTYM")
    protected String yptym;
    @XmlElement(name = "YPSPM")
    protected String ypspm;
    @XmlElement(name = "CDID")
    protected String cdid;
    @XmlElement(name = "CDMC")
    protected String cdmc;
    @XmlElement(name = "YPBZGGXS")
    protected String ypbzggxs;
    @XmlElement(name = "YPBZGGDW")
    protected String ypbzggdw;
    @XmlElement(name = "SFYPBS", required = true)
    protected String sfyp;
    @XmlElement(name = "YPGG")
    protected String ypgg;
    @XmlElement(name = "YPSL")
    protected String ypsl;
    @XmlElement(name = "YPYF")
    protected String ypyf;
    @XmlElement(name = "YYPD")
    protected String yypd;
    @XmlElement(name = "YWFZY")
    protected String ywfzy;
    @XmlElement(name = "MCSYJL")
    protected String mcsyjl;
    @XmlElement(name = "MCSYJLDW")
    protected String mcsyjldw;
    @XmlElement(name = "MCSYSL")
    protected String mcsysl;
    @XmlElement(name = "MCSYSLDW")
    protected String mcsysldw;
    @XmlElement(name = "ZFYBS")
    protected String zfybs;
    @XmlElement(name = "TJDM",required = true) //给药途径代码
    protected String tjdm;
    /*
     * 1口服2直肠用药3舌下用药4注射用药401皮下注射402皮内注射403肌肉注射404	静脉注射或静脉滴注5吸人用药
     * 6局部用药601椎管内用药602关节腔内用药603胸膜腔用药604腹腔用药605阴道用药606气管内用药607滴眼608滴鼻609喷喉610含化611敷伤口612擦皮肤
	   699其他局部用药途径9其他用药途径
     * */
    @XmlElement(name = "ZYJZFDM")
    protected String zyjzfdm;
    @XmlElement(name = "YYTS")
    protected String yyts;
    @XmlElement(name = "YPYXCFSL")
    protected String ypyxcfsl;
    @XmlElement(name = "YPYXCFDW")
    protected String ypyxcfdw;
    @XmlElement(name = "YYMD")
    protected String yymd;
    @XmlElement(name = "KDYSGH")
    protected String kdysgh;
    @XmlElement(name = "KDYSXM", required = true)
    protected String kdysxm;
    @XmlElement(name = "KDKSDM", required = true)
    protected String kdksdm;
    @XmlElement(name = "KDYSZC")
    protected String kdyszc;
    @XmlElement(name = "KDSJ", required = true)
    protected String kdsj;
    @XmlElement(name = "ZXRYGH")
    protected String zxrygh;
    @XmlElement(name = "ZXRYXM", required = true)
    protected String zxryxm;
    @XmlElement(name = "ZXKSDM", required = true)
    protected String zxksdm;
    @XmlElement(name = "JCBWACRBM")
    protected String jcbwacrbm;
    @XmlElement(name = "DJ")
    protected String dj;
    @XmlElement(name = "ZE")
    protected String ze;
    @XmlElement(name = "ZZSJ")
    protected String zzsj;
    @XmlElement(name = "TJMC")
    protected String tjmc;
    @XmlElement(name = "YWJXMC")
    protected String ywjxmc;
    @XmlElement(name = "YWJXDM")
    protected String ywjxdm;
    @XmlElement(name = "HZXM")
    protected String hzxm;
    @XmlElement(name = "XBDM")
    protected String xbdm;
    @XmlElement(name = "CSRQ")
    protected String csrq;
    @XmlElement(name = "YZXMLXBM")
    protected String yzxmlxbm;
//    @XmlElement(name = "YZXMLXMC")
//    protected String yzxmlxmc;
    @XmlElement(name = "YPPZWH")
    protected String yppzwh;
    @XmlElement(name = "YPTYBM")
    protected String yptybm;
    @XmlElement(name = "YPYXQ")
    protected String ypyxq;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlElement(name = "ZYZDLSH", required = true)//住院诊断流水号
    protected String zyzdlsh;
    @XmlElement(name = "ZYBAH", required = true)//住院病案号
    protected String zybah;
    @XmlElement(name = "BQ_MC", required = true)//病区名称
    protected String bqmc;
    @XmlElement(name = "BFH", required = true)	//病房号
    protected String bfh;
    @XmlElement(name = "BCH", required = true)	//病床号
    protected String bch;
    @XmlElement(name = "KSBM", required = true)	//科室编码
    protected String ksbm;
    @XmlElement(name = "KSMC", required = true)	//科室名称
    protected String ksmc;
    @XmlElement(name = "YZBZXX", required = true)	//医嘱备注信息
    protected String yzbzxx;
    @XmlElement(name = "YPDM")	//药品代码
    protected String ypdm;
    @XmlElement(name = "YPMC")	//药品名称
    protected String ypmc;
    @XmlElement(name = "YWLX_DM", required = true)	//药物类型代码//HIS18_01_006
    protected String ywlxDm;
    @XmlElement(name = "YZSHZ", required = true)	//医嘱审核者	
    protected String yzshz;
    @XmlElement(name = "YZSHSJ", required = true)	//医嘱审核时间
    protected String yzshsj;
    @XmlElement(name = "HDYZHS", required = true)	//核对医嘱护士
    protected String hdyzhs;
    @XmlElement(name = "YZHDRQ", required = true)	//医嘱核对时间
    protected String yzhdrq;
    @XmlElement(name = "ZYBZ_DM", required = true)	//主药标识代码1否2是9不详
    protected String zybzdm;
    @XmlElement(name = "YZZXZT", required = true)	//医嘱执行状态
    protected String yzzxzt;
    @XmlElement(name = "QXYZZ", required = true)	//取消医嘱者
    protected String qxyzz;
    @XmlElement(name = "YZQXRQ", required = true)	//医嘱取消时间
    protected String yzqxrq;
    @XmlElement(name = "TZYZZ", required = true)	//停止医嘱者
    protected String tzyzz;
    @XmlElement(name = "FWXMDM")	//服务项目代码
    protected String fwxmdm;
    @XmlElement(name = "FWXMMC")	//服务项目名称
    protected String fwxmmc;
    @XmlElement(name = "ZYSYLB_DM")	//中药使用类别代码
    protected String zysylbDm;
    @XmlElement(name = "ZJLDW")	//总剂量单位
    protected String zjldw;
    @XmlElement(name = "ZJL")	//总剂量单位
    protected String zjl;
    @XmlElement(name = "YWGG")	//药物规格
    protected String ywgg;
    @XmlElement(name = "ZYLBDM")	//中药类别代码/CV06_00_101/1未使用  2	中成药  3	中草药  9	其他中药
    protected String zylbdm;
    @XmlElement(name = "ZH")	//组号
    protected String zh;
    @XmlElement(name = "SFPSBS")	//是否皮试标识0.否1.是
    protected String sfpsbs;
    @XmlElement(name = "YZXMMXID")	//医嘱项目明细ID
	protected String yzxmmxid;
    
    
	public void setZxsj(String zxsj) {
		this.zxsj = zxsj;
	}
	public void setYzmxmc(String yzmxmc) {
		this.yzmxmc = yzmxmc;
	}
	public void setYpdw(String ypdw) {
		this.ypdw = ypdw;
	}
	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}
	public void setXsypdw(String xsypdw) {
		this.xsypdw = xsypdw;
	}
	public void setYzid(String yzid) {
		this.yzid = yzid;
	}
	public void setYzzh(String yzzh) {
		this.yzzh = yzzh;
	}
	public void setYzbs(String yzbs) {
		this.yzbs = yzbs;
	}
	public void setYzmxbm(String yzmxbm) {
		this.yzmxbm = yzmxbm;
	}
	public void setYptym(String yptym) {
		this.yptym = yptym;
	}
	public void setYpspm(String ypspm) {
		this.ypspm = ypspm;
	}
	public void setCdid(String cdid) {
		this.cdid = cdid;
	}
	public void setCdmc(String cdmc) {
		this.cdmc = cdmc;
	}
	public void setYpbzggxs(String ypbzggxs) {
		this.ypbzggxs = ypbzggxs;
	}
	public void setYpbzggdw(String ypbzggdw) {
		this.ypbzggdw = ypbzggdw;
	}
	public void setSfyp(String sfyp) {
		this.sfyp = sfyp;
	}
	public void setYpgg(String ypgg) {
		this.ypgg = ypgg;
	}
	public void setYpsl(String ypsl) {
		this.ypsl = ypsl;
	}
	public void setYpyf(String ypyf) {
		this.ypyf = ypyf;
	}
	public void setYypd(String yypd) {
		this.yypd = yypd;
	}
	public void setYwfzy(String ywfzy) {
		this.ywfzy = ywfzy;
	}
	public void setMcsyjl(String mcsyjl) {
		this.mcsyjl = mcsyjl;
	}
	public void setMcsyjldw(String mcsyjldw) {
		this.mcsyjldw = mcsyjldw;
	}
	public void setMcsysl(String mcsysl) {
		this.mcsysl = mcsysl;
	}
	public void setMcsysldw(String mcsysldw) {
		this.mcsysldw = mcsysldw;
	}
	public void setZfybs(String zfybs) {
		this.zfybs = zfybs;
	}
	public void setTjdm(String tjdm) {
		this.tjdm = tjdm;
	}
	public void setZyjzfdm(String zyjzfdm) {
		this.zyjzfdm = zyjzfdm;
	}
	public void setYyts(String yyts) {
		this.yyts = yyts;
	}
	public void setYpyxcfsl(String ypyxcfsl) {
		this.ypyxcfsl = ypyxcfsl;
	}
	public void setYpyxcfdw(String ypyxcfdw) {
		this.ypyxcfdw = ypyxcfdw;
	}
	public void setYymd(String yymd) {
		this.yymd = yymd;
	}
	public void setKdysgh(String kdysgh) {
		this.kdysgh = kdysgh;
	}
	public void setKdysxm(String kdysxm) {
		this.kdysxm = kdysxm;
	}
	public void setKdksdm(String kdksdm) {
		this.kdksdm = kdksdm;
	}
	public void setKdyszc(String kdyszc) {
		this.kdyszc = kdyszc;
	}
	public void setKdsj(String kdsj) {
		this.kdsj = kdsj;
	}
	public void setZxrygh(String zxrygh) {
		this.zxrygh = zxrygh;
	}
	public void setZxryxm(String zxryxm) {
		this.zxryxm = zxryxm;
	}
	public void setZxksdm(String zxksdm) {
		this.zxksdm = zxksdm;
	}
	public void setJcbwacrbm(String jcbwacrbm) {
		this.jcbwacrbm = jcbwacrbm;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public void setZe(String ze) {
		this.ze = ze;
	}
	public void setZzsj(String zzsj) {
		this.zzsj = zzsj;
	}
	public void setTjmc(String tjmc) {
		this.tjmc = tjmc;
	}
	public void setYwjxmc(String ywjxmc) {
		this.ywjxmc = ywjxmc;
	}
	public void setYwjxdm(String ywjxdm) {
		this.ywjxdm = ywjxdm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public void setXbdm(String xbdm) {
		this.xbdm = xbdm;
	}
	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
	public void setYzxmlxbm(String yzxmlxbm) {
		this.yzxmlxbm = yzxmlxbm;
	}
//	public void setYzxmlxmc(String yzxmlxmc) {
//		this.yzxmlxmc = yzxmlxmc;
//	}
	public void setYppzwh(String yppzwh) {
		this.yppzwh = yppzwh;
	}
	public void setYptybm(String yptybm) {
		this.yptybm = yptybm;
	}
	public void setYpyxq(String ypyxq) {
		this.ypyxq = ypyxq;
	}
	public void setName(String name) {
		this.name = name;
	}
    
	public String getSourceId() {
		return yzid;
	}
	public String getZyzdlsh() {
		return zyzdlsh;
	}
	public void setZyzdlsh(String zyzdlsh) {
		this.zyzdlsh = zyzdlsh;
	}
	public String getZybah() {
		return zybah;
	}
	public void setZybah(String zybah) {
		this.zybah = zybah;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getBfh() {
		return bfh;
	}
	public void setBfh(String bfh) {
		this.bfh = bfh;
	}
	public String getBch() {
		return bch;
	}
	public void setBch(String bch) {
		this.bch = bch;
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
	public String getYzbzxx() {
		return yzbzxx;
	}
	public void setYzbzxx(String yzbzxx) {
		this.yzbzxx = yzbzxx;
	}
	public String getZxsj() {
		return zxsj;
	}
	public String getYzmxmc() {
		return yzmxmc;
	}
	public String getYpdw() {
		return ypdw;
	}
	public String getJzlsh() {
		return jzlsh;
	}
	public String getXsypdw() {
		return xsypdw;
	}
	public String getYzid() {
		return yzid;
	}
	public String getYzzh() {
		return yzzh;
	}
	public String getYzbs() {
		return yzbs;
	}
	public String getYzmxbm() {
		return yzmxbm;
	}
	public String getYptym() {
		return yptym;
	}
	public String getYpspm() {
		return ypspm;
	}
	public String getCdid() {
		return cdid;
	}
	public String getCdmc() {
		return cdmc;
	}
	public String getYpbzggxs() {
		return ypbzggxs;
	}
	public String getYpbzggdw() {
		return ypbzggdw;
	}
	public String getSfyp() {
		return sfyp;
	}
	public String getYpgg() {
		return ypgg;
	}
	public String getYpsl() {
		return ypsl;
	}
	public String getYpyf() {
		return ypyf;
	}
	public String getYypd() {
		return yypd;
	}
	public String getYwfzy() {
		return ywfzy;
	}
	public String getMcsyjl() {
		return mcsyjl;
	}
	public String getMcsyjldw() {
		return mcsyjldw;
	}
	public String getMcsysl() {
		return mcsysl;
	}
	public String getMcsysldw() {
		return mcsysldw;
	}
	public String getZfybs() {
		return zfybs;
	}
	public String getTjdm() {
		return tjdm;
	}
	public String getZyjzfdm() {
		return zyjzfdm;
	}
	public String getYyts() {
		return yyts;
	}
	public String getYpyxcfsl() {
		return ypyxcfsl;
	}
	public String getYpyxcfdw() {
		return ypyxcfdw;
	}
	public String getYymd() {
		return yymd;
	}
	public String getKdysgh() {
		return kdysgh;
	}
	public String getKdysxm() {
		return kdysxm;
	}
	public String getKdksdm() {
		return kdksdm;
	}
	public String getKdyszc() {
		return kdyszc;
	}
	public String getKdsj() {
		return kdsj;
	}
	public String getZxrygh() {
		return zxrygh;
	}
	public String getZxryxm() {
		return zxryxm;
	}
	public String getZxksdm() {
		return zxksdm;
	}
	public String getJcbwacrbm() {
		return jcbwacrbm;
	}
	public String getDj() {
		return dj;
	}
	public String getZe() {
		return ze;
	}
	public String getZzsj() {
		return zzsj;
	}
	public String getTjmc() {
		return tjmc;
	}
	public String getYwjxmc() {
		return ywjxmc;
	}
	public String getYwjxdm() {
		return ywjxdm;
	}
	public String getHzxm() {
		return hzxm;
	}
	public String getXbdm() {
		return xbdm;
	}
	public String getCsrq() {
		return csrq;
	}
	public String getYzxmlxbm() {
		return yzxmlxbm;
	}
	public String getYppzwh() {
		return yppzwh;
	}
	public String getYptybm() {
		return yptybm;
	}
	public String getYpyxq() {
		return ypyxq;
	}
	public String getName() {
		return name;
	}
	public String getYpdm() {
		return ypdm;
	}
	public void setYpdm(String ypdm) {
		this.ypdm = ypdm;
	}
	public String getYpmc() {
		return ypmc;
	}
	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}
	public String getYwlxDm() {
		return ywlxDm;
	}
	public void setYwlxDm(String ywlxDm) {
		this.ywlxDm = ywlxDm;
	}
	public String getYzshz() {
		return yzshz;
	}
	public void setYzshz(String yzshz) {
		this.yzshz = yzshz;
	}
	public String getYzshsj() {
		return yzshsj;
	}
	public void setYzshsj(String yzshsj) {
		this.yzshsj = yzshsj;
	}
	public String getHdyzhs() {
		return hdyzhs;
	}
	public void setHdyzhs(String hdyzhs) {
		this.hdyzhs = hdyzhs;
	}
	public String getYzhdrq() {
		return yzhdrq;
	}
	public void setYzhdrq(String yzhdrq) {
		this.yzhdrq = yzhdrq;
	}
	public String getZybzdm() {
		return zybzdm;
	}
	public void setZybzdm(String zybzdm) {
		this.zybzdm = zybzdm;
	}
	public String getYzzxzt() {
		return yzzxzt;
	}
	public void setYzzxzt(String yzzxzt) {
		this.yzzxzt = yzzxzt;
	}
	public String getQxyzz() {
		return qxyzz;
	}
	public void setQxyzz(String qxyzz) {
		this.qxyzz = qxyzz;
	}
	public String getYzqxrq() {
		return yzqxrq;
	}
	public void setYzqxrq(String yzqxrq) {
		this.yzqxrq = yzqxrq;
	}
	public String getTzyzz() {
		return tzyzz;
	}
	public void setTzyzz(String tzyzz) {
		this.tzyzz = tzyzz;
	}
	public String getFwxmdm() {
		return fwxmdm;
	}
	public void setFwxmdm(String fwxmdm) {
		this.fwxmdm = fwxmdm;
	}
	public String getFwxmmc() {
		return fwxmmc;
	}
	public void setFwxmmc(String fwxmmc) {
		this.fwxmmc = fwxmmc;
	}
	public String getZysylbDm() {
		return zysylbDm;
	}
	public void setZysylbDm(String zysylbDm) {
		this.zysylbDm = zysylbDm;
	}
	public String getZjldw() {
		return zjldw;
	}
	public void setZjldw(String zjldw) {
		this.zjldw = zjldw;
	}
	public String getZjl() {
		return zjl;
	}
	public void setZjl(String zjl) {
		this.zjl = zjl;
	}
	public String getYwgg() {
		return ywgg;
	}
	public void setYwgg(String ywgg) {
		this.ywgg = ywgg;
	}
	public String getZylbdm() {
		return zylbdm;
	}
	public void setZylbdm(String zylbdm) {
		this.zylbdm = zylbdm;
	}
	public String getZh() {
		return zh;
	}
	public void setZh(String zh) {
		this.zh = zh;
	}
	public String getSfpsbs() {
		return sfpsbs;
	}
	public void setSfpsbs(String sfpsbs) {
		this.sfpsbs = sfpsbs;
	}
	public String getYzxmmxid() {
		return yzxmmxid;
	}
	public void setYzxmmxid(String yzxmmxid) {
		this.yzxmmxid = yzxmmxid;
	}
	
}
