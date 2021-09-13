package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.st.vo.SpecialPdVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SpecialPdStatisticMapper {
	/**
	 * 查询特殊药品统计信息
	 * @param map
	 * @return
	 */
	public List<SpecialPdVo> querySpecialPdNumList(Map<String,String> map);
	/**
	 * 查询药品出入库明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryStDetailByPd(Map<String,String> map);
	
}
