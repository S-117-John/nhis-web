package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmBlSendMapper {

    /**
     * 查询门诊欠费患者姓名、联系电话
     * @param dateHap
     * @return
     */
    List<Map<String,Object>> getOpArrearsLis(@Param("dateHap") String dateHap);
}
