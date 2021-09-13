package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_PAY - pd_pay 
 *
 * @since 2016-11-28 11:18:54
 */
@Table(value="PD_PAY")
public class PdPay extends BaseModule  {

	@PK
	@Field(value="PK_PDPAY",id=KeyId.UUID)
    private String pkPdpay;

	@Field(value="PAY_NO")
    private String payNo;

	@Field(value="PK_SUPPLYER")
    private String pkSupplyer;

	@Field(value="DT_PAYMODE")
    private String dtPaymode;

	@Field(value="DT_BANK")
    private String dtBank;

	@Field(value="BANK_NO")
    private String bankNo;

	@Field(value="CERT_NO")
    private String certNo;

	@Field(value="AMOUNT_PAY")
    private Double amountPay;

	@Field(value="DATE_PAY")
    private Date datePay;

	@Field(value="PK_EMP_PAY")
    private String pkEmpPay;

	@Field(value="NAME_EMP_PAY")
    private String nameEmpPay;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdpay(){
        return this.pkPdpay;
    }
    public void setPkPdpay(String pkPdpay){
        this.pkPdpay = pkPdpay;
    }

    public String getPayNo(){
        return this.payNo;
    }
    public void setPayNo(String payNo){
        this.payNo = payNo;
    }

    public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
    }

    public String getDtPaymode(){
        return this.dtPaymode;
    }
    public void setDtPaymode(String dtPaymode){
        this.dtPaymode = dtPaymode;
    }

    public String getDtBank(){
        return this.dtBank;
    }
    public void setDtBank(String dtBank){
        this.dtBank = dtBank;
    }

    public String getBankNo(){
        return this.bankNo;
    }
    public void setBankNo(String bankNo){
        this.bankNo = bankNo;
    }

    public String getCertNo(){
        return this.certNo;
    }
    public void setCertNo(String certNo){
        this.certNo = certNo;
    }

    public Double getAmountPay(){
        return this.amountPay;
    }
    public void setAmountPay(Double amountPay){
        this.amountPay = amountPay;
    }

    public Date getDatePay(){
        return this.datePay;
    }
    public void setDatePay(Date datePay){
        this.datePay = datePay;
    }

    public String getPkEmpPay(){
        return this.pkEmpPay;
    }
    public void setPkEmpPay(String pkEmpPay){
        this.pkEmpPay = pkEmpPay;
    }

    public String getNameEmpPay(){
        return this.nameEmpPay;
    }
    public void setNameEmpPay(String nameEmpPay){
        this.nameEmpPay = nameEmpPay;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}