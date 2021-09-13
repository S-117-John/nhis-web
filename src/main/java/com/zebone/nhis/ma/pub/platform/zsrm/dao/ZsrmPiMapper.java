package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmPiMapper {
    //根据就诊流水号查询患者诊断信息
    List<Map<String,Object>> getPvDiagCodePv(String codePv);
    //查询患者基础信息
    List<PiMaster> getPiMaster(Map<String,Object> paramMap);
    List<Map<String,Object>> getPiMasterIdNo(String codeOp);
}
