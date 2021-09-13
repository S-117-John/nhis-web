package com.zebone.nhis.compay.ins.shenzhen.dao.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDict;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ShenZhenYBBaseDictMaintainMapper {

	List<InsSzybDict> querySZYBBaseDictType(String euHpdicttype);

	List<InsSzybDict> querySZYBBaseDictInfo(Map<String, Object> paramMap);

	/**
	 * 批量更新医保数据信息
	 * @param list
	 */
	public void batchUpdateSZYBBaseDict(@Param(value="list")List<InsSzybDict> list);
	
	
	/**
	 * 批量更新医保数据信息
	 * @param list
	 */
	public void batchUpdateSZYBBaseDict1(@Param(value="list")List<Map<String,Object>>  list);
	
	

}
