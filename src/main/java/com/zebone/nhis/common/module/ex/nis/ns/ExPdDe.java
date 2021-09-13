package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: EX_PD_DE - ex_pd_de
 * 
 * @since 2016-10-12 04:29:08
 */
@Table(value = "EX_PD_DE")
public class ExPdDe extends BaseModule {

	@PK
	@Field(value = "PK_PDDE", id = KeyId.UUID)
	private String pkPdde;

	@Field(value = "PK_PDAPDT")
	private String pkPdapdt;

	@Field(value = "CODE_DE")
	private String codeDe;

	/** EU_DIRECT - 对应本次请领单的请退方向：1 请领，-1 请退 */
	@Field(value = "EU_DIRECT")
	private String euDirect;

	@Field(value = "PK_PV")
	private String pkPv;

	@Field(value = "PK_CNORD")
	private String pkCnord;

	@Field(value = "PK_PD")
	private String pkPd;

	@Field(value = "PK_UNIT")
	private String pkUnit;

	@Field(value = "PACK_SIZE")
	private Integer packSize;

	@Field(value = "QUAN_PACK")
	private Double quanPack;

	@Field(value = "QUAN_MIN")
	private Double quanMin;

	@Field(value = "BATCH_NO")
	private String batchNo;

	@Field(value = "PRICE_COST")
	private Double priceCost;

	@Field(value = "PRICE")
	private Double price;

	@Field(value = "AMOUNT")
	private Double amount;

	@Field(value = "PK_PDSTDT")
	private String pkPdstdt;

	@Field(value = "PK_DEPT_DE")
	private String pkDeptDe;

	@Field(value = "PK_DEPT_AP")
	private String pkDeptAp;

	@Field(value = "PK_ORG_AP")
	private String pkOrgAp;

	@Field(value = "PK_STORE_DE")
	private String pkStoreDe;

	@Field(value = "DATE_DE")
	private Date dateDe;

	@Field(value = "PK_EMP_DE")
	private String pkEmpDe;

	@Field(value = "NAME_EMP_DE")
	private String nameEmpDe;

	@Field(value = "FLAG_PRT")
	private String flagPrt;

	@Field(value = "FLAG_PIVAS")
	private String flagPivas;

	@Field(value = "FLAG_BARCODE")
	private String flagBarcode;

	@Field(value = "NOTE")
	private String note;

	@Field(value = "MODITY_TIME")
	private Date modityTime;

	@Field(value = "DATE_EXPIRE")
	private Date dateExpire;
	
	@Field(value = "PK_PDDECATE")
	private String pkPddecate;
	
	@Field(value = "NAME_DECATE")
	private String nameDecate;
	
	@Field(value = "EU_STATUS")
	private String euStatus;
	
	@Field(value = "NAME_EMP_SIGN")
	private String nameEmpSign;
	
	@Field(value = "PK_EMP_SIGN")
	private String pkEmpSign;
	
	@Field(value = "DATE_SIGN")
	private Date dateSign;
	
	@Field(value = "FLAG_SIGN")
	private String flagSign;
	
	@Field(value = "NAME_EMP_DIST")
	private String nameEmpDist;
	
	@Field(value = "PK_EMP_DIST")
	private String pkEmpDist;
	
	@Field(value = "DATE_DIST")
	private Date dateDist;
	
	@Field(value = "FLAG_DIST")
	private String flagDist;
	
	@Field(value="FLAG_SENDTOFA")
	private String flagSendtofa;

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getNameEmpSign() {
		return nameEmpSign;
	}

	public void setNameEmpSign(String nameEmpSign) {
		this.nameEmpSign = nameEmpSign;
	}

	public String getPkEmpSign() {
		return pkEmpSign;
	}

	public void setPkEmpSign(String pkEmpSign) {
		this.pkEmpSign = pkEmpSign;
	}

	public Date getDateSign() {
		return dateSign;
	}

	public void setDateSign(Date dateSign) {
		this.dateSign = dateSign;
	}

