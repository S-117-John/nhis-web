package com.zebone.nhis.ex.nis.pub.dao;

import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

@Mapper
public interface ExLabOccMapper {
    int deleteByPrimaryKey(String pkLabocc);

    int insert(ExLabOcc record);

    int insertSelective(ExLabOcc record);

    ExLabOcc selectByPrimaryKey(String pkLabocc);

    int updateByPrimaryKeySelective(ExLabOcc record);

    int updateByPrimaryKey(ExLabOcc record);

    List<ExLabOcc> selectAll(ExLabOcc record);
}