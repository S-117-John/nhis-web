package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

public class NssBlInvoiceListVo {

    private String pkInvoicedt; //发票明细主键
    private String pkInvoice; //发票主键
    private String pkBill; //账单项目主键
    private String codeBill; //账单项目编码
    private String nameBill; //账单项目名称
    private Double amount;  //金额
    private String creator; //创建人
    private Date createTime; //创建时间
    private String modifier; //修改人
    private String modityTime; //修改时间
    private String delFlag;  //删除标志
    private Date ts; //时间戳
    
	public String getPkInvoicedt() {
		return pkInvoicedt;
	}
	public void setPkInvoicedt(String pkInvoicedt) {
		this.pkInvoicedt = pkInvoicedt;
	}
	public String getPkInvoice() {
		return pkInvoice;
	}
	public void setPkInvoice(String pkInvoice) {
		this.pkInvoice = pkInvoice;
	}
	public String getPkBill() {
		return pkBill;
	}
	public void setPkBill(String pkBill) {
		this.pkBill = pkBill;
	}
	public String getCodeBill() {
		return codeBill;
	}
	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}
	public String getNameBill() {
		return nameBill;
	}
	public void setNameBill(String nameBill) {
		this.nameBill = nameBill;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
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
	public String getModityTime() {
		return modityTime;
	}
	public void setModityTime(String modityTime) {
		this.modityTime = modityTime;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
