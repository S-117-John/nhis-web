package com.zebone.nhis.sch.appt.dao;

import java.util.List;

import com.zebone.nhis.sch.appt.vo.DepartmentInformation;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DepartmentInformationMapper {
	List<DepartmentInformation> getDepartmentInformation_sql(String pkOrg);
	List<DepartmentInformation> getDepartmentInformation_oracle(String pkOrg);
}
