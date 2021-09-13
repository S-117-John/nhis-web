package com.zebone.nhis.pro.zsba.nm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NmCiStDetailsMapper {
	
	/**
     * 根据主键查询
     */
    public NmCiStDetails getById(@Param("pkCiStd") String pkCiStd); 

    /**
     * 查询出所有记录
     */
    public List<NmCiStDetails> findByPropertys(@Param("params") Map<String,String> params);    
    
    /**
     * 保存
     */
    public int saveCiStd(NmCiStDetails ciStd);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateCiStd(NmCiStDetails ciStd);
    
    /**
     * 根据主键删除
     */
    public int deleteCiStd(@Param("pkCiStd") String pkCiStd);

}
