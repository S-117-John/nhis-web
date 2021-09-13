package com.zebone.nhis.compay.ins.lb.dao.szyb;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsSzybItemMapMapper {
	/**
	 * 查询宿州医保药品与医院药匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qrySzybPdDicWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州医保收费项目与医院信息匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qrySzybItemDicWithInfo(Map<String,Object> paramMap);
	
	
}
