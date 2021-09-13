package com.zebone.nhis.scm.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.pub.vo.PdOutDtParamVo;
import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdStPubMapper {
	/**
	 * 根据不同条件查询库存记录主键
	 * @param map{pkPd,batchNo,dateExpire,priceCost,price,pkStore}
	 * @return
	 */
	public List<Map<String,Object>> queryPkPdStoreByCon(Map<String,Object> map);
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
	/**
	 * 查询物品是否全部在本仓库中存在
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdByStore(Map<String,Object> map);
	
	/**
	 * 查询调价历史
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> queryRepriceHist(Map<String,Object> map);
	
	/**
	 * 获取通过调拨申请，获取待出库明细数据
	 * @param paramMap
	 * @return
	 */
	public List<PdOutDtParamVo> getWaitInStdtDataList(Map<String, Object> paramMap);
	
	/**
	 * 调拨申请待出库时，服务仓库无可用入库数据时，获取药品基本信息
	 * @param paramMap
	 * @return
	 */
	public List<PdOutDtParamVo> getUnHaveInStdtList(Map<String,Object> paramMap);

	/**
	 * 校验当前仓库是否初始建账
	 * @return
	 */
	public Integer verfyPdIsStRecord(Map<String,Object> map);

	List<Map<String,Object>> qryStockNum(Map<String,Object> map);

	List<PdOutDtParamVo> queryBatchInStDtByOp(Map<String,Object> map);

}
