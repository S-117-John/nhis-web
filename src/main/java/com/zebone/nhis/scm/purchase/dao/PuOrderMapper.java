package com.zebone.nhis.scm.purchase.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.pub.vo.PdPlanDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PuOrderMapper {
    /**
     * 以供应商为单位，查询采购计划
     * @param map
     * @return
     */
	public List<Map<String,Object>>  queryPuPlanByCon(Map<String,Object> map);
	 /**
     * 以供应商为单位，查询采购计划
     * @param map
     * @return
     */
	public List<PdPlanDtVo>  queryPuPlanDtByCon(Map<String,Object> map);
	/**
	 * 查询采购订单列表
	 * @param map
	 * @return
	 */
	public List<PuOrderVo> queryPuOrderList(Map<String,Object> map);
	/**
	 * 查询采购订单明细
	 * @param map
	 * @return
	 */
	public List<PuOrderDtVo> queryPuOrderDtList(@Param("pkPdpu") String pk_pdpu);
	
}
