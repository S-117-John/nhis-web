package com.zebone.nhis.pro.zsba.mz.pub.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_CC_PAY  - 收费结算-操作员结账-付款方式 
 *
 * @since 2016-10-21 09:37:19
 */
@Table(value="BL_CC_PAY")
public class ZsBaBlCcPay extends BaseModule  {

	private static final long serialVersionUID = 1L;


    /** DT_BANK - 收付款方式  例如：现金；支票；银行卡；公众号 */
	@Field(value="DT_BANK")
    private String dtBank;

	public String getDtBank() {
		return dtBank;
	}

	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}
	/** PK_CCPAY - 操作员结账付款方式信息主键 */
	@PK
	@Field(value="PK_CCPAY",id=KeyId.UUID)
    private String pkCcpay;

    /** PK_CC - 操作员结账主键 */
	@Field(value="PK_CC")
    private String pkCc;

    /** DT_PAYMODE - 收付款方式  例如：现金；支票；银行卡；账户 */
	@Field(value="DT_PAYMODE")
    private String dtPaymode;

    /** AMT - 收款金额 */
	@Field(value="AMT")
    private Double amt;

    /** AMT_BACK - 退款金额 */
	@Field(value="AMT_BACK")
    private Double amtBack;

    /** CNT_TRADE - 收款交易笔数 */
	@Field(value="CNT_TRADE")
    private Long cntTrade;

    /** EU_PAYTYPE - 收付款类型:0 结算；1 就诊预交金；2 患者预交金 */
	@Field(value="EU_PAYTYPE")
    private String euPaytype;

    /** CNT_TRADE_BACK - 退款交易笔数 */
	@Field(value="CNT_TRADE_BACK")
    private Long cntTradeBack;
	
	/** 收付款方式名称 */
	private String namePaymode;
	
	private String euDirect;


    public String getEuDirect() {
		return euDirect;
	}
	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}
	public String getPkCcpay(){
        return this.pkCcpay;
    }
    public void setPkCcpay(String pkCcpay){
        this.pkCcpay = pkCcpay;
    }

    public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
    }

    public String getDtPaymode(){
        return this.dtPaymode;
    }
    public void setDtPaymode(String dtPaymode){
        this.dtPaymode = dtPaymode;
    }

    public Double getAmt(){
        return this.amt;
    }
    public void setAmt(Double amt){
        this.amt = amt;
    }

    public Double getAmtBack(){
        return this.amtBack;
    }
    public void setAmtBack(Double amtBack){
        this.amtBack = amtBack;
    }

    public Long getCntTrade(){
        return this.cntTrade;
    }
    public void setCntTrade(Long cntTrade){
        this.cntTrade = cntTrade;
    }

    public String getEuPaytype(){
        return this.euPaytype;
    }
    public void setEuPaytype(String euPaytype){
        this.euPaytype = euPaytype;
    }

    public Long getCntTradeBack(){
        return this.cntTradeBack;
    }
    public void setCntTradeBack(Long cntTradeBack){
        this.cntTradeBack = cntTradeBack;
    }
	public String getNamePaymode() {
		return namePaymode;
	}
	public void setNamePaymode(String namePaymode) {
		this.namePaymode = namePaymode;
	}
}
