package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.CnOrderVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IpCnOperationMapper {

    List<PvEncounterVo> qryPiPv(@Param("pkPv") String pkPv);

    List<CnOrderVo> qryCnOrd(Map<String,Object> paramMap);
}
