package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlSettle;

/**
 * 结算记录VO
 * @author chengjia
 *
 */
public class RhipBlSettleVo extends BlSettle{
	private String hpCode;
	private String hpName;
	private String billName;
	private String invCodes;
	private String quan;
	private String amount;
	private String codeSt;
	private String codeEmp;
	private String codeEmpSt;
	
	private List<RhipBlSettleItemVo> items;

	
	
	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getInvCodes() {
		return invCodes;
	}

	public void setInvCodes(String invCodes) {
		this.invCodes = invCodes;
	}

	public List<RhipBlSettleItemVo> getItems() {
		return items;
	}

	public void setItems(List<RhipBlSettleItemVo> items) {
		this.items = items;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getCodeEmpSt() {
		return codeEmpSt;
	}

	public void setCodeEmpSt(String codeEmpSt) {
		this.codeEmpSt = codeEmpSt;
	}

 
	
}
