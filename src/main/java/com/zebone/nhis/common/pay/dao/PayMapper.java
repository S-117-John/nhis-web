package com.zebone.nhis.common.pay.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayMapper {
	/**
	 * 订单列表
	 * @param cardNo
	 * @param dtCardtype
	 * @param endDate
	 * @param beginDate
	 * @param pkOrg
	 * @return
	 */
	List<Map<String,Object>> getOrderListSqlSer(Map<String, Object> paramMap);
	List<Map<String,Object>> getOrderListOracle(Map<String, Object> paramMap);
	
	
}
