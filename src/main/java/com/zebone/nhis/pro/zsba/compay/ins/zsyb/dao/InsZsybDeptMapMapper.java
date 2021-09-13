package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDeptMap;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybDeptMapMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbDeptMap getInsDeptMapById(@Param("pkInsdeptmap")String pkInsdeptmap); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbDeptMap> findAllInsDeptMap();    
    
    /**
     * 保存
     */
    public int saveInsDeptMap(InsZsBaYbDeptMap insDeptMap);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsDeptMap(InsZsBaYbDeptMap insDeptMap);
    
    /**
     * 根据主键删除
     */
    public int deleteInsDeptMap(@Param("pkInsdeptmap")String pkInsdeptmap);
}

