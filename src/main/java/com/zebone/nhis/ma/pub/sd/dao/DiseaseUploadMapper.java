package com.zebone.nhis.ma.pub.sd.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DiseaseUploadMapper {
    public List<Map<String, Object>> getDiseaseList(String pkPv);
}
