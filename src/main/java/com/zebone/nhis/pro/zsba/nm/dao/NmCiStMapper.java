package com.zebone.nhis.pro.zsba.nm.dao;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NmCiStMapper {
	
	/**
     * 根据主键查询
     */
    public NmCiSt getById(@Param("pkCiSt") String pkCiSt); 

    /**
     * 保存
     */
    public int saveCiSt(NmCiSt ciSt);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateCiSt(NmCiSt ciSt);
    
    /**
     * 根据主键删除
     */
    public int deleteCiSt(@Param("pkCiSt") String pkCiSt);

}
