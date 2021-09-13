package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.nhis.scm.st.vo.InventoryDtVo;
import com.zebone.nhis.scm.st.vo.InventoryVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InventoryMapper {
   /***
	 * 查询盘点单
	 * @param map{pkStore，codeInv，dateBegin，dateEnd，euStatus}
	 * @return
	 */
	public List<InventoryVo> queryInvenList(Map<String,Object> map);
	/**
	 * 查询盘点明细--显示使用明细
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<InventoryDtVo> queryInvenDtList(@Param("pkPdinv") String pkPdinv);
	/**
	 * 查询盘点明细--真实明细
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<InventoryDtVo> queryInvenRealDtList(@Param("pkPdinv") String pkPdinv);
	
	/**
	 * 根据仓库查询物品全部库存
	 * @param map{pkStore}
	 * @return
	 */
	public List<InventoryDtVo> queryAllPdStockList(@Param("pkStore") String pkStore);
	/**
	 * 根据不同条件查询库存
	 * @param map{pkStore,posis,drugtypes,dtpois,usecates,antis,flagPrecious,flagVacc,flagRm,flagReag,flagHis}
	 * @return
	 */
	public List<InventoryDtVo> queryPdStockByConList(Map<String,Object> map);
	
	/**
	 * 查询某物品账面数量
	 * @param map{pkStore}
	 * @return
	 */
	public List<InventoryDtVo> queryPdAccNum(@Param("pkStore") String pkStore,@Param("pkPd") String pkPd);
	
	/**
	 * 查询物品账面数量
	 * @param map{pkStore}
	 * @return
	 */
	public List<InventoryDtVo> queryPdAccNumBymode(Map<String,Object> paramMap);
	
	/**
	 * 根据盘点单主键查询盘点明细，含金额
	 * @param map{pkPdinv}
	 * @return
	 */
	public List<InventoryDtVo> queryInvtDtByDiff(Map<String,Object> map);
	
	/**
	 * 查询物品账面数量
	 * @param map{pkStore}
	 * @return
	 */
	public List<PdStockVo> queryAccNum(Map<String,Object> map);
	
	/**
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryBatchNoByPkPd(Map<String,Object> paramMap);
	
	/**
	 * 选择-全部数据（明细）
	 * @param pkStore {"pkStore":"当前仓库"}
	 * @return
	 */
	public List<Map<String,Object>> qryRealInvDtAll(String pkStore);
	
	/**
	 * 选择-部分数据（明细）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryRealInvDtKind(Map<String,Object> paramMap);
}