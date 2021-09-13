package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

/**
 * 单病种控费vo
 * 
 * @author yangxue
 * 
 */
public class BlDiagDivVo {
	private String pkHp;//保险计划
	private String pkDiag;//诊断
	private String pkItemcate;//费用类别
	private BigDecimal rate;//占比
	private BigDecimal amount;//限价金额
	private String nameItemcate;//费用分类名称
	private String namePi;//患者姓名
	
	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getNameItemcate() {
		return nameItemcate;
	}

	public void setNameItemcate(String nameItemcate) {
		this.nameItemcate = nameItemcate;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getPkItemcate() {
		return pkItemcate;
	}

	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
