package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DiagTypeMapper {
	/**
	 * 删除时校验是否被引用
	 * @param params
	 * @return
	 */
	public int isQuote(Map<String, String> params);
	
	public List<BdTermDiagTreatway> qryDiagtreatway(Map<String, String> params);

	public void delTermDiagTreatway(String pkDiagtreatway);

	public int updateByPk(BdTermDiagTreatway bdTermDiagTreatway);
}
