package com.zebone.nhis.pro.zsba.mz.opcg.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlInvoiceDt;

/**
 * 前台传递 多张发票信息
 * @author Administrator
 */
public class InvoiceInfo {

	private String inVoiceNo; // 发票号
	private String pkEmpinvoice; // 票据领用主键
    private String pkInvcate; // 票据分类主键
    private String codeInv; // 发票号马
    private String dateInv; // 发票日期 yyyyMMddHHmmss格式
    private String pkempInv; // 发票开立人员
    private String nameEmpInv; // 发票开立人员名称
    private String machineName;//本地计算机名称
	private List<BlInvoiceDt> blInDts;// 发票明细
	
	private String pkSettleOld;//取消结算主键(灵璧部分退费时用，其他项目可不传)
	
	private String flagPrint;//是否打印纸质票据(电子票据使用)
	
    public String getFlagPrint() {
		return flagPrint;
	}
	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}
	public String getPkSettleOld() {
		return pkSettleOld;
	}
	public void setPkSettleOld(String pkSettleOld) {
		this.pkSettleOld = pkSettleOld;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getInVoiceNo() {
		return inVoiceNo;
	}
	public void setInVoiceNo(String inVoiceNo) {
		this.inVoiceNo = inVoiceNo;
	}
	public String getPkEmpinvoice() {
		return pkEmpinvoice;
	}
	public void setPkEmpinvoice(String pkEmpinvoice) {
		this.pkEmpinvoice = pkEmpinvoice;
	}
	public String getPkInvcate() {
		return pkInvcate;
	}
	public void setPkInvcate(String pkInvcate) {
		this.pkInvcate = pkInvcate;
	}
	public String getCodeInv() {
		return codeInv;
	}
	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
	public String getDateInv() {
		return dateInv;
	}
	public void setDateInv(String dateInv) {
		this.dateInv = dateInv;
	}
	public String getPkempInv() {
		return pkempInv;
	}
	public void setPkempInv(String pkempInv) {
		this.pkempInv = pkempInv;
	}
	public String getNameEmpInv() {
		return nameEmpInv;
	}
	public void setNameEmpInv(String nameEmpInv) {
		this.nameEmpInv = nameEmpInv;
	}
	public List<BlInvoiceDt> getBlInDts() {
		return blInDts;
	}
	public void setBlInDts(List<BlInvoiceDt> blInDts) {
		this.blInDts = blInDts;
	}

    
}
