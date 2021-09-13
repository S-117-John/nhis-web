package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;



import com.zebone.nhis.bl.pub.syx.vo.AccAmtVo;
import com.zebone.nhis.bl.pub.syx.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.syx.vo.StInvInfo;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpBlccMapper {
	
	/**查询未结账明细信息*/
	public List<BlCcPay> qryUnSummaryDtls(Map<String,Object> paramMap);
	
	/**查询未结账记账金额信息*/
	public List<AccAmtVo> qryAccAmtDtls(Map<String,Object> paramMap);
	
	/**查询门诊支付类型*/
	public List<Map<String,Object>> qryOpPayMode();
	
	/**查询发票号段信息*/
	public List<StInvInfo> qryStInvInfo(Map<String,Object> paramMap);
	
	/**查询补打发票信息*/
	public List<Map<String,Object>> qryBdInvInfo(Map<String,Object> paramMap);
	
	/**查询结算总额*/
	public BlCc qryStAmtInfo(Map<String,Object> paramMap);
	
	/**作废发票*/
	public List<InvalidStInv> qryStInv_Invalid(Map<String,Object> param);
	
	/** 根据操作员获取上一次结账信息（sqlserver） */
	public BlCc getLastBlCcByPkEmpForSql(Map<String,Object> param);
	
	/** 根据操作员获取上一次结账信息 */
	public BlCc getLastCcByPkEmp(Map<String,Object> param);
	
	/** 根据操作员获取时间最早的结算记录（sqlserver） */
	public BlSettle getLastBlSettleByPkEmpForSql(Map<String,Object> param);
	
	/** 根据操作员获取时间最早的结算记录 */
	public BlSettle getLastStByPkEmp(Map<String,Object> param);
	
	/**查询需要更新的业务表pk*/
	public Set<String> qryUpPkSt(Map<String,Object> paramMap);
	
	/**查询门诊日结账信息*/
	public List<BlCc> qryBlccInfo(Map<String,Object> paramMap);
	
	public List<Map<String,Object>> cancelData(@Param(value="pkCc")String pkCc);
	
	//1.删除结账付款表bl_cc_pay；
	@Delete("delete from bl_cc_pay where pk_cc = #{pkCc}")
	public void delBlCcPay (@Param(value="pkCc")String pkCc);
	
	//2.删除结算票据表bl_cc_inv；
	@Delete("delete from bl_cc_inv where pk_cc = #{pkCc}")
	public void delBlCcInv (@Param(value="pkCc")String pkCc);
	
	//3.删除结账表bl_cc；
	@Delete("delete from bl_cc where pk_cc = #{pkCc}")
	public void delBlCc (@Param(value="pkCc")String pkCc);
	
	//4.更新结算bl_settle
	@Update("	update bl_settle  set flag_cc = 0,pk_cc = null  where pk_cc = #{pkCc}")
	public void updBlSt (@Param(value="pkCc")String pkCc);
	
	//5.更新结算收付款bl_deposit
	@Update("update bl_deposit  set flag_cc = 0, pk_cc = null where pk_cc = #{pkCc}")
	public void updBlDepo (@Param(value="pkCc")String pkCc);
	
	//6.更新结算发票bl_invoice
	@Update("update bl_invoice  set flag_cc = 0,pk_cc = null where pk_cc = #{pkCc}")
	public void updBlInvoice (@Param(value="pkCc")String pkCc);
	
	//7.更新作废发票bl_invoice
	@Update("update bl_invoice   set flag_cc_cancel = 0,pk_cc_cancel = null where pk_cc_cancel = #{pkCc}")
	public void updBlInvCanc (@Param(value="pkCc")String pkCc);
	
	public BlCc getEuStatus(Map<String, Object> params);
	
	/**查询已结账付款方式信息*/
	public List<Map<String,Object>> qrySumyPayInfo(Map<String,Object> paramMap);
	
	/**查询已结账作废发票信息*/
	public List<Map<String,Object>> qrySumyCanlInv(Map<String,Object> paramMap);
	
	/**查询已结账收费信息*/
	public List<Map<String,Object>> qrySumyCgInfo(Map<String,Object> paramMap);
	
	/**查询已结账医保收费信息*/
	public List<Map<String,Object>> qrySumyHpInfo(Map<String,Object> paramMap);
	
	/**查询结账人数和发票数*/
	public List<Map<String,Object>> qrySumyCntInfo(Map<String,Object> paramMap);
	
	/**查询取消结算信息所关联的正记录PK_SETTLE*/
	public List<String> qryCanlStpkList(Map<String,Object> paramMap);
	
	
	
	
}
