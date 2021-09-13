package com.zebone.nhis.common.module.base.ou;

import java.io.Serializable;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 功能操作
 * @author Xulj
 *
 */
@Table(value="bd_ou_oper")
public class BdOuOper implements Serializable{

	/**
	 * 功能主键
	 */
	@PK
	@Field(value="pk_oper",id=KeyId.UUID)
	private String pkOper;
	
	/**
	 * 功能编码
	 */
	@Field(value="code_oper")
	private String codeOper;
	
	/**
	 * 功能名称
	 */
	@Field(value="name_oper")
	private String nameOper;
	
	/**
     * 拼音码
     */
    @Field(value = "spcode")
    private String spcode;
    
    /**
     * 自定义码
     */
    @Field(value = "d_code")
    private String dCode;
	
	/**
	 * 所属子系统
	 */
	@Field(value="pk_subsys")
	private String pkSubsys;
	
	/**
	 * 上级功能
	 */
	@Field(value="pk_father")
	private String pkFather;
	
	/**
	 * 功能类型
	 */
	@Field(value="eu_bustype")
	private String euBustype;
	
	/**
	 * 功能分类
	 */
	@Field(value="eu_opertype")
	private String euOpertype;
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;
	
	/**
	 * 功能描述
	 */
	@Field(value="descr")
	private String descr;
	
	/**
	 * 功能dll
	 */
	@Field(value="oper_dll")
	private String operDll;
	
	/**
	 * 调用参数
	 */
	@Field(value="param")
	private String param;
	
	/**
	 * 备注
	 */
	@Field(value="note")
	private String note;

	/**
	 * 功能级别
	 */
	@Field(value="eu_level")
	private String euLevel;
	
	/**
	 * 快捷键
	 */
	@Field
	private String flagShortcut =  "0" ; 
    /**
     * 默认
     */
	@Field
	private String flagDef =  "0" ; 
	
	/**
	 * 使用范围（0 集团，1 机构）
	 */
	@Field(value="eu_range")
	private String euRange;
	
	/**
	 * 超级管理标志
	 */
	@Field(value="flag_super")
	private String flagSuper;
	
	/**
     * 机构主键
     */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    public String pkOrg;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;
	
	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	public Date ts;
	
	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getPkOper() {
		return pkOper;
	}

	public void setPkOper(String pkOper) {
		this.pkOper = pkOper;
	}

	public String getCodeOper() {
		return codeOper;
	}

	public void setCodeOper(String codeOper) {
		this.codeOper = codeOper;
	}

	public String getNameOper() {
		return nameOper;
	}

	public void setNameOper(String nameOper) {
		this.nameOper = nameOper;
	}

	public String getPkSubsys() {
		return pkSubsys;
	}

	public void setPkSubsys(String pkSubsys) {
		this.pkSubsys = pkSubsys;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getEuBustype() {
		return euBustype;
	}

	public void setEuBustype(String euBustype) {
		this.euBustype = euBustype;
	}

	public String getEuOpertype() {
		return euOpertype;
	}

	public void setEuOpertype(String euOpertype) {
		this.euOpertype = euOpertype;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getEuLevel() {
		return euLevel;
	}

	public void setEuLevel(String euLevel) {
		this.euLevel = euLevel;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getOperDll() {
		return operDll;
	}

	public void setOperDll(String operDll) {
		this.operDll = operDll;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
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

	public Date getTs() {
		return ts;
	}

	public String getFlagShortcut() {
		return flagShortcut;
	}

	public void setFlagShortcut(String flagShortcut) {
		this.flagShortcut = flagShortcut;
	}

	public String getFlagDef() {
		return flagDef;
	}

	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}

	public String getEuRange() {
		return euRange;
	}

	public void setEuRange(String euRange) {
		this.euRange = euRange;
	}

	public String getFlagSuper() {
		return flagSuper;
	}

	public void setFlagSuper(String flagSuper) {
		this.flagSuper = flagSuper;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
}
