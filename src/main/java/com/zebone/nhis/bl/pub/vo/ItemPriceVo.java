package com.zebone.nhis.bl.pub.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.platform.modules.dao.build.au.Field;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收费项目及其价格
 * 
 * @author yangxue
 * 
 */
public class ItemPriceVo extends BdItem implements Cloneable {
	private String pkItemOld;// 原始收费项目主键（若非组套收费项目，则pkItem与pkItemOld相同）
	private String nameItemOld;
	private String pkOrdOld;// 原始医嘱项目主键（用来取到收费项目后计算数量）
	private String flagSetOld;// 原始收费项目组套标志
	private String euPricemodeOld;// 原始收费项目定价模式（主要针对组套的收费项目）
	private Double quan;// 收费项目数量，通过收费项目维护或医嘱项目维护或者组套功能维护的单个医嘱或收费项目的数量
	private Double priceOrg;// 经过收费项目及价格使用配置业务配置取出的收费项目价格
	private Double quanOld;//需要记费数量，由业务接口传入
	private Date dateStart;//医嘱开始日期
	private String codeOrdtype;//医嘱类型编码 
	private String flagFit;	//适应症标志 0非适应症，1适应症；
	private String descFit;	//适应症描述
	private Integer ordsn;//医嘱号，检查类医嘱记费使用
	private Integer ordsnParent;//父医嘱号，检查类医嘱记费使用
	private String dtSamptype;//标本类型,检查类医嘱记费使用--中二
	private String barcode;//高值耗材材料编码
	
	///////////以下是医保相关信息//////
	private BigDecimal amountMed;//医保支付金额，单价
	private Double ratioDisc;//患者优惠比例
	private String pkDisc;//患者优惠类型
	private Double ratioSelf;//患者自付比例
	private String flagHppi;//患者自付金额标志--广州公医添加
	private BigDecimal amtHppi;//(患者自付金额)  --广州公医添加
	private String euDivtype;//(策略类型(0 床位费策略，1特殊项目策略，2高值耗材策略))
	private Double priceCs;//根据项目策略bd_chap表计算出的单价,默认为原始收费项目价格，即priceOrg的值
	private Double ratioPock;//医嘱打折比例存放增加字段
	private Double amountPock;//自付比例打折金额
	
	///////以下是记费明细相关信息/////////
	private String pkCgip;
	private String pkCgop;
	private String nameCg;//记费名称
	private String pkPi;
	private String pkPv;
	private String pkHp;
	private String pkOrgApp;
	private String pkDeptApp;
	private String pkDeptNsApp;
	private String pkEmpApp;
	private String nameEmpApp;
	private String pkOrgEx;
	private String pkDeptEx;
	private String pkCnord;
	private Date dateHap;
	private String flagPv;//是否是挂号费
	private String pkPres;
	private String pkDeptCg;
	private String pkEmpCg;
	private String nameEmpCg;
	private String pkOrdexdt;
	private Date dateCg;//记费日期
	/**开立医生考勤科室*/
	private String pkDeptJob;
	
	///////以下是药品信息///////////
	private Integer packSize;
	private Double priceCost;
	private Date dateExpire;
	private String pkUnitPd;
	private String batchNo;
	private String euOrdtype;//其他医嘱类型0普通，1科研
	
	
	////以下是中山二院添加特殊定价信息
	private String euCdmode;//儿童加收模式  0 比例，1金额
	private Double ratioChildren;//六岁以下儿童加价比例
	private Double amountChildren;//六岁以下儿童加价金额
	private String euSpmode;//特诊加收模式  0 比例，1金额
	private Double ratioSpec;//特诊加价比例
	private Double amountSpec;//特诊加价金额
	
	private String flagPdOrd;//收费项目对医嘱项目中间表bd_ord_item的药品标志
	/**
	 * 附加项目标志 0 非附加，1 附加项目--yangxue添加2018.8.6
	 */
	private String euAdditem;
	
	/**
	 * 自动执行记费标志(此标志为true则bl_ip_dt记费部门、记费人员、记费人员姓名为空)
	 */
	private String flagSign;
	
	private String pkEmpEx;//执行医生--中二
	
	private String nameEmpEx;//执行医生姓名--中二
	
	private String euBltype;//记费类型--中二
	
	private String pkCnordRl;//联医嘱--中二
	
	/**
	 * 序号--中二
	 */
	private int sortno;
	
	/**
	 * 用量--中二
	 */
	private Double dosage;
	
	/**
	 * 用量单位--中二
	 */
	private String unitDos;
	
	/**
	 * 用法--中二
	 */
	private String nameSupply;
	
	/**
	 * 频次--中二
	 */
	private String nameFreq;
	/**
	 * 出库明细主键--发药用
	 */
	private String pkPdstdt;
	
	/**
	 * 是否是固定记费--中二
	 */
	private Boolean isFixedCg;

	/**
	 * 医嘱项目是否合并标志-医嘱项目合并用
	 */
	private Boolean flagUnion;
	/*
	* 科研医嘱优惠方
	* */
	private String  pkPayer;
	
