package com.zebone.nhis.pro.zsba.rent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.rent.vo.NmRentApp;
import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface NmRentAppMapper {
	
	/**
     * 根据主键查询
     */
    public NmRentApp getById(@Param("pkApp") String pkApp); 

    /**
     * 查询出所有记录
     */
    public List<NmRentApp> findAll();    
    
    /**
     * 查询出所有启用中的记录
     */
    public List<NmRentApp> findByUse();    
    
    /**
     * 保存
     */
    public int saveApp(NmRentApp app);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateApp(NmRentApp app);
    
    /**
     * 根据主键删除
     */
    public int deleteApp(@Param("pkApp") String pkApp);

}
