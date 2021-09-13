package com.zebone.nhis.compay.ins.shenzhen.dao.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybDictMap;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ShenZhenMediBaseDictMapMapper {
	/**
	  * 查询医保基础数据字典类型 ，名称
	 * @param euHpdicttype 
	 * @return
	 */
	public List<InsSzybDictMap> getMediType(String euHpdicttype);
	
	/**
	    *     根据医保类别查询医保基础数据字典信息 
	 */
	public List<Map<String, Object>> querySZMediBaseData(Map<String,Object> paramMap);
	
	/**
	   *      批量删除（更新）
	 * @param delList
	 */
	public void batchUpdateInsSzybInfo(@Param(value="list")List<InsSzybDictMap> list);
}
