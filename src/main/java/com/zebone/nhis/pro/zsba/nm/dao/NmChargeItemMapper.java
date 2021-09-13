package com.zebone.nhis.pro.zsba.nm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.nm.vo.NmChargeItem;
import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface NmChargeItemMapper {
	
	
	/**
     * 根据主键查询
     */
    public NmChargeItem getById(@Param("pkCi") String pkCi); 

    /**
     * 查询出所有记录
     */
    public List<NmChargeItem> findAll();    
    
    /**
     * 查询出所有启用中的记录
     */
    public List<NmChargeItem> findByUse(@Param("pkDept") String pkDept, @Param("showSite") String showSite);    
    
    /**
     * 保存
     */
    public int saveCi(NmChargeItem ci);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateCi(NmChargeItem ci);
    
    /**
     * 根据主键删除
     */
    public int deleteCi(@Param("pkCi") String pkCi);

}
