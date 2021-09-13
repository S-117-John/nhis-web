package com.zebone.nhis.nd.temp.vo;

import java.util.List;

import com.zebone.nhis.common.module.nd.temp.NdTemplate;
import com.zebone.nhis.common.module.nd.temp.NdTemplateDept;

public class NdTemplateVo extends NdTemplate {
	private List<NdTemplateDept> tempDeptList;

	public List<NdTemplateDept> getTempDeptList() {
		return tempDeptList;
	}

	public void setTempDeptList(List<NdTemplateDept> tempDeptList) {
		this.tempDeptList = tempDeptList;
	}

}
