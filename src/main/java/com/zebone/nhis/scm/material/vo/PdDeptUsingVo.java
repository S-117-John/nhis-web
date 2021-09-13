package com.zebone.nhis.scm.material.vo;

public class PdDeptUsingVo {
    // 使用科室主键
    private String pkDeptUse ;

    // 使用科室名称
    private String deptName ;

    // 物品主键
    private String pkPd ;

    // 物品名称
    private String name ;

    // 规格
    private String spec ;

    // 厂家主键
    private String pkFactory ;

    // 厂家名称
    private String factoryName ;

    // 单位主键
    private String pkUnitPack ;

    // 单位名称
    private String unitName ;

    // 数量
    private Long quan ;

    // 成本金额
    private Double amt;

	public String getPkDeptUse() {
		return pkDeptUse;
	}

	public void setPkDeptUse(String pkDeptUse) {
		this.pkDeptUse = pkDeptUse;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
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

	public String getPkFactory() {
		return pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getQuan() {
		return quan;
	}

	public void setQuan(Long quan) {
		this.quan = quan;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}
    
}
