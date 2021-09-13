package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.nhis.webservice.pskq.model.Department;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DepartmentDao {

    Department findDeptInfoById(String id);
}
