package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbZydj;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybZydjMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbZydj getInsZsybZydjById(@Param("pkInszsybzydj")String pkInszsybzydj); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbZydj> findAllInsZsybZydj();    
    
    /**
     * 保存
     */
    public int saveInsZsybZydj(InsZsBaYbZydj insZsybZydj);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybZydj(InsZsBaYbZydj insZsybZydj);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybZydj(@Param("pkInszsybzydj")String pkInszsybzydj);
    
    /**
     * 获取明细
     */
    public List<InsZsBaYbZydj> getYbMx(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

