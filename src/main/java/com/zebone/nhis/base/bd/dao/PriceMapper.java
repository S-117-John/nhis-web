package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.base.pub.vo.RptPrintDataDto;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PriceMapper {

	public List<BdPayer> findAllPayers(@Param("pkOrg") String pkOrg);
	
	/**
	 * 查询医保计划 
	 * @param 
	 * @param 
	 * @return
	 */
	List<BdHp> queryHp();
	
	/**
	 * 查询医保计划下的策略配置
	 * @param 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> queryHpDivConfig(String pkHp);
	
	/**
	 * 查询医保计划 使用机构
	 * @param 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> queryHpOrg(String pkHp);
	
	/**
	 * 查询医保计划拓展属性
	 * @param 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> queryHpDictAttr(@Param("pkOrgUse")String pkOrgUse,@Param("pkDict")String pkDict);
	
	/**
	 * 查询医保计划 使用科室
	 * @param 
	 * @param 
	 * @return
	 */
	List<Map<String, Object>> queryHpDept(String pkHp);
}
