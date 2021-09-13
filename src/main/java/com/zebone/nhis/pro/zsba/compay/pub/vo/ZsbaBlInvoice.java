package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "BL_INVOICE")
public class ZsbaBlInvoice extends BaseModule{
	
	@PK
	@Field(value = "PK_INVOICE", id = KeyId.UUID)
	private String pkInvoice;

	@Field(value = "PK_INVCATE")
	private String pkInvcate;

	@Field(value = "PK_EMPINVOICE")
	private String pkEmpinvoice;

	@Field(value = "CODE_INV")
	private String codeInv;

	@Field(value = "DATE_INV")
	private Date dateInv;

	@Field(value = "AMOUNT_INV")
	private Double amountInv;

	@Field(value = "AMOUNT_PI")
	private Double amountPi;

	@Field(value = "NOTE")
	private String note;

	@Field(value = "PK_EMP_INV")
	private String pkEmpInv;

	@Field(value = "NAME_EMP_INV")
	private String nameEmpInv;

	@Field(value = "PRINT_TIMES")
	private Integer printTimes;

	@Field(value = "FLAG_CANCEL")
	private String flagCancel;

	@Field(value = "DATE_CANCEL")
	private Date dateCancel;

	@Field(value = "PK_EMP_CANCEL")
	private String pkEmpCancel;

	@Field(value = "NAME_EMP_CANCEL")
	private String nameEmpCancel;

	@Field(value = "FLAG_CC")
	private String flagCc;

	/** PK_CC - 收费员结账时填入 */
	@Field(value = "PK_CC")
	private String pkCc;

	@Field(value = "FLAG_CC_CANCEL")
	private String flagCcCancel;

	/** PK_CC_CANCEL - 如果发票作废，则在收费员结账时填入 */
	@Field(value = "PK_CC_CANCEL")
	private String pkCcCancel;

	@Field(value = "MODITY_TIME")
	private Date modityTime;
	
	/**纸质票据代码*/
	@Field(value = "BILLBATCHCODE")
	private String billbatchcode;
	
	/**电子票据代码*/
	@Field(value = "EBILLBATCHCODE")
	private String ebillbatchcode;
	
	/**电子票据号码*/
	@Field(value = "EBILLNO")
	private String ebillno;
	
	/**电子票据校验码*/
	@Field(value = "CHECKCODE")
	private String checkcode;
	
	/**电子票据生成时间*/
	@Field(value = "DATE_EBILL")
	private Date dateEbill;
	
	/**电子票据二维码*/
	@Field(value = "QRCODE_EBILL")
	private byte[] qrcodeEbill;
	
	/**电子票据H5页面url*/
	@Field(value = "URL_EBILL")
	private String urlEbill;
	
	/**电子票据外网H5页面url*/
	@Field(value = "URL_NETEBILL")
	private String urlNetebill;
	
	/**红冲电子票据代码*/
	@Field(value = "EBILLBATCHCODE_CANCEL")
	private String ebillbatchcodeCancel;
	
	/**红冲电子票据号码*/
	@Field(value = "EBILLNO_CANCEL")
	private String ebillnoCancel;
	
	/**红冲电子票据校验码*/
	@Field(value = "CHECKCODE_CANCEL")
	private String checkcodeCancel;
	
	/**红冲电子票据生成时间*/
	@Field(value = "DATE_EBILL_CANCEL")
	private Date dateEbillCancel;
	
	/**红冲电子票据二维码*/
	@Field(value = "QRCODE_EBILL_CANCEL")
	private byte[] qrcodeEbillCancel;
	
	/**红冲电子票据H5页面url*/
	@Field(value = "URL_EBILL_CANCEL")
	private String urlEbillCancel;
	
	/**红冲电子票据外网H5页面url*/
	@Field(value = "URL_NETEBILL_CANCEL")
	private String urlNetebillCancel;
	
	/**电子发票开立人员*/
	@Field(value = "PK_EMP_EBILL")
	private String pkEmpEbill;
	
	/**电子发票开立人员名称*/
	@Field(value = "NAME_EMP_EBILL")
	private String nameEmpEbill;
	
	/**电子发票结账标志*/
	@Field(value = "FLAG_CC_EBILL")
	private String flagCcEbill;
	
	/**电子发票结账主键*/
	@Field(value = "PK_CC_EBILL")
	private String pkCcEbill;
	
	/**电子发票作废标志*/
	@Field(value = "FLAG_CANCEL_EBILL")
	private String flagCancelEbill;
	
	/**电子发票作废人员*/
	@Field(value = "PK_EMP_CANCEL_EBILL")
	private String pkEmpCancelEbill;
	
	/**电子发票作废人员名称*/
	@Field(value = "NAME_EMP_CANCEL_EBILL")
	private String nameEmpCancelEbill;
	
	/**电子发票作废结账标志*/
	@Field(value = "FLAG_CC_CANCEL_EBILL")
	private String flagCcCancelEbill;
	
