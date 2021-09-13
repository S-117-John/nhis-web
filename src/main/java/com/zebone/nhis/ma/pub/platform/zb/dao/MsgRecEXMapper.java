package com.zebone.nhis.ma.pub.platform.zb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface MsgRecEXMapper {
	/**
	 * 查询住院医嘱信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOrder(List<String> pkCnords);
	
	/**
	 * 查询医嘱执行信息
	 * @param occList
	 * @return
	 */
	public List<Map<String,Object>> queryOrderItem(List<String> occList);

}
