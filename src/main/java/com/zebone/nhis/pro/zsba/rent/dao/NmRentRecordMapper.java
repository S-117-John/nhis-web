package com.zebone.nhis.pro.zsba.rent.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.rent.vo.NmRentRecord;
import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface NmRentRecordMapper {
	
	/**
     * 根据主键查询
     */
    public NmRentRecord getById(@Param("pkRent") String pkRent); 

    /**
     * 查询病人所有的出租记录
     */
    public List<Map<String,Object>> findByCodeIp(@Param("codeIp") String codeIp);    
    
    /**
     * 保存
     */
    public int saveEntity(NmRentRecord entity);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEntity(NmRentRecord entity);
    
    /**
     * 根据主键删除
     */
    public int deleteById(@Param("pkRent") String pkRent);

}