	/**电子发票作废结账主键*/
	@Field(value = "PK_CC_CANCEL_EBILL")
	private String pkCcCancelEbill;
	
	@Field(value = "CODE_SN")
	private String codeSn;
	
	private List<BlInvoiceDt> invDt;
	
	@Field(value = "FLAG_REPRINT")
	private String flagReprint;
	
	@Field(value = "FLAG_BACK")
	private String flagBack;
	
	public List<BlInvoiceDt> getInvDt() {
		return invDt;
	}

	public void setInvDt(List<BlInvoiceDt> invDt) {
		this.invDt = invDt;
	}

	/** 最小号 */
	private String minCodeInv;
	
	/** 最大号 */
	private String maxCodeInv;
	
	/** 数量 */
	private Integer cnt;

	public String getPkInvoice() {

		return this.pkInvoice;
	}

	public void setPkInvoice(String pkInvoice) {

		this.pkInvoice = pkInvoice;
	}

	public String getPkInvcate() {

		return this.pkInvcate;
	}

	public void setPkInvcate(String pkInvcate) {

		this.pkInvcate = pkInvcate;
	}

	public String getPkEmpinvoice() {

		return this.pkEmpinvoice;
	}

	public void setPkEmpinvoice(String pkEmpinvoice) {

		this.pkEmpinvoice = pkEmpinvoice;
	}

	public String getCodeInv() {

		return this.codeInv;
	}

	public void setCodeInv(String codeInv) {

		this.codeInv = codeInv;
	}

	public Date getDateInv() {

		return this.dateInv;
	}

	public void setDateInv(Date dateInv) {

		this.dateInv = dateInv;
	}

	public Double getAmountInv() {

		return this.amountInv;
	}

	public void setAmountInv(Double amountInv) {

		this.amountInv = amountInv;
	}

	public Double getAmountPi() {

		return this.amountPi;
	}

	public void setAmountPi(Double amountPi) {

		this.amountPi = amountPi;
	}

	public String getNote() {

		return this.note;
	}

	public void setNote(String note) {

		this.note = note;
	}

	public String getPkEmpInv() {

		return this.pkEmpInv;
	}

	public void setPkEmpInv(String pkEmpInv) {

		this.pkEmpInv = pkEmpInv;
	}

	public String getNameEmpInv() {

		return this.nameEmpInv;
	}

	public void setNameEmpInv(String nameEmpInv) {

		this.nameEmpInv = nameEmpInv;
	}

	public Integer getPrintTimes() {

		return this.printTimes;
	}

	public void setPrintTimes(Integer printTimes) {

		this.printTimes = printTimes;
	}

	public String getFlagCancel() {

		return this.flagCancel;
	}

	public void setFlagCancel(String flagCancel) {

		this.flagCancel = flagCancel;
	}

	public Date getDateCancel() {

		return this.dateCancel;
	}

	public void setDateCancel(Date dateCancel) {

		this.dateCancel = dateCancel;
	}

	public String getPkEmpCancel() {

		return this.pkEmpCancel;
	}

	public void setPkEmpCancel(String pkEmpCancel) {

		this.pkEmpCancel = pkEmpCancel;
	}

	public String getNameEmpCancel() {

		return this.nameEmpCancel;
	}

	public void setNameEmpCancel(String nameEmpCancel) {

		this.nameEmpCancel = nameEmpCancel;
	}

	public String getFlagCc() {

		return this.flagCc;
	}

	public void setFlagCc(String flagCc) {

		this.flagCc = flagCc;
	}

	public String getPkCc() {

		return this.pkCc;
	}

	public void setPkCc(String pkCc) {

		this.pkCc = pkCc;
	}

	public String getFlagCcCancel() {

		return this.flagCcCancel;
	}

	public void setFlagCcCancel(String flagCcCancel) {

		this.flagCcCancel = flagCcCancel;
	}

	public String getPkCcCancel() {

		return this.pkCcCancel;
	}

	public void setPkCcCancel(String pkCcCancel) {

		this.pkCcCancel = pkCcCancel;
	}

	public Date getModityTime() {

		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {

		this.modityTime = modityTime;
	}

	public String getMinCodeInv() {
		return minCodeInv;
	}

	public void setMinCodeInv(String minCodeInv) {
		this.minCodeInv = minCodeInv;
	}

	public String getMaxCodeInv() {
		return maxCodeInv;
	}

	public void setMaxCodeInv(String maxCodeInv) {
		this.maxCodeInv = maxCodeInv;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public String getBillbatchcode() {
		return billbatchcode;
	}

	public void setBillbatchcode(String billbatchcode) {
		this.billbatchcode = billbatchcode;
	}

	public String getEbillbatchcode() {
		return ebillbatchcode;
	}

	public void setEbillbatchcode(String ebillbatchcode) {
		this.ebillbatchcode = ebillbatchcode;
	}

	public String getEbillno() {
		return ebillno;
	}

	public void setEbillno(String ebillno) {
		this.ebillno = ebillno;
	}

	public String getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public Date getDateEbill() {
		return dateEbill;
	}

	public void setDateEbill(Date dateEbill) {
		this.dateEbill = dateEbill;
	}
	
	public byte[] getQrcodeEbill() {
		return qrcodeEbill;
	}

	public void setQrcodeEbill(byte[] qrcodeEbill) {
		this.qrcodeEbill = qrcodeEbill;
	}

	public String getUrlEbill() {
		return urlEbill;
	}

	public void setUrlEbill(String urlEbill) {
		this.urlEbill = urlEbill;
	}

	public String getUrlNetebill() {
		return urlNetebill;
	}

	public void setUrlNetebill(String urlNetebill) {
		this.urlNetebill = urlNetebill;
	}

	public String getEbillbatchcodeCancel() {
		return ebillbatchcodeCancel;
	}

	public void setEbillbatchcodeCancel(String ebillbatchcodeCancel) {
		this.ebillbatchcodeCancel = ebillbatchcodeCancel;
	}

	public String getEbillnoCancel() {
		return ebillnoCancel;
	}

	public void setEbillnoCancel(String ebillnoCancel) {
		this.ebillnoCancel = ebillnoCancel;
	}

	public String getCheckcodeCancel() {
		return checkcodeCancel;
	}

	public void setCheckcodeCancel(String checkcodeCancel) {
		this.checkcodeCancel = checkcodeCancel;
	}

	public Date getDateEbillCancel() {
		return dateEbillCancel;
	}

	public void setDateEbillCancel(Date dateEbillCancel) {
		this.dateEbillCancel = dateEbillCancel;
	}
	public byte[] getQrcodeEbillCancel() {
		return qrcodeEbillCancel;
	}

	public void setQrcodeEbillCancel(byte[] qrcodeEbillCancel) {
		this.qrcodeEbillCancel = qrcodeEbillCancel;
	}

	public String getUrlEbillCancel() {
		return urlEbillCancel;
	}

	public void setUrlEbillCancel(String urlEbillCancel) {
		this.urlEbillCancel = urlEbillCancel;
	}

	public String getUrlNetebillCancel() {
		return urlNetebillCancel;
	}

	public void setUrlNetebillCancel(String urlNetebillCancel) {
		this.urlNetebillCancel = urlNetebillCancel;
	}

	public String getPkEmpEbill() {
		return pkEmpEbill;
	}

	public void setPkEmpEbill(String pkEmpEbill) {
		this.pkEmpEbill = pkEmpEbill;
	}

	public String getNameEmpEbill() {
		return nameEmpEbill;
	}

	public void setNameEmpEbill(String nameEmpEbill) {
		this.nameEmpEbill = nameEmpEbill;
	}

	public String getFlagCcEbill() {
		return flagCcEbill;
	}

	public void setFlagCcEbill(String flagCcEbill) {
		this.flagCcEbill = flagCcEbill;
	}

	public String getPkCcEbill() {
		return pkCcEbill;
	}

	public void setPkCcEbill(String pkCcEbill) {
		this.pkCcEbill = pkCcEbill;
	}

	public String getFlagCancelEbill() {
		return flagCancelEbill;
	}

	public void setFlagCancelEbill(String flagCancelEbill) {
		this.flagCancelEbill = flagCancelEbill;
	}

	public String getPkEmpCancelEbill() {
		return pkEmpCancelEbill;
	}

	public void setPkEmpCancelEbill(String pkEmpCancelEbill) {
		this.pkEmpCancelEbill = pkEmpCancelEbill;
	}

	public String getNameEmpCancelEbill() {
		return nameEmpCancelEbill;
	}

	public void setNameEmpCancelEbill(String nameEmpCancelEbill) {
		this.nameEmpCancelEbill = nameEmpCancelEbill;
	}

	public String getFlagCcCancelEbill() {
		return flagCcCancelEbill;
	}

	public void setFlagCcCancelEbill(String flagCcCancelEbill) {
		this.flagCcCancelEbill = flagCcCancelEbill;
	}

	public String getPkCcCancelEbill() {
		return pkCcCancelEbill;
	}

	public void setPkCcCancelEbill(String pkCcCancelEbill) {
		this.pkCcCancelEbill = pkCcCancelEbill;
	}

	public String getCodeSn() {
		return codeSn;
	}

	public void setCodeSn(String codeSn) {
		this.codeSn = codeSn;
	}

	public String getFlagReprint() {
		return flagReprint;
	}

	public void setFlagReprint(String flagReprint) {
		this.flagReprint = flagReprint;
	}

	public String getFlagBack() {
		return flagBack;
	}

	public void setFlagBack(String flagBack) {
		this.flagBack = flagBack;
	}
	
	
}
