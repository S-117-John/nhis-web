package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;

/**
 * 发票明细VO
 * @author chengjia
 *
 */
public class RhipBlInvoice{
	
	private List<BlInvoiceDt> items;
	private BlSettle bs;
	private BlInvoice inv;
	private String hpCode;
	private String hpName;
	private String invCodes;
	
	public List<BlInvoiceDt> getItems() {
		return items;
	}
	public void setItems(List<BlInvoiceDt> items) {
		this.items = items;
	}
	public BlSettle getBs() {
		return bs;
	}
	public void setBs(BlSettle bs) {
		this.bs = bs;
	}
	public BlInvoice getInv() {
		return inv;
	}
	public void setInv(BlInvoice inv) {
		this.inv = inv;
	}
	public String getHpCode() {
		return hpCode;
	}
	public void setHpCode(String hpCode) {
		this.hpCode = hpCode;
	}
	public String getHpName() {
		return hpName;
	}
	public void setHpName(String hpName) {
		this.hpName = hpName;
	}
	public String getInvCodes() {
		return invCodes;
	}
	public void setInvCodes(String invCodes) {
		this.invCodes = invCodes;
	}
		
}
