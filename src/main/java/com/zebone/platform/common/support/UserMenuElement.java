package com.zebone.platform.common.support;

import java.io.Serializable;

public class UserMenuElement implements Serializable{
	/**
     * 机构主键
     */
    public String pkOrg;
    
    private String pkMenu;
    
    private String pkElement;
    
    private String nameEl;
    
    private String codeEl;
    
    private String flagEnable;
    
    

	public String getFlagEnable() {
		return flagEnable;
	}

	public void setFlagEnable(String flagEnable) {
		this.flagEnable = flagEnable;
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

	public String getPkElement() {
		return pkElement;
	}

	public void setPkElement(String pkElement) {
		this.pkElement = pkElement;
	}

	public String getNameEl() {
		return nameEl;
	}

	public void setNameEl(String nameEl) {
		this.nameEl = nameEl;
	}

	public String getCodeEl() {
		return codeEl;
	}

	public void setCodeEl(String codeEl) {
		this.codeEl = codeEl;
	}
    
    
    
    
}
