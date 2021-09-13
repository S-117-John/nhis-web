package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlStoreHouseGoodsMapper {
	
	/**查询仓库物品信息*/
	public List<Map<String,Object>> queMtlPdStores(Map<String,Object> paramMap);
	
	/** 查询可选物资物品*/
	public List<Map<String,Object>> queNewMtlBdPds(Map<String,Object> paramMap);
	
}
