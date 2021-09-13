package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: tp_serv_unionpay_data_bill - 下载银行对账单明细（杉德） 
 *
 * @since 2017-09-07 12:29:02
 */
@Table(value="TP_SERV_UNIONPAY_DATA_BILL")
public class TpServUnionpayDataBill   {

    /** PK_DATA_BILL - 编号 */
	@PK
	@Field(value="PK_DATA_BILL",id=KeyId.UUID)
    private String pkDataBill;

    /** PK_DATA_BILL_P - 对账编号 */
	@Field(value="PK_DATA_BILL_P")
    private String pkDataBillP;

    /** TERMINAL_ID - 终端号 */
	@Field(value="TERMINAL_ID")
    private String terminalId;

    /** MERCHANT_NAME - 商户名称 */
	@Field(value="MERCHANT_NAME")
    private String merchantName;

    /** NET_ADDRNAME - 网络名称 */
	@Field(value="NET_ADDRNAME")
    private String netAddrname;

    /** CARD_NO - 持卡人银行卡号 */
	@Field(value="CARD_NO")
    private String cardNo;

    /** BANK_NAME - 持卡人银行名称 */
	@Field(value="BANK_NAME")
    private String bankName;

    /** TRANS_DATE - 交易日期 */
	@Field(value="TRANS_DATE")
    private String transDate;

    /** TRANS_TIME - 交易时间 */
	@Field(value="TRANS_TIME")
    private String transTime;

    /** AMOUNT - 交易金额 */
	@Field(value="AMOUNT")
    private String amount;

    /** AMOUNT_TOTAL - 交易总金额 */
	@Field(value="AMOUNT_TOTAL")
    private BigDecimal amountTotal;

    /** TRANS_FEE - 手续费 */
	@Field(value="TRANS_FEE")
    private String transFee;

    /** TRANS_FEE_TOTAL - 手续费总金额 */
	@Field(value="TRANS_FEE_TOTAL")
    private BigDecimal transFeeTotal;

    /** B_FEE - 实收金额 */
	@Field(value="B_FEE")
    private String bFee;

    /** B_FEE_TOTAL - 实收总金额 */
	@Field(value="B_FEE_TOTAL")
    private BigDecimal bFeeTotal;

    /** TRANS_TYPE - 交易类别 */
	@Field(value="TRANS_TYPE")
    private String transType;

    /** END_TIME - 结束日期 */
	@Field(value="END_TIME")
    private String endTime;

    /** SYSREFNO - 系统参考号 */
	@Field(value="SYSREFNO")
    private String sysrefno;

    /** BILL_STATUS - 对账状态 */
	@Field(value="BILL_STATUS")
    private Integer billStatus;

    /** BILL_TIME - 对账时间 */
	@Field(value="BILL_TIME")
    private Date billTime;

    /** BILL_DESC - 对账描述 */
	@Field(value="BILL_DESC")
    private String billDesc;

    /** MERCHANT_ID - 商户号 */
	@Field(value="MERCHANT_ID")
    private String merchantId;

    /** SYSREFNO1 - 系统流水号 */
	@Field(value="SYSREFNO1")
    private String sysrefno1;


    public String getPkDataBill(){
        return this.pkDataBill;
    }
    public void setPkDataBill(String pkDataBill){
        this.pkDataBill = pkDataBill;
    }

    public String getPkDataBillP(){
        return this.pkDataBillP;
    }
    public void setPkDataBillP(String pkDataBillP){
        this.pkDataBillP = pkDataBillP;
    }

    public String getTerminalId(){
        return this.terminalId;
    }
    public void setTerminalId(String terminalId){
        this.terminalId = terminalId;
    }

    public String getMerchantName(){
        return this.merchantName;
    }
    public void setMerchantName(String merchantName){
        this.merchantName = merchantName;
    }

    public String getNetAddrname(){
        return this.netAddrname;
    }
    public void setNetAddrname(String netAddrname){
        this.netAddrname = netAddrname;
    }

    public String getCardNo(){
        return this.cardNo;
    }
    public void setCardNo(String cardNo){
        this.cardNo = cardNo;
    }

    public String getBankName(){
        return this.bankName;
    }
    public void setBankName(String bankName){
        this.bankName = bankName;
    }

    public String getTransDate(){
        return this.transDate;
    }
    public void setTransDate(String transDate){
        this.transDate = transDate;
    }

    public String getTransTime(){
        return this.transTime;
    }
    public void setTransTime(String transTime){
        this.transTime = transTime;
    }

    public String getAmount(){
        return this.amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }

    public BigDecimal getAmountTotal(){
        return this.amountTotal;
    }
    public void setAmountTotal(BigDecimal amountTotal){
        this.amountTotal = amountTotal;
    }

    public String getTransFee(){
        return this.transFee;
    }
    public void setTransFee(String transFee){
        this.transFee = transFee;
    }

    public BigDecimal getTransFeeTotal(){
        return this.transFeeTotal;
    }
    public void setTransFeeTotal(BigDecimal transFeeTotal){
        this.transFeeTotal = transFeeTotal;
    }

    public String getbFee(){
        return this.bFee;
    }
    public void setbFee(String bFee){
        this.bFee = bFee;
    }

    public BigDecimal getbFeeTotal(){
        return this.bFeeTotal;
    }
    public void setbFeeTotal(BigDecimal bFeeTotal){
        this.bFeeTotal = bFeeTotal;
    }

    public String getTransType(){
        return this.transType;
    }
    public void setTransType(String transType){
        this.transType = transType;
    }

    public String getEndTime(){
        return this.endTime;
    }
    public void setEndTime(String endTime){
        this.endTime = endTime;
    }

    public String getSysrefno(){
        return this.sysrefno;
    }
    public void setSysrefno(String sysrefno){
        this.sysrefno = sysrefno;
    }

    public Integer getBillStatus(){
        return this.billStatus;
    }
    public void setBillStatus(Integer billStatus){
        this.billStatus = billStatus;
    }

    public Date getBillTime(){
        return this.billTime;
    }
    public void setBillTime(Date billTime){
        this.billTime = billTime;
    }

    public String getBillDesc(){
        return this.billDesc;
    }
    public void setBillDesc(String billDesc){
        this.billDesc = billDesc;
    }

    public String getMerchantId(){
        return this.merchantId;
    }
    public void setMerchantId(String merchantId){
        this.merchantId = merchantId;
    }

    public String getSysrefno1(){
        return this.sysrefno1;
    }
    public void setSysrefno1(String sysrefno1){
        this.sysrefno1 = sysrefno1;
    }
}