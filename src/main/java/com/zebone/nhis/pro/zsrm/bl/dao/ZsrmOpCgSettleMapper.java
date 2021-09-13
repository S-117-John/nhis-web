package com.zebone.nhis.pro.zsrm.bl.dao;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 中山人民医院门诊结算查询服务
 */
@Mapper
public interface ZsrmOpCgSettleMapper {

    /**
     * 查询48小时内未缴费诊金或医嘱
     * @param paramMap
     * @return
     */
    public Integer getUnSettleRecordNum(@Param("pkPi") String pkPi);

    /**
     * 查询患者的退费结算记录
     * @param pkPi
     * @return
     */
	public List<Map<String, Object>> qryPatiRefSettle(@Param("pkPi") String pkPi);
	
    /**
     * 查询患者的退费结算明细
     * @param pkSettle
     * @return
     */
	public List<Map<String, Object>> qryPatiRefSettleDt(@Param("pkSettle") String pkSettle);
	
	/**
	 *  查询患者费用明细
	 * @param pkCgopList
	 */
	public List<BlOpDt> qryblOpDtList(List<String> pkCgopList);

	/**
	 * 查询交款记录列表
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getPaymentRecords(Map<String, Object> paramMap);

	/**
	 * 1）校验数据，录入核销金额不得大于剩余未核销金额；
	 */
	BigDecimal compareTheAmount(Map<String, Object> paramMap);

	List<Map<String, Object>> getBlDepoRemove(Map<String, Object> paramMap);
}
