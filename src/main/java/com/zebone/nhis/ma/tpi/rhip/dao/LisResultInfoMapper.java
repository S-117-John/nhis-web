package com.zebone.nhis.ma.tpi.rhip.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ma.tpi.rhip.LisResultInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface LisResultInfoMapper {

    /**
     * 查询检验报告信息
     * @param map(inpatientNo住院号)
     * @return
     */
    public List<LisResultInfo> queryLisResultInfo(Map map);
}