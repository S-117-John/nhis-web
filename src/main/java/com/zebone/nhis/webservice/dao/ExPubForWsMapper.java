package com.zebone.nhis.webservice.dao;
import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExPubForWsMapper {
	/**
	 * 查询执行单（主要用于查询执行确认与取消执行的执行单列表查询）
	 * @param map{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,cancelFlag,nameOrd,pkPvs,pkDeptNs}
	 * @return
	 */
	public List<Map<String,Object>> queryExecListByCon(Map<String, Object> param);
}
