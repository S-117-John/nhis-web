package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRpt;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybRptMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbRpt getInsZsybRptById(@Param("pkAcctrpt")String pkAcctrpt); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbRpt> findAllInsZsybRpt();    
    
    /**
     * 保存
     */
    public int saveInsZsybRpt(InsZsBaYbRpt insZsybRpt);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybRpt(InsZsBaYbRpt insZsybRpt);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybRpt(@Param("pkAcctrpt")String pkAcctrpt);
    
    /**
     * 获取报表类型获取医保月结报表数据
     */
    public List<InsZsBaYbRpt> getRptList(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

