package com.zebone.nhis.common.module.base.bd.price;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 固定收费套定义
 * Table: BD_DAYCG_SET  - bd_daycg_set 
 *
 * @since 2016-08-26 02:55:31
 */
@Table(value="BD_DAYCG_SET")
public class BdDaycgSet extends BaseModule {

	/** 固定收费套主键 */
	@PK
	@Field(value="PK_DAYCGSET",id=KeyId.UUID)
    private String pkDaycgset;

    /** 对应科室   PK_DEPT - 如果为科室范围时，对应的科室。全院范围时可为空 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** 范围   eu_type - 0 全院范围，1 科室范围 */
	@Field(value="EU_TYPE")
    private String euType;
	
	/** 固定收费套编码 */
	@Field(value="CODE")
    private String code;

	/** 固定收费套名称 */
	@Field(value="NAME")
    private String name;

	/** 固定收费套描述 */
	@Field(value="NOTE")
    private String note;
	
	/** 拼音码 */
	@Field(value="PY_CODE")
    private String pyCode;
	
	/** 自定义码 */
	@Field(value="D_CODE")
    private String dCode;	
	
	/** 适用医保 */
	@Field(value="CODE_HPS")
    private String codeHps;	

	public String getPkDaycgset() {
		return pkDaycgset;
	}

	public void setPkDaycgset(String pkDaycgset) {
		this.pkDaycgset = pkDaycgset;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getCodeHps() {
		return codeHps;
	}

	public void setCodeHps(String codeHps) {
		this.codeHps = codeHps;
	}

}