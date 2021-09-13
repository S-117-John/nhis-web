package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.bl.pub.vo.InsuCountVo;
import com.zebone.nhis.bl.pub.vo.InvInfo;
import com.zebone.nhis.bl.pub.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.vo.OpBlCcPayAndPiVo;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊日结账、汇总结账mapper
 * 
 * @author wangpeng
 * @date 2016年10月24日
 *
 */
@Mapper
public interface OpblccMapper {
	
	/** 根据结账主键获取结账信息 */
	BlCc getBlCcById(String pkCc);
	
	/** 根据操作员获取上一次结账信息 */
	BlCc getLastBlCcByUserId(Map<String,Object> mapParam);
	
	/** 根据操作员获取上一次结账信息（sqlserver） */
	BlCc getLastBlCcByUserIdForSql(String pkEmp);
	
	/** 根据操作员获取时间最早的结算记录 */
	BlSettle getLastBlSettleByUserId(Map<String,Object> mapParm);
	
	/** 根据操作员获取时间最早的结算记录（sqlserver） */
	BlSettle getLastBlSettleByUserIdForSql(String pkEmp);
	
	/** 根据操作员获取未结账的收费结算-交款记录 */
	List<BlCcPay> getBlDepositNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未结账的患者账户交退款记录 */
	List<BlCcPay> getBlDepositPiNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未结账的结算明细（支付信息）  */
	List<BlSettleDetail> getBlSettleDetailNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未挂号医保分组信息  */
	List<InsuCountVo> getInsuCountVoList(Map<String, Object> mapParam);
	
	/** 获取收费结算-发票记录分组信息（使用） */
	List<BlInvoice> getBlInvoiceGroup(Map<String, Object> mapParam);
	
	/** 获取收费结算-发票记录信息（作废） */
	List<BlInvoice> getBlInvoiceCancelList(Map<String, Object> mapParam);
	
	/** 获取已日结的结算收退款信息 */
	List<BlCcPay> getBlCcPayListByPkCc(String pkCc);
	
	/** 获取已日结账户收退款信息 */
	List<BlCcPay> getBlCcPayPiListByPkCc(String pkCc);
	
	/** 获取已日结账支付信息 */
	List<BlSettleDetail> getBlSettleDetaiListByPkCc(String pkCc);
	
	/** 获取已日结账挂号信息 */
	List<InsuCountVo> getInsuCountVoListByPkCc(String pkCc);
	
	/** 按操作员分组获取未汇总结账操作员结账付款信息（包含结算跟患者账户）  */
	List<OpBlCcPayAndPiVo> getNoBlCcPayGroupByOperaList(Map<String, Object> mapParam);
	
	/** 按付款方式分组获取未汇总结账收付款方式分组信息（包含结算跟患者账户） */
	List<OpBlCcPayAndPiVo> getNoBlCcPayGroupByPaymodeList(Map<String, Object> mapParam);
	
	/** 查询已汇总结账操作员分组信息 */
	List<OpBlCcPayAndPiVo> getBlCcPayGroupByOperaList(Map<String, Object> mapParam);
	
	/** 查询已汇总结账付款方式分组信息 */
	List<OpBlCcPayAndPiVo> getBlCcPayGroupByPaymodeList(Map<String, Object> mapParam);
	
	/** 获取已结账挂号记录 */
	List<Map<String,Object>> getBlCcPvEncounterList(Map<String, Object> mapParam);
	
	/** 获取未结账挂号记录 */
	List<Map<String,Object>> getNoBlCcPvEncounterList(Map<String, Object> mapParam);
	
	/**
	 * 查询未日结账结算信息
	 * @param paramMap{pkOrg,pkEmp,dateEnd,codeInv,namePi}
	 * @return
	 */
	public List<Map<String,Object>> getNoBlCcSettleInfo(Map<String,Object> paramMap);
	/**
	 * 查询已日结账结算信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getBlCcSettleInfo(Map<String,Object> paramMap);
	
	/**未日结中查询收费收退款信息(Oracle)*/
	public List<Map<String,Object>> searchBlCcUnSumOracle(Map<String,Object> paramMap);
	
	/**未日结中查询收费收退款信息*/
	public List<Map<String,Object>> searchBlCcUnSum(Map<String,Object> paramMap);
	
	/**查询已结账业务结算记录列表(Oracle)*/
	public List<Map<String,Object>> searchBlCcByCcOracle(Map<String,Object> paramMap);
	
	/**查询已结账业务结算记录列表*/
	public List<Map<String,Object>> searchBlCcByCc(Map<String,Object> paramMap);
	
	/** 获取未结账挂号记录(Oracle) */
	public List<Map<String,Object>> getNoBlCcPvListOracle(Map<String, Object> mapParam);
	
	/** 获取已结账挂号记录 (Oracle)*/
	List<Map<String,Object>> getBlCcPvListOracle(Map<String, Object> mapParam);
	
	/**查询门诊票据分类金额信息(深大用)*/
	public List<Map<String,Object>> qryOpInvDtAmtList(Map<String,Object> param);

	/** 查询医保支付信息(深大用) */
	public List<Map<String, Object>> qryOpInsuPayInfoSd(Map<String, Object> param);
	
	/**查询付款方式信息*/
	public List<BlCcPay> qryPayModeList();
	/**查询需要更新的业务表pk*/
	public Set<String> qryAllPkSt(Map<String,Object> param);
	/** 查询开立票据pk */
	public Set<String> qryAllPkInv(Map<String, Object> param);

	/** 查询作废票据pk */
	public Set<String> qryAllPkInvCancel(Map<String, Object> param);

	/** 查询现金舍入金额 */
	public Double qryOpAmtAround(Map<String, Object> param);

	/**查询电子发票号码使用号段(深大用)*/
	public List<InvInfo> qryOpStInvEBill(Map<String,Object> param);

	/**电子发票作废号码*/
	public List<InvalidStInv> qryOpStInvEBillCanl(Map<String,Object> param);


	/** 查询医保支付信息(人医用) */
	public List<Map<String, Object>> qryOpInsuPayInfoZsrm(Map<String, Object> param);
	/** 查询结算信息信息(人医用) */
	public List<Map<String, Object>> qryOpStZsrm(Map<String, Object> param);

}
