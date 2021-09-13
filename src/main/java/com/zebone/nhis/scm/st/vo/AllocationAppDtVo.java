package com.zebone.nhis.scm.st.vo;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

public class AllocationAppDtVo  extends PdPlanDetail{
	private String pdname;
	private String pdcode;
	private String spec;
	private String factory;
	private String unit;
	private String nameOrg;
	private String nameStore;
	private Double quanStk;
	private String spcode;
	private String pdnameas;
	private String unitPd;
	private String pkUnit;
	private BigDecimal packSizeMax;
	private String flagStop;
	private String targetStore;
	private Double quanStkSelf;
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
	/**下限天数对应的数量*/
	private Double lowQuanMin;
	/** 药品类别 :0西药，1成药，2草药 */
	private String euDrugtype;

	/**
	 * 消耗量大于库存
	 */
	private String isStockMax;

	/**
	 * 业务类型
	 */
	private List<String> dtSttypes;

	private Double price;
	private Integer packSizePd;

	public String getIsStockMax() {
		return isStockMax;
	}

	public void setIsStockMax(String isStockMax) {
		this.isStockMax = isStockMax;
	}

	public List<String> getDtSttypes() {
		return dtSttypes;
	}

	public void setDtSttypes(List<String> dtSttypes) {
		this.dtSttypes = dtSttypes;
	}

	public String getCalculateWay() {
		return calculateWay;
	}

	public void setCalculateWay(String calculateWay) {
		this.calculateWay = calculateWay;
	}

	public String getEuDrugtype() {
		return euDrugtype;
	}

	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
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

	public Double getLowQuanMin() {
		return lowQuanMin;
	}

	public void setLowQuanMin(Double lowQuanMin) {
		this.lowQuanMin = lowQuanMin;
	}

	public String getTargetStore() {
		return targetStore;
	}
	public void setTargetStore(String targetStore) {
		this.targetStore = targetStore;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public BigDecimal getPackSizeMax() {
		return packSizeMax;
	}
	public void setPackSizeMax(BigDecimal packSizeMax) {
		this.packSizeMax = packSizeMax;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	//消耗计算
	 private String dateEnd;
	    
	 private String accounts;
	 
	 private Integer cnt;
	 
	 private Integer cntPlan;
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
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
	public String getUnitPd() {
		return unitPd;
	}
	public void setUnitPd(String unitPd) {
		this.unitPd = unitPd;
	}
	public String getPdnameas() {
		return pdnameas;
	}
	public void setPdnameas(String pdnameas) {
		this.pdnameas = pdnameas;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQuanStk() {
		return quanStk;
	}
	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	public Double getQuanStkSelf() {
		return quanStkSelf;
	}
	public void setQuanStkSelf(Double quanStkSelf) {
		this.quanStkSelf = quanStkSelf;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getPackSizePd() {
		return packSizePd;
	}

	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
}
