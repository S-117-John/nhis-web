package com.zebone.nhis.bl.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zebone.nhis.bl.pub.vo.AmtInsuVo;
import com.zebone.nhis.bl.pub.vo.BlCcDepoDetail;
import com.zebone.nhis.bl.pub.vo.BlCcStDetail;
import com.zebone.nhis.bl.pub.vo.BlCcStGetDetail;
import com.zebone.nhis.bl.pub.vo.BlSumDepoVo;
import com.zebone.nhis.bl.pub.vo.BlSumPayVo;
import com.zebone.nhis.bl.pub.vo.BlSumRecords;
import com.zebone.nhis.bl.pub.vo.DepoRtnInfo;
import com.zebone.nhis.bl.pub.vo.DepositInv;
import com.zebone.nhis.bl.pub.vo.InvInfo;
import com.zebone.nhis.bl.pub.vo.InvalidStInv;
import com.zebone.nhis.bl.pub.vo.PayInfoSd;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlIpCcMapper {
	
	public BlCc qryStAmtInfo(Map<String,Object> param);
	//查询欠款金额
	public Double qryStAmtAr(Map<String,Object> param);
	//2.2发票信息
	public List<InvInfo> qryStInv(Map<String,Object> param);	
	//发票号串(Oracle)
	public List<InvInfo> qryStInvOracle(Map<String,Object> param);	
	//发票号串(SqlServer)
	public List<InvInfo> qryStInvSqlServer(Map<String,Object> param);	
	//2.3作废发票
	public List<InvalidStInv> qryStInv_Invalid(Map<String,Object> param);
	//2.4预交金信息
	public BlCc qryPrepPay(Map<String,Object> param);
	//2.5收款收据号串
	public List<DepositInv> qryDepoInvOracle(Map<String,Object> param);
	//2.5收款数据号串
	public List<DepositInv> qryDepoInvSqlServer(Map<String,Object> param);
	
	//2.6退款收据号串
	public List<DepoRtnInfo> qryDepoInv_Rtn(Map<String,Object> param);
	//2.7付款方式信息
	public List<BlCcPay> qryDepoInfo(Map<String,Object> param);
	
	
	@Select("SELECT * FROM bl_cc WHERE pk_cc =  #{pkCc}")
	public BlCc qryBlCcByPk(@Param(value = "pkCc")String pkCc);
	
	@Select("SELECT * FROM bl_cc_pay WHERE pk_cc =  #{pkCc}")
	public List<BlCcPay> qryBlCcPayByPk(@Param(value = "pkCc")String pkCc);
	
	
	public void updBlSettle(Map<String,Object> param);
	
	public void updBlDeposit(Map<String,Object> param);



	/** 根据操作员获取上一次结账信息 */
	public BlCc getLastCcByPkEmp(String pkEmp);
	
	/** 根据操作员获取时间最早的结算记录 */
	public BlSettle getLastStByPkEmp(String pkEmp);
	/** 根据操作员获取上一次结账信息（sqlserver） */
	BlCc getLastBlCcByPkEmpForSql(String pkEmp);
	
	
	/** 根据操作员获取时间最早的结算记录（sqlserver） */
	BlSettle getLastBlSettleByPkEmpForSql(String pkEmp);
	
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
	
	//7.更新作废发票bl_settle_ar
	@Update("update bl_settle_ar set flag_cc = 0,pk_cc = null where pk_cc = #{pkCc}")
	public void updBlStArCanc(@Param(value="pkCc")String pkCc);
	//跟新预交金补打发票bl_deposit
	@Update("update bl_deposit set flag_cc_rept = 0,pk_cc_rept = null where pk_cc_rept = #{pkCc}")
	public void updBlDepoReplenish(@Param(value="pkCc")String pkCc);
	
	/**住院日结信息查询**/
	public List<BlCcDepoDetail> getBlCcDepoDetail(@Param(value="pkCc")String pkCc);
	
	public List<BlCcStDetail> getBlCcStDetail(@Param(value="pkCc")String pkCc);

	public List<BlCcStGetDetail> getBlCcStGetDetail(@Param(value="pkCc")String pkCc);
	
	
    public List<BlCcDepoDetail> getBlCcDepoDetail_Unclose(Map<String,Object> param);
	
	public List<BlCcStDetail> getBlCcStDetail_Unclose(Map<String,Object> param);

	public List<BlCcStGetDetail> getBlCcStGetDetail_Unclose(Map<String,Object> param);
	
	/**住院结账汇总查询*/
	public void updSumCc (Map<String,Object> param);
	
	public List<BlSumRecords> QrySumRecords(Map<String,Object> param);

	public List<Map<String,Object>> cancelSumData(Map<String,Object> param);
	
	public void updCancelSumCc (Map<String,Object> param);
	
	public List<BlSumDepoVo> QrySumDepo(Map<String,Object> param);
	
	public List<BlSumPayVo> QrySumPay(Map<String,Object> param);
	
	public List<BlSumDepoVo> QryUnSumDepo(Map<String, Object> params);
	
	public List<BlSumPayVo> QryUnSumPay(Map<String, Object> params);
		
	public BlSumRecords QryUnSumInfo(Map<String,Object> params);
	
	public BlSumRecords QrySumInfo(Map<String,Object> params);
	
	public BlCc getEuStatus(Map<String, Object> params);

	/** 根据操作员获取时间最早的预交金记录 */
	public BlDeposit getLastPreByPkEmp(String pkEmp);

	/** 根据操作员获取时间最早的预交金记录（sqlserver） */
	public BlDeposit getLastPreByPkEmpForSql(String pkEmp);
	
	/**查询医保支付金额*/
	public List<AmtInsuVo> qryAmtInsu(Map<String,Object> paramMap);
	
	/**查询结算欠款金额*/
	public Double qrySettleAmtAr(Map<String,Object> paramMap);
	
	/**查询欠费补交金额*/
	public Double qrySettleAmtArFee(Map<String,Object> paramMap);
	
	/**查询预结应收金额*/
	public Double qrySettleAmtSor(Map<String,Object> paramMap);
	
	/**查询预结冲销金额*/
	public Double qrySettleAmtCa(Map<String,Object> paramMap);
	
	public List<BlDeposit> qryReceiptsData(Map<String,Object> param);
	
	/**查询参与结算的预交金金额(深大用)*/
	public Map<String,Object> qryAmtStPrep(Map<String,Object> param);
	
	/**查询支付方式付款信息(深大用)*/
	public List<PayInfoSd> qryPayListSd(Map<String,Object> param);
	
	/**查询医保支付信息(深大用)*/
	public List<Map<String,Object>> qryInsuPayInfoSd(Map<String,Object> param);
	
	/**查询预交金退款笔数(深大用)*/
	public Integer qryPrepBackCnt(Map<String,Object> param);
	
	/**查询结算作废笔数(深大用)*/
	public Integer qryStCanlCnt(Map<String,Object> param);
	
	/**查询票据分类金额信息(深大用)*/
	public List<Map<String,Object>> qryInvDtAmtList(Map<String,Object> param);
	
	/**查询结算预交金退回数量(深大用)*/
	public Integer qryStDepoCnt(Map<String,Object> param);
	
	/**查询预交金有效张数(深大用)*/
	public Integer qryDepoCnt(Map<String,Object> param);
	
	/**查询电子发票号码使用号段(深大用)*/
	public List<InvInfo> qryStInvEBill(Map<String,Object> param);
	
	/**电子发票作废号码*/
	public List<InvalidStInv> qryStInvEBillCanl(Map<String,Object> param);
	
	//6.更新结算发票bl_invoice
	@Update("update bl_invoice  set flag_cc_ebill = 0,pk_cc_ebill = null where pk_cc_ebill = #{pkCc}")
	public void updBlInvEbill (@Param(value="pkCc")String pkCc);
	
	//7.更新作废发票bl_invoice
	@Update("update bl_invoice   set flag_cc_cancel_ebill = 0,pk_cc_cancel_ebill = null where pk_cc_cancel_ebill = #{pkCc}")
	public void updBlInvEbillCanl (@Param(value="pkCc")String pkCc);
	
	/**查询现金短款金额*/
	public Double qryCashShortAmt(Map<String,Object> param);

	/**查询全国医保支付信息(深大用)*/
	public List<Map<String,Object>> qryQgInsuPayInfoSd(Map<String,Object> param);

	/**查询全国医保门诊支付信息(深大用)*/
	public List<Map<String,Object>> qryQgOpInsuPayInfoSd(Map<String,Object> param);
}
