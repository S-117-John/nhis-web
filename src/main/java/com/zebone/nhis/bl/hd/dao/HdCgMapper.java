package com.zebone.nhis.bl.hd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.bl.hd.vo.CostVo;
import com.zebone.nhis.bl.hd.vo.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 透析业务
 * @author leiminjian
 *
 */
@Mapper
public interface HdCgMapper {
	
	//查询透析治疗患者
	public List<Map<String,Object>> queryHdPi(Map<String,Object> map);
	
	//查询费用和余额-查询账户余额和费用信息
	public List<Map<String,Object>> queryAmtAcc(Map<String,Object> map);
	
	//查询费用和余额-未结费用
	public List<Map<String,Object>> queryUseAmt(Map<String,Object> map);
	
	//查询排班
	public List<Map<String,Object>> querySchHd(Map<String,Object> map);
	
	//查询当前患者的透析治疗记录
	public List<Map<String,Object>> queyrPiRecord(Map<String,Object> map);
	
	//查询患者就诊信息
	public List<Map<String,Object>> queryPiVis(Map<String,Object> map);
	
	//查询治疗费用信息
	public List<Map<String,Object>> queryVisCost(Map<String,Object> map);
	
	//查询结算费用分类
	public List<Map<String,Object>> queryCostClassification(Map<String,Object> map);
	
	//查询结算费用明细
	public List<Map<String,Object>> queryCostDetailed(Map<String,Object> map);
	
	public List<BdItem> getBdItemsByCon(List<BlOpDt> list);
	
	/**
	 * 根据退费参数查询记费记录
	 * @param vos
	 * @return
	 */
	public List<BlOpDt> QryBlDtOpByRefunds(List<RefundVo> vos);
	
	//查询患者消费总额
	public List<CostVo> queryPvCost(Map<String,Object> map);
}
