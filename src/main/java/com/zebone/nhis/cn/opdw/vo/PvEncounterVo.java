package com.zebone.nhis.cn.opdw.vo;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.pv.PvEncounter;

public class PvEncounterVo extends PvEncounter {	
	/**
	 * 主诊断名称
	 */
	private String nameDiag;
	
	/**
	 * 本次就诊处方总金额，住院金额不计算，设为0
	 */
	private BigDecimal recipeTotalMoney;
	
	/**
	 * 本次就诊已缴费处方总金额，住院金额不计算，设为0
	 */
	private BigDecimal recipeTotalMoneyForPayed;
	
	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public BigDecimal getRecipeTotalMoney() {
		return recipeTotalMoney;
	}

	public void setRecipeTotalMoney(BigDecimal recipeTotalMoney) {
		this.recipeTotalMoney = recipeTotalMoney;
	}

	public BigDecimal getRecipeTotalMoneyForPayed() {
		return recipeTotalMoneyForPayed;
	}

	public void setRecipeTotalMoneyForPayed(BigDecimal recipeTotalMoneyForPayed) {
		this.recipeTotalMoneyForPayed = recipeTotalMoneyForPayed;
	}
}
