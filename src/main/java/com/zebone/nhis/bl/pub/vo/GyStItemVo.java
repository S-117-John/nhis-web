package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

/**
 * 待结算非药品记费明细Vo
 * @author 32916
 *
 */
public class GyStItemVo {
	private BigDecimal ratioSelf;
	
	private BigDecimal price;
	
	private BigDecimal quan;
	
	private BigDecimal amt;
	
	private BigDecimal amtHppi;
	
	private BigDecimal amtPi;
	
	private String dtItemtype;
	
	private BigDecimal rateOp;
	
	private BigDecimal dtquotaOp;
	
	private BigDecimal bedquota;
	
	private BigDecimal amountMax;
	
	private BigDecimal euCalcmode;
	
	private BigDecimal ratio;
	
	private BigDecimal rateIp;
	
	private BigDecimal dtquotaIp;
	
	public BigDecimal getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(BigDecimal amountMax) {
		this.amountMax = amountMax;
	}

	public BigDecimal getEuCalcmode() {
		return euCalcmode;
	}

	public void setEuCalcmode(BigDecimal euCalcmode) {
		this.euCalcmode = euCalcmode;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public BigDecimal getRateIp() {
		return rateIp;
	}

	public void setRateIp(BigDecimal rateIp) {
		this.rateIp = rateIp;
	}

	public BigDecimal getDtquotaIp() {
		return dtquotaIp;
	}

	public void setDtquotaIp(BigDecimal dtquotaIp) {
		this.dtquotaIp = dtquotaIp;
	}

	public BigDecimal getRatioSelf() {
		return ratioSelf;
	}

	public void setRatioSelf(BigDecimal ratioSelf) {
		this.ratioSelf = ratioSelf;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuan() {
		return quan;
	}

	public void setQuan(BigDecimal quan) {
		this.quan = quan;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getAmtHppi() {
		return amtHppi;
	}

	public void setAmtHppi(BigDecimal amtHppi) {
		this.amtHppi = amtHppi;
	}

	public BigDecimal getAmtPi() {
		return amtPi;
	}

	public void setAmtPi(BigDecimal amtPi) {
		this.amtPi = amtPi;
	}

	public String getDtItemtype() {
		return dtItemtype;
	}

	public void setDtItemtype(String dtItemtype) {
		this.dtItemtype = dtItemtype;
	}

	public BigDecimal getRateOp() {
		return rateOp;
	}

	public void setRateOp(BigDecimal rateOp) {
		this.rateOp = rateOp;
	}

	public BigDecimal getDtquotaOp() {
		return dtquotaOp;
	}

	public void setDtquotaOp(BigDecimal dtquotaOp) {
		this.dtquotaOp = dtquotaOp;
	}

	public BigDecimal getBedquota() {
		return bedquota;
	}

	public void setBedquota(BigDecimal bedquota) {
		this.bedquota = bedquota;
	}
	
}
