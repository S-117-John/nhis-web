package com.zebone.nhis.common.module.base.ou;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * bd_ou_menu表结构
 * @author Administrator
 *
 */
@Table(value="bd_ou_menu")
public class BdOuMenu extends BaseModule{
	
	/**
	 * 菜单主键
	 */
	@PK
	@Field(value="pk_menu",id=KeyId.UUID)
	private String pkMenu;
	
	/**
	 * 菜单编码
	 */
	@Field(value="code_menu")
	private String codeMenu;
	
	/**
	 * 菜单名称
	 */
	@Field(value="name_menu")
	private String nameMenu;
	
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
	 * 使用范围
	 */
	@Field(value="eu_range")
	private String euRange;
	
	/**
	 * 上级菜单
	 */
	@Field(value="pk_father")
	private String pkFather;
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;
	
	/**
	 * 菜单级别
	 */
	@Field(value="eu_level")
	private String euLevel;
	
	/**
	 * 参数
	 */
	@Field(value="param")
	private String param;
	
	/**
	 * 描述
	 */
	@Field(value="descr")
	private String descr;
	
	/**
	 * 对应功能
	 */
	@Field(value="pk_oper")
	private String pkOper;
	
	/**
	 * 快捷标志
	 */
	@Field(value="flag_shortcut")
	private String flagShortcut;
	
	/**
	 * 默认启动标志
	 */
	@Field(value="flag_def")
	private String flagDef;
	
	/**
	 * 超级管理员标志
	 */
	@Field(value="flag_super")
	private String flagSuper;
	
	/**
	 * 应用分类
	 */
	@Field(value="dt_abutype")
	private String dtAbutype;
	
	/**
	 * 备注
	 */
	@Field(value="note")
	
	private String note;

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

	public String getPkMenu() {
		return pkMenu;
	}

	public void setPkMenu(String pkMenu) {
		this.pkMenu = pkMenu;
	}

	public String getCodeMenu() {
		return codeMenu;
	}

	public void setCodeMenu(String codeMenu) {
		this.codeMenu = codeMenu;
	}

	public String getNameMenu() {
		return nameMenu;
	}

	public void setNameMenu(String nameMenu) {
		this.nameMenu = nameMenu;
	}

	public String getEuRange() {
		return euRange;
	}

	public void setEuRange(String euRange) {
		this.euRange = euRange;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
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

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPkOper() {
		return pkOper;
	}

	public void setPkOper(String pkOper) {
		this.pkOper = pkOper;
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

	public String getFlagSuper() {
		return flagSuper;
	}

	public void setFlagSuper(String flagSuper) {
		this.flagSuper = flagSuper;
	}

	public String getDtAbutype() {
		return dtAbutype;
	}

	public void setDtAbutype(String dtAbutype) {
		this.dtAbutype = dtAbutype;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
