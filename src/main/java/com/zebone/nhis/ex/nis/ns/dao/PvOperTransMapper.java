package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.PvOperTrans;
import com.zebone.nhis.ex.nis.ns.vo.PvOperTransItemVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvOperTransMapper {

	/**
	 * 根据手术单查询手术交接单
	 * @param paramMap( pk_ordop)
	 * @return
	 */
	public PvOperTrans queryPvOperTrans(Map<String,Object> paramMap);
	
	/**
	 * 根据手术交接单查询术前准备项目内容
	 * @param paramMap( pk_opertrans)
	 * @return
	 */
	public List<PvOperTransItemVo> queryPvOperTransItem(Map<String,Object> paramMap);
	
}
