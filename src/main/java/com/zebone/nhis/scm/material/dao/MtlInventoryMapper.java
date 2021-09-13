package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.material.vo.MtlInventoryDtVo;
import com.zebone.nhis.scm.material.vo.MtlInventoryVo;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlInventoryMapper {
   /***
	 * 查询盘点单
	 * @param map{pkStore，codeInv，dateBegin，dateEnd，euStatus}
	 * @return
	 */
	public List<MtlInventoryVo> queryInvenList(Map<String,Object> map);
	/**
	 * 查询盘点明细--显示使用明细
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<MtlInventoryDtVo> queryInvenDtList(@Param("pkPdinv") String pkPdinv);
	/**
	 * 查询盘点明细--真实明细
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<MtlInventoryDtVo> queryInvenRealDtList(@Param("pkPdinv") String pkPdinv);
	
	/**
	 * 根据不同条件查询库存|全部
	 * @param map{flagAll,pkStore,posi,dtPdtype,pkPdcate,flagPrecious,flagSingle,flagHis}
	 * @return
	 */
	public List<MtlInventoryDtVo> queryPdStockByConList(Map<String,Object> map);
	
	/**
	 * 查询某物品账面数量
	 * @param map{pkStore}
	 * @return
	 */
	public List<MtlInventoryDtVo> queryPdAccNum(@Param("pkStore") String pkStore,@Param("pkPd") String pkPd);
	
	/**
	 * 根据盘点单主键查询盘点明细，含金额
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<MtlInventoryDtVo> queryInvtDtByDiff(Map<String,Object> map);
	
	/**
	 * 查询物品账面数量
	 * @param map{pkStore}
	 * @return
	 */
	public List<PdStockVo> queryAccNum(Map<String,Object> map);
	
	/**
	 * 查询物品的单品记录
	 * @param map{pkPd,pkStore}
	 * @return
	 */
	public List<Map<String,Object>> queryPdSin(Map<String,Object> map);
}
