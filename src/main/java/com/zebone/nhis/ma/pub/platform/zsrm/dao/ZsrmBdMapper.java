package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ZsrmBdMapper {
    BdOuDept getZsrmBdouDept(@Param("codeDept")String codeDept);
}
