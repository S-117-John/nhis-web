package com.zebone.nhis.compay.ins.syx.vo.gzxnh;

import java.math.BigDecimal;

/**
 * 结算单打印明细Vo
 * @author Administrator
 *
 */
public class SettlePageDtVo {
	//项目编码
	private String code;
	//项目名称
	private String name;
	//项目金额
	private BigDecimal amt;
	//自付金额
	private BigDecimal amtPi;
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
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public BigDecimal getAmtPi() {
		return amtPi;
	}
	public void setAmtPi(BigDecimal amtPi) {
		this.amtPi = amtPi;
	}
	
}