	public String getFlagSign() {
		return flagSign;
	}

	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}

	public String getNameEmpDist() {
		return nameEmpDist;
	}

	public void setNameEmpDist(String nameEmpDist) {
		this.nameEmpDist = nameEmpDist;
	}

	public String getPkEmpDist() {
		return pkEmpDist;
	}

	public void setPkEmpDist(String pkEmpDist) {
		this.pkEmpDist = pkEmpDist;
	}

	public Date getDateDist() {
		return dateDist;
	}

	public void setDateDist(Date dateDist) {
		this.dateDist = dateDist;
	}

	public String getFlagDist() {
		return flagDist;
	}

	public void setFlagDist(String flagDist) {
		this.flagDist = flagDist;
	}

	public String getPkPddecate() {
		return pkPddecate;
	}

	public void setPkPddecate(String pkPddecate) {
		this.pkPddecate = pkPddecate;
	}

	public String getNameDecate() {
		return nameDecate;
	}

	public void setNameDecate(String nameDecate) {
		this.nameDecate = nameDecate;
	}

	public String getPkPdde() {

		return this.pkPdde;
	}

	public void setPkPdde(String pkPdde) {

		this.pkPdde = pkPdde;
	}

	public Date getDateExpire() {

		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {

		this.dateExpire = dateExpire;
	}

	public String getPkPdapdt() {

		return this.pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {

		this.pkPdapdt = pkPdapdt;
	}

	public String getCodeDe() {

		return this.codeDe;
	}

	public void setCodeDe(String codeDe) {

		this.codeDe = codeDe;
	}

	public String getEuDirect() {

		return this.euDirect;
	}

	public void setEuDirect(String euDirect) {

		this.euDirect = euDirect;
	}

	public String getPkPv() {

		return this.pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public String getPkCnord() {

		return this.pkCnord;
	}

	public void setPkCnord(String pkCnord) {

		this.pkCnord = pkCnord;
	}

	public String getPkPd() {

		return this.pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public String getPkUnit() {

		return this.pkUnit;
	}

	public void setPkUnit(String pkUnit) {

		this.pkUnit = pkUnit;
	}

	public Integer getPackSize() {

		return this.packSize;
	}

	public void setPackSize(Integer packSize) {

		this.packSize = packSize;
	}

	public Double getQuanPack() {

		return this.quanPack;
	}

	public void setQuanPack(Double quanPack) {

		this.quanPack = quanPack;
	}

	public Double getQuanMin() {

		return this.quanMin;
	}

	public void setQuanMin(Double quanMin) {

		this.quanMin = quanMin;
	}

	public Double getPriceCost() {

		return this.priceCost;
	}

	public void setPriceCost(Double priceCost) {

		this.priceCost = priceCost;
	}

	public Double getPrice() {

		return this.price;
	}

	public void setPrice(Double price) {

		this.price = price;
	}

	public Double getAmount() {

		return this.amount;
	}

	public void setAmount(Double amount) {

		this.amount = amount;
	}

	public String getPkPdstdt() {

		return this.pkPdstdt;
	}

	public void setPkPdstdt(String pkPdstdt) {

		this.pkPdstdt = pkPdstdt;
	}

	public String getPkDeptDe() {

		return this.pkDeptDe;
	}

	public void setPkDeptDe(String pkDeptDe) {

		this.pkDeptDe = pkDeptDe;
	}

	public String getPkStoreDe() {

		return this.pkStoreDe;
	}

	public void setPkStoreDe(String pkStoreDe) {

		this.pkStoreDe = pkStoreDe;
	}

	public Date getDateDe() {

		return this.dateDe;
	}

	public void setDateDe(Date dateDe) {

		this.dateDe = dateDe;
	}

	public String getPkEmpDe() {

		return this.pkEmpDe;
	}

	public void setPkEmpDe(String pkEmpDe) {

		this.pkEmpDe = pkEmpDe;
	}

	public String getNameEmpDe() {

		return this.nameEmpDe;
	}

	public void setNameEmpDe(String nameEmpDe) {

		this.nameEmpDe = nameEmpDe;
	}

	public String getFlagPrt() {

		return this.flagPrt;
	}

	public void setFlagPrt(String flagPrt) {

		this.flagPrt = flagPrt;
	}

	public String getFlagPivas() {

		return this.flagPivas;
	}

	public void setFlagPivas(String flagPivas) {

		this.flagPivas = flagPivas;
	}

	public String getFlagBarcode() {

		return this.flagBarcode;
	}

	public void setFlagBarcode(String flagBarcode) {

		this.flagBarcode = flagBarcode;
	}

	public String getNote() {

		return this.note;
	}

	public void setNote(String note) {

		this.note = note;
	}

	public Date getModityTime() {

		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {

		this.modityTime = modityTime;
	}

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
	}

	public String getPkDeptAp() {

		return pkDeptAp;
	}

	public void setPkDeptAp(String pkDeptAp) {

		this.pkDeptAp = pkDeptAp;
	}

	public String getPkOrgAp() {

		return pkOrgAp;
	}

	public void setPkOrgAp(String pkOrgAp) {

		this.pkOrgAp = pkOrgAp;
	}

	public String getFlagSendtofa() {
		return flagSendtofa;
	}

	public void setFlagSendtofa(String flagSendtofa) {
		this.flagSendtofa = flagSendtofa;
	}

	
}