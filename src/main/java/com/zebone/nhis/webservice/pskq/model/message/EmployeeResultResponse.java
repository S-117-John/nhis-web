package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class EmployeeResultResponse {

    @JSONField(name = "EMPLOYEE")
    private List<DataElement> employee;

    public List<DataElement> getEmployee() {
        return employee;
    }

    public void setEmployee(List<DataElement> employee) {
        this.employee = employee;
    }
}
