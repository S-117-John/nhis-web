package com.zebone.nhis.common.module.compay.ins.zsba.zsyb;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_bear - 外部医保-生育资料维[2047] 
 *
 * @since 2017-09-06 10:42:09
 */
@Table(value="INS_BIRTH_CATALOGUE")
public class InsZsybBirthCatalogue extends BaseModule  {

	@PK
	@Field(value="PK_INSBIRTHCATALOGUE",id=KeyId.UUID)
    private String pk_insbirthcatalogue;

	/**
	 * 生育目录库类型
	 */
	@Field(value="SYMLKLX")
    private String symlklx;
	/**
	 * 项目编号
	 */
	@Field(value="XMBH")
    private String xmbh;

	/**
	 * 项目类别
	 */
	@Field(value="XMLB")
    private String xmlb;
	/**
	 * 中文名称
	 */
	@Field(value="ZWMC")
    private String zwmc;
	/**
	 * 英文名称
	 */
	@Field(value="YWMC")
    private String ywmc;

	/**
	 * 分类代码
	 */
	@Field(value="FLDM")
    private String fldm;
	/**
	 * 备注
	 */
	@Field(value="BZ")
    private String bz;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPk_insbirthcatalogue() {
		return pk_insbirthcatalogue;
	}

	public void setPk_insbirthcatalogue(String pk_insbirthcatalogue) {
		this.pk_insbirthcatalogue = pk_insbirthcatalogue;
	}

	public String getSymlklx() {
		return symlklx;
	}

	public void setSymlklx(String symlklx) {
		this.symlklx = symlklx;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmlb() {
		return xmlb;
	}

	public void setXmlb(String xmlb) {
		this.xmlb = xmlb;
	}

	public String getZwmc() {
		return zwmc;
	}

	public void setZwmc(String zwmc) {
		this.zwmc = zwmc;
	}

	public String getYwmc() {
		return ywmc;
	}

	public void setYwmc(String ywmc) {
		this.ywmc = ywmc;
	}

	public String getFldm() {
		return fldm;
	}

	public void setFldm(String fldm) {
		this.fldm = fldm;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}