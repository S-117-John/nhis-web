package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BL_DEPOSIT_CANCEL
 *
 * @since 2018-04-03 11:24:42
 */
@Table(value="BL_DEPOSIT_CANCEL")
public class BlDepositCancel extends BaseModule {
	
	/** PK_DEPOSIT_CANCEL - 作废预交金票据记录主键 */
	@PK
	@Field(value="PK_DEPOSIT_CANCEL",id=KeyId.UUID)
    private String pkDepositCancel;
	
	/** PK_DEPO - 交款记录主键 */
	@Field(value="PK_DEPO")
    private String pkDepo;
	
	/** PK_EMPINVOICE - 票据领用主键 */
	@Field(value="PK_EMPINVOICE")
    private String pkEmpinvoice;
	
	/** CODE_BILL - 票据号 */
	@Field(value="CODE_BILL")
    private String codeBill;
	
	/** DATE_BILL - 票据日期 */
	@Field(value="DATE_BILL")
    private Date dateBill;
	
	/** AMOUNT_BILL - 票据金额 */
	@Field(value="AMOUNT_BILL")
    private Double amountBill;

	/** NOTE - 描述 */
	@Field(value="NOTE")
    private String note;
	
	/** PK_EMP_BILL - 票据开立人员 */
	@Field(value="PK_EMP_BILL")
    private String pkEmpBill;
	
	/** NAME_EMP_BILL - 票据开立人员名称 */
	@Field(value="NAME_EMP_BILL")
    private String nameEmpBill;
	
	/** DATE_CANCEL - 票据作废日期 */
	@Field(value="DATE_CANCEL")
    private Date dateCancel;
	
	/** PK_EMP_CANCEL - 票据作废操作人员 */
	@Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;
	
	/** NAME_EMP_CANCEL - 票据作废操作人员名称 */
	@Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;
	
	/** MODIFY_TIME - 修改时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	public String getPkDepositCancel() {
		return pkDepositCancel;
	}

	public void setPkDepositCancel(String pkDepositCancel) {
		this.pkDepositCancel = pkDepositCancel;
	}

	public String getPkDepo() {
		return pkDepo;
	}

	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}

	public String getPkEmpinvoice() {
		return pkEmpinvoice;
	}

	public void setPkEmpinvoice(String pkEmpinvoice) {
		this.pkEmpinvoice = pkEmpinvoice;
	}

	public String getCodeBill() {
		return codeBill;
	}

	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}

	public Date getDateBill() {
		return dateBill;
	}

	public void setDateBill(Date dateBill) {
		this.dateBill = dateBill;
	}

	public Double getAmountBill() {
		return amountBill;
	}

	public void setAmountBill(Double amountBill) {
		this.amountBill = amountBill;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkEmpBill() {
		return pkEmpBill;
	}

	public void setPkEmpBill(String pkEmpBill) {
		this.pkEmpBill = pkEmpBill;
	}

	public String getNameEmpBill() {
		return nameEmpBill;
	}

	public void setNameEmpBill(String nameEmpBill) {
		this.nameEmpBill = nameEmpBill;
	}

	public Date getDateCancel() {
		return dateCancel;
	}

	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}

	public String getPkEmpCancel() {
		return pkEmpCancel;
	}

	public void setPkEmpCancel(String pkEmpCancel) {
		this.pkEmpCancel = pkEmpCancel;
	}

	public String getNameEmpCancel() {
		return nameEmpCancel;
	}

	public void setNameEmpCancel(String nameEmpCancel) {
		this.nameEmpCancel = nameEmpCancel;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
