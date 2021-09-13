package com.zebone.nhis.bl.pub.pay.service;

import com.zebone.nhis.bl.pub.dao.OpcgQryWrapMapper;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.vo.BlExtPayVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方支付接口
 * @author zhangmenghao
 */
@Service
public class ThirdPartyPaymentService {

	@Autowired
	private CgQryMaintainService cgQryMaintainService;
	
	@Resource
	private OpcgQryWrapMapper opcgQryWrapMapper;
	
	/**
	 * 交易号：002004001020
	 * 在调用第三方支付前，根据银行交易号生成订单
	 * @param blDeposit
	 */
	public List<BlExtPay> createOrder(String param, IUser user){
		List<BlDeposit> paramList = JsonUtil.readValue(param, new TypeReference<List<BlDeposit>>() {});
		List<BlExtPay> blExtPays = new ArrayList<>();
		for (BlDeposit blDeposit : paramList) {
			BlExtPay blExtPay = new BlExtPay();
			blExtPay.setAmount(blDeposit.getAmount()); //金额
			blExtPay.setFlagPay(EnumerateParameter.ZERO); //支付状态 -未支付
			ApplicationUtils.setDefaultValue(blExtPay, true);
			blExtPays.add(blExtPay);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlExtPay.class), blExtPays);
		return blExtPays;
	}
	
	/**
	 * 交易号：002004001021
	 * 调用第三方接口成功后，修改支付状态,填写 银行交易号,交易时间,对应银行,银行名称,
	 * 结算标志：pay_result（1未结算，0已结算）,pos机编码:out_trade_no
	 * 根据银行支付状态更新bl_ext_pay支付状态
	 * @param tradeNo
	 */
	public void updateOrderFlagPay(String param, IUser user){
		List<BlExtPay> blExtPays = JsonUtil.readValue(param, new TypeReference<List<BlExtPay>>() {});
		for (BlExtPay blExtPay : blExtPays) {
			if(StringUtils.isEmpty(blExtPay.getPkExtpay())){
				throw new BusException("更新主键为空");
			}
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), blExtPays);
	}
	
	/**
	 * 交易号：002004001023
	 * 将POS机未结算记录更新为结算记录
	 * @param outTradeNo
	 */
	@SuppressWarnings("unchecked")
	public void updateOrderNoPosSettle(String param, IUser user) {
		Map<String,String>  map = JsonUtil.readValue(param, Map.class);
		List<BlExtPay> blExtPays = DataBaseHelper.queryForList("select * from bl_ext_pay where out_trade_no = ?", BlExtPay.class, new Object[]{map.get("pOSID")});
		if (blExtPays.size() > 0) {
			for (BlExtPay blExtPay : blExtPays) {
				if (StringUtils.isEmpty(blExtPay.getPkExtpay())) {
					throw new BusException("更新主键为空");
				}
				blExtPay.setPayResult(EnumerateParameter.ZERO);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), blExtPays);
		}
	}
	
	/**
	 * 查询未支付的订单
	 * @return 订单
	 */
	public List<BlExtPay> queryNotPayList(String flagPay){
		String querySql = "select * from bl_ext_pay where flag_pay = ?";
		List<BlExtPay> blExtPays = DataBaseHelper.queryForList(querySql, BlExtPay.class, flagPay);
		return blExtPays;
	}
	
	/**
	 * 根据交款记录查询与银行对接的记录表
	 * @param mapParam
	 * @return
	 */
	public BlExtPay qryBlExtPayByBlDeposit(String pkDepo){
		String sql = "select * from bl_ext_pay where PK_DEPO=?";
		BlExtPay blExtPay = DataBaseHelper.queryForBean(sql, BlExtPay.class, new Object[]{pkDepo});
		return blExtPay;
	}
	
	
	/**
	 * 查询支付状态成功的订单，修改订单 关联就诊缴款
	 * @param blDeposit
	 */
	public void updatePkDepo(String pkPv, String pkPi){
		Map<String, Object> mapParam = new HashMap<>();
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkPi", pkPi);
		mapParam.put("euDirect", EnumerateParameter.ONE);
		mapParam.put("dtPaymode", EnumerateParameter.THREE);
		
		//根据用户和就诊主键查询交款记录
		List<BlDeposit> blDeposits = cgQryMaintainService.qryBldepositByPk(mapParam);
		
		if(CollectionUtils.isEmpty(blDeposits)){
			return;
		}
		//查询已支付的订单（银行接口成功）
		List<BlExtPay> blExtPays = queryNotPayList(EnumerateParameter.ONE);
		for (BlExtPay blExtPay : blExtPays) {
			for(BlDeposit blDeposit : blDeposits){
					if(blExtPay.getTradeNo() == null){
						continue;
					}
					if(blExtPay.getTradeNo().equals(blDeposit.getPayInfo())){
						blExtPay.setPkDepo(blDeposit.getPkDepo());
						blExtPay.setPkPi(blDeposit.getPkPi()); //患者主键
						blExtPay.setPkPv(blDeposit.getPkPv()); //就诊主键
						blExtPay.setSysname("银联");
						DataBaseHelper.updateBeanByPk(blExtPay);
					}
				}
			}
	}
	
	/**
	 * 通过患者主键主键查询第三方支付信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BlExtPayVo> queryBlExtPay(String param, IUser user) {
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		List<BlExtPayVo> list = opcgQryWrapMapper.queryBlExtPayRefund(map);
		return list;
	}
	
	
	/**
	 * 微信/支付宝退款时更新第三方表
	 * @param blDeposit
	 * @param blExt
	 * @return 第三方表
	 */
	public BlExtPay refundWeixinOrAliPayBlExtPay(BlDeposit blDeposit, BlExtPayBankVo blExt) {
		String serialNoSql = "select * from bl_ext_pay where serial_no = ?";
		BlExtPay weixinAliblExtPay = DataBaseHelper.queryForBean(serialNoSql, BlExtPay.class, blExt.getNewBankCode());
		//取新生成的主键
		weixinAliblExtPay.setPkPi(blDeposit.getPkPi()); //患者主键
		weixinAliblExtPay.setPkPv(blDeposit.getPkPv()); //就诊主键
		weixinAliblExtPay.setPkDepo(blDeposit.getPkDepo());  // 对应bl_deposit的主键 退款主键
		if(weixinAliblExtPay.getEuPaytype().equals(EnumerateParameter.SEVEN)){
			weixinAliblExtPay.setAmount(new BigDecimal(-1.0).multiply(blExt.getAmount().divide(new BigDecimal(100)).abs())); //微信金额---分
			weixinAliblExtPay.setSysname("微信");
		}else{
			weixinAliblExtPay.setAmount(new BigDecimal(-1.0).multiply(blExt.getAmount().abs())); //支付宝金额 --元
			weixinAliblExtPay.setSysname("支付宝");
		}
		weixinAliblExtPay.setFlagPay(EnumerateParameter.ONE);  //支付状态 -已支付
		return weixinAliblExtPay;
	}

	/**
	 * 银行卡退款时更新第三方表
	 * @param blDeposit
	 * @param blExt
	 * @return 第三方表
	 */
	public BlExtPay refundBankCardBlExtPay(BlDeposit blDeposit, BlExtPayBankVo blExt) {
		String tradeNoSql  = "select * from bl_ext_pay where trade_no = ?";
		BlExtPay blExtPayBank = DataBaseHelper.queryForBean(tradeNoSql,	BlExtPay.class, blExt.getNewBankCode());
		//取新生成的主键
		blExtPayBank.setPkPi(blDeposit.getPkPi()); //患者主键
		blExtPayBank.setPkPv(blDeposit.getPkPv()); //就诊主键
		blExtPayBank.setPkDepo(blDeposit.getPkDepo());  // 对应bl_deposit的主键 退款主键
		blExtPayBank.setAmount(new BigDecimal(-1.0).multiply(blExt.getAmount().abs())); //金额
		blExtPayBank.setFlagPay(EnumerateParameter.ONE);  //支付状态 -已支付
		blExtPayBank.setSysname("银联");
		return blExtPayBank;
	}
	
	/**
	 * 自动更新当天的所有未结算记录（spring以@Scheduled的注解模式实现）
	 */
//	@Scheduled(cron = "0 0 0 * * ?")
//    public void UpdatePosNoSettle(){
//		
//    	//查询当天未结算的第三方订单
//		List<BlExtPay> list = DataBaseHelper.queryForList(
//				"select * from bl_ext_pay where result_pay ='1' and  ",
//				BlExtPay.class);
//		// 将其未结算记录改为结算记录
//		for (BlExtPay blExtPay : list) {
//			blExtPay.setResultPay(EnumerateParameter.ZERO);
//		}
//		// 更新未结算记录
//		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BlExtPay.class), list);
		
//		List<Test> list = new ArrayList<>();
//		for (int i = 0; i < 10; i++) {
//			Test test = new Test();
//			test.setPkTest(NHISUUID.getKeyId());
//			ApplicationUtils.setDefaultValue(test, true);
//			test.setNameTest("测试"+i);
//			list.add(test);
//		}
//		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(Test.class),list);
}
	
	
