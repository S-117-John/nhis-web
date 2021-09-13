package com.zebone.nhis.webservice.vo.employeevo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class EmployeeDataVo {
    private List<ResEmployeeVo> employeeVos;
    
	@XmlElement(name = "employee")
	public List<ResEmployeeVo> getEmployeeVos() {
		return employeeVos;
	}

	public void setEmployeeVos(List<ResEmployeeVo> employeeVos) {
		this.employeeVos = employeeVos;
	}
    
}
