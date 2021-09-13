package com.zebone.nhis.scm.material.vo;

import com.zebone.nhis.common.module.scm.pub.BdPdStore;

@SuppressWarnings("serial")
public class BdPdStoreInfo extends BdPdStore {
	
	private String codeStore;
	
	private String nameStore;
	
	private String nameOrg;
	
	private String nameDept;

	public String getCodeStore() {
		return codeStore;
	}

	public void setCodeStore(String codeStore) {
		this.codeStore = codeStore;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
}
