package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_UNIT 
 *
 * @since 2016-10-13 03:18:37
 */
@Table(value="BD_UNIT")
public class BdUnit extends BaseModule {
	@PK
	@Field(value="PK_UNIT",id=KeyId.UUID)
	private String pkUnit;
	
	@Field(value="PK_ORG")
	private String pkOrg;
	
	@Field(value="CODE")
	private String code;
	
	@Field(value="NAME")
	private String name;
	
	@Field(value="SPCODE")
	private String spcode;
	
	@Field(value="D_CODE")
	private String dcode;
		
	@Field(value="CREATOR")
	private String creator;
	
	@Field(value="CREATE_TIME")
	private Date createTime;
	
	@Field(value="MODIFIER")
	private String modifier;
	
	@Field(value="MODITY_TIME")
	private Date modityTime;
	
	@Field(value="DEL_FLAG")
	private String delFlag;
	
	@Field(value="TS")
	private Date ts;

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
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

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
	
	

}
