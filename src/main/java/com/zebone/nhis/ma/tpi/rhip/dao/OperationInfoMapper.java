package com.zebone.nhis.ma.tpi.rhip.dao;


import com.zebone.nhis.ma.tpi.rhip.vo.OperationRecordInfo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 查询手术记录
 */
@Mapper
public interface OperationInfoMapper {

    /**
     * 根据codePi查询手术记录
     * @param map
     * @return
     */
    List<OperationRecordInfo> OperationRecordInfo(Map map);
}
