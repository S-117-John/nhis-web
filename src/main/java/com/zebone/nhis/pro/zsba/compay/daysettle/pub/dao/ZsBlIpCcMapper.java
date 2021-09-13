package com.zebone.nhis.pro.zsba.compay.daysettle.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsAmtInsuVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBaBlSumVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcDepoDetail;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcStDetail;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlCcStGetDetail;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumDepoVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumPayVo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsBlSumRecords;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsDepoRtnInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsDepositInv;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsInvInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsInvalidStInv;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsPayerData;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBackDepoInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBackInvInfo;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBlCc;
import com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo.ZsbaBlCcDs;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ZsBlIpCcMapper {
	
	public ZsbaBlCc qryStAmtInfo(Map<String,Object> param);
	//查询欠款金额
	public Double qryStAmtAr(Map<String,Object> param);
	//2.2发票信息
	public List<ZsInvInfo> qryStInv(Map<String,Object> param);	
	//发票号串(Oracle)
	public List<ZsInvInfo> qryStInvOracle(Map<String,Object> param);	
	//发票号串(SqlServer)
	public List<ZsInvInfo> qryStInvSqlServer(Map<String,Object> param);	
	//2.3作废发票
	public List<ZsInvalidStInv> qryStInv_Invalid(Map<String,Object> param);
	//2.4预交金信息
	public BlCc qryPrepPay(Map<String,Object> param);
	//2.5收款收据号串
	public List<ZsDepositInv> qryDepoInvOracle(Map<String,Object> param);
	//2.5收款数据号串
	public List<ZsDepositInv> qryDepoInvSqlServer(Map<String,Object> param);
	
	//2.6退款收据号串
	public List<ZsDepoRtnInfo> qryDepoInv_Rtn(Map<String,Object> param);
	//2.7付款方式信息
	public List<BlCcPay> qryDepoInfo(Map<String,Object> param);
	
	
	@Select("SELECT * FROM bl_cc WHERE pk_cc =  #{pkCc}")
	public ZsbaBlCc qryBlCcByPk(@Param(value = "pkCc")String pkCc);
	
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
	public List<ZsBlCcDepoDetail> getBlCcDepoDetail(@Param(value="pkCc")String pkCc);
	
	public List<ZsBlCcStDetail> getBlCcStDetail(@Param(value="pkCc")String pkCc);

	public List<ZsBlCcStGetDetail> getBlCcStGetDetail(@Param(value="pkCc")String pkCc);
	
	
    public List<ZsBlCcDepoDetail> getBlCcDepoDetail_Unclose(Map<String,Object> param);
	
	public List<ZsBlCcStDetail> getBlCcStDetail_Unclose(Map<String,Object> param);

	public List<ZsBlCcStGetDetail> getBlCcStGetDetail_Unclose(Map<String,Object> param);
	
	/**住院结账汇总查询*/
	public void updSumCc (Map<String,Object> param);
	
	public List<ZsBlSumRecords> QrySumRecords(Map<String,Object> param);

	public List<Map<String,Object>> cancelSumData(Map<String,Object> param);
	
	public void updCancelSumCc (Map<String,Object> param);
	
	public List<ZsBlSumDepoVo> QrySumDepo(Map<String,Object> param);
	
	public List<ZsBlSumPayVo> QrySumPay(Map<String,Object> param);
	
	public List<ZsBlSumDepoVo> QryUnSumDepo(Map<String, Object> params);
	
	public List<ZsBlSumPayVo> QryUnSumPay(Map<String, Object> params);
		
	public ZsBlSumRecords QryUnSumInfo(Map<String,Object> params);
	
	public ZsBlSumRecords QrySumInfo(Map<String,Object> params);
	
	public BlCc getEuStatus(Map<String, Object> params);

	/** 根据操作员获取时间最早的预交金记录 */
	public BlDeposit getLastPreByPkEmp(String pkEmp);

	/** 根据操作员获取时间最早的预交金记录（sqlserver） */
	public BlDeposit getLastPreByPkEmpForSql(String pkEmp);
	
	/**查询医保支付金额*/
	public List<ZsAmtInsuVo> qryAmtInsu(Map<String,Object> paramMap);
	
	/**查询结算欠款金额*/
	public Double qrySettleAmtAr(Map<String,Object> paramMap);
	
	/**查询欠费补交金额*/
	public Double qrySettleAmtArFee(Map<String,Object> paramMap);
	
	/**查询预结应收金额*/
	public Double qrySettleAmtSor(Map<String,Object> paramMap);
	
	/**查询预结冲销金额*/
	public Double qrySettleAmtCa(Map<String,Object> paramMap);
	
	public List<BlDeposit> qryReceiptsData(Map<String,Object> param);
	
	/**
	 * 查询付款方信息
	 * @param param
	 * @return
	 */
	public List<ZsPayerData> qryPayerData(Map<String,Object> param);
	
	/**
	 * 查询已日结付款方信息
	 * @param param
	 * @return
	 */
	public List<ZsPayerData> qryPayerDataRj(Map<String,Object> param);
	
	/**
	 * 查询待汇总付款方信息
	 * @param param
	 * @return
	 */
	public List<ZsPayerData> QryUnSumPayerData(Map<String,Object> param);
	
	/**
	 * 查询已汇总付款方信息
	 * @param param
	 * @return
	 */
	public List<ZsPayerData> QrySumPayerData(Map<String,Object> param);
	
	/**
	 * 查询已汇总的操作者
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> QrySumEmp(Map<String,Object> param);
	
	/**
	 * 依据拓展属性，查询操作员信息
	 * @param codeAttr
	 * @return
	 */
	public List<BdOuEmployee> queryEmployees(Map<String,Object> params);
	
	/**
	 * 根据收款表、结算表查询操作员信息
	 * @param codeAttr
	 * @return
	 */
	public List<BdOuEmployee> queryEmployeesRj(Map<String,Object> params);
	
	/**
	 * 查询退款的收据号数量
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCntRetreat(Map<String,Object> param);
	
	/**
	 * 查询作废的收据号数量
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCntScrap(Map<String,Object> param);
	
	/**
	 * 查询作废发票数量（包括退费、重打）
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryStInvScrapCnt(Map<String,Object> param);
	
	/**
	 * 查询退款发票
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryStInvRetreatCnt(Map<String,Object> param);
	
	/**
	 * 查询已日结退款发票数据
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCcStInvRetreatCnt(Map<String,Object> param);
	
	/**
	 * 查询预交金各支付方式付款 不含微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryYjkAmt(Map<String,Object> param);
	
	/**
	 * 查询预交金各支付方式付款   衫德的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryYjkAmtSand(Map<String,Object> param);
	
	/**
	 * 查询预交金各支付方式付款  公众号、自助机的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryYjkAmtGzh(Map<String,Object> param);
	
	/**
	 * 查询门禁卡各支付方式付款
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryMjkAmt(Map<String,Object> param);
	
	/**
	 * 查询结算退款各支付方式 不包含微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstkAmt(Map<String,Object> param);
	
	/**
	 * 查询结算退款各支付方式  衫德pos机的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstkAmtSand(Map<String,Object> param);
	
	/**
	 * 查询结算退款各支付方式 自助机公众号的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstkAmtGzh(Map<String,Object> param);
	
	/**
	 * 已经出院病人欠费后交款各支付方式 不包含微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJsskAmt(Map<String,Object> param);
	
	/**
	 * 已经出院病人欠费后交款各支付方式    衫德微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJsskAmtSand(Map<String,Object> param);
	
	/**
	 * 已经出院病人欠费后交款各支付方式  自助机公众号支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJsskAmtGzh(Map<String,Object> param);
	
	/**
	 * 获取科研费用
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryAmtKy(Map<String,Object> param);
	
	/**
	 * 作废发票
	 * @param param
	 * @return
	 */
	public List<ZsInvalidStInv> qryStInvVoid(Map<String,Object> param);
	
	/**
	 * 退费发票
	 * @param param
	 * @return
	 */
	public List<ZsInvalidStInv> qryStInvBack(Map<String,Object> param);
	
	/**
	 * 作废发票
	 * @param param
	 * @return
	 */
	public List<ZsDepoRtnInfo> qryDepoInvVoid(Map<String,Object> param);
	
	/**
	 * 退费发票
	 * @param param
	 * @return
	 */
	public List<ZsDepoRtnInfo> qryDepoInvBack(Map<String,Object> param);
	
	/**
	 * 查询已日结的自付金额
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryAmtPi(Map<String,Object> param);
	
	/**
	 * 查询收款的预交金
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryDepoAmtCollect(Map<String,Object> param);
	
	/**
	 * 查询退款的预交金
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryDepoAmtBack(Map<String,Object> param);
	
	/**
	 * 查询未汇总日结信息
	 * @param param
	 * @return
	 */
	public List<ZsBaBlSumVo> qryNotSummarizedInfo(Map<String,Object> param);

	@Select("SELECT * FROM bl_cc_ds WHERE pk_cc =  #{pkCc}")
	public ZsbaBlCcDs qryBlCcDsByPk(@Param(value = "pkCc")String pkCc);
	
	//3.删除结账表bl_cc；
	@Delete("delete from bl_cc_ds where pk_cc = #{pkCc}")
	public void delBlCcDs (@Param(value="pkCc")String pkCc);
	
	/**
	 * 查询退票信息
	 * @param param
	 * @return
	 */
	public List<ZsbaBackInvInfo> qryBackInvInfo(Map<String,Object> param);
	
	/**
	 * 查询退预交金票据信息
	 * @param param
	 * @return
	 */
	public List<ZsbaBackDepoInfo> qryBackDepoInfo(Map<String,Object> param);

	/**
	 * 查询结算各支付方式退的预交金不包含微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstyjjAmt(Map<String,Object> param);
	
	/**
	 * 查询结算各支付方式退的预交金 衫德的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstyjjAmtSand(Map<String,Object> param);
	
	/**
	 * 查询结算各支付方式退的预交金 自助机的微信支付宝
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryJstyjjAmtGzh(Map<String,Object> param);
	
	/**
	 * 查询结算记录用到的支付类型为内部转账的预交金
	 * @param param
	 * @return
	 */
	public List<ZsbaBackDepoInfo> qryNbzzList(Map<String,Object> param);
}
