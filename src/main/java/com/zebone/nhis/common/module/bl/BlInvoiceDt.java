package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_INVOICE_DT - bl_invoice_dt 
 *
 * @since 2016-10-11 09:08:42
 */
@Table(value="BL_INVOICE_DT")
public class BlInvoiceDt {

	@PK
	@Field(value="PK_INVOICEDT",id=KeyId.UUID)
    private String pkInvoicedt;

	@Field(value="PK_INVOICE")
    private String pkInvoice;

	@Field(value="PK_PAYER")
    private String pkPayer;

    /** PK_BILL - 对应bd_invcateitem.pk_invcateitem */
	@Field(value="PK_BILL")
    private String pkBill;

    /** CODE_BILL - 对应bd_invcateitem.bill_code */
	@Field(value="CODE_BILL")
    private String codeBill;

	@Field(value="NAME_BILL")
    private String nameBill;

	@Field(value="AMOUNT")
    private Double amount;
	
	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInvoicedt(){
        return this.pkInvoicedt;
    }
    public void setPkInvoicedt(String pkInvoicedt){
        this.pkInvoicedt = pkInvoicedt;
    }
    
    public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkInvoice(){
        return this.pkInvoice;
    }
    public void setPkInvoice(String pkInvoice){
        this.pkInvoice = pkInvoice;
    }

    public String getPkPayer(){
        return this.pkPayer;
    }
    public void setPkPayer(String pkPayer){
        this.pkPayer = pkPayer;
    }

    public String getPkBill(){
        return this.pkBill;
    }
    public void setPkBill(String pkBill){
        this.pkBill = pkBill;
    }

    public String getCodeBill(){
        return this.codeBill;
    }
    public void setCodeBill(String codeBill){
        this.codeBill = codeBill;
    }

    public String getNameBill(){
        return this.nameBill;
    }
    public void setNameBill(String nameBill){
        this.nameBill = nameBill;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}