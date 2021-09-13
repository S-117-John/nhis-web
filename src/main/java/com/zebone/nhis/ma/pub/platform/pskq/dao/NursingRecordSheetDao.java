package com.zebone.nhis.ma.pub.platform.pskq.dao;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NursingRecordSheetDao {

    List<NdRecordDt> findRecord(Map<String,Object> param);
}
