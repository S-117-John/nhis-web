package com.zebone.nhis.webservice.vo.deptvo;

import javax.xml.bind.annotation.XmlElement;

public class DeptData {
	private DeptDataVo deptDataVo;
	@XmlElement(name="deptList")
	public DeptDataVo getDeptDataVo() {
		return deptDataVo;
	}

	public void setDeptDataVo(DeptDataVo deptDataVo) {
		this.deptDataVo = deptDataVo;
	}

}
