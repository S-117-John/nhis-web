package com.zebone.nhis.webservice.pskq.dao;



import java.util.Map;

import com.zebone.nhis.webservice.pskq.model.ReserveOutpatient;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PskqSchPubForWsMapper {
	/**
	 * 查询预约记录
	 * @param map
	 * @return
	 */
	ReserveOutpatient queryOrders(Map<String, Object> map);
}
