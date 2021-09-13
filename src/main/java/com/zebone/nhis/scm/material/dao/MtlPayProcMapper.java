package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.material.vo.MtlPdPayVo;
import com.zebone.nhis.scm.material.vo.MtlPdStDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 物资-付款处理Mapper
 * @author chengjia
 *
 */
@Mapper
public interface MtlPayProcMapper {

	/**
	 * 查询待付款供应商列表
	 * @param param(pk_org)
	 * @return
	 */
	public List<Map<String,Object>> queryPaySupplyerList(Map<String,Object> param);
	
	/**
	 * 查询待付款列表
	 * @param param(pk_supplyer)
	 * @return list
	 */
	public List<MtlPdStDtVo> queryStPayList(Map<String,Object> param);
	
	
	/**
	 * 查询已付款列表
	 * @param param
	 * @return list
	 */
	public List<MtlPdPayVo> queryPdPayList(Map<String,Object> param);
	
	
	/**
	 * 查询付款明细
	 * @param param
	 * @return list
	 */
	public List<MtlPdStDtVo> queryPdPayDtList(Map<String,Object> param);	
}
