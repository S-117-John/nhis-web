package com.zebone.nhis.scm.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlPdStPubMapper {
	/**
	 * 根据不同条件查询库存记录主键
	 * @param map{pkPd,batchNo,dateExpire,priceCost,price,pkStore}
	 * @return
	 */
	public Map<String,Object> queryPkPdStoreByCon(Map<String,Object> map);
	/**
	 * 查询入库明细
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> queryPdStDetailList(Map<String,Object> map);
	/**
	 * 根据排序条件查询入库明细
	 * @param map
	 * @return
	 */
	public List<PdStDtVo> queryPdStByCon(Map<String,Object> map);
}
