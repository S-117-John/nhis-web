package com.zebone.nhis.sch.appt.dao;

import java.util.List;

import com.zebone.nhis.sch.appt.vo.SchedulingResources;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchedulingResourcesMapper {
	
	List<SchedulingResources> getSchedulingResources_sql(String pkDept);
	
	List<SchedulingResources> getSchedulingResources_oracle(String pkDept);

}
