package com.zebone.nhis.base.pub.dao;

import com.zebone.nhis.base.pub.vo.BdDatadumpDic;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DumpTableStuckMapper {

    public List<BdDatadumpDic> qryDumpTableDicList(Map<String,Object> paramMap);

}
