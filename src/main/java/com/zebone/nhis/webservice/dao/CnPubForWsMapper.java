package com.zebone.nhis.webservice.dao;
import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnPubForWsMapper {
	/**
	 * 灵璧查询门诊患者待缴纳处方信息
	 * @param pkPv
	 * @return
	 */
	public List<Map<String, Object>> LbqueryCnOrderUnpaid(Map<String, Object> param);
	/**
	 * webservice
	 * 医嘱信息查询
	 * @param pkPv
	 * @return
	 */
	public List<Map<String, Object>> queryCnOrderWeb(Map<String, Object> param);
}
