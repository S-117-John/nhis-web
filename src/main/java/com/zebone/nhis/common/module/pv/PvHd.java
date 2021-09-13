package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: PV_HD  - 患者就诊-透析记录 
 *
 * @since 2016-09-09 04:08:55
 */
@Table(value="PV_HD")
public class PvHd extends BaseModule{
	
	@PK
	@Field(value="PK_PVHD",id=KeyId.UUID)
    private String pkPvhd;
	
	/** 所属机构 */
	@Field(value="PK_ORG")
    private String pkOrg;
	
	/** 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;
	
	/** 治疗科室 */
	@Field(value="PK_DEPT")
    private String pkDept;
	
	/** 治疗区 */
	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;
	
	/** 透析方式 */
	@Field(value="DT_HDTYPE")
    private String dtHdtype;
	
	/** 透析床位 */
	@Field(value="PK_HDBED")
    private String pkHdbed;
	
	/** 治疗班次 */
	@Field(value="PK_DATESLOT")
    private String pkDateslot;
	
	/** 关联排班 */
	@Field(value="PK_SCHHD")
    private String pkSchhd;
	
	/** 治疗医师 */
	@Field(value="PK_EMP_HD")
    private String pkEmpHd;
	
	/** 医师姓名 */
	@Field(value="NAME_EMP_HD")
    private String nameEmpHd;
	
	/** 责任护士 */
	@Field(value="PK_EMP_NS")
    private String pkEmpNs;
	
	/** 护士姓名 */
	@Field(value="NAME_EMP_NS")
    private String nameEmpNs;
	
	/** 开始时间 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;
	
	/** 结束时间 */
	@Field(value="DATE_END")
    private Date dateEnd;
	
	/** 透析状态 */
	@Field(value="EU_STATUS_HD")
    private String euStatusHd;

	public String getPkPvhd() {
		return pkPvhd;
	}

	public void setPkPvhd(String pkPvhd) {
		this.pkPvhd = pkPvhd;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public String getPkHdbed() {
		return pkHdbed;
	}

	public void setPkHdbed(String pkHdbed) {
		this.pkHdbed = pkHdbed;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getPkSchhd() {
		return pkSchhd;
	}

	public void setPkSchhd(String pkSchhd) {
		this.pkSchhd = pkSchhd;
	}

	public String getPkEmpHd() {
		return pkEmpHd;
	}

	public void setPkEmpHd(String pkEmpHd) {
		this.pkEmpHd = pkEmpHd;
	}

	public String getNameEmpHd() {
		return nameEmpHd;
	}

	public void setNameEmpHd(String nameEmpHd) {
		this.nameEmpHd = nameEmpHd;
	}

	public String getPkEmpNs() {
		return pkEmpNs;
	}

	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}

	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getEuStatusHd() {
		return euStatusHd;
	}

	public void setEuStatusHd(String euStatusHd) {
		this.euStatusHd = euStatusHd;
	}

	
}
