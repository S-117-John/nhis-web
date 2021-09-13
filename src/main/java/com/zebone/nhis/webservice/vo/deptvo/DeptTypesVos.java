package com.zebone.nhis.webservice.vo.deptvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DeptTypesVos {
	private List<DeptTypesVo> deptTypesVo;
   
	@XmlElement(name = "deptType")
    public List<DeptTypesVo> getDeptTypesVo() {
		return deptTypesVo;
	}

	public void setDeptTypesVo(List<DeptTypesVo> deptTypesVo) {
		this.deptTypesVo = deptTypesVo;
	}

}
