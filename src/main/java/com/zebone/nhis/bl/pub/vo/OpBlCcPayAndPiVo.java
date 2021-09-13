package com.zebone.nhis.bl.pub.vo;


/**
 * 操作员结账-付款方式（包含结算和患者账户）
 *
 * @author wangpeng
 * @date 2016年10月28日
 *
 */
public class OpBlCcPayAndPiVo {
	
	/** 操作员 */
    private String pkEmpOpera;

    /** 操作员姓名 */
    private String nameEmpOpera;
    
    /** 收付款方式  例如：现金；支票；银行卡；账户 */
    private String dtPaymode;
    
    /** 收付款方式名称  例如：现金；支票；银行卡；账户 */
    private String namePaymode;
    
    /** 收款金额 */
    private Double amt;

    /** 退款金额 */
    private Double amtBack;

    /** 收款交易笔数 */
    private Long cntTrade;

    /** 退款交易笔数 */
    private Long cntTradeBack;
    
    /** 收款金额(患者账户) */
    private Double amtPi;

    /** 退款金额(患者账户) */
    private Double amtBackPi;

    /** 收款交易笔数(患者账户) */
    private Long cntTradePi;

    /** 退款交易笔数(患者账户) */
    private Long cntTradeBackPi;

	public String getPkEmpOpera() {
		return pkEmpOpera;
	}

	public void setPkEmpOpera(String pkEmpOpera) {
		this.pkEmpOpera = pkEmpOpera;
	}

	public String getNameEmpOpera() {
		return nameEmpOpera;
	}

	public void setNameEmpOpera(String nameEmpOpera) {
		this.nameEmpOpera = nameEmpOpera;
	}

	public String getNamePaymode() {
		return namePaymode;
	}

	public void setNamePaymode(String namePaymode) {
		this.namePaymode = namePaymode;
	}

	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Double getAmtBack() {
		return amtBack;
	}

	public void setAmtBack(Double amtBack) {
		this.amtBack = amtBack;
	}

	public Long getCntTrade() {
		return cntTrade;
	}

	public void setCntTrade(Long cntTrade) {
		this.cntTrade = cntTrade;
	}

	public Long getCntTradeBack() {
		return cntTradeBack;
	}

	public void setCntTradeBack(Long cntTradeBack) {
		this.cntTradeBack = cntTradeBack;
	}

	public Double getAmtPi() {
		return amtPi;
	}

	public void setAmtPi(Double amtPi) {
		this.amtPi = amtPi;
	}

	public Double getAmtBackPi() {
		return amtBackPi;
	}

	public void setAmtBackPi(Double amtBackPi) {
		this.amtBackPi = amtBackPi;
	}

	public Long getCntTradePi() {
		return cntTradePi;
	}

	public void setCntTradePi(Long cntTradePi) {
		this.cntTradePi = cntTradePi;
	}

	public Long getCntTradeBackPi() {
		return cntTradeBackPi;
	}

	public void setCntTradeBackPi(Long cntTradeBackPi) {
		this.cntTradeBackPi = cntTradeBackPi;
	}

}
