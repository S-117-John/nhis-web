package com.zebone.nhis.emr.scires.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.scires.EmrRptCnd;
import com.zebone.nhis.common.module.emr.scires.EmrRptExp;
import com.zebone.nhis.common.module.emr.scires.EmrRptList;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * @author 
 */
@Mapper
public interface EmrSciResMapper{   
    /**
     * 根据主键查询
     */
    public EmrRptList getEmrRptListById(@Param("pkRpt")String pkRpt); 

    /**
     * 查询出所有记录
     */
    public List<EmrRptList> findAllEmrRptList();    
    
    /**
     * 保存
     */
    public int saveEmrRptList(EmrRptList emrRptList);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrRptList(EmrRptList emrRptList);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrRptList(@Param("pkRpt")String pkRpt);
    
    /**
     * 根据主键查询
     */
    public EmrRptCnd getEmrRptCndById(@Param("pkCnd")String pkCnd); 

    /**
     * 查询出所有记录
     */
    public List<EmrRptCnd> findAllEmrRptCnd();    
    
    /**
     * 保存
     */
    public int saveEmrRptCnd(EmrRptCnd emrRptCnd);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrRptCnd(EmrRptCnd emrRptCnd);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrRptCnd(@Param("pkCnd")String pkCnd);
    
    /**
     * 根据主键查询
     */
    public EmrRptExp getEmrRptExpById(@Param("pkExp")String pkExp); 

    /**
     * 查询出所有记录
     */
    public List<EmrRptExp> findAllEmrRptExp();    
    
    /**
     * 保存
     */
    public int saveEmrRptExp(EmrRptExp emrRptExp);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrRptExp(EmrRptExp emrRptExp);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrRptExp(@Param("pkExp")String pkExp);
    
    /**
     * 查询科研分析报表
     * @param map
     * @return
     */
    public List<EmrRptList> queryRptList(Map map);
    
    /**
     * 查询科研条件设置列表
     * @param map
     * @return
     */
    public List<EmrRptCnd> queryRptCndList(Map map);
    
    /**
     * 查询科研数据导出列表
     * @param map
     * @return
     */
    public List<EmrRptExp> queryRptExpList(Map map);
}

