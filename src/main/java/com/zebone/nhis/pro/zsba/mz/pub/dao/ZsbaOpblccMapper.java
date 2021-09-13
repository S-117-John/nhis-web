package com.zebone.nhis.pro.zsba.mz.pub.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zebone.nhis.bl.pub.vo.InsuCountVo;
import com.zebone.nhis.bl.pub.vo.InvInfo;
import com.zebone.nhis.bl.pub.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.vo.OpBlCcPayAndPiVo;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.pro.zsba.mz.datsett.vo.ZsbaOpSettleBackInvDtl;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsBaBlCcPay;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsbaBlCcDsMz;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊日结账、汇总结账mapper
 * 
 * @author wangpeng
 * @date 2016年10月24日
 *
 */
@Mapper
public interface ZsbaOpblccMapper {
	
	/** 根据结账主键获取结账信息 */
	BlCc getBlCcById(String pkCc);
	
	/** 根据操作员获取上一次结账信息（sqlserver） */
	BlCc getLastBlCcByUserIdForSql(String pkEmp);
	
	/** 根据操作员获取时间最早的结算记录 */
	BlSettle getLastBlSettleByUserId(Map<String,Object> mapParm);
	
	/** 根据操作员获取时间最早的结算记录（sqlserver） */
	BlSettle getLastBlSettleByUserIdForSql(String pkEmp);
	
	/** 根据操作员获取未结账的收费结算-交款记录 */
	List<ZsBaBlCcPay> getBlDepositNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未结账的患者账户交退款记录 */
	List<ZsBaBlCcPay> getBlDepositPiNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未结账的结算明细（支付信息）  */
	List<BlSettleDetail> getBlSettleDetailNoCcList(Map<String, Object> mapParam);
	
	/** 根据操作员获取未挂号医保分组信息  */
	List<InsuCountVo> getInsuCountVoList(Map<String, Object> mapParam);
	
	/** 获取收费结算-发票记录分组信息（使用） */
	List<BlInvoice> getBlInvoiceGroup(Map<String, Object> mapParam);
	
	/** 获取收费结算-发票记录信息（作废） */
	List<BlInvoice> getBlInvoiceCancelList(Map<String, Object> mapParam);
	
	/** 获取已日结的结算收退款信息 */
	List<ZsBaBlCcPay> getBlCcPayListByPkCc(String pkCc);
	
	/** 获取已日结账户收退款信息 */
	List<ZsBaBlCcPay> getBlCcPayPiListByPkCc(String pkCc);
	
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
	
	/**未日结中查询收费收退款信息*/
	public List<Map<String,Object>> searchBlCcUnSum(Map<String,Object> paramMap);
	
	/**查询已结账业务结算记录列表*/
	public List<Map<String,Object>> searchBlCcByCc(Map<String,Object> paramMap);
	
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
	
	/**日结报表 现金、银行卡、财务记账**/
	public List<Map<String, Object>> qryDepostitUnRj(Map<String, Object> param);
	/**日结报表 医保结算 **/
	public Double qryHpUnRj(Map<String, Object> param);

	/**日结报表 医保结算-工商  **/
	public Double qryHpGsUnRj(Map<String, Object> param);
	
	/**日结报表 医院优惠 **/
	public Double qryYyyhPayerUnRj(Map<String, Object> param); 
	
	/**日结报表 HP **/
	public List<Map<String, Object>> qryHpPayUnRj(Map<String, Object> param); 
	
	/***日结报表 纸质票据*/
	public Map<String, Object> qryInvUnRj(Map<String, Object> param); 
	
	/***日结报表 纸质票据 作废 退票*/
	public Map<String, Object> qryInvCancelUnRj(Map<String, Object> param); 
	
	/**日结报表  电子票据**/
	public Map<String, Object> qryInvEbillUnRj(Map<String, Object> param); 
	
	/**日结报表  电子票据**/
	public Map<String, Object> qryInvEbillBackUnRj(Map<String, Object> param); 
	
	/**日结报表  退票明细**/
	public List<ZsbaOpSettleBackInvDtl> qryInvoBackUnRj(Map<String, Object> param); 
	
	/** 日结报表 根据结账主键获取门诊结账扩展信息 */
	public ZsbaBlCcDsMz getBlCcDsMzBypkCc(String pkCc);
	
	/**日结报表   根据结账主键获取退票明细**/
	public List<ZsbaOpSettleBackInvDtl> qryInvoBackRj(String pkCc); 
	
	/** 日结报表 根据结账主键获取结算总金额 */
	public Double getBlCcAmountBypkCc(String pkCc);
	
	/**日结报表 结算总金额 **/
	public Double getAmountTs(Map<String, Object> param);
	
	/**日结报表 病历本金额 **/
	public Double getAmountBl(Map<String, Object> param);
	
	/**日结报表 科研 GCP 产筛 **/
	public List<Map<String, Object>> qryKyPayUnRj(Map<String, Object> param); 
	
	/**日结报表 医疗救助 **/
	public Double getMafPay(Map<String, Object> param);
	
	/**日结报表 个账 **/
	public Double getPsnCashPay(Map<String, Object> param);
	
	/**
	 * 根据收款表、结算表查询操作员信息
	 * @param 
	 * @return
	 */
	public List<BdOuEmployee> queryEmployeesRj(Map<String,Object> params);
}
