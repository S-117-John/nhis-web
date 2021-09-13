package com.zebone.nhis.pro.zsba.ex.dao;

import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 执行单处理类
 * @author zhangtao
 *
 */
@Mapper
public interface OrderExecBaMapper {
	/**
	 * 根据医嘱主键，作废医嘱执行单
	 * @param map{pkOrds}
	 * @return
	 */
	public void cancelExecListByPkOrd(Map<String,Object> map);
	
}
