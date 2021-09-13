package com.zebone.nhis.scm.purchase.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.module.scm.pub.BdSupplyer;
import com.zebone.nhis.scm.purchase.vo.PuSearchQueryDrugBySupplyerResult;
import com.zebone.nhis.scm.purchase.vo.PuSearchQuerySupplyerByDrugResult;
import com.zebone.platform.modules.mybatis.Mapper;

/*
 * 采购查询mapper
 */
@Mapper
public interface PuSearchMapper {
	
	public List<BdSupplyer> querySupplyerByParam(Map<String, Object> map);
	
	public List<PuSearchQueryDrugBySupplyerResult> queryDrugBySupplyer(Map<String, Object> map);
	
	public List<BdPd> queryDrugPyParam(Map<String, Object> map);
	
	public List<PuSearchQuerySupplyerByDrugResult> querySupplyerByDrug(Map<String, Object> map);
	
	public List<Map<String, Object>> querySupplyerPrint(Map<String, Object> map);
}
