package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.InpatientVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InpatientMapper {
	
	/**
	 * 查询左侧列表
	 * @param pkPv
	 * @return
	 * @throws Exception
	 */
	public List<InpatientVo> selInThrDayLeftList(Map params) throws Exception;

	/**
	 * 右侧明细
	 * @param pkiprep
	 * @return
	 * @throws Exception
	 */
	public InpatientVo selThrDayRightDetail(Map params) throws Exception;
}
