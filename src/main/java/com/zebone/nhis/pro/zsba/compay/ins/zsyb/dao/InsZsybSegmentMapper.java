package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSegment;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybSegmentMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbSegment getInsSegmentById(@Param("pkInssegment")String pkInssegment); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbSegment> findAllInsSegment();    
    
    /**
     * 保存
     */
    public int saveInsSegment(InsZsBaYbSegment insSegment);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsSegment(InsZsBaYbSegment insSegment);
    
    /**
     * 根据主键删除
     */
    public int deleteInsSegment(@Param("pkInssegment")String pkInssegment);
}

