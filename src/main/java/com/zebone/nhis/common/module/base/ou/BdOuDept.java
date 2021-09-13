package com.zebone.nhis.common.module.base.ou;

import com.zebone.nhis.base.ou.vo.BdOuDeptType;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
import java.util.List;

/**
 * 科室
 * @author Xulj
 *
 */
@Table(value="bd_ou_dept")
public class BdOuDept extends BaseModule {

	/**
	 * 部门主键
	 */
	@PK
	@Field(value="pk_dept",id=KeyId.UUID)
	private String pkDept;
	
	/**
	 * 部门编码
	 */
	@Field(value="code_dept")
	private String codeDept;
	
	/**
	 * 部门名称
	 */
	@Field(value="name_dept")
	private String nameDept;
	
	/**
	 * 上级部门
	 */
	@Field(value="pk_father")
	private String pkFather;
	
	/**
	 * 部门级别
	 */
	@Field(value="eu_level")
	private String euLevel;
	
	/**
	 * 部门类型
	 */
	@Field(value="dept_type")
	private String deptType;
	
	/**
	 * 简称
	 */
	@Field(value="shortname")
	private String shortname; 
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;
	
	/**
	 * 医疗类型
	 */
	@Field(value="dt_depttype")
	private String dtDepttype;
	
	/**
	 * 床位数
	 */
	@Field(value="bednum")
	private int bednum;
	
	/**
	 * 开放床位数
	 */
	@Field(value="bednum_open")
	private int bednumOpen;
	
	/**
	 * 位置点名称
	 */
	@Field(value="name_place")
	private String namePlace;
	
	/**
	 * 部门简介
	 */
	@Field(value="dept_desc")
	private String deptDesc;
	
	/**
	 * 部门排序号
	 */
	@Field(value="sortno")
	private int sortno;
	
	/**
	 * 门诊使用
	 */
	@Field(value="flag_op")
	private String flagOp;
	
	/**
	 * 急诊使用
	 */
	@Field(value="flag_er")
	private String flagEr;
	
	/**
	 * 住院使用
	 */
	@Field(value="flag_ip")
	private String flagIp;
	
	/**
	 * 体检使用
	 */
	@Field(value="flag_pe")
	private String flagPe;
	
	/**
	 * 家床使用
	 */
	@Field(value="flag_hm")
	private String flagHm;
	
	/**
	 * 备注
	 */
	@Field(value="note")
	private String note;
	
	/**
	 * 拼音码
	 */
	@Field(value="py_code")
	private String pyCode;
	
	
	/**
	 * 自定义码
	 */
	@Field(value="d_code")
	private String dCode;

	/**
	 * 科室电话
	 */
	@Field(value="telno_dept")
	private String telnoDept;
	
	/**
	 * 标准科室编码
	 */
	@Field(value="dt_stdepttype")
	private String dtStdepttype;
	
	/**
	 * 核算科室编码
	 */
	@Field(value="dt_acctdept")
	private String dtAcctdept;
	
	/**
	 * 所属院区
	 */
	@Field(value="pk_orgarea")
	private String pkOrgarea;
	
	@Field(value="id_master")
	private String idMaster;
	/**
	 * 专业类型
	 */
	@Field(value="dt_medicaltype")
    private String dtMedicaltype;
	/**
	 * 专业类型
	 */
	@Field(value="dt_building")
	private String dtBuilding;

	@Override
	public String getModifier() {
		return modifier;
	}

	@Override
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Field(userfield = "pkEmp", userfieldscop = Field.FieldType.ALL)
	private String modifier;


	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}





	/**
	 * 修改时间
	 */
	@Field(value="modity_time")
	private Date modityTime;
	
	public String getDtMedicaltype() {
		return dtMedicaltype;
	}

	public void setDtMedicaltype(String dtMedicaltype) {
		this.dtMedicaltype = dtMedicaltype;
	}

	public String getPkOrgarea() {
		return pkOrgarea;
	}

	public void setPkOrgarea(String pkOrgarea) {
		this.pkOrgarea = pkOrgarea;
	}

	private List<BdOuDeptType> depts;
	
	public String getIdMaster() {
		return idMaster;
	}

	public void setIdMaster(String idMaster) {
		this.idMaster = idMaster;
	}

	public List<BdOuDeptType> getDepts() {
		return depts;
	}

	public void setDepts(List<BdOuDeptType> depts) {
		this.depts = depts;
	}

	public String getDtAcctdept() {
		return dtAcctdept;
	}

	public void setDtAcctdept(String dtAcctdept) {
		this.dtAcctdept = dtAcctdept;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getEuLevel() {
		return euLevel;
	}

	public void setEuLevel(String euLevel) {
		this.euLevel = euLevel;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getDtDepttype() {
		return dtDepttype;
	}

	public void setDtDepttype(String dtDepttype) {
		this.dtDepttype = dtDepttype;
	}

	public int getBednum() {
		return bednum;
	}

	public void setBednum(int bednum) {
		this.bednum = bednum;
	}

	public int getBednumOpen() {
		return bednumOpen;
	}

	public void setBednumOpen(int bednumOpen) {
		this.bednumOpen = bednumOpen;
	}

	public String getNamePlace() {
		return namePlace;
	}

	public void setNamePlace(String namePlace) {
		this.namePlace = namePlace;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public String getFlagOp() {
		return flagOp;
	}

	public void setFlagOp(String flagOp) {
		this.flagOp = flagOp;
	}

	public String getFlagEr() {
		return flagEr;
	}

	public void setFlagEr(String flagEr) {
		this.flagEr = flagEr;
	}

	public String getFlagIp() {
		return flagIp;
	}

	public void setFlagIp(String flagIp) {
		this.flagIp = flagIp;
	}

	public String getFlagPe() {
		return flagPe;
	}

	public void setFlagPe(String flagPe) {
		this.flagPe = flagPe;
	}

	public String getFlagHm() {
		return flagHm;
	}

	public void setFlagHm(String flagHm) {
		this.flagHm = flagHm;
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

	public String getTelnoDept() {
		return telnoDept;
	}

	public void setTelnoDept(String telnoDept) {
		this.telnoDept = telnoDept;
	}

	public String getDtStdepttype() {
		return dtStdepttype;
	}

	public void setDtStdepttype(String dtStdepttype) {
		this.dtStdepttype = dtStdepttype;
	}

	public String getDtBuilding() {
		return dtBuilding;
	}

	public void setDtBuilding(String dtBuilding) {
		this.dtBuilding = dtBuilding;
	}
}
