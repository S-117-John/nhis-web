package com.zebone.nhis.ma.pub.sd.dao;

import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.InvItemVo;
import com.zebone.nhis.ma.pub.sd.vo.ChargeDetailVo;
import com.zebone.nhis.ma.pub.sd.vo.ListDetailVo;
import com.zebone.nhis.ma.pub.sd.vo.PayChannelDetailVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 电子票据DAO
 * @author Administrator
 *
 */
@Mapper
public interface EnoteMapper {
	
	/**查询结算票据信息*/
	public List<Map<String,Object>> qryENoteInvInfo(Map<String,Object> paramMap);
	
	/**查询通知信息*/
	public Map<String,Object> qryInformInfo(Map<String,Object> paramMap);
	
	/**查询就诊信息*/
	public Map<String,Object> qryENotePvInfo(Map<String,Object> paramMap);

	/**zsrm-查询就诊信息*/
	public Map<String,Object>  qryENotePvInfoZsrm(Map<String,Object> paramMap);
	
	/**查询结算首付款信息*/
	public List<PayChannelDetailVo> qryStDepoList(Map<String,Object> paramMap);
	
	/**查询本次结算收费项目信息*/
	public List<ChargeDetailVo> qryStIpDtList(Map<String,Object> paramMap);
	/**查询本次结算收费项目信息*/
	public List<ChargeDetailVo> qryStIpDtListZsrm(Map<String,Object> paramMap);
	/**查询本次结算收费项目信息(校验总金额使用)*/
	public Double qryStIpDtListChk(Map<String,Object> paramMap);
	
	/**查询本次结算清单信息*/
	public List<ListDetailVo> qryStIpDtDtlList(Map<String,Object> paramMap);
	/**查询本次结算清单信息*/
	public List<ListDetailVo> qryStIpDtDtlListZsrm(Map<String,Object> paramMap);
    /**zsrm-获取本次结算收费类型为特需的sum(Amount)额*/
	public Double qryDtSumAmountZsrm(Map<String,Object> paramMap);
	/**zsrm-获取本次结算的sum(AmountAdd)额*/
	public Double qryDtSumAmountAddZsrm(Map<String,Object> paramMap);

	/**查询本次结算清单信息(校验总金额使用)*/
	public Double qryStIpDtDtlListChk(Map<String,Object> paramMap);
	
	/**查询需要冲红的票据信息*/
	public List<Map<String,Object>> qryEbillInfo(Map<String,Object> paramMap);
	
	public List<InvItemVo> qryInvItemInfo(Map<String,Object> paramMap);
	
	/**查询结算相关联的票据信息*/
	public List<BlInvoice> qryInvInfoByPkSettle(Map<String,Object> paramMap);
	
	/**根据pkpv查询患者有效预交金总金额*/
	public Double qryDepoAmtByPkPv(Map<String,Object> paramMap);
	
	/**根据pksettle查询医保报销明细金额*/
	public Map<String,Object> qryInsuAmtByPkSettle(Map<String,Object> paramMap);

	/**根据pksettle查询医保报销明细金额*/
	public Map<String,Object> qryInsuAmtByNamePi(Map<String,Object> paramMap);

	/**根据pkPv查询医保报销明细金额*/
	public Map<String,Object> qryInsuAmtByPkPv(Map<String,Object> paramMap);

	/**查询支付方式里包含新冠、职业暴露、大肠癌筛查的信息*/
	public List<BdDefdoc> qryPayInfoList();

	/**根据票据前缀和使用人查询当前使用票据信息*/
	public List<BlEmpInvoice> qryPrefixByEuType(Map<String,Object> paramMap);

	/**根据pkSettle查询医保类别*/
	public String qryHpTypeByPkSettle(Map<String,Object> paramMap);

	/**zsrm-查询个账金额*/
	public Double  qryAumByPkSettleZsrm(Map<String,Object> paramMap);
	/**zsrm-查询个人自付金额*/
	public Double  qrySelfConceitedAmtPkSettleZsrm(Map<String,Object> paramMap);
	/**zsrm-查询支付订单号**/
	public List<String> qrySerialNoByPkSettleZsrm(Map<String,Object> paramMap);

	/**zsrm-查询患者医疗类别**/
	public Map<String, Object> qryENotePvMedTypeZsrm(Map<String, Object> paramMap);

	/**
	 * zsrm-查询患者住院预交金信息
	 * @param paramMap
	 * @return
	 */
    List<Map<String,Object>> qryENoteAdvancePayInfo(Map<String, Object> paramMap);

	/**
	 * zsrm-查询原票据信息
	 * @param paramMap
	 * @return
	 */
	Map<String, Object> qryDepoInfo(Map<String, Object> paramMap);

	/**
	 * 更新电子票据信息
	 * @param blDeposit
	 */
    void updateBlDeposit(BlDeposit blDeposit);
    //医保计划拓展属性
    List<Map<String,Object>> ExtendedAttributes(String pkInsu);
}

