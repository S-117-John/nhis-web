package com.zebone.platform.common.support;

import java.io.Serializable;
import java.util.List;


public class UserMenu implements Serializable{

	
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
    
    private String pkMenu;
    
    private String nameMenu;
    
    private String codeMenu;
    
    private String pkFather;
    
    private String dtAbutype;
    
    private List<UserMenuElement> elementList;
    

    public List<UserMenuElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<UserMenuElement> elementList) {
		this.elementList = elementList;
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

    public String getCodeMenu() {
        return codeMenu;
    }

    public void setCodeMenu(String codeMenu) {
        this.codeMenu = codeMenu;
    }

    public String getPkFather() {
        return pkFather;
    }

    public void setPkFather(String pkFather) {
        this.pkFather = pkFather;
    }

	public String getDtAbutype() {
		return dtAbutype;
	}

	public void setDtAbutype(String dtAbutype) {
		this.dtAbutype = dtAbutype;
	}


}
