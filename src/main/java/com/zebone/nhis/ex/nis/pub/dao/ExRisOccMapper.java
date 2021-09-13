package com.zebone.nhis.ex.nis.pub.dao;


import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;

/**
 * 医疗执行，检查结果
 */
@Mapper
public interface ExRisOccMapper {

    /**
     * 删除
     * @param pkRisocc
     * @return
     */
    int deleteByPrimaryKey(String pkRisocc);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(ExRisOcc record);

    /**
     * 条件插入
     * @param record
     * @return
     */
    int insertSelective(ExRisOcc record);

    /**
     * 根据主键查询
     * @param pkRisocc
     * @return
     */
    ExRisOcc selectByPrimaryKey(String pkRisocc);

    /**
     * 根据条件更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ExRisOcc record);

    /**
     * 根据主键更新
     * @param record
     * @return
     */
    int updateByPrimaryKey(ExRisOcc record);

    /**
     * 查询条件结果集
     * @param record
     * @return
     */
    List<ExRisOcc> selectAll(ExRisOcc record);
}