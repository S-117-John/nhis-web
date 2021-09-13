package com.zebone.nhis.webservice.vo.employeevo;

import javax.xml.bind.annotation.XmlElement;

public class EmployeeData {
	
    private EmployeeDataVo employeeDataVo;
    
	@XmlElement(name = "employeeList")
	public EmployeeDataVo getEmployeeDataVo() {
		return employeeDataVo;
	}

	public void setEmployeeDataVo(EmployeeDataVo employeeDataVo) {
		this.employeeDataVo = employeeDataVo;
	}
     
     
}
