package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmPhSettleServiceMapper {
    //根据pk_settle查询code_apply 和 code_op
    List<Map<String,Object>> getApplyOpByPKSettle(String pkSettle);
}
