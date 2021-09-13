package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.mybatis.DynaBean;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnOrdHerbMapper {
	
	/**
	 * 获取草药医嘱定义
	 * @param pkOrder
	 * @return
	 */
	BdOrd getHerbOrderSrv(String pkOrder);
	/**
	 * 获取草药医嘱列表（不包括明细）
	 * @param pkPv
	 * @return
	 */
	List<Map<String,Object>> getHerbOrders(@Param("pkPv") String pkPv, @Param("herbOrder")String herbOrder);
	/**
	 * 获取草药医嘱的明细
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> getHerbItems(Map<String,Object> paramMap);

	/**
	 * 获取草药医嘱的明细--sql
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> getHerbItemsSqlServer(Map<String,Object> paramMap);
    /**
     * 撤销草药医嘱
     * @param pkOpList
     * @param empSn
     * @param empName
     */
    void cancelHerbOrder(@Param("pkOrderList") List<String> pkOpList, @Param("empSn")String empSn, @Param("empName")String empName);
    /**
     * 签署草药医嘱
     * @param pkOpList
     */
    void signHerbOrder(@Param("pkOrderList")List<String> pkOpList);
    
    List<String> getPdStore(String pkDept);

    /**
     * 删除草药申请
     * @param pkOrdop
     */
    void removeHerbOrder(String pkCnOrd);
    
    /**
     * 根据草药药品主键查询药品单位
     */
    List<BdUnit> getUnitVol(@Param("pkPd") String pkPd);
    
    public List<Map<String,Object>> queryHerbTempList(Map<String,Object> paramMap);

	/**
	 * 查询草药明细
	 * @param pkCnOrd
	 * @return
	 */
	List<CnOrdHerb> findByPkCnOrd(String pkCnOrd);
}