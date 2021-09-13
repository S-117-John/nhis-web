package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_SZYB_JS - 宿州市医保结算返回结果
 * 
 * @since 2018-06-21 08:58:25
 */
@Table(value = "INS_SZYB_JS")
public class InsSzybJs extends BaseModule {

	/** ID - 主键 */
	@PK
	@Field(value = "ID", id = KeyId.UUID)
	private String id;

	/** YWZQH - 业务周期号 */
	@Field(value = "YWZQH")
	private String ywzqh;

	/** PK_PV - 病人就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** PK_SETTLE - 结算主键 */
	@Field(value = "PK_SETTLE")
	private String pkSettle;

	/** YWLSH - 住院流水号(门诊流水号) */
	@Field(value = "YWLSH")
	private String ywlsh;

	/** DDYLJGBH - 定点医疗机构编号 */
	@Field(value = "DDYLJGBH")
	private String ddyljgbh;

	/** DDYLJGMC - 定点医疗机构名称 */
	@Field(value = "DDYLJGMC")
	private String ddyljgmc;

	/** JYLX - 交易类型 */
	@Field(value = "JYLX")
	private String jylx;

	/** DWBH - 单位编号 */
	@Field(value = "DWBH")
	private String dwbh;

	/** HZXM - 患者姓名 */
	@Field(value = "HZXM")
	private String hzxm;

	/** GRBH - 个人编号 */
	@Field(value = "GRBH")
	private String grbh;

	/** SFZH - 身份证号 */
	@Field(value = "SFZH")
	private String sfzh;

	/** XB - 性别 */
	@Field(value = "XB")
	private String xb;

	/** CSNY - 出生年月 */
	@Field(value = "CSNY")
	private String csny;

	/** NL - 年龄 */
	@Field(value = "NL")
	private String nl;

	/** YLLB - 医疗类别 */
	@Field(value = "YLLB")
	private String yllb;

	/** YYDJ - 医院等级 */
	@Field(value = "YYDJ")
	private String yydj;

	/** YLRYLB - 医疗人员类别 */
	@Field(value = "YLRYLB")
	private String ylrylb;

	/** RYRQ - 入院日期 */
	@Field(value = "RYRQ")
	private String ryrq;

	/** RYZDJBBH - 入院诊断疾病编号 */
	@Field(value = "RYZDJBBH")
	private String ryzdjbbh;

	/** RYZDJBMC - 入院诊断疾病名称 */
	@Field(value = "RYZDJBMC")
	private String ryzdjbmc;

	/** CYRQ - 出院日期 */
	@Field(value = "CYRQ")
	private String cyrq;

	/** CYYY - 出院原因 */
	@Field(value = "CYYY")
	private String cyyy;

	/** CYZDJBBH - 出院诊断疾病编号 */
	@Field(value = "CYZDJBBH")
	private String cyzdjbbh;

	/** CYZDJBMC - 出院诊断疾病名称 */
	@Field(value = "CYZDJBMC")
	private String cyzdjbmc;

	/** ZYCS - 本年度住院次数 */
	@Field(value = "ZYCS")
	private String zycs;

	/** ZYTS - 住院天数 */
	@Field(value = "ZYTS")
	private String zyts;

	/** DJH - 单据号 */
	@Field(value = "DJH")
	private String djh;

	/** BCTCFY - 本次进入统筹费用 */
	@Field(value = "BCTCFY")
	private String bctcfy;

	/** BCJZJFY - 本次进入救助金费用 */
	@Field(value = "BCJZJFY")
	private String bcjzjfy;

	/** BCGWYBZJ - 本次进入公务员补助金额 */
	@Field(value = "BCGWYBZJ")
	private String bcgwybzj;

	/** YLFYZE - 医疗费用总额 */
	@Field(value = "YLFYZE")
	private String ylfyze;

	/** JBYLFYZE - 符合基本医疗费用总额 */
	@Field(value = "JBYLFYZE")
	private String jbylfyze;

	/** GRZHZF - 个人账户支付 */
	@Field(value = "GRZHZF")
	private String grzhzf;

	/** GRXJZF - 个人现金支付 */
	@Field(value = "GRXJZF")
	private String grxjzf;

	/** TCZFJE - 统筹支付金额 */
	@Field(value = "TCZFJE")
	private String tczfje;

	/** JZJZFJE - 救助金支付金额 */
	@Field(value = "JZJZFJE")
	private String jzjzfje;

	/** GWYBZZFJE - 公务员不住支付金额 */
	@Field(value = "GWYBZZFJE")
	private String gwybzzfje;

	/** DWTCFDJE - 单位统筹分担金额 */
	@Field(value = "DWTCFDJE")
	private String dwtcfdje;

	/** DDYLJGFDJE - 定点医疗机构分担 */
	@Field(value = "DDYLJGFDJE")
	private String ddyljgfdje;

	/** BLZF - 丙类自费 */
	@Field(value = "BLZF")
	private String blzf;

	/** YLYPZL - 乙类药品自理 */
	@Field(value = "YLYPZL")
	private String ylypzl;

	/** TJGRZL - 特检个人自理 */
	@Field(value = "TJGRZL")
	private String tjgrzl;

	/** TZGRZL - 特治个人自理 */
	@Field(value = "TZGRZL")
	private String tzgrzl;

	/** ZZXZFJE - 转诊先自付金额 */
	@Field(value = "ZZXZFJE")
	private String zzxzfje;

	/** QFBZZFJE - 起付标准自付金额 */
	@Field(value = "QFBZZFJE")
	private String qfbzzfje;

	/** FDZF1 - 分段1自付 */
	@Field(value = "FDZF1")
	private String fdzf1;

	/** FDZF2 - 分段2自付 */
	@Field(value = "FDZF2")
	private String fdzf2;

	/** FDTCYLFY1 - 分段1进入统筹医疗费用 */
	@Field(value = "FDTCYLFY1")
	private String fdtcylfy1;

	/** FDTCYLFY2 - 分段2进入统筹医疗费用 */
	@Field(value = "FDTCYLFY2")
	private String fdtcylfy2;

	/** FDZFBL2 - 分段2自付比例 */
	@Field(value = "FDZFBL2")
	private String fdzfbl2;

	/** FDZFBL1 - 分段1自付比例 */
	@Field(value = "FDZFBL1")
	private String fdzfbl1;

	/** DEFDZF1 - 大额分段1自付 */
	@Field(value = "DEFDZF1")
	private String defdzf1;

	/** DEFDZF2 - 大额分段2自付 */
	@Field(value = "DEFDZF2")
	private String defdzf2;

	/** FDJRDEFY1 - 分段1进入大额费用 */
	@Field(value = "FDJRDEFY1")
	private String fdjrdefy1;

	/** FDJRDEFY2 - 分段2进入大额费用 */
	@Field(value = "FDJRDEFY2")
	private String fdjrdefy2;

	/** BNZYFHJBYLFYLJ - 本年住院符合基本基本医疗费用累计(大额) */
	@Field(value = "BNZYFHJBYLFYLJ")
	private String bnzyfhjbylfylj;

	/** QTZLJE - 其它自理金额 */
	@Field(value = "QTZLJE")
	private String qtzlje;

	/** JKCLZL - 进口材料自理 */
	@Field(value = "JKCLZL")
	private String jkclzl;

	/** TCQH - 统筹区号 */
	@Field(value = "TCQH")
	private String tcqh;

	/** NDTKBZLJ - 年度特困补助累计 */
	@Field(value = "NDTKBZLJ")
	private String ndtkbzlj;

	/** NDYBFYLJ - 年度医保费用累计 */
	@Field(value = "NDYBFYLJ")
	private String ndybfylj;

	/** DBBXLJBXJE1 - 本年度大病保险1累计报销金额 */
	@Field(value = "DBBXLJBXJE1")
	private Double dbbxljbxje1;

	/** DBBXLJBXJE2 - 本年度大病保险2累计报销金额 */
	@Field(value = "DBBXLJBXJE2")
	private Double dbbxljbxje2;

	/** BCFWNFYJE - 本次范围内费用金额 */
	@Field(value = "BCFWNFYJE")
	private Double bcfwnfyje;

	/** BCHGFYJE - 本次合规费用金额 */
	@Field(value = "BCHGFYJE")
	private Double bchgfyje;

	/** JZDXZF - 救助段先自付(转外) */
	@Field(value = "JZDXZF")
	private String jzdxzf;

	/** BZDXZF - 补助段先自付 */
	@Field(value = "BZDXZF")
	private String bzdxzf;

	/** QFGB - 起付公补 */
	@Field(value = "QFGB")
	private String qfgb;

	/** BCDBBX1QFBZZF - 本次大病保险1起付标准自付 */
	@Field(value = "BCDBBX1QFBZZF")
	private Double bcdbbx1qfbzzf;

	/** BCJRGWYTKBZJE - 本次进入公务员特困补助金额 */
	@Field(value = "BCJRGWYTKBZJE")
	private String bcjrgwytkbzje;

	/** BCDBBX2QFBZZF - 本次大病保险2起付标准自付 */
	@Field(value = "BCDBBX2QFBZZF")
	private Double bcdbbx2qfbzzf;

	/** BCJRDBYL - 本次进入大病医疗 */
	@Field(value = "BCJRDBYL")
	private Double bcjrdbyl;

	/** BCDBZCJE - 本次大病支出金额 */
	@Field(value = "BCDBZCJE")
	private Double bcdbzcje;

	/** FD1JRDBYLFY - 分段1进入大病医疗费用 */
	@Field(value = "FD1JRDBYLFY")
	private Double fd1jrdbylfy;

	/** DBYLFD1ZF - 大病医疗分段1自付 */
	@Field(value = "DBYLFD1ZF")
	private Double dbylfd1zf;

	/** FD2JRDBYLFY - 分段2进入大病医疗费用 */
	@Field(value = "FD2JRDBYLFY")
	private Double fd2jrdbylfy;

	/** DBYLFD2ZF - 大病医疗分段2自付 */
	@Field(value = "DBYLFD2ZF")
	private Double dbylfd2zf;

	/** FD3JRDBYLFY - 分段3进入大病医疗费用 */
	@Field(value = "FD3JRDBYLFY")
	private Double fd3jrdbylfy;

	/** DBYLFD3ZF - 大病医疗分段3自付 */
	@Field(value = "DBYLFD3ZF")
	private Double dbylfd3zf;

	/** FD4JRDBYLFY - 分段4进入大病医疗费用 */
	@Field(value = "FD4JRDBYLFY")
	private Double fd4jrdbylfy;

	/** DBYLFD4ZF - 大病医疗分段4自付 */
	@Field(value = "DBYLFD4ZF")
	private Double dbylfd4zf;

	/** BCDBBX2ZFJE - 本次大病保险2支付金额 */
	@Field(value = "BCDBBX2ZFJE")
	private Double bcdbbx2zfje;

	/** QTJJZC - 其他基金支出 */
	@Field(value = "QTJJZC")
	private Double qtjjzc;

	@Field(value = "MODIFY_TIME")
	private Date modifyTime;

	/** JSRQ - 结算日期 */
	@Field(value = "JSRQ")
	private Date jsrq;

	/** YZXX - 医嘱信息 */
	@Field(value = "YZXX")
	private String yzxx;

	/** CYLB - 出院类别 */
	@Field(value = "CYLB")
	private String cylb;

	/** BXLB - 报销类别,报销标志 */
	@Field(value = "BXLB")
	private String bxlb;

	/** ZHSYBZ - 账户使用标志 */
	@Field(value = "ZHSYBZ")
	private String zhsybz;

	/** ZTJSBZ - 中途结算标志 */
	@Field(value = "ZTJSBZ")
	private String ztjsbz;

	/** JBR - 经办人 */
	@Field(value = "JBR")
	private String jbr;

	/** SFBCCFBZ - 是否保存处方标志 */
	@Field(value = "SFBCCFBZ")
	private String sfbccfbz;

	/** YSXM - 医师姓名 */
	@Field(value = "YSXM")
	private String ysxm;

	/** LXZYBZ - 连续住院标志 */
	@Field(value = "LXZYBZ")
	private String lxzybz;

	/** YWLX - 1.门诊 2住院 */
	@Field(value = "YWLX")
	private String ywlx;

	/** 社会保险号 */
	@Field(value = "SHBXH")
	private String shbxh;

	/** 本年统筹支出累 */
	@Field(value = "BNTCZCLJ")
	private String bntczclj;

	/** 一般起付标准额 */
	@Field(value = "YBQFBZE")
	private String ybqfbze;

	/** 本年救助金支出累计 */
	@Field(value = "BNJZJZCLJ")
	private String bnjzjzclj;

	/** 补充医疗保险支付 */
	@Field(value = "BCYLBXZF")
	private String bcylbxzf;

	/** 伤残人员医疗保障支付 */
	@Field(value = "SCRYYLBZZF")
	private String scryylbzzf;
	
	/** 账户余额 */
	@Field(value = "ZHYE")
	private String zhye;
	
	/** 联系电话 */
	@Field(value = "LXDH")
	private String lxdh;
	
	/**是否贫困   取值是、否、脱贫不脱政策 */
	@Field(value = "SFWPKRK")
	private String sfwpkrk;
	
	/** 医院行政级别  取值县域内、市内、市外省内、省外 */
	@Field(value = "YYXZJB")
	private String yyxzjb;
	
	/** 治疗方式编码 */
	@Field(value = "ZLFSBM")
	private String zlfsbm;
	
	/** 治疗方式名称 */
	@Field(value = "ZLFSMC")
	private String zlfsmc;
	
	/** 保底标志  2表示此费用是保底 */
	@Field(value = "BDBZ")
	private String bdbz;
	
	/** 转诊类型 */
	@Field(value = "ZZLX")
	private String zzlx;
	
	/** 本年医疗费累计 */
	@Field(value = "BNYLFLJ")
	private Double bnylflj;
	
	/** 是否按病种付费  异地出参 1是 0否，本地为空*/
	@Field(value = "SFABZFF")
	private String sfabzff;
	
	/** 本年住院符合基本医疗费用累计 */
	@Field(value = "BNZYFHJBYLFYLJ1")
	private Double bnzyfhjbylfylj1;
	
	/** 民政补助支付 */
	@Field(value = "MZBZZF")
	private Double mzbzzf;
	
	/** 财政兜底支付 */
	@Field(value = "CZDDZF")
	private Double czddzf;
	
	/** 本次180救助支付金额 */
	@Field(value = "BCJZZFJE")
	private Double bcjzzfje;
	
	/** 疾病分组编码 */
	@Field(value = "JBFZBM")
	private String jbfzbm;
	
	/** 疾病分组名称 */
	@Field(value = "JBFZMC")
	private String jbfzmc;
	
	/** 个人属性 */
	@Field(value = "GRSX")
	private String grsx;

	@Field(value="QDID")
	private String qdid;

	public String getQdid() {
		return qdid;
	}

	public void setQdid(String qdid) {
		this.qdid = qdid;
	}

	public String getGrsx() {
		return grsx;
	}

	public void setGrsx(String grsx) {
		this.grsx = grsx;
	}

	public String getJbfzbm() {
		return jbfzbm;
	}

	public void setJbfzbm(String jbfzbm) {
		this.jbfzbm = jbfzbm;
	}

	public String getJbfzmc() {
		return jbfzmc;
	}

	public void setJbfzmc(String jbfzmc) {
		this.jbfzmc = jbfzmc;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getSfwpkrk() {
		return sfwpkrk;
	}

	public void setSfwpkrk(String sfwpkrk) {
		this.sfwpkrk = sfwpkrk;
	}

	public String getYyxzjb() {
		return yyxzjb;
	}

	public void setYyxzjb(String yyxzjb) {
		this.yyxzjb = yyxzjb;
	}

	public String getZlfsbm() {
		return zlfsbm;
	}

	public void setZlfsbm(String zlfsbm) {
		this.zlfsbm = zlfsbm;
	}

	public String getZlfsmc() {
		return zlfsmc;
	}

	public void setZlfsmc(String zlfsmc) {
		this.zlfsmc = zlfsmc;
	}

	public String getBdbz() {
		return bdbz;
	}

	public void setBdbz(String bdbz) {
		this.bdbz = bdbz;
	}

	public String getZzlx() {
		return zzlx;
	}

	public void setZzlx(String zzlx) {
		this.zzlx = zzlx;
	}

	public Double getBnylflj() {
		return bnylflj;
	}

	public void setBnylflj(Double bnylflj) {
		this.bnylflj = bnylflj;
	}

	public String getSfabzff() {
		return sfabzff;
	}

	public void setSfabzff(String sfabzff) {
		this.sfabzff = sfabzff;
	}

	public Double getBnzyfhjbylfylj1() {
		return bnzyfhjbylfylj1;
	}

	public void setBnzyfhjbylfylj1(Double bnzyfhjbylfylj1) {
		this.bnzyfhjbylfylj1 = bnzyfhjbylfylj1;
	}

	public Double getMzbzzf() {
		return mzbzzf;
	}

	public void setMzbzzf(Double mzbzzf) {
		this.mzbzzf = mzbzzf;
	}

	public Double getCzddzf() {
		return czddzf;
	}

	public void setCzddzf(Double czddzf) {
		this.czddzf = czddzf;
	}

	public Double getBcjzzfje() {
		return bcjzzfje;
	}

	public void setBcjzzfje(Double bcjzzfje) {
		this.bcjzzfje = bcjzzfje;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYwzqh() {
		return this.ywzqh;
	}

	public void setYwzqh(String ywzqh) {
		this.ywzqh = ywzqh;
	}

	public String getPkPv() {
		return this.pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkSettle() {
		return this.pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getYwlsh() {
		return this.ywlsh;
	}

	public void setYwlsh(String ywlsh) {
		this.ywlsh = ywlsh;
	}

	public String getDdyljgbh() {
		return this.ddyljgbh;
	}

	public void setDdyljgbh(String ddyljgbh) {
		this.ddyljgbh = ddyljgbh;
	}

	public String getDdyljgmc() {
		return this.ddyljgmc;
	}

	public void setDdyljgmc(String ddyljgmc) {
		this.ddyljgmc = ddyljgmc;
	}

	public String getJylx() {
		return this.jylx;
	}

	public void setJylx(String jylx) {
		this.jylx = jylx;
	}

	public String getDwbh() {
		return this.dwbh;
	}

	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}

	public String getHzxm() {
		return this.hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getGrbh() {
		return this.grbh;
	}

	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}

	public String getSfzh() {
		return this.sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXb() {
		return this.xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getCsny() {
		return this.csny;
	}

	public void setCsny(String csny) {
		this.csny = csny;
	}

	public String getNl() {
		return this.nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getYllb() {
		return this.yllb;
	}

	public void setYllb(String yllb) {
		this.yllb = yllb;
	}

	public String getYydj() {
		return this.yydj;
	}

	public void setYydj(String yydj) {
		this.yydj = yydj;
	}

	public String getYlrylb() {
		return this.ylrylb;
	}

	public void setYlrylb(String ylrylb) {
		this.ylrylb = ylrylb;
	}

	public String getRyrq() {
		return this.ryrq;
	}

	public void setRyrq(String ryrq) {
		this.ryrq = ryrq;
	}

	public String getRyzdjbbh() {
		return this.ryzdjbbh;
	}

	public void setRyzdjbbh(String ryzdjbbh) {
		this.ryzdjbbh = ryzdjbbh;
	}

	public String getRyzdjbmc() {
		return this.ryzdjbmc;
	}

	public void setRyzdjbmc(String ryzdjbmc) {
		this.ryzdjbmc = ryzdjbmc;
	}

	public String getCyrq() {
		return this.cyrq;
	}

	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}

	public String getCyyy() {
		return this.cyyy;
	}

	public void setCyyy(String cyyy) {
		this.cyyy = cyyy;
	}

	public String getCyzdjbbh() {
		return this.cyzdjbbh;
	}

	public void setCyzdjbbh(String cyzdjbbh) {
		this.cyzdjbbh = cyzdjbbh;
	}

	public String getCyzdjbmc() {
		return this.cyzdjbmc;
	}

	public void setCyzdjbmc(String cyzdjbmc) {
		this.cyzdjbmc = cyzdjbmc;
	}

	public String getZycs() {
		return this.zycs;
	}

	public void setZycs(String zycs) {
		this.zycs = zycs;
	}

	public String getZyts() {
		return this.zyts;
	}

	public void setZyts(String zyts) {
		this.zyts = zyts;
	}

	public String getDjh() {
		return this.djh;
	}

	public void setDjh(String djh) {
		this.djh = djh;
	}

	public String getBctcfy() {
		return this.bctcfy;
	}

	public void setBctcfy(String bctcfy) {
		this.bctcfy = bctcfy;
	}

	public String getBcjzjfy() {
		return this.bcjzjfy;
	}

	public void setBcjzjfy(String bcjzjfy) {
		this.bcjzjfy = bcjzjfy;
	}

	public String getBcgwybzj() {
		return this.bcgwybzj;
	}

	public void setBcgwybzj(String bcgwybzj) {
		this.bcgwybzj = bcgwybzj;
	}

	public String getYlfyze() {
		return this.ylfyze;
	}

	public void setYlfyze(String ylfyze) {
		this.ylfyze = ylfyze;
	}

	public String getJbylfyze() {
		return this.jbylfyze;
	}

	public void setJbylfyze(String jbylfyze) {
		this.jbylfyze = jbylfyze;
	}

	public String getGrzhzf() {
		return this.grzhzf;
	}

	public void setGrzhzf(String grzhzf) {
		this.grzhzf = grzhzf;
	}

	public String getGrxjzf() {
		return this.grxjzf;
	}

	public void setGrxjzf(String grxjzf) {
		this.grxjzf = grxjzf;
	}

	public String getTczfje() {
		return this.tczfje;
	}

	public void setTczfje(String tczfje) {
		this.tczfje = tczfje;
	}

	public String getJzjzfje() {
		return this.jzjzfje;
	}

	public void setJzjzfje(String jzjzfje) {
		this.jzjzfje = jzjzfje;
	}

	public String getGwybzzfje() {
		return this.gwybzzfje;
	}

	public void setGwybzzfje(String gwybzzfje) {
		this.gwybzzfje = gwybzzfje;
	}

	public String getDwtcfdje() {
		return this.dwtcfdje;
	}

	public void setDwtcfdje(String dwtcfdje) {
		this.dwtcfdje = dwtcfdje;
	}

	public String getDdyljgfdje() {
		return this.ddyljgfdje;
	}

	public void setDdyljgfdje(String ddyljgfdje) {
		this.ddyljgfdje = ddyljgfdje;
	}

	public String getBlzf() {
		return this.blzf;
	}

	public void setBlzf(String blzf) {
		this.blzf = blzf;
	}

	public String getYlypzl() {
		return this.ylypzl;
	}

	public void setYlypzl(String ylypzl) {
		this.ylypzl = ylypzl;
	}

	public String getTjgrzl() {
		return this.tjgrzl;
	}

	public void setTjgrzl(String tjgrzl) {
		this.tjgrzl = tjgrzl;
	}

	public String getTzgrzl() {
		return this.tzgrzl;
	}

	public void setTzgrzl(String tzgrzl) {
		this.tzgrzl = tzgrzl;
	}

	public String getZzxzfje() {
		return this.zzxzfje;
	}

	public void setZzxzfje(String zzxzfje) {
		this.zzxzfje = zzxzfje;
	}

	public String getQfbzzfje() {
		return this.qfbzzfje;
	}

	public void setQfbzzfje(String qfbzzfje) {
		this.qfbzzfje = qfbzzfje;
	}

	public String getFdzf1() {
		return this.fdzf1;
	}

	public void setFdzf1(String fdzf1) {
		this.fdzf1 = fdzf1;
	}

	public String getFdzf2() {
		return this.fdzf2;
	}

	public void setFdzf2(String fdzf2) {
		this.fdzf2 = fdzf2;
	}

	public String getFdtcylfy1() {
		return this.fdtcylfy1;
	}

	public void setFdtcylfy1(String fdtcylfy1) {
		this.fdtcylfy1 = fdtcylfy1;
	}

	public String getFdtcylfy2() {
		return this.fdtcylfy2;
	}

	public void setFdtcylfy2(String fdtcylfy2) {
		this.fdtcylfy2 = fdtcylfy2;
	}

	public String getFdzfbl2() {
		return this.fdzfbl2;
	}

	public void setFdzfbl2(String fdzfbl2) {
		this.fdzfbl2 = fdzfbl2;
	}

	public String getFdzfbl1() {
		return this.fdzfbl1;
	}

	public void setFdzfbl1(String fdzfbl1) {
		this.fdzfbl1 = fdzfbl1;
	}

	public String getDefdzf1() {
		return this.defdzf1;
	}

	public void setDefdzf1(String defdzf1) {
		this.defdzf1 = defdzf1;
	}

	public String getDefdzf2() {
		return this.defdzf2;
	}

	public void setDefdzf2(String defdzf2) {
		this.defdzf2 = defdzf2;
	}

	public String getFdjrdefy1() {
		return this.fdjrdefy1;
	}

	public void setFdjrdefy1(String fdjrdefy1) {
		this.fdjrdefy1 = fdjrdefy1;
	}

	public String getFdjrdefy2() {
		return this.fdjrdefy2;
	}

	public void setFdjrdefy2(String fdjrdefy2) {
		this.fdjrdefy2 = fdjrdefy2;
	}

	public String getBnzyfhjbylfylj() {
		return this.bnzyfhjbylfylj;
	}

	public void setBnzyfhjbylfylj(String bnzyfhjbylfylj) {
		this.bnzyfhjbylfylj = bnzyfhjbylfylj;
	}

	public String getQtzlje() {
		return this.qtzlje;
	}

	public void setQtzlje(String qtzlje) {
		this.qtzlje = qtzlje;
	}

	public String getJkclzl() {
		return this.jkclzl;
	}

	public void setJkclzl(String jkclzl) {
		this.jkclzl = jkclzl;
	}

	public String getTcqh() {
		return this.tcqh;
	}

	public void setTcqh(String tcqh) {
		this.tcqh = tcqh;
	}

	public String getNdtkbzlj() {
		return this.ndtkbzlj;
	}

	public void setNdtkbzlj(String ndtkbzlj) {
		this.ndtkbzlj = ndtkbzlj;
	}

	public String getNdybfylj() {
		return this.ndybfylj;
	}

	public void setNdybfylj(String ndybfylj) {
		this.ndybfylj = ndybfylj;
	}

	public Double getDbbxljbxje1() {
		return this.dbbxljbxje1;
	}

	public void setDbbxljbxje1(Double dbbxljbxje1) {
		this.dbbxljbxje1 = dbbxljbxje1;
	}

	public Double getDbbxljbxje2() {
		return this.dbbxljbxje2;
	}

	public void setDbbxljbxje2(Double dbbxljbxje2) {
		this.dbbxljbxje2 = dbbxljbxje2;
	}

	public Double getBcfwnfyje() {
		return this.bcfwnfyje;
	}

	public void setBcfwnfyje(Double bcfwnfyje) {
		this.bcfwnfyje = bcfwnfyje;
	}

	public Double getBchgfyje() {
		return this.bchgfyje;
	}

	public void setBchgfyje(Double bchgfyje) {
		this.bchgfyje = bchgfyje;
	}

	public String getJzdxzf() {
		return this.jzdxzf;
	}

	public void setJzdxzf(String jzdxzf) {
		this.jzdxzf = jzdxzf;
	}

	public String getBzdxzf() {
		return this.bzdxzf;
	}

	public void setBzdxzf(String bzdxzf) {
		this.bzdxzf = bzdxzf;
	}

	public String getQfgb() {
		return this.qfgb;
	}

	public void setQfgb(String qfgb) {
		this.qfgb = qfgb;
	}

	public Double getBcdbbx1qfbzzf() {
		return this.bcdbbx1qfbzzf;
	}

	public void setBcdbbx1qfbzzf(Double bcdbbx1qfbzzf) {
		this.bcdbbx1qfbzzf = bcdbbx1qfbzzf;
	}

	public String getBcjrgwytkbzje() {
		return this.bcjrgwytkbzje;
	}

	public void setBcjrgwytkbzje(String bcjrgwytkbzje) {
		this.bcjrgwytkbzje = bcjrgwytkbzje;
	}

	public Double getBcdbbx2qfbzzf() {
		return this.bcdbbx2qfbzzf;
	}

	public void setBcdbbx2qfbzzf(Double bcdbbx2qfbzzf) {
		this.bcdbbx2qfbzzf = bcdbbx2qfbzzf;
	}

	public Double getBcjrdbyl() {
		return this.bcjrdbyl;
	}

	public void setBcjrdbyl(Double bcjrdbyl) {
		this.bcjrdbyl = bcjrdbyl;
	}

	public Double getBcdbzcje() {
		return this.bcdbzcje;
	}

	public void setBcdbzcje(Double bcdbzcje) {
		this.bcdbzcje = bcdbzcje;
	}

	public Double getFd1jrdbylfy() {
		return this.fd1jrdbylfy;
	}

	public void setFd1jrdbylfy(Double fd1jrdbylfy) {
		this.fd1jrdbylfy = fd1jrdbylfy;
	}

	public Double getDbylfd1zf() {
		return this.dbylfd1zf;
	}

	public void setDbylfd1zf(Double dbylfd1zf) {
		this.dbylfd1zf = dbylfd1zf;
	}

	public Double getFd2jrdbylfy() {
		return this.fd2jrdbylfy;
	}

	public void setFd2jrdbylfy(Double fd2jrdbylfy) {
		this.fd2jrdbylfy = fd2jrdbylfy;
	}

	public Double getDbylfd2zf() {
		return this.dbylfd2zf;
	}

	public void setDbylfd2zf(Double dbylfd2zf) {
		this.dbylfd2zf = dbylfd2zf;
	}

	public Double getFd3jrdbylfy() {
		return this.fd3jrdbylfy;
	}

	public void setFd3jrdbylfy(Double fd3jrdbylfy) {
		this.fd3jrdbylfy = fd3jrdbylfy;
	}

	public Double getDbylfd3zf() {
		return this.dbylfd3zf;
	}

	public void setDbylfd3zf(Double dbylfd3zf) {
		this.dbylfd3zf = dbylfd3zf;
	}

	public Double getFd4jrdbylfy() {
		return this.fd4jrdbylfy;
	}

	public void setFd4jrdbylfy(Double fd4jrdbylfy) {
		this.fd4jrdbylfy = fd4jrdbylfy;
	}

	public Double getDbylfd4zf() {
		return this.dbylfd4zf;
	}

	public void setDbylfd4zf(Double dbylfd4zf) {
		this.dbylfd4zf = dbylfd4zf;
	}

	public Double getBcdbbx2zfje() {
		return this.bcdbbx2zfje;
	}

	public void setBcdbbx2zfje(Double bcdbbx2zfje) {
		this.bcdbbx2zfje = bcdbbx2zfje;
	}

	public Double getQtjjzc() {
		return this.qtjjzc;
	}

	public void setQtjjzc(Double qtjjzc) {
		this.qtjjzc = qtjjzc;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getJsrq() {
		return this.jsrq;
	}

	public void setJsrq(Date jsrq) {
		this.jsrq = jsrq;
	}

	public String getYzxx() {
		return this.yzxx;
	}

	public void setYzxx(String yzxx) {
		this.yzxx = yzxx;
	}

	public String getCylb() {
		return this.cylb;
	}

	public void setCylb(String cylb) {
		this.cylb = cylb;
	}

	public String getBxlb() {
		return this.bxlb;
	}

	public void setBxlb(String bxlb) {
		this.bxlb = bxlb;
	}

	public String getZhsybz() {
		return this.zhsybz;
	}

	public void setZhsybz(String zhsybz) {
		this.zhsybz = zhsybz;
	}

	public String getZtjsbz() {
		return this.ztjsbz;
	}

	public void setZtjsbz(String ztjsbz) {
		this.ztjsbz = ztjsbz;
	}

	public String getJbr() {
		return this.jbr;
	}

	public void setJbr(String jbr) {
		this.jbr = jbr;
	}

	public String getSfbccfbz() {
		return this.sfbccfbz;
	}

	public void setSfbccfbz(String sfbccfbz) {
		this.sfbccfbz = sfbccfbz;
	}

	public String getYsxm() {
		return this.ysxm;
	}

	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}

	public String getLxzybz() {
		return this.lxzybz;
	}

	public void setLxzybz(String lxzybz) {
		this.lxzybz = lxzybz;
	}

	public String getYwlx() {
		return this.ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getShbxh() {
		return shbxh;
	}

	public void setShbxh(String shbxh) {
		this.shbxh = shbxh;
	}

	public String getBntczclj() {
		return bntczclj;
	}

	public void setBntczclj(String bntczclj) {
		this.bntczclj = bntczclj;
	}

	public String getYbqfbze() {
		return ybqfbze;
	}

	public void setYbqfbze(String ybqfbze) {
		this.ybqfbze = ybqfbze;
	}

	public String getBnjzjzclj() {
		return bnjzjzclj;
	}

	public void setBnjzjzclj(String bnjzjzclj) {
		this.bnjzjzclj = bnjzjzclj;
	}

	public String getBcylbxzf() {
		return bcylbxzf;
	}

	public void setBcylbxzf(String bcylbxzf) {
		this.bcylbxzf = bcylbxzf;
	}

	public String getScryylbzzf() {
		return scryylbzzf;
	}

	public void setScryylbzzf(String scryylbzzf) {
		this.scryylbzzf = scryylbzzf;
	}

	public String getZhye() {
		return zhye;
	}

	public void setZhye(String zhye) {
		this.zhye = zhye;
	}
}