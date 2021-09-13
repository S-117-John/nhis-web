package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.code.BdFilterItemDept;

public class BdFilterItemDeptVO extends BdFilterItemDept {
	 /// <summary>
    /// 获取或设置科室名称
    /// </summary>
    public String nameDept; 

	 /// <summary>
    /// 获取或设置科室code
    /// </summary>
    public String codeDept; 
    
    /// <summary>
    /// 获取或设置机构名称
    /// </summary>
    public String nameOrg;

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

}
