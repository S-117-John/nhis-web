package com.zebone.nhis.compay.ins.zsrm.dao.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybDict;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsrmQgBaseDictMaintainMapper {
	List<InsQgybDict> querySZYBBaseDictType(String euHpdicttype);

	List<InsQgybDict> querySZYBBaseDictInfo(Map<String, Object> paramMap);

	/**
	 * 批量更新医保数据信息
	 * @param list
	 */
	public void batchUpdateSZYBBaseDict(@Param(value="list")List<InsQgybDict> list);
	
	
	/**
	 * 批量更新医保数据信息
	 * @param list
	 */
	public void batchUpdateSZYBBaseDict1(@Param(value="list")List<Map<String,Object>>  list);
}
