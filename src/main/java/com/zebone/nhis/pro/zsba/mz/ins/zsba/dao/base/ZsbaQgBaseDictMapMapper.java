package com.zebone.nhis.pro.zsba.mz.ins.zsba.dao.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.base.InsQgybDictMap;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaQgBaseDictMapMapper {

	/**
	  * 查询医保基础数据字典类型 ，名称
	 * @param euHpdicttype 
	 * @return
	 */
	public List<InsQgybDictMap> getMediType(String euHpdicttype);
	
	/**
	    *     根据医保类别查询医保基础数据字典信息 
	 */
	public List<Map<String, Object>> querySZMediBaseData(Map<String,Object> paramMap);
	
	/**
	   *      批量删除（更新）
	 * @param delList
	 */
	public void batchUpdateInsSzybInfo(@Param(value="list")List<InsQgybDictMap> list);
}
