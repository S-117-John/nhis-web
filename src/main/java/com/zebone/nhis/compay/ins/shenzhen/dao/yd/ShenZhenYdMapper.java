package com.zebone.nhis.compay.ins.shenzhen.dao.yd;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShenZhenYdMapper {

    public List<Map<String, Object>> qryUploadDetails(Map<String, Object> paramMap);

    public Map<String, Object> getHpDictmap(String pkPv);
}
