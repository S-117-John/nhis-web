package com.zebone.nhis.common.module.pay;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Table: BL_EXT_PAY  - 外部接口-支付
 *
 * @since 2017-09-20 09:24:57
 */
@Table(value = "BL_EXT_PAY")
public class BlExtPay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * PK_EXTPAY - 支付接口主键
     */
    @PK
    @Field(value = "PK_EXTPAY", id = KeyId.UUID)
    private String pkExtpay;

    /**
     * 机构主键
     */
    @Field(value = "PK_ORG", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
    private String pkOrg;

    /**
     * 支付金额
     */
    @Field(value = "AMOUNT")
    private BigDecimal amount;

    /**
     * 支付方式
     */
    @Field(value = "EU_PAYTYPE")
    private String euPaytype;

    /**
     * 支付结果
     */
    @Field(value = "PAY_RESULT")
    private String payResult;
    /**
     * 关联银行
     */
    @Field(value = "DT_BANK")
    private String dtBank;

    /**
     * 银行名称
     */
    @Field(value = "NAME_BANK")
    private String nameBank;

    /**
     * 请求日期
     */
    @Field(value = "DATE_AP")
    private Date dateAp;

    /**
     * 支付标志
     */
    @Field(value = "FLAG_PAY")
    private String flagPay;

    /**
     * 支付时间
     */
    @Field(value = "DATE_PAY")
    private Date datePay;

    /**
     * 交易编码——接口交易号，用于对账
     */
    @Field(value = "TRADE_NO")
    private String tradeNo;

    /**
     * 系统交易流水号(商户订单号)
     */
    @Field(value = "SERIAL_NO")
    private String serialNo;

    /**
     * 描述
     */
    @Field(value = "DESC_PAY")
    private String descPay;

    /**
     * 支付内容
     */
    @Field(value = "BODY_PAY")
    private String bodyPay;

    /**
     * 外部系统名称
     */
    @Field(value = "SYSNAME")
    private String sysname;

    /**
     * 患者主键
     */
    @Field(value = "PK_PI")
    private String pkPi;

    /**
     * 就诊主键
     */
    @Field(value = "PK_PV")
    private String pkPv;

    /**
     * 关联就诊缴款
     */
    @Field(value = "PK_DEPO")
    private String pkDepo;

    /**
     * 关联患者缴款
     */
    @Field(value = "PK_DEPOPI")
    private String pkDepopi;

    /**
     * 支付回写
     */
    @Field(value = "RESULT_PAY")
    private String resultPay;

    /**
     * 退款单号
     */
    @Field(value = "REFUND_NO")
    private String refundNo;

    /**
     * 退款支付时间
     */
    @Field(value = "DATE_REFUND")
    private Date dateRefund;

    /**
     * 对账标识
     */
    @Field(value = "EU_BILL")
    private String euBill;

    /**
     * 对账单主键
     */
    @Field(value = "PK_BILL")
    private String pkBill;

    /**
     * 对账时间
     */
    @Field(value = "DATE_BILL")
    private Date dateBill;

    /**
     * 订单交易号
     */
    @Field(value = "OUT_TRADE_NO")
    private String outTradeNo;

    /**
     * 创建人
     */
    @Field(userfield = "pkEmp", userfieldscop = FieldType.INSERT)
    private String creator;


    /**
     * 创建时间
     */
    @Field(value = "CREATE_TIME", date = FieldType.INSERT)
    private Date createTime;


    /**
     * 修改人
     */
    @Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
    private String modifier;


    /**
     * 修改时间
     */
    @Field(value = "MODITY_TIME", date = FieldType.INSERT)
    private Date modityTime;


    /**
     * 时间戳
     */
    @Field(value = "TS", date = FieldType.ALL)
    private Date ts;

    /**
     * 删除标志
     */
    @Field(value = "DEL_FLAG")
    private String delFlag = "0";  // 0未删除  1：删除

    /**
     * 业务主键
     */
    @Field(value = "PK_BUS")
    private String pkBus;
    /**
     * 业务主键
     */
    @Field(value = "PK_SETTLE")
    private String pkSettle;

    /**
     * 银行卡号
     */
    @Field(value = "CARD_NO")
    private String cardNo;

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getPkExtpay() {
        return pkExtpay;
    }

    public void setPkExtpay(String pkExtpay) {
        this.pkExtpay = pkExtpay;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEuPaytype() {
        return euPaytype;
    }

    public void setEuPaytype(String euPaytype) {
        this.euPaytype = euPaytype;
    }

    public String getDtBank() {
        return dtBank;
    }

    public void setDtBank(String dtBank) {
        this.dtBank = dtBank;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public Date getDateAp() {
        return dateAp;
    }

    public void setDateAp(Date dateAp) {
        this.dateAp = dateAp;
    }

    public String getFlagPay() {
        return flagPay;
    }

    public void setFlagPay(String flagPay) {
        this.flagPay = flagPay;
    }

    public Date getDatePay() {
        return datePay;
    }

    public void setDatePay(Date datePay) {
        this.datePay = datePay;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getDescPay() {
        return descPay;
    }

    public void setDescPay(String descPay) {
        this.descPay = descPay;
    }


    public String getPkDepo() {
        return pkDepo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public void setPkDepo(String pkDepo) {
        this.pkDepo = pkDepo;
    }

    public String getPkDepopi() {
        return pkDepopi;
    }

    public void setPkDepopi(String pkDepopi) {
        this.pkDepopi = pkDepopi;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModityTime() {
        return modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getResultPay() {
        return resultPay;
    }

    public void setResultPay(String resultPay) {
        this.resultPay = resultPay;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public Date getDateRefund() {
        return dateRefund;
    }

    public void setDateRefund(Date dateRefund) {
        this.dateRefund = dateRefund;
    }

    public String getEuBill() {
        return euBill;
    }

    public void setEuBill(String euBill) {
        this.euBill = euBill;
    }

    public String getPkBill() {
        return pkBill;
    }

    public void setPkBill(String pkBill) {
        this.pkBill = pkBill;
    }

    public Date getDateBill() {
        return dateBill;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPkBus() {
        return pkBus;
    }

    public void setPkBus(String pkBus) {
        this.pkBus = pkBus;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getBodyPay() {
        return bodyPay;
    }

    public void setBodyPay(String bodyPay) {
        this.bodyPay = bodyPay;
    }

    public String getCardNo() {  return cardNo; }

    public void setCardNo(String cardNo) { this.cardNo = cardNo; }


}