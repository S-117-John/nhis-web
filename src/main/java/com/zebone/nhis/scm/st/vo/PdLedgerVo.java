package com.zebone.nhis.scm.st.vo;

import java.util.Date;

public class PdLedgerVo {
	private String mapKey;//	主键
	private String pkPd;//	药品主键
	private String packUnit;//	包装单位
	private String packUnitName;//
	private String name;//	药品名称
	private String spec;//	药品规格
	private String fac;//	生产厂家
	private String facName;//
	private String spcode;//拼音码
	private String pkStroe;//
	private String calMonBegin;//查询开始财务月份-1
	private String monthBegin;//查询开始财务月份
	private String monthEnd;//查询结束财务月份
	private String dateBegin;  //查询开始日期
	private String dateEnd;    //查询结束日期
	private String ledgerDate;//明细日期
	private String oddNum;  //单据号码
	private String remark;  //摘要
		
	private Long quanCC;//	期初数量
	private Double amtCC;//	期初零售金额
	private Double amtCostCC;//	期初成本金额
		
	private Long quanIncom;//	收入数量
	private Double amtInCom	;//收入零售金额
	private Double amtCostInCom;//收入成本金额
		
	private Long quanOutcom;//支出数量
	private Double amtOutCom;//	支出零售金额
	private Double amtCostOutCom;//支出成本金额
		
	private Long quanBalance;//	结存数量
	private Double amtBalance ;//	结存零售金额	
	private Double amtCostBalance ;//结存成本金额

	
	
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getCalMonBegin() {
		return calMonBegin;
	}
	public void setCalMonBegin(String calMonBegin) {
		this.calMonBegin = calMonBegin;
	}
	public String getPkStroe() {
		return pkStroe;
	}
	public void setPkStroe(String pkStroe) {
		this.pkStroe = pkStroe;
	}
	public String getMonthBegin() {
		return monthBegin;
	}
	public void setMonthBegin(String monthBegin) {
		this.monthBegin = monthBegin;
	}
	public String getMonthEnd() {
		return monthEnd;
	}
	public void setMonthEnd(String monthEnd) {
		this.monthEnd = monthEnd;
	}

	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getPackUnitName() {
		return packUnitName;
	}
	public void setPackUnitName(String packUnitName) {
		this.packUnitName = packUnitName;
	}
	public String getFacName() {
		return facName;
	}
	public void setFacName(String facName) {
		this.facName = facName;
	}

	public String getMapKey() {
		return mapKey;
	}
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getPackUnit() {
		return packUnit;
	}
	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
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
	public String getFac() {
		return fac;
	}
	public void setFac(String fac) {
		this.fac = fac;
	}
	public Long getQuanCC() {
		return quanCC;
	}
	public void setQuanCC(Long quanCC) {
		this.quanCC = quanCC;
	}
	public Double getAmtCC() {
		return amtCC;
	}
	public void setAmtCC(Double amtCC) {
		this.amtCC = amtCC;
	}
	public Double getAmtCostCC() {
		return amtCostCC;
	}
	public void setAmtCostCC(Double amtCostCC) {
		this.amtCostCC = amtCostCC;
	}
	public Long getQuanIncom() {
		return quanIncom;
	}
	public void setQuanIncom(Long quanIncom) {
		this.quanIncom = quanIncom;
	}
	public Double getAmtInCom() {
		return amtInCom;
	}
	public void setAmtInCom(Double amtInCom) {
		this.amtInCom = amtInCom;
	}
	public Double getAmtCostInCom() {
		return amtCostInCom;
	}
	public void setAmtCostInCom(Double amtCostInCom) {
		this.amtCostInCom = amtCostInCom;
	}
	public Long getQuanOutcom() {
		return quanOutcom;
	}
	public void setQuanOutcom(Long quanOutcom) {
		this.quanOutcom = quanOutcom;
	}
	public Double getAmtOutCom() {
		return amtOutCom;
	}
	public void setAmtOutCom(Double amtOutCom) {
		this.amtOutCom = amtOutCom;
	}
	public Double getAmtCostOutCom() {
		return amtCostOutCom;
	}
	public void setAmtCostOutCom(Double amtCostOutCom) {
		this.amtCostOutCom = amtCostOutCom;
	}
	public Long getQuanBalance() {
		return quanBalance;
	}
	public void setQuanBalance(Long quanBalance) {
		this.quanBalance = quanBalance;
	}
	public Double getAmtBalance() {
		return amtBalance;
	}
	public void setAmtBalance(Double amtBalance) {
		this.amtBalance = amtBalance;
	}
	public Double getAmtCostBalance() {
		return amtCostBalance;
	}
	public void setAmtCostBalance(Double amtCostBalance) {
		this.amtCostBalance = amtCostBalance;
	}

	public String getLedgerDate() {
		return ledgerDate;
	}
	public void setLedgerDate(String ledgerDate) {
		this.ledgerDate = ledgerDate;
	}
	public String getOddNum() {
		return oddNum;
	}
	public void setOddNum(String oddNum) {
		this.oddNum = oddNum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	


}
