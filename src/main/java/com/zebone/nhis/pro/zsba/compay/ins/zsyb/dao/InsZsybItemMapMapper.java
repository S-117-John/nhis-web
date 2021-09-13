package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbItemMap;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybItemMapMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbItemMap getInsItemMapById(@Param("pkInsitemmap")String pkInsitemmap); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbItemMap> findAllInsItemMap();    
    
    /**
     * 保存
     */
    public int saveInsItemMap(InsZsBaYbItemMap insItemMap);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsItemMap(InsZsBaYbItemMap insItemMap);
    
    /**
     * 根据主键删除
     */
    public int deleteInsItemMap(@Param("pkInsitemmap")String pkInsitemmap);
}

