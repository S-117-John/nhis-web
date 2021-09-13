package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.PvEncounterVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model.SchApptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.OpPatiInfo;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.scm.QueCallVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OpSendMapper {

    List<PvEncounterVo> getPvEncounter(@Param("pkPv") String pkPv);

    List<SchApptVo> getSchAppt(Map<String, Object> param);

    List<QueCallVo> getDisposeInfo(Map<String, Object> paramMap);

    OpPatiInfo qryPatiOpInfo(@Param("pkPv") String pkPv);

    List<Map<String, Object>> qryDaigByPk(@Param("pkDiag") String pkDiag);

    List<Map<String, Object>> getSumAmount(@Param("day") Integer day);
}
