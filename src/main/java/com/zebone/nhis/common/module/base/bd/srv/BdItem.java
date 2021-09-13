package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 * Table: BD_ITEM - bd_item
 * 
 * @since 2016-09-09 09:35:40
 */
@Table(value = "BD_ITEM")
public class BdItem {

	@PK
	@Field(value = "PK_ITEM", id = KeyId.UUID)
	private String pkItem;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field(value = "NAME_PRT")
	private String namePrt;

	@Field(value = "SPCODE")
	private String spcode;

	@Field(value = "D_CODE")
	private String dCode;

	@Field(value = "PK_UNIT")
	private String pkUnit;
	
	//单位名称
	private String pkUnitName;

	@Field(value = "SPEC")
	private String spec;

	@Field(value = "PRICE")
	private double price;

	@Field(value = "FLAG_SET")
	private String flagSet;

	@Field(value = "FLAG_PD")
	private String flagPd;

	/**
	 * 启用标志
	 */
	@Field(value = "FLAG_ACTIVE")
	private String flagActive;

	/**
	 * EU_PRICEMODE - 0 本服务定价; 1 服务套成员合计价; 2 服务套成员项目数量定价; 3 服务套成员项目数量加收; 4
	 * 对应物品价格; 5 体检包总价模式
	 */
	@Field(value = "EU_PRICEMODE")
	private String euPricemode;

	@Field(value = "PK_ITEMCATE")
	private String pkItemcate;
	
	//服务分类名称
	private String pkItemcateName;

	@Field(value = "DT_CHCATE")
	private String dtChcate;
	
	//病案分类名称
	private String dtChcateName;

	@Field(value = "NOTE")
	private String note;
	
	@Field(value = "DESC_ITEM")
	private String descItem;
	
	@Field(value = "EXCEPT_ITEM")
	private String exceptItem;

	@Field(value = "DESC_PRICE")
	private String descPrice;
	
	@Field(value = "CODE_HP")
	private String codeHp;
	
	@Field(value = "CODE_STD")
	private String codeStd;
	
	@Field(value="PK_FACTORY")
	private String pkFactory;

	@Field(value="OLD_ID")
	private String oldId;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;

	@Field(value = "MODITY_TIME", date = FieldType.INSERT)
	private Date modityTime;
	/**
	 * 创建人
	 */
	@Field(userfield = "pkEmp", userfieldscop = FieldType.INSERT)
	public String creator;

	/**
	 * 创建时间
	 */
	@Field(value = "create_time", date = FieldType.INSERT)
	public Date createTime;

	/**
	 * 时间戳
	 */
	@Field(date = FieldType.ALL)
	public Date ts;

	/**
	 * 删除标志
	 */
	@Field(value = "del_flag")
	public String delFlag = "0"; // 0未删除 1：删除

	public String state;
	
	/*
	 * 收费项目类型
	 */
	@Field(value = "DT_ITEMTYPE")
	private String dtItemtype;
	
	//项目类型名称
	private String dtItemtypeName;

	@Field(value = "PK_AUDIT")
	private String pkAudit;
	
	@Field(value="CODE_EXT")
	private String codeExt;
	
	@Field(value="FACTORY")
	private String factory;

	/**
	 * 材料其他属性
	 */
	@Field(value="DT_SANITYPE")
	private String dtSanitype;

	/**
	 * 医保备案类型
	 */
	@Field(value="EU_HPRECTYPE")
	private String euHprectype;

	@Field(value="CODE_EXT2")
	private String codeExt2;

	@Field(value="CODE_EXT3")
	private String codeExt3;

	public String getDtSanitype() {
		return dtSanitype;
	}

	public void setDtSanitype(String dtSanitype) {
		this.dtSanitype = dtSanitype;
	}

	public String getEuHprectype() {
		return euHprectype;
	}

	public void setEuHprectype(String euHprectype) {
		this.euHprectype = euHprectype;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getPkAudit() {
		return pkAudit;
	}

	public void setPkAudit(String pkAudit) {
		this.pkAudit = pkAudit;
	}

	public String getPkUnitName() {
		return pkUnitName;
	}

	public void setPkUnitName(String pkUnitName) {
		this.pkUnitName = pkUnitName;
	}

	public String getDtChcateName() {
		return dtChcateName;
	}

	public void setDtChcateName(String dtChcateName) {
		this.dtChcateName = dtChcateName;
	}

	public String getDtItemtypeName() {
		return dtItemtypeName;
	}

	public void setDtItemtypeName(String dtItemtypeName) {
		this.dtItemtypeName = dtItemtypeName;
	}

	public String getPkItemcateName() {
		return pkItemcateName;
	}

	public void setPkItemcateName(String pkItemcateName) {
		this.pkItemcateName = pkItemcateName;
	}

	public String getDtItemtype() {
		return dtItemtype;
	}

	public void setDtItemtype(String dtItemtype) {
		this.dtItemtype = dtItemtype;
	}

	public String getDescItem() {
		return descItem;
	}

	public void setDescItem(String descItem) {
		this.descItem = descItem;
	}

	public String getExceptItem() {
		return exceptItem;
	}

	public void setExceptItem(String exceptItem) {
		this.exceptItem = exceptItem;
	}

	public String getCodeHp() {
		return codeHp;
	}

	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}

	public String getCodeStd() {
		return codeStd;
	}

	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPkItem() {

		return this.pkItem;
	}

	public void setPkItem(String pkItem) {

		this.pkItem = pkItem;
	}

	public String getCode() {

		return this.code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getName() {

		return this.name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getNamePrt() {

		return this.namePrt;
	}

	public void setNamePrt(String namePrt) {

		this.namePrt = namePrt;
	}

	public String getSpcode() {

		return this.spcode;
	}

	public void setSpcode(String spcode) {

		this.spcode = spcode;
	}

	public String getdCode() {

		return this.dCode;
	}

	public void setdCode(String dCode) {

		this.dCode = dCode;
	}


	public String getDescPrice() {
		return descPrice;
	}

	public void setDescPrice(String descPrice) {
		this.descPrice = descPrice;
	}
	public String getPkUnit() {

		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {

		this.pkUnit = pkUnit;
	}

	public String getSpec() {

		return this.spec;
	}

	public void setSpec(String spec) {

		this.spec = spec;
	}

	public double getPrice() {

		return this.price;
	}

	public void setPrice(double price) {

		this.price = price;
	}

	public String getFlagSet() {

		return this.flagSet;
	}

	public void setFlagSet(String flagSet) {

		this.flagSet = flagSet;
	}

	public String getFlagPd() {

		return this.flagPd;
	}

	public void setFlagPd(String flagPd) {

		this.flagPd = flagPd;
	}

	public String getFlagActive() {

		return this.flagActive;
	}

	public void setFlagActive(String flagActive) {

		this.flagActive = flagActive;
	}

	public String getEuPricemode() {

		return this.euPricemode;
	}

	public void setEuPricemode(String euPricemode) {

		this.euPricemode = euPricemode;
	}

	public String getPkItemcate() {

		return this.pkItemcate;
	}

	public void setPkItemcate(String pkItemcate) {

		this.pkItemcate = pkItemcate;
	}

	public String getDtChcate() {

		return this.dtChcate;
	}

	public void setDtChcate(String dtChcate) {

		this.dtChcate = dtChcate;
	}

	public String getNote() {

		return this.note;
	}

	public void setNote(String note) {

		this.note = note;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getCreator() {

		return this.creator;
	}

	public void setCreator(String creator) {

		this.creator = creator;
	}

	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	public String getModifier() {

		return this.modifier;
	}

	public void setModifier(String modifier) {

		this.modifier = modifier;
	}

	public String getDelFlag() {

		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {

		this.delFlag = delFlag;
	}

	public Date getTs() {

		return this.ts;
	}

	public void setTs(Date ts) {

		this.ts = ts;
	}

	public String getPkFactory() {
		return pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getCodeExt() {
		return codeExt;
	}

	public void setCodeExt(String codeExt) {
		this.codeExt = codeExt;
	}

	public String getCodeExt2() {
		return codeExt2;
	}

	public void setCodeExt2(String codeExt2) {
		this.codeExt2 = codeExt2;
	}

	public String getCodeExt3() {
		return codeExt3;
	}

	public void setCodeExt3(String codeExt3) {
		this.codeExt3 = codeExt3;
	}
	
	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	 
}