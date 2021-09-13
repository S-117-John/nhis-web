package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 收费项目明细
 * @author Administrator
 *
 */
public class ChargeDetailVo {
	
	/**
	 * 序号
	 * true
	 */
	private Integer sortNo;
	
	/**
	 * 收费项目代码
	 * true
	 */
	private String chargeCode;
	
	/**
	 * 收费项目名称
	 * true
	 */
	private String chargeName;
	
	/**
	 * 计量单位
	 * false
	 */
	private String unit;
	
	/**
	 * 收费标准
	 * true
	 */
	private Double std;
	
	/**
	 * 数量
	 * true
	 */
	private Double number;
	
	/**
	 * 金额
	 * true
	 */
	private Double amt;
	
	/**
	 * 自费金额
	 * true
	 */
	private Double selfAmt;
	
	/**
	 * 备注
	 * false
	 */
	private String remark;
	
//	/**
//	 * 用来做金额比较，不上传平台
//	 */
//	private Double amtChk;
//	
//	/**
//	 * 用来做金额比较，不上传平台
//	 */
//	private Double selfAmtChk;
//
//	
//	public Double getAmtChk() {
//		return amtChk;
//	}
//
//	public void setAmtChk(Double amtChk) {
//		this.amtChk = amtChk;
//	}
//
//	public Double getSelfAmtChk() {
//		return selfAmtChk;
//	}
//
//	public void setSelfAmtChk(Double selfAmtChk) {
//		this.selfAmtChk = selfAmtChk;
//	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getStd() {
		return std;
	}

	public void setStd(Double std) {
		this.std = std;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Double getSelfAmt() {
		return selfAmt;
	}

	public void setSelfAmt(Double selfAmt) {
		this.selfAmt = selfAmt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
