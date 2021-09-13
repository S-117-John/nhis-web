package com.zebone.nhis.ma.pub.platform.zsrm.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsphLisRisMapper {

    //查询检验信息
    List<Map<String,Object>> getLisInfo(@Param("codePv") String codePv,@Param("codeApply") String codeApply,@Param("codeOp") String codeOp,@Param("pkCnord") String pkCnord);

    //查询检查申请
    List<Map<String,Object>> getRisInfo(@Param("codePv") String codePv,@Param("codeApply") String codeApply,@Param("codeOp") String codeOp,@Param("pkCnord") String pkCnord);

    /**
     * 查询检查预约
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> getRisAppointList(Map<String,Object> paramMap);


}
