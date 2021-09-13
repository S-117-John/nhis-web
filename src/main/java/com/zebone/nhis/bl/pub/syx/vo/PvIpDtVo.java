package com.zebone.nhis.bl.pub.syx.vo;

import java.util.Date;

/**
 * 患者费用明细Vo
 * @author c
 *
 */
public class PvIpDtVo {
	
	//就诊PK
	private String pkPv;
	
	//患者PK
	private String pkPi;
	
	//就诊类型
	private String euPvtype;
	
	//主医保
	private String pkInsu;
	
	//开始日期
	private Date dateBegin;
	
	//结束日期
	private Date dateEnd;
	
	//结算金额
	private Double amountSt;
	
	//患者自付
	private Double amountPi;
	
	//医保支付
	private Double amountInsu;
	
	//优惠金额
	private Double amountDisc;
	
	//单位支付
	private Double amountUnit;

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Double getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public Double getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(Double amountInsu) {
		this.amountInsu = amountInsu;
	}

	public Double getAmountDisc() {
		return amountDisc;
	}

	public void setAmountDisc(Double amountDisc) {
		this.amountDisc = amountDisc;
	}

	public Double getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(Double amountUnit) {
		this.amountUnit = amountUnit;
	}
}
