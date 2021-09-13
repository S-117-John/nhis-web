package com.zebone.nhis.task.ex.dao;

import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 执行单处理类
 * @author
 *
 */
@Mapper
public interface OrderExecAndCgListMapper {
	/**
	 * 根据不同条件查询执行单列表（主要用于查询执行确认与取消执行的执行单列表查询）
	 * @param map{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,cancelFlag,nameOrd,pkPvs,pkDeptNs}
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByCon(Map<String, Object> map);
	
}
