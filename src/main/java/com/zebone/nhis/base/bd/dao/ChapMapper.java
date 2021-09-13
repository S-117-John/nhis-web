package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.price.BdChap;
import com.zebone.platform.modules.mybatis.Mapper;


@Mapper
public interface ChapMapper {
	int saveChaps();
	
	List<BdChap> findByPkOrg(Map<String,String> mapParam);
	

	
	//int delByPkChap(String pkChap);

	

	

	
}
