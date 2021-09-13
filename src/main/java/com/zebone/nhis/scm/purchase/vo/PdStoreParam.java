package com.zebone.nhis.scm.purchase.vo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdSt;


/***
 * 仓库物品
 * 
 * @author wangpeng
 * @date 2016年11月2日
 *
 */
public class PdStoreParam {
	
	/** 机构主键 */
	private String pkOrg;
	
	/** 仓库主键 */
	private String pkStore;
	
	/** 药品类别 :0西药，1成药，2草药 */
	private String euDrugtype;
	
	/** 毒麻分类 */
	private String dtPois;
	
	/** 来源分类:0 国产, 1 合资, 2 进口 */
	private String dtAbrd;
	
	/** 抗菌药分类 */
	private String dtAnti;
	
	/** 是否小于库存下限：0-否 1-是 ,默认否*/
	private String flagSrote = "0";
	
	/** 物品主键 */
	private String pkPd;
	
	/** 物品编码 */
	private String code;
	
	/** 物品名称 */
	private String name;
	
	/** 包装规格 */
	private String spec;
	
	/** 计量单位主键 */
	private String pkUnit;
	
	/** 计量单位名称 */
	private String nameUnit;
	
	/** 生产厂家 */
	private String pkFactory;
	
	/** 生产厂家 */
	private String nameFactory;
	
	/** 计量关系 */
	private String packSize;
	
	/** 供应商主键 */
	private String pkSupplyer;

	/** 供应商名称 */
	private String nameSupplyer;
	
	/** 库存上限 */
	private Double stockMax;

	/** 库存下限 */
    private Double stockMin;
    
    /** 计算周期*/
    private Integer cnt;
    
    /** 计划周期*/
    private Integer cntPlan;
    
    /** 截止日期*/
    private String dateEnd;
    
    /** 计算类型*/
    private String account;
    
    /** 需要数量*/
    private Double quanPack;
    
    /** 实际消耗量*/
    private Double realQuanPack;    
    
    /** 箱包装*/
	private Double packSizeMax;
    
	/** 单价*/
    private BigDecimal price;    

	private Double quanMin;
    
    //仓库停用标志
    private String flagStop;
	/**计算方式*/
    private String calculateWay;
    /**上限天数*/
    private String upperDays;
    /**下限天数*/
    private String lowerDays;
    /**消耗起止日期*/
    private String dateStart;
    /**消耗截止日期*/
    private String dateEnd2;
    /**库存*/
	private Double quanStk;
	/**下限天数对应的数量*/
	private Double lowQuanMin;
	
	/**仓库主键集合*/
	private List<String> pkStores;

	private List<String> dtSttypes;

	/**
	 * 药理分类
	 */
	private String dtPharm;

	/**
	 * 批准文号
	 */
	private String apprNo;

	/**
	 * 统计编码
	 */
	private String dtUnify;

	/**
	 * 货位码
	 */
	private String posiNo;


	public Double getLowQuanMin() {
		return lowQuanMin;
	}

	public void setLowQuanMin(Double lowQuanMin) {
		this.lowQuanMin = lowQuanMin;
	}

	public Double getQuanMin() {
		return quanMin;
	}

	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}

	public Double getQuanStk() {
		return quanStk;
	}

	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}

	public String getCalculateWay() {
		return calculateWay;
	}

	public void setCalculateWay(String calculateWay) {
		this.calculateWay = calculateWay;
	}

	public String getUpperDays() {
		return upperDays;
	}

	public void setUpperDays(String upperDays) {
		this.upperDays = upperDays;
	}

	public String getLowerDays() {
		return lowerDays;
	}

	public void setLowerDays(String lowerDays) {
		this.lowerDays = lowerDays;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd2() {
		return dateEnd2;
	}

	public void setDateEnd2(String dateEnd2) {
		this.dateEnd2 = dateEnd2;
	}

	public String getFlagStop() {
		return flagStop;
	}

	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}

	public Double getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getCntPlan() {
		return cntPlan;
	}

	public void setCntPlan(Integer cntPlan) {
		this.cntPlan = cntPlan;
	}


	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkStore() {
		return pkStore;
	}

	public void setPkStore(String pkStore) {
		this.pkStore = pkStore;
	}

	public String getEuDrugtype() {
		return euDrugtype;
	}

	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}

	public String getDtPois() {
		return dtPois;
	}

	public void setDtPois(String dtPois) {
		this.dtPois = dtPois;
	}

	public String getDtAbrd() {
		return dtAbrd;
	}

	public void setDtAbrd(String dtAbrd) {
		this.dtAbrd = dtAbrd;
	}

	public String getDtAnti() {
		return dtAnti;
	}

	public void setDtAnti(String dtAnti) {
		this.dtAnti = dtAnti;
	}

	public String getFlagSrote() {
		return flagSrote;
	}

	public void setFlagSrote(String flagSrote) {
		this.flagSrote = flagSrote;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getNameUnit() {
		return nameUnit;
	}

	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	public String getPkFactory() {
		return pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getNameFactory() {
		return nameFactory;
	}

	public void setNameFactory(String nameFactory) {
		this.nameFactory = nameFactory;
	}

	public String getPackSize() {
		return packSize;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}

	public String getPkSupplyer() {
		return pkSupplyer;
	}

	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}

	public String getNameSupplyer() {
		return nameSupplyer;
	}

	public void setNameSupplyer(String nameSupplyer) {
		this.nameSupplyer = nameSupplyer;
	}

	public Double getStockMax() {
		return stockMax;
	}

	public void setStockMax(Double stockMax) {
		this.stockMax = stockMax;
	}

	public Double getStockMin() {
		return stockMin;
	}

	public void setStockMin(Double stockMin) {
		this.stockMin = stockMin;
	}

	public List<String> getPkStores() {
		return pkStores;
	}

	public void setPkStores(List<String> pkStores) {
		this.pkStores = pkStores;
	}

	public String getApprNo() {
		return apprNo;
	}

	public void setApprNo(String apprNo) {
		this.apprNo = apprNo;
	}

	public String getDtPharm() {
		return dtPharm;
	}

	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}

	public String getDtUnify() {
		return dtUnify;
	}

	public void setDtUnify(String dtUnify) {
		this.dtUnify = dtUnify;
	}

	public String getPosiNo() {
		return posiNo;
	}

	public void setPosiNo(String posiNo) {
		this.posiNo = posiNo;
	}

	public List<String> getDtSttypes() {
		return dtSttypes;
	}

	public void setDtSttypes(List<String> dtSttypes) {
		this.dtSttypes = dtSttypes;
	}
	
	public Double getRealQuanPack() {
		return realQuanPack;
	}

	public void setRealQuanPack(Double realQuanPack) {
		this.realQuanPack = realQuanPack;
	}
	
	public Double getPackSizeMax() {
		return packSizeMax;
	}

	public void setPackSizeMax(Double packSizeMax) {
		this.packSizeMax = packSizeMax;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
