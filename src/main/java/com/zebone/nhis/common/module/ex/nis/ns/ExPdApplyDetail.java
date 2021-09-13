package com.zebone.nhis.common.module.ex.nis.ns;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * Table: EX_PD_APPLY_DETAIL - ex_pd_apply_detail
 * 
 * @since 2016-10-26 03:26:33
 */
@Table(value = "EX_PD_APPLY_DETAIL")
public class ExPdApplyDetail extends BaseModule {

	@PK
	@Field(value = "PK_PDAPDT", id = KeyId.UUID)
	private String pkPdapdt;

	@Field(value = "PK_PDAP")
	private String pkPdap;

	/** EU_DIRECT - 1 请领，-1 请退 */
	@Field(value = "EU_DIRECT")
	private String euDirect;

	@Field(value = "PK_PV")
	private String pkPv;

	@Field(value = "PK_PRES")
	private String pkPres;

	@Field(value = "PK_CNORD")
	private String pkCnord;

	/** EU_DETYPE - 0 药房发，1 病区发，2 不发，9外部接口 */
	@Field(value = "EU_DETYPE")
	private String euDetype;

	@Field(value = "FLAG_BASE")
	private String flagBase;

	@Field(value = "FLAG_MEDOUT")
	private String flagMedout;

	@Field(value = "FLAG_SELF")
	private String flagSelf;

	@Field(value = "PK_PD")
	private String pkPd;

	@Field(value = "BATCH_NO")
	private String batchNo;

	@Field(value = "PK_UNIT")
	private String pkUnit;

	@Field(value = "PACK_SIZE")
	private Integer packSize;

	@Field(value = "QUAN_PACK")
	private Double quanPack;

	@Field(value = "QUAN_MIN")
	private Double quanMin;

	/** ORDS - 冗余 仅在中药开立时有效，表示中药的付数。默认为1 */
	@Field(value = "ORDS")
	private Integer ords;

	@Field(value = "PRICE_COST")
	private Double priceCost;

	@Field(value = "PRICE")
	private Double price;

	@Field(value = "AMOUNT")
	private Double amount;

	@Field(value = "FLAG_DE")
	private String flagDe;

	@Field(value = "FLAG_FINISH")
	private String flagFinish;

	@Field(value = "FLAG_STOP")
	private String flagStop;

	@Field(value = "REASON_STOP")
	private String reasonStop;

	@Field(value = "PK_EMP_STOP")
	private String pkEmpStop;

	@Field(value = "NAME_EMP_STOP")
	private String nameEmpStop;

	/** PK_CGIP - 用于退费 */
	@Field(value = "PK_CGIP")
	private String pkCgip;

	@Field(value = "MODITY_TIME")
	private Date modityTime;

	@Field(value = "DATE_EXPIRE")
	private Date dateExpire;
	
	@Field(value = "FLAG_EMER")
	private String flagEmer;
	
	@Field(value = "FLAG_PIVAS")
	private String flagPivas;
	
	@Field(value = "PK_EMP_BACK")
	private String pkEmpBack;
	
	@Field(value = "NAME_EMP_BACK")
	private String nameEmpBack;
	
	@Field(value = "DATE_BACK")
	private Date dateBack;
	
	@Field(value = "NOTE")
	private String note;
	
	@Field(value = "FLAG_CANC")
	private String flagCanc;

	@Field(value = "DATE_OCC")
	private Date dateOcc;

	@Field(value = "NOTE_BACK")
	private String noteBack;

	public String getNoteBack() {
		return noteBack;
	}

	public void setNoteBack(String noteBack) {
		this.noteBack = noteBack;
	}

	public Date getDateOcc() {
		return dateOcc;
	}

	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}

	// 申请机构和申请部门,用于前后台交互传值
	private String pkDeptAp;

	private String pkOrgAp;

	// 物品包装量
	private Integer pdPackSize;

	// 临时添加，实际发放数量（前台手输入的数量）
	private Double actualQuanPack;

	// 药品的取整模式
	private String euMuputype;
	
	//静配瓶签排批数量
	private Double pivasQuanPack;
	
	//静配瓶签排批数量
	private Double pivasQuanMin;
	
	//发放分类主键
	private String pkPddecate;
	
	//发放分类名称
	private String nameDecate;
	
	//发放分类编码
	private String codeDecate;

   //用于查询发药的价格批次
	private String pkPdapdtBack;
	//处方名称
	private String presName;

	public String getPkPdapdtBack() {
		return pkPdapdtBack;
	}

	public void setPkPdapdtBack(String pkPdapdtBack) {
		this.pkPdapdtBack = pkPdapdtBack;
	}

	public String getFlagPivas() {
		return flagPivas;
	}

	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}

	public String getPresName() {
		return presName;
	}

	public void setPresName(String presName) {
		this.presName = presName;
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

	public String getCodeDecate() {
		return codeDecate;
	}

	public void setCodeDecate(String codeDecate) {
		this.codeDecate = codeDecate;
	}

	public String getEuMuputype() {

		return euMuputype;
	}

	public void setEuMuputype(String euMuputype) {

		this.euMuputype = euMuputype;
	}

	public Integer getPdPackSize() {

		return pdPackSize;
	}

	public void setPdPackSize(Integer pdPackSize) {

		this.pdPackSize = pdPackSize;
	}

	public Double getActualQuanPack() {

		return actualQuanPack;
	}

	public void setActualQuanPack(Double actualQuanPack) {

		this.actualQuanPack = actualQuanPack;
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

	public String getPkPdap() {

		return this.pkPdap;
	}

	public void setPkPdap(String pkPdap) {

		this.pkPdap = pkPdap;
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

	public String getPkPres() {

		return this.pkPres;
	}

	public void setPkPres(String pkPres) {

		this.pkPres = pkPres;
	}

	public String getPkCnord() {

		return this.pkCnord;
	}

	public void setPkCnord(String pkCnord) {

		this.pkCnord = pkCnord;
	}

	public String getEuDetype() {

		return this.euDetype;
	}

	public void setEuDetype(String euDetype) {

		this.euDetype = euDetype;
	}

	public String getFlagBase() {

		return this.flagBase;
	}

	public void setFlagBase(String flagBase) {

		this.flagBase = flagBase;
	}

	public String getFlagMedout() {

		return this.flagMedout;
	}

	public void setFlagMedout(String flagMedout) {

		this.flagMedout = flagMedout;
	}

	public String getFlagSelf() {

		return this.flagSelf;
	}

	public void setFlagSelf(String flagSelf) {

		this.flagSelf = flagSelf;
	}

	public String getPkPd() {

		return this.pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public String getBatchNo() {

		return this.batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
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

	public Integer getOrds() {

		return this.ords;
	}

	public void setOrds(Integer ords) {

		this.ords = ords;
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

	public String getFlagDe() {

		return this.flagDe;
	}

	public void setFlagDe(String flagDe) {

		this.flagDe = flagDe;
	}

	public String getFlagFinish() {

		return this.flagFinish;
	}

	public void setFlagFinish(String flagFinish) {

		this.flagFinish = flagFinish;
	}

	public String getFlagStop() {

		return this.flagStop;
	}

	public void setFlagStop(String flagStop) {

		this.flagStop = flagStop;
	}

	public String getReasonStop() {

		return this.reasonStop;
	}

	public void setReasonStop(String reasonStop) {

		this.reasonStop = reasonStop;
	}

	public String getPkEmpStop() {

		return this.pkEmpStop;
	}

	public void setPkEmpStop(String pkEmpStop) {

		this.pkEmpStop = pkEmpStop;
	}

	public String getNameEmpStop() {

		return this.nameEmpStop;
	}

	public void setNameEmpStop(String nameEmpStop) {

		this.nameEmpStop = nameEmpStop;
	}

	public String getPkCgip() {

		return this.pkCgip;
	}

	public void setPkCgip(String pkCgip) {

		this.pkCgip = pkCgip;
	}

	public Date getModityTime() {

		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {

		this.modityTime = modityTime;
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

	public Double getPivasQuanPack() {
		return pivasQuanPack;
	}

	public void setPivasQuanPack(Double pivasQuanPack) {
		this.pivasQuanPack = pivasQuanPack;
	}

	public Double getPivasQuanMin() {
		return pivasQuanMin;
	}

	public void setPivasQuanMin(Double pivasQuanMin) {
		this.pivasQuanMin = pivasQuanMin;
	}

	public String getFlagEmer() {
		return flagEmer;
	}

	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}

	public String getPkEmpBack() {
		return pkEmpBack;
	}

	public void setPkEmpBack(String pkEmpBack) {
		this.pkEmpBack = pkEmpBack;
	}

	public String getNameEmpBack() {
		return nameEmpBack;
	}

	public void setNameEmpBack(String nameEmpBack) {
		this.nameEmpBack = nameEmpBack;
	}

	public Date getDateBack() {
		return dateBack;
	}

	public void setDateBack(Date dateBack) {
		this.dateBack = dateBack;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagCanc() {
		return flagCanc;
	}

	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}

}