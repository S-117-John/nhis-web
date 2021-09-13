package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptLx;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybRptLxMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbRptLx getInsZsybRptLxById(@Param("pkInsacct")String pkInsacct); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbRptLx> findAllInsZsybRptLx();    
    
    /**
     * 保存
     */
    public int saveInsZsybRptLx(InsZsBaYbRptLx insZsybRptLx);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybRptLx(InsZsBaYbRptLx insZsybRptLx);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybRptLx(@Param("pkInsacct")String pkInsacct);
    
    /**
     * 获取明细
     */
    public List<InsZsBaYbRptLx> getYbMx(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

