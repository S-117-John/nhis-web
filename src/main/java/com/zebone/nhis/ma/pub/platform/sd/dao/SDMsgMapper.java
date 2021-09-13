package com.zebone.nhis.ma.pub.platform.sd.dao;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author
 */
@Mapper
public interface SDMsgMapper{
	/**
	 * 查询发票信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryInvoice(Map<String,Object> paramMap);

	/**
	 * 根据就诊主键查询，费用分类
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryZMRDataByPkPv(Map<String,Object> paramMap);

	/**
	 * 根据医嘱查询门诊收费信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOpBlInfo(Map<String,Object> paramMap);

	/**
	 * 根据医嘱查询住院收费信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryIpBlInfo(Map<String,Object> paramMap);

	/**
	 * 查询住院医嘱信息
	 * @param paramMap： pkCnords：医嘱主键集合 ；或者ordsns医嘱号集合
	 * @return
	 */
	public List<Map<String,Object>> queryOrder(Map<String, Object> paramMap);

	/**
	 * 查询发退药药品请领信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOrderPKPI(Map<String, Object> paramMap);

	/**
	 * Z06 - 查询药品基本信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPdInfoList(Map<String,Object> paramMap);

    /**
     * 查询HL7消息记录
     * @param map
     * @return
     */
    public List<SysMsgRec> queryMsgList(Map<String,Object> map);
   
    /**
     * 查询患者信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPatList(Map<String,Object> map);


    /**
     * 查询申请医嘱信息（暂时作废）
     * @param map
     * @return
     */
    //public List<Map<String,Object>> queryReqOrdList(Map<String,Object> map);


    /**
     * 查询病人信息，后期发现缺少什么字段在补充
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPatListOut(Map<String, Object> map);

	/**
	 * 根据pk_op查询手术信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOperation(Map<String,Object> map);

	/**
	 * 查询医嘱信息
	 * @param pkcnords
	 * @return
	 */
	public List<Map<String,Object>> queryOrderByPkCnord(List<String> pkCnords);


	/**
	 * 通过发药单号查询医嘱执行信息
	 * @param codeDe
	 * @return
	 */
	public List<Map<String,Object>> queryPddeExorderInfo(String codeDe);
	
	/**
	 * 查询高值耗材费用信息
	 * @param pkcgipList
	 * @return
	 */
	public List<Map<String,Object>> queryHighvaluIpcg(List<String> pkcgipList);
	
	
	/**
	 * 根据母亲就诊信息及就诊病区查询婴儿信息
	 * @param 
	 * @return
	 */
	public List<Map<String,Object>> getInfantListByMother(Map<String,Object> paramMap);
	
	/**
	 * 查询会诊申请和会诊应答
	 * @return
	 */
	public List<Map<String,Object>> queryConsultApply(Map<String,Object> paramMap);
	/**
	 * 会诊应答
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryConsultResponse(Map<String,Object> paramMap);
	
	
	/**
	 * 高值耗材门诊数据构建
	 * @param pkcgOpList
	 * @return
	 */
	public List<Map<String,Object>> queryHighvaluOpcg(List<String> pkcgOpList);
	
	/**
	 * 通过pkSettle 查询结算数据
	 * @param pkSettle
	 * @return
	 */
	public Map<String,Object> querySettleDataByPkSettle(String pkSettle);
}

