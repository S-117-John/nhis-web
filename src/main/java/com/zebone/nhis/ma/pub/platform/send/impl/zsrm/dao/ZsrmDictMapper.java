package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmDictMapper {

    List<Map<String, Object>> queryBedInfoByPk(String pkBed);
}
