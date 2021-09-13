package com.zebone.nhis.pro.zsba.compay.other.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 投票人员字典
 * @author 85102
 *
 */
@Table(value="DRUG_OPT_PD")
public class DrugOptPd {
	
	/** 药品主键 */
	@PK
	@Field(value="PK_PD",id=KeyId.UUID)
    private String pkPd;
    
    /** 药品名称 */
	@Field(value="NAME_PD")
    private String namePd;

    /** 属性【国基、非国基】 **/
	@Field(value="EU_BASE")
    private String euBase;

    /** 分类【西药、中成药】 **/
	@Field(value="EU_TYPE")
    private String euType;

    /** 剂型 */
	@Field(value="DOSAGE")
    private String dosage;

    /** 规格 */
	@Field(value="SPEC")
    private String spec;

    /** 描述 */
	@Field(value="DESC_PD")
    private String descPd;

    /** 医保类型 */
	@Field(value="HP_TYPE")
    private String hpType;

    /**  药学评估 */
	@Field(value="ASSESS")
    private String assess;

    /** 生产厂家  **/
	@Field(value="FACTORY")
    private String factory;

	/** 创建时间 */
	@Field(value="CREATE_TIME")
    private Date createTime;

    /** 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getNamePd() {
		return namePd;
	}

	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}

	public String getEuBase() {
		return euBase;
	}

	public void setEuBase(String euBase) {
		this.euBase = euBase;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getHpType() {
		return hpType;
	}

	public void setHpType(String hpType) {
		this.hpType = hpType;
	}

	public String getAssess() {
		return assess;
	}

	public void setAssess(String assess) {
		this.assess = assess;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getDescPd() {
		return descPd;
	}

	public void setDescPd(String descPd) {
		this.descPd = descPd;
	}

}
