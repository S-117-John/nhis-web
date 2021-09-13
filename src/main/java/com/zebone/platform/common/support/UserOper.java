package com.zebone.platform.common.support;

import java.io.Serializable;


public class UserOper implements Serializable{

	/**
	 * 功能主键
	 */
	private String pkOper;
	
	/**
	 * 功能编码
	 */
	private String codeOper;
	
	/**
	 * 功能名称
	 */
	private String nameOper;
	

	
	/**
	 * 上级功能
	 */
	private String pkFather;
	
	
	/**
	 * 快捷键
	 */
	private String flagShortcut =  "0" ; 
    /**
     * 默认
     */
	private String flagDef =  "0" ;
	
	
	
	/**
	 * 功能dll
	 */
	private String operDll;
	
	/**
	 * 调用参数
	 */
	private String param;

	/**
     * 机构主键
     */
    public String pkOrg;
    
    public String pkMenu;
    
    public String nameMenu;
    
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

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
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

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
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

    public String getPkMenu() {
        return pkMenu;
    }

    public void setPkMenu(String pkMenu) {
        this.pkMenu = pkMenu;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }


}
