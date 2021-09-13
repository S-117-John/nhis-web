package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class DepartmentResultResponse {

    @JSONField(name = "DEPARTMENT")
    private List<DataElement> department;

    public List<DataElement> getDepartment() {
        return department;
    }

    public void setDepartment(List<DataElement> department) {
        this.department = department;
    }
}
