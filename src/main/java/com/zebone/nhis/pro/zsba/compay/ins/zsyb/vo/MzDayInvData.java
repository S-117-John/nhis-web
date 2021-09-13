package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

/**
 * 门诊日间手术发票费用
 * @author zrj
 *
 */
public class MzDayInvData {
	
	//上次明细所需数据
	private String mzId; //门诊id
	private String ledgerSn; //门诊结账次数
	private String name; //姓名
	private String zyPatientId; //住院ID号
	private String confirmInpatientNo; //住院号
	private String zyAdmissTimes; //住院次数
	private String cashDate; //缴费时间
	private String chargeTotal; //缴费金额
	private String receiptNo; //发票号
	private String receiptSn; //发票流水号
	private String isImport; //是否已导入 1是0否
	private String pkSettleOp; //新系统门诊结算id
	public String getMzId() {
		return mzId;
	}
	public void setMzId(String mzId) {
		this.mzId = mzId;
	}
	public String getLedgerSn() {
		return ledgerSn;
	}
	public void setLedgerSn(String ledgerSn) {
		this.ledgerSn = ledgerSn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZyPatientId() {
		return zyPatientId;
	}
	public void setZyPatientId(String zyPatientId) {
		this.zyPatientId = zyPatientId;
	}
	public String getConfirmInpatientNo() {
		return confirmInpatientNo;
	}
	public void setConfirmInpatientNo(String confirmInpatientNo) {
		this.confirmInpatientNo = confirmInpatientNo;
	}
	public String getZyAdmissTimes() {
		return zyAdmissTimes;
	}
	public void setZyAdmissTimes(String zyAdmissTimes) {
		this.zyAdmissTimes = zyAdmissTimes;
	}
	public String getCashDate() {
		return cashDate;
	}
	public void setCashDate(String cashDate) {
		this.cashDate = cashDate;
	}
	public String getChargeTotal() {
		return chargeTotal;
	}
	public void setChargeTotal(String chargeTotal) {
		this.chargeTotal = chargeTotal;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getReceiptSn() {
		return receiptSn;
	}
	public void setReceiptSn(String receiptSn) {
		this.receiptSn = receiptSn;
	}
	public String getIsImport() {
		return isImport;
	}
	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}
	public String getPkSettleOp() {
		return pkSettleOp;
	}
	public void setPkSettleOp(String pkSettleOp) {
		this.pkSettleOp = pkSettleOp;
	}

	
}
