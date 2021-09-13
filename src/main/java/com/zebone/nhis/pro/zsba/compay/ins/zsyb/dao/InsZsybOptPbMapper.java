package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbOptPb;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybOptPbMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbOptPb getInsOptPbById(@Param("pkOptpb")String pkOptpb); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbOptPb> findAllInsOptPb();    
    
    /**
     * 保存
     */
    public int saveInsOptPb(InsZsBaYbOptPb insOptPb);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsOptPb(InsZsBaYbOptPb insOptPb);
    
    /**
     * 根据主键删除
     */
    public int deleteInsOptPb(@Param("pkOptpb")String pkOptpb);
    
    public List<Map<String,Object>> getOperationDateData(Map<String,Object> map);
}

