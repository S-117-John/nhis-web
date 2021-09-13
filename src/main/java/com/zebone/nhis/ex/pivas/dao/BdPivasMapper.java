package com.zebone.nhis.ex.pivas.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasBatch;
import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasPd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.ex.pivas.vo.PdConditionParam;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdPivasMapper {

	public List<BdPivasBatch> findAllPivasBatchs(@Param("pkDept")String pkDept);
	/**
	 * 查询当前机构下所有静配分类下的物品
	 * @param pkOrg
	 * @return
	 */
	public List<BdPivasPd> queryBdPivasPds(@Param("pkOrg")String pkOrg);

	public List<BdPd> queryBeImportedPds();
	
	public List<Map<String, Object>> queryPdsByCondition(PdConditionParam pdConditionParam);
}
