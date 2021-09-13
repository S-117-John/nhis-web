package com.zebone.nhis.pro.zsba.mz.opcg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdDept;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pro.zsba.mz.opcg.vo.PibaseVo;
import com.zebone.nhis.scm.pub.vo.PdStockVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsbaOpCgQueryMapper {

	/**
	 * 根据pkpv获取pv_encounter的接诊医生
	 * @param pkPv
	 * @return
	 */
	public PvEncounter getDrawDocName(String pkPv);
	
	/**
	 * 根据机构和医嘱项目字典主键查询对应的机构和科室关系
	 * @param pkOrd
	 * @param pkOrg
	 * @return
	 */
	public List<BdOrdDept> getBdOrdDepts(Map<String,Object> params);
	
	/**
	 * 根据物品主键查询物品库存
	 * @param params
	 * @return
	 */
	public List<PdStockVo> getStocks(Map<String,Object> params);
	
	/**
	 * 查询门诊是否停用
	 * @param params
	 * @return
	 */
	public int getIsStopOp(Map<String, String> params);
    /**
     * 查询患者住院就诊及使用主医保信息
     * @param map
     * @return
     */
	public PvEncounter queryPvAndHpInof(String pkPi);

	/**
	 * 根据主键查询医嘱表
	 * @param pkCnords
	 * @return
	 */
	public List<CnOrder> qryCnOrderList(Map<String, Object> map);
	
	//查询结算费用明细信息
	public List<Map<String,Object>> querySettleCgDetail(Map<String,Object> paramMap);
	
	//查询患者结算基本信息
	public Map<String,Object> querySettleInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryPatientSettleList(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> queryQgybSettleDetail(Map<String,Object> paramMap);
	
	List<PibaseVo> getPibaseVoList(PvEncounter pvEncounter);
}
