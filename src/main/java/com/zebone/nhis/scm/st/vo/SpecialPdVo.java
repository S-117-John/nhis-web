package com.zebone.nhis.scm.st.vo;

public class SpecialPdVo {
	private String pkPd;
	private String code;
	private String name;
	private String spec;
	private String spectype;
	private Double quanSt;// 入库数量
	private Double amountSt;// 入库金额
	private Double quanOut;// 出库数量
	private Double amountOut;// 出库金额
	private Double quanQc;// 期初数量
	private Double amountQc;// 期初金额
	private Double quanJc;// 结存数量
	private Double amountJc;// 结存金额
    private String unitName;//单位名称
    
    
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
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

	public String getSpectype() {
		return spectype;
	}

	public void setSpectype(String spectype) {
		this.spectype = spectype;
	}

	public Double getQuanSt() {
		return quanSt;
	}

	public void setQuanSt(Double quanSt) {
		this.quanSt = quanSt;
	}

	public Double getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}

	public Double getQuanOut() {
		return quanOut;
	}

	public void setQuanOut(Double quanOut) {
		this.quanOut = quanOut;
	}

	public Double getAmountOut() {
		return amountOut;
	}

	public void setAmountOut(Double amountOut) {
		this.amountOut = amountOut;
	}

	public Double getQuanQc() {
		return quanQc;
	}

	public void setQuanQc(Double quanQc) {
		this.quanQc = quanQc;
	}

	public Double getAmountQc() {
		return amountQc;
	}

	public void setAmountQc(Double amountQc) {
		this.amountQc = amountQc;
	}

	public Double getQuanJc() {
		return quanJc;
	}

	public void setQuanJc(Double quanJc) {
		this.quanJc = quanJc;
	}

	public Double getAmountJc() {
		return amountJc;
	}

	public void setAmountJc(Double amountJc) {
		this.amountJc = amountJc;
	}

}
