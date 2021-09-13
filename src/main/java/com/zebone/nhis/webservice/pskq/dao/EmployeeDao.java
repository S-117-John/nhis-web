package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.webservice.pskq.model.Employee;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface EmployeeDao {

    Employee findEmpInfoById(String id);

    List<Employee> findByOrgCode(String code);
}
