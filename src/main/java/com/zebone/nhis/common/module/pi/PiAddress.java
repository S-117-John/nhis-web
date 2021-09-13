package com.zebone.nhis.common.module.pi;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: PI_ADDRESS - 患者信息-地址
 * 
 * @since 2016-09-14 01:52:21
 */
@Table(value = "PI_ADDRESS")
public class PiAddress {

	/** PK_ADDR - 地址主键 */
	@PK
	@Field(value = "PK_ADDR", id = KeyId.UUID)
	private String pkAddr;

	/** PK_PI - 患者主键 */
	@Field(value = "PK_PI")
	private String pkPi;

	/** SORT_NO - 排序号 */
	@Field(value = "SORT_NO")
	private BigDecimal sortNo;

	/** DT_ADDRTYPE - 地址类型 */
	@Field(value = "DT_ADDRTYPE")
	private String dtAddrtype;

	/** ADDR - 地址描述 */
	@Field(value = "ADDR")
	private String addr;

	/** POST_CODE - 邮政编码 */
	@Field(value = "POST_CODE")
	private String postCode;

	/** DT_REGION_PROV - 省(直辖市) */
	@Field(value = "DT_REGION_PROV")
	private String dtRegionProv;

	/** DT_REGION_CITY - 市（地） */
	@Field(value = "DT_REGION_CITY")
	private String dtRegionCity;

	/** DT_REGION_DIST - 区（县） */
	@Field(value = "DT_REGION_DIST")
	private String dtRegionDist;

	/** DT_STREET - 街道/乡镇 */
	@Field(value = "DT_STREET")
	private String dtStreet;

	/** TEL - 电话号码 */
	@Field(value = "TEL")
	private String tel;

	/** FLAG_USE - 常用地址标志 */
	@Field(value = "FLAG_USE")
	private String flagUse;

	/** CREATOR - 创建人 */
	@Field(value = "CREATOR", userfield = "pkEmp", userfieldscop = FieldType.INSERT)
	private String creator;

	/** CREATE_TIME - 创建时间 */
	@Field(value = "CREATE_TIME", date = FieldType.INSERT)
	private Date createTime;

	/** MODIFIER - 修改人 */
	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;

	/** DEL_FLAG - 删除标志 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS - 时间戳 */
	@Field(value = "TS", date = FieldType.ALL)
	private Date ts;

	/** NAME_PROV - 省市名称 */
	@Field(value = "NAME_PROV", date = FieldType.ALL)
	private String nameProv;

	/** NAME_CITY - 地市名称 */
	@Field(value = "NAME_CITY", date = FieldType.ALL)
	private String nameCity;


	/** NAME_DIST - 区县名称 */
	@Field(value = "NAME_DIST", date = FieldType.ALL)
	private String nameDist;

	/** DT_STREET - 街道/乡镇名称 */
	@Field(value = "NAME_STREET")
	private String nameStreet;

	/** NAME_REL - 联系人 */
	@Field(value = "NAME_REL", date = FieldType.ALL)
	private String nameRel;

	/** AMT_FEE - 费用 */
	@Field(value = "AMT_FEE")
	private String amtFee;

	public String getNameProv() {
		return nameProv;
	}

	public void setNameProv(String nameProv) {
		this.nameProv = nameProv;
	}

	public String getNameCity() {
		return nameCity;
	}

	public void setNameCity(String nameCity) {
		this.nameCity = nameCity;
	}

	public String getNameDist() {
		return nameDist;
	}

	public void setNameDist(String nameDist) {
		this.nameDist = nameDist;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getAmtFee() {
		return amtFee;
	}

	public void setAmtFee(String amtFee) {
		this.amtFee = amtFee;
	}

	public String getPkAddr() {
		return this.pkAddr;
	}

	public void setPkAddr(String pkAddr) {
		this.pkAddr = pkAddr;
	}

	public String getPkPi() {
		return this.pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public BigDecimal getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(BigDecimal sortNo) {
		this.sortNo = sortNo;
	}

	public String getDtAddrtype() {
		return this.dtAddrtype;
	}

	public void setDtAddrtype(String dtAddrtype) {
		this.dtAddrtype = dtAddrtype;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getDtRegionProv() {
		return this.dtRegionProv;
	}

	public void setDtRegionProv(String dtRegionProv) {
		this.dtRegionProv = dtRegionProv;
	}

	public String getDtRegionCity() {
		return this.dtRegionCity;
	}

	public void setDtRegionCity(String dtRegionCity) {
		this.dtRegionCity = dtRegionCity;
	}

	public String getDtRegionDist() {
		return this.dtRegionDist;
	}

	public void setDtRegionDist(String dtRegionDist) {
		this.dtRegionDist = dtRegionDist;
	}

	public String getDtStreet() {
		return this.dtStreet;
	}

	public void setDtStreet(String dtStreet) {
		this.dtStreet = dtStreet;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFlagUse() {
		return this.flagUse;
	}

	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
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

	public String getNameStreet() {
		return nameStreet;
	}

	public void setNameStreet(String nameStreet) {
		this.nameStreet = nameStreet;
	}
}