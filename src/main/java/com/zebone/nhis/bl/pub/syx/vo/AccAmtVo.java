package com.zebone.nhis.bl.pub.syx.vo;

/**
 * 记账金额Vo
 * @author 32916
 *
 */
public class AccAmtVo {
	
	private String euHptype;//医保类型
	
	private Double amt;//收款金额
	
	private Long cntTrade;//收款发票张数
	
	private Double amtBack;//退款金额
	
	private Long cntTradeBack;//退款金额发票张数
	
	private Long cnt;	//记账人次

	public Long getCnt() {
		return cnt;
	}

	public void setCnt(Long cnt) {
		this.cnt = cnt;
	}

	public String getEuHptype() {
		return euHptype;
	}

	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Long getCntTrade() {
		return cntTrade;
	}

	public void setCntTrade(Long cntTrade) {
		this.cntTrade = cntTrade;
	}

	public Double getAmtBack() {
		return amtBack;
	}

	public void setAmtBack(Double amtBack) {
		this.amtBack = amtBack;
	}

	public Long getCntTradeBack() {
		return cntTradeBack;
	}

	public void setCntTradeBack(Long cntTradeBack) {
		this.cntTradeBack = cntTradeBack;
	}

	
}
