package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.material.vo.MtlPdPlanDtInfo;
import com.zebone.nhis.scm.material.vo.MtlPuOrdDtInfo;
import com.zebone.nhis.scm.material.vo.MtlPuOrdInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlPuOrderMapper {
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
	public List<MtlPdPlanDtInfo>  queryPuPlanDtByCon(Map<String,Object> map);
	/**
	 * 查询采购订单列表
	 * @param map
	 * @return
	 */
	public List<MtlPuOrdInfo> queryPuOrderList(Map<String,Object> map);
	/**
	 * 查询采购订单明细
	 * @param map
	 * @return
	 */
	public List<MtlPuOrdDtInfo> queryPuOrderDtList(@Param("pkPdpu") String pk_pdpu);
	
}
