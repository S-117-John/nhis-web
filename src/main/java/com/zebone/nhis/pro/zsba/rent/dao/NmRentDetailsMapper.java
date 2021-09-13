package com.zebone.nhis.pro.zsba.rent.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.rent.vo.NmRentDetails;
import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface NmRentDetailsMapper {
	
	/**
     * 根据主键查询
     */
    public NmRentDetails getById(@Param("pkRentDetails") String pkRentDetails); 
    
    /**
     * 查询对应出租记录的详情
     */
    public List<Map<String,Object>> findByPkRent(@Param("pkRent") String pkRent);    
    
    /**
     * 保存
     */
    public int saveEntity(NmRentDetails entity);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEntity(NmRentDetails entity);
    
    /**
     * 根据主键删除
     */
    public int deleteById(@Param("pkRentDetails") String pkRentDetails);
    
    /**
     * 根据出租记录删除
     */
    public int deleteByPkRent(@Param("pkRent") String pkRent);

}
