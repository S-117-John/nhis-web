package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PuOrderDtVo;
import com.zebone.nhis.scm.pub.vo.PuOrderVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.pub.vo.PdStVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PuInStoreMapper {
	/**
	 * 查询采购订单
	 * @param map
	 * @return
	 */
	public List<PuOrderVo> queryOrderList(Map<String,Object> map);
	/**
	 * 查询采购订单对应的明细
	 * @param map
	 * @return
	 */
	public List<PuOrderDtVo> queryOrderDtList(Map<String,Object> map);
	
	/***
	 * 查询采购入库单
	 * @param map
	 * @return
	 */
	public List<PdStVo> queryPuPdStList(Map<String,Object> map);
	/**
	 * 查询采购入库明细
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> queryPuPdStDetailList(Map<String,Object> map);
	
	/**
	 * 根据科室主键查询仓库主键
	 * @param map
	 * @return
	 */
	public String qryPkStoreByPkdept(Map<String,Object> map);
	
	/**
	 * 获取采购药品单价
	 * @param paramMap
	 * @return
	 */
	public Double getPdPuPrice(Map<String,Object> paramMap);
}
