package com.zebone.nhis.pro.zsba.nm.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 非医疗费用-收费项目
 * @author lipz
 *
 */
@Table(value="nm_charge_item")
public class NmChargeItem  extends BaseModule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4368425606161907989L;

	@PK
	@Field(value="pk_ci",id=KeyId.UUID)
    private String pkCi; // 收费项目主键

	@Field(value="code_item")
    private String codeItem;//项目编码
	
    @Field(value="name_item")
    private String nameItem;//项目名称
    
    @Field(value="py_code")
    private String pyCode;//拼音码
    
    @Field(value="spec")
    private String spec;//规格
    
    @Field(value="unit")
    private String unit;//规格
    
    @Field(value="price")
    private BigDecimal price;//单价
    
    @Field(value="nhis_pk_item")
    private String nhisPkItem;//nhis项目字典主键

    @Field(value="modity_time")
    private Date modityTime;//
    
	@Field(value="pk_dept")
    private String pkDept;//归属科室
    
    @Field(value="show_site")
    private String showSite;//显示位置：0都显示、1只在住院、2只在门诊
    
    @Field(value="auto_annal")
    private String autoAnnal;//自动计费：0是、1否

    
	public String getPkCi() {
		return pkCi;
	}
	public void setPkCi(String pkCi) {
		this.pkCi = pkCi;
	}

	public String getCodeItem() {
		return codeItem;
	}
	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}

	public String getNameItem() {
		return nameItem;
	}
	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getNhisPkItem() {
		return nhisPkItem;
	}
	public void setNhisPkItem(String nhisPkItem) {
		this.nhisPkItem = nhisPkItem;
	}
	
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	public String getShowSite() {
		return showSite;
	}
	public void setShowSite(String showSite) {
		this.showSite = showSite;
	}
	
	public String getAutoAnnal() {
		return autoAnnal;
	}
	public void setAutoAnnal(String autoAnnal) {
		this.autoAnnal = autoAnnal;
	}
    



}