	/**开立诊区**/
	private String  pkDeptAreaapp;
	/** 来自医保计划-项目计费策略的--优惠数量上限*/
	private Double maxQuan;

	/** 是否自备药（继承自医嘱）*/
	private String flagSelf;

	/**
	 * 原医疗组
	 */
	private String pkWgOrg;
	/**
	 * 执行医疗组
	 */
	private String pkWgEx;
	/**
	 * 医疗组
	 */
	private String pkWg;


	public Double getRatioPock() {
		return ratioPock;
	}

	public void setRatioPock(Double ratioPock) {
		this.ratioPock = ratioPock;
	}

	public Boolean getFlagUnion() {
		return flagUnion;
	}

	public void setFlagUnion(Boolean flagUnion) {
		this.flagUnion = flagUnion;
	}

	public String getEuCdmode() {
		return euCdmode;
	}

	public void setEuCdmode(String euCdmode) {
		this.euCdmode = euCdmode;
	}

	public Double getAmountChildren() {
		return amountChildren;
	}

	public void setAmountChildren(Double amountChildren) {
		this.amountChildren = amountChildren;
	}

	public String getEuSpmode() {
		return euSpmode;
	}

	public void setEuSpmode(String euSpmode) {
		this.euSpmode = euSpmode;
	}

	public Double getAmountSpec() {
		return amountSpec;
	}

	public void setAmountSpec(Double amountSpec) {
		this.amountSpec = amountSpec;
	}

	public String getEuOrdtype() {
		return euOrdtype;
	}

	public void setEuOrdtype(String euOrdtype) {
		this.euOrdtype = euOrdtype;
	}

	public Boolean getIsFixedCg() {
		return isFixedCg;
	}

	public void setIsFixedCg(Boolean isFixedCg) {
		this.isFixedCg = isFixedCg;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getUnitDos() {
		return unitDos;
	}

	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}

	public String getNameSupply() {
		return nameSupply;
	}

	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}

	public String getNameFreq() {
		return nameFreq;
	}

	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}

	public Date getDateCg() {
		return dateCg;
	}

	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}

	public String getPkEmpEx() {
		return pkEmpEx;
	}

	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}

	public String getNameEmpEx() {
		return nameEmpEx;
	}

	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}

	public String getEuBltype() {
		return euBltype;
	}

	public void setEuBltype(String euBltype) {
		this.euBltype = euBltype;
	}

	public String getPkCnordRl() {
		return pkCnordRl;
	}

	public void setPkCnordRl(String pkCnordRl) {
		this.pkCnordRl = pkCnordRl;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getFlagPdOrd() {
		return flagPdOrd;
	}

	public void setFlagPdOrd(String flagPdOrd) {
		this.flagPdOrd = flagPdOrd;
	}

	public String getDtSamptype() {
		return dtSamptype;
	}

	public void setDtSamptype(String dtSamptype) {
		this.dtSamptype = dtSamptype;
	}

	public Integer getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}

	public Integer getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public String getFlagSign() {
		return flagSign;
	}

	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}
	
	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	public String getFlagHppi() {
		return flagHppi;
	}

	public void setFlagHppi(String flagHppi) {
		this.flagHppi = flagHppi;
	}

	public String getPkOrdexdt() {
		return pkOrdexdt;
	}

	public void setPkOrdexdt(String pkOrdexdt) {
		this.pkOrdexdt = pkOrdexdt;
	}

	public String getPkDeptNsApp() {
		return pkDeptNsApp;
	}

	public void setPkDeptNsApp(String pkDeptNsApp) {
		this.pkDeptNsApp = pkDeptNsApp;
	}

	public Double getQuanOld() {
		return quanOld;
	}

	public void setQuanOld(Double quanOld) {
		this.quanOld = quanOld==null?0D:quanOld;
	}

	public String getEuAdditem() {
		return euAdditem;
	}

	public void setEuAdditem(String euAdditem) {
		this.euAdditem = euAdditem;
	}
	
	public BigDecimal getAmtHppi() {
		return amtHppi;
	}

	public void setAmtHppi(BigDecimal amtHppi) {
		this.amtHppi = amtHppi;
	}

	public Double getPriceCs() {
		return priceCs;
	}

	public void setPriceCs(Double priceCs) {
		this.priceCs = priceCs==null?0D:priceCs;
	}

	public String getEuDivtype() {
		return euDivtype;
	}

	public void setEuDivtype(String euDivtype) {
		this.euDivtype = euDivtype;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public Double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getPkUnitPd() {
		return pkUnitPd;
	}

	public void setPkUnitPd(String pkUnitPd) {
		this.pkUnitPd = pkUnitPd;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getPkDeptCg() {
		return pkDeptCg;
	}

	public void setPkDeptCg(String pkDeptCg) {
		this.pkDeptCg = pkDeptCg;
	}

	public String getPkEmpCg() {
		return pkEmpCg;
	}

	public void setPkEmpCg(String pkEmpCg) {
		this.pkEmpCg = pkEmpCg;
	}

	public String getNameEmpCg() {
		return nameEmpCg;
	}

	public void setNameEmpCg(String nameEmpCg) {
		this.nameEmpCg = nameEmpCg;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}


	public String getPkOrgApp() {
		return pkOrgApp;
	}

	public void setPkOrgApp(String pkOrgApp) {
		this.pkOrgApp = pkOrgApp;
	}

	public String getPkDeptApp() {
		return pkDeptApp;
	}

	public void setPkDeptApp(String pkDeptApp) {
		this.pkDeptApp = pkDeptApp;
	}

	public String getPkEmpApp() {
		return pkEmpApp;
	}

	public void setPkEmpApp(String pkEmpApp) {
		this.pkEmpApp = pkEmpApp;
	}

	public String getNameEmpApp() {
		return nameEmpApp;
	}

	public void setNameEmpApp(String nameEmpApp) {
		this.nameEmpApp = nameEmpApp;
	}

	public String getPkOrgEx() {
		return pkOrgEx;
	}

	public void setPkOrgEx(String pkOrgEx) {
		this.pkOrgEx = pkOrgEx;
	}

	public String getPkDeptEx() {
		return pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}

	public Date getDateHap() {
		return dateHap;
	}

	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
	}

	public String getFlagPv() {
		return flagPv;
	}

	public void setFlagPv(String flagPv) {
		this.flagPv = flagPv;
	}

	public String getPkPres() {
		return pkPres;
	}

	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}

	////////
	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getPkOrdOld() {
		return pkOrdOld;
	}

	public void setPkOrdOld(String pkOrdOld) {
		this.pkOrdOld = pkOrdOld;
	}

	public BigDecimal getAmountMed() {
		return amountMed;
	}

	public void setAmountMed(BigDecimal amountMed) {
		this.amountMed = amountMed;
	}

	public String getPkDisc() {
		return pkDisc;
	}

	public void setPkDisc(String pkDisc) {
		this.pkDisc = pkDisc;
	}

	public String getPkItemOld() {
		return pkItemOld;
	}

	public void setPkItemOld(String pkItemOld) {
		this.pkItemOld = pkItemOld;
	}

	public String getNameItemOld() {
		return nameItemOld;
	}

	public void setNameItemOld(String nameItemOld) {
		this.nameItemOld = nameItemOld;
	}

	public String getFlagSetOld() {
		return flagSetOld;
	}

	public void setFlagSetOld(String flagSetOld) {
		this.flagSetOld = flagSetOld;
	}

	public String getEuPricemodeOld() {
		return euPricemodeOld;
	}

	public void setEuPricemodeOld(String euPricemodeOld) {
		this.euPricemodeOld = euPricemodeOld;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public Double getPriceOrg() {
		return priceOrg;
	}

	public void setPriceOrg(Double priceOrg) {
		this.priceOrg = priceOrg==null ? 0D : priceOrg;
	}

	public Double getRatioDisc() {
		return ratioDisc;
	}

	public void setRatioDisc(Double ratioDisc) {
		this.ratioDisc = ratioDisc;
	}

	public Double getRatioSelf() {
		return ratioSelf;
	}

	public void setRatioSelf(Double ratioSelf) {
		this.ratioSelf = ratioSelf;
	}

	public Double getRatioChildren() {
		return ratioChildren;
	}

	public void setRatioChildren(Double ratioChildren) {
		this.ratioChildren = ratioChildren;
	}

	public Double getRatioSpec() {
		return ratioSpec;
	}

	public void setRatioSpec(Double ratioSpec) {
		this.ratioSpec = ratioSpec;
	}

	public String getPkCgop() {
		return pkCgop;
	}

	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}

	public String getPkPdstdt() {
		return pkPdstdt;
	}

	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
	}
	
	public String getPkDeptJob() {
		return pkDeptJob;
	}

	public void setPkDeptJob(String pkDeptJob) {
		this.pkDeptJob = pkDeptJob;
	}
	

	public Double getAmountPock() {
		return amountPock;
	}

	public void setAmountPock(Double amountPock) {
		this.amountPock = amountPock;
	}

	public Object clone() {
		ItemPriceVo o = null;
		try {
			o = (ItemPriceVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}


	public String getPkPayer() {
		return pkPayer;
	}

	public void setPkPayer(String pkPayer) {
		this.pkPayer = pkPayer;
	}

	public String getPkDeptAreaapp() {
		return pkDeptAreaapp;
	}

	public void setPkDeptAreaapp(String pkDeptAreaapp) {
		this.pkDeptAreaapp = pkDeptAreaapp;
	}

	public Double getMaxQuan() {
		return maxQuan;
	}

	public void setMaxQuan(Double maxQuan) {
		this.maxQuan = maxQuan;
	}

	public String getFlagSelf() {
		return flagSelf;
	}

	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}

	public String getPkWgOrg() {
		return pkWgOrg;
	}

	public void setPkWgOrg(String pkWgOrg) {
		this.pkWgOrg = pkWgOrg;
	}

	public String getPkWgEx() {
		return pkWgEx;
	}

	public void setPkWgEx(String pkWgEx) {
		this.pkWgEx = pkWgEx;
	}

	public String getPkWg() {
		return pkWg;
	}

	public void setPkWg(String pkWg) {
		this.pkWg = pkWg;
	}
}
