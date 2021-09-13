package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptMz;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybRptMzMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbRptMz getInsZsybRptMzById(@Param("pkInsacct")String pkInsacct); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbRptMz> findAllInsZsybRptMz();    
    
    /**
     * 保存
     */
    public int saveInsZsybRptMz(InsZsBaYbRptMz insZsybRptMz);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybRptMz(InsZsBaYbRptMz insZsybRptMz);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybRptMz(@Param("pkInsacct")String pkInsacct);
    
    /**
     * 获取明细
     */
    public List<InsZsBaYbRptMz> getYbMx(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

