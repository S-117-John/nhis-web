package com.zebone.nhis.compay.pub.syx.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class InsSyncBasicsDataVo {

	/** CODE_HP - 上传编码 */
	@Field(value = "CODE_HP")
	private String codeHp;

	/** CODE - 项目编码 */
	@Field(value = "CODE")
	private String code;

	/** NAME - 项目名称 */
	@Field(value = "NAME")
	private String name;

	/** SPEC - 规格 */
	@Field(value = "SPEC")
	private String spec;

	/** DOSAGE - 剂型 */
	@Field(value = "DOSAGE")
	private String dosage;

	/** CODE_CENTER - 中心编码 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** NAME_CENTER - 中心名称 */
	@Field(value = "NAME_CENTER")
	private String nameCenter;

	/** DOSAGE_CENTER - 中心剂型 */
	@Field(value = "DOSAGE_CENTER")
	private String dosageCenter;

	/** STAPLE - 等级 0 - 甲类，1 - 乙类，2- 全自费 */
	@Field(value = "STAPLE")
	private String staple;

	/** FLAG_WL - 工伤 */
	@Field(value = "FLAG_WL")
	private String flagWl;

	/** FLAG_BO - 生育 */
	@Field(value = "FLAG_BO")
	private String flagBo;

	/** FLAG_REST - 限制 */
	@Field(value = "FLAG_REST")
	private String flagRest;

	/** RATIO - 自负比例 */
	@Field(value = "RATIO")
	private BigDecimal ratio;

	/** DATE_EXPIRE - 失效日期 */
	@Field(value = "DATE_EXPIRE")
	private Date dateExpire;

	/** RANGE_REST - 限制用药范围 */
	@Field(value = "RANGE_REST")
	private String rangeRest;

	/** SPCODE - 助记码 */
	@Field(value = "SPCODE")
	private String spcode;
	
	public String getCodeHp() {
		return codeHp;
	}

	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getCodeCenter() {
		return codeCenter;
	}

	public void setCodeCenter(String codeCenter) {
		this.codeCenter = codeCenter;
	}

	public String getNameCenter() {
		return nameCenter;
	}

	public void setNameCenter(String nameCenter) {
		this.nameCenter = nameCenter;
	}

	public String getDosageCenter() {
		return dosageCenter;
	}

	public void setDosageCenter(String dosageCenter) {
		this.dosageCenter = dosageCenter;
	}

	public String getStaple() {
		return staple;
	}

	public void setStaple(String staple) {
		this.staple = staple;
	}

	public String getFlagWl() {
		return flagWl;
	}

	public void setFlagWl(String flagWl) {
		this.flagWl = flagWl;
	}

	public String getFlagBo() {
		return flagBo;
	}

	public void setFlagBo(String flagBo) {
		this.flagBo = flagBo;
	}

	public String getFlagRest() {
		return flagRest;
	}

	public void setFlagRest(String flagRest) {
		this.flagRest = flagRest;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	
	public String getRangeRest() {
		return rangeRest;
	}

	public void setRangeRest(String rangeRest) {
		this.rangeRest = rangeRest;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

}
