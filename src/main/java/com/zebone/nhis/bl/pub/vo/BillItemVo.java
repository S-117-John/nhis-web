package com.zebone.nhis.bl.pub.vo;

/**
 * 账单码vo
 * 
 * @author yangxue
 * 
 */
public class BillItemVo {
	private String pkItem;
	private String flagPd;
	private String codeBill;
	private String billType;//bill:发票;acc:账单

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getFlagPd() {
		return flagPd;
	}

	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}

	public String getCodeBill() {
		return codeBill;
	}

	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}
	

}
