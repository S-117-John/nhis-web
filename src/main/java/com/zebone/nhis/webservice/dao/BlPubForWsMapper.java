package com.zebone.nhis.webservice.dao;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.webservice.vo.EnoteInvInfo;
import com.zebone.nhis.webservice.vo.tmisvo.ResponseHISFeeVo;
import com.zebone.nhis.webservice.vo.wechatvo.ItemsVO;
import com.zebone.nhis.webservice.vo.wechatvo.SumItemsVO;
import com.zebone.platform.modules.mybatis.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlPubForWsMapper {
 
	/**
	 * 获取电子票据信息
	 * @param mapParam
	 * @return
	 */
    public List<EnoteInvInfo> qryEnoteInvInfo(Map<String, Object> mapParam) ;

    public List<Map<String, Object>> getPrePayDetail(Map<String, Object> map);

    public List<Map<String, Object>> getDepositInfo(String pkPi);

    public List<Map<String, Object>> getIpCgDetail(Map<String, Object> map);
    public List<Map<String, Object>> getIpCgDayDetail(Map<String, Object> map);
   public List<Map<String, Object>> getOpCgDayDetail(Map<String, Object> map);
    /**
     *
     * 查询患者已缴费用
     * @return
     */
    public  List<Map<String, Object>> getPaidFeeByOp(Map<String, Object> map);
    /**
    *
    * 查询患者已缴费用（第三方缴费记录）
    * @return
    */
    public  List<Map<String, Object>> getThirdPaidFeeByOp(Map<String, Object> map);
    
    /**
     * 查询门诊缴费明细
     * @return
     */
    public  List<Map<String, Object>> getPayDetailByOp(String pkSettle);
    /**
     * 灵璧自助机查询患者住院预交金记录
     * @return
     */
    public List<Map<String, Object>> LbgetBldeposit(String pkPv);
    /**
     * 患者费用分类汇总
     * @param map{pkPvs,dateBegin,dateEnd,type,pkItemcate}
     * @return
     */
 	public List<Map<String,Object>> LbqueryBlCgIpSummer(Map<String,Object> map);

    public List<Map<String, Object>> getDeptSchInfo(Map<String, Object> paramMap);
    
    /**
     * 查询就诊记录
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> queryPvList(Map<String,Object> paramMap);
    
    /**
     * 查询有效就诊记录
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> queryEffectPvList(Map<String,Object> paramMap);
    
    /**
     * 订单查询类接口
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> queryOrderCenterInfo(Map<String,Object> paramMap);
    
    /**
     * 微信服务查询挂号信息 
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> queryReg(@Param("pkPv") String pkPv);
    
    /**
     * 微信服务查询结算信息
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> querySettle(@Param("pkPv") String pkPv);
    
    /**
     * 微信服务查询出院结算信息
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> queryInSettle(@Param("pkPv") String pkPv,@Param("payOrderSn") String payOrderSn);
    
    /**
     * 微信服务查询住院预缴金信息 
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> queryIpFee(@Param("pkPv") String pkPv,@Param("payOrderSn") String payOrderSn);
    
    /**
     * 根据执行科室查询门诊未结算收费信息
     * @param paramMap{pkPv}
     * @return
     */
    public List<Map<String,Object>> queryBlOpDtsToPay(Map<String,Object> paramMap);
    
    /**
     * 查询门诊缴费明细总额 
     * @param pkCgop
     * @return
     */
    public BigDecimal getBlOpDtAmountSum(List<String> pkCgop);
    
    /**
     * 查询计费明细
     * @param pkCgops
     * @return
     */
    public List<BlOpDt> getBlOpDtList(List<String> pkCgops);
    
    /**
     * 根据执行科室查询门诊未结算收费信息
     * @param paramMap{pkPv}
     * @return
     */
    public List<Map<String,Object>> queryBlOpDtsToPrePay(List<String> pkCgops);
    
    /**
     * 查询患者输血明细费用（灵璧输血）
     * @author ds
     */
    public List<ResponseHISFeeVo>  queryBloodCost(Map<String,Object> param); 
    //更新bl_ext_pay表flag_pay字段状态未0
    public int updateBlExtPayFlagpay(Map<String,Object> param);
    //批量更新数据
    public int updateBlOpDtList(List<BlOpDt> blOpDtList);

    /**
     * 获取医院微信公众号每日账单
     * @param paramMap
     */
	public List<SumItemsVO> getHisMerchantSum(Map<String, Object> paramMap);

	/**
     * 获取医院微信公众号每日账单明细
     * @param paramMap
     */
	public List<ItemsVO> getHisMerchantDetail(Map<String, Object> paramMap);
}
