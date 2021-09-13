package com.zebone.nhis.pro.pskq.health.dao;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author
 */
@Mapper
public interface PskqHealthMapper{
	/**
	 * 通过pkSettle 查询结算数据
	 * @param pkSettle
	 * @return
	 */
	public Map<String,Object> querySettleDataByPkSettle(String pkSettle);

	public Map<String, Object> queryDepositDataByPkDepo(String pkDepo);
}

