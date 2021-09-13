package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.platform.modules.mybatis.Mapper;




@Mapper
public interface HpCgdivMapper {

	List<Map<String,Object>> qryPd(@Param("pkHpCgdiv") String pkHpCgdiv);
	
	List<Map<String,Object>> qryItem(@Param("pkHpCgdiv") String pkHpCgdiv);
	
	List<Map<String,Object>> qryCate(@Param("pkHpCgdiv") String pkHpCgdiv);
	
	List<BdHpCgdivItem> exportsMed(@Param("pkHpcgdiv") String pkHpcgdiv);
	
	List<BdHpCgdivItem> exportsItem(@Param("pkHpcgdiv") String pkHpcgdiv);
}
