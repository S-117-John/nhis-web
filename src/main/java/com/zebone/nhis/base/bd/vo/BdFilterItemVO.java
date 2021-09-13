package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.BdFilterItem;
import com.zebone.nhis.common.module.base.bd.code.BdFilterItemDept;

public class BdFilterItemVO extends BdFilterItem {
	private List<BdFilterItemDept> depts;
	 /// <summary>
    /// 获取或设置
    /// </summary>
    public String dtFiltertypeName;

	public String getDtFiltertypeName() {
		return dtFiltertypeName;
	}

	public void setDtFiltertypeName(String dtFiltertypeName) {
		this.dtFiltertypeName = dtFiltertypeName;
	}

	public List<BdFilterItemDept> getDepts() {
		return depts;
	}

	public void setDepts(List<BdFilterItemDept> depts) {
		this.depts = depts;
	}
	

}
