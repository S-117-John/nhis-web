package com.zebone.nhis.common.pay.service;

import com.google.common.collect.Maps;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.pay.PayProxy;
import com.zebone.nhis.common.pay.dao.PayMapper;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PayService {
	
	@Resource
	private PayMapper payMapper;
	
	public Map<String,String> getWeixinPayQrcode(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		//支付主键
		String pkExtpay = paramMap.get("pkExtpay");
		//业务主键
		String pkBus = paramMap.get("pkBus");
		//业务类型
		String busType = paramMap.get("busType");
		//支付描述
		String descPay = paramMap.get("descPay");
		//商品描述
		String body = paramMap.get("bodyPay");
		//商品详情描述
		String detail = paramMap.get("detailPay");
		//患者主键
		String pkPi = paramMap.get("pkPi");
		//总金额
		BigDecimal totalFee = new BigDecimal(paramMap.get("amount"));
		//回调url
		String notifyUrl = PayProxy.notifyDomain + "/src/main/static/pay/weixinPay/notify";
		System.out.println("notify:"+notifyUrl);
		//额外参数
		String attach = "";
		//交易流水号
		String serialNum = NHISUUID.getKeyId();
		
		BlExtPay blExtPay = new BlExtPay();
		if(StringUtils.isEmpty(pkExtpay)){
			blExtPay.setAmount(totalFee);
			//blExtPay.setBodyPay(body);
			//blExtPay.setDetailPay(detail);
			blExtPay.setDescPay(descPay);
			blExtPay.setEuPaytype("7");
			//blExtPay.setBusType(busType);
			//blExtPay.setPkBus(pkBus);
			blExtPay.setPkPi(pkPi);
			//blExtPay.setSerialNum(serialNum);
			blExtPay.setDateAp(new Date());
			pkExtpay = cratePay(blExtPay);
		}else{
			blExtPay.setPkExtpay(pkExtpay);
			//blExtPay.setSerialNum(serialNum);
			DataBaseHelper.updateBeanByPk(blExtPay, false);
		}
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("pkExtpay", pkExtpay);
		resultMap.put("qrCode", PayProxy.getWeixinPayQrcode(pkBus, body, serialNum, totalFee.doubleValue(), notifyUrl, "127.0.0.1", attach));
		return resultMap;
	}
	
	
	
	public Map<String,String> weixinPayRefund(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String tradeNo = paramMap.get("tradeNo");
		BigDecimal refundAmount = new BigDecimal(paramMap.get("refundAmount"));
		BigDecimal totalFee = new BigDecimal(paramMap.get("totalFee"));
		String refundReason = paramMap.get("refundReason");
		
		
//		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from bl_ext_pay where out_trade_no = ?", BlExtPay.class, new Object[]{ tradeNo });
		String refundNum = NHISUUID.getKeyId();
		String pkExtpay = "";
		BlExtPay blExtPay = new BlExtPay();
		blExtPay.setEuPaytype("1");//微信退款
		blExtPay.setFlagPay("0");
		//blExtPay.setRefundNum(refundNum);
		//blExtPay.setOutTradeNo(tradeNo);
		//blExtPay.setDetailPay(refundReason);//退款原因
		blExtPay.setAmount(refundAmount);
		blExtPay.setDateAp(new Date());
		pkExtpay = cratePay(blExtPay);
		String payResult = PayProxy.weixinPayRefund(tradeNo, refundNum, totalFee.doubleValue(), refundAmount.doubleValue());
		if(!"fail".equals(payResult)){
			blExtPay.setFlagPay("1");
			//blExtPay.setPayResult(payResult);
			blExtPay.setDatePay(new Date());
			updatePay(blExtPay);
		}
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("pkExtpay", pkExtpay);
		resultMap.put("flagRefund", "fail".equals(payResult) ? "fail" : "success");
		return resultMap;
		
	}
	
	
	
	/**
	 * 支付宝退款
	 * @param tradeNo        支付宝订单号
	 * @param refundAmount   需要退款的金额
	 * @param refundReason   退款原因
	 */
	public Map<String,String> alipayRefund(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String tradeNo = paramMap.get("tradeNo");
		BigDecimal refundAmount = new BigDecimal(paramMap.get("refundAmount"));
		String refundReason = paramMap.get("refundReason");
		
		String refundNum = NHISUUID.getKeyId();
		String pkExtpay = "";
		BlExtPay blExtPay = new BlExtPay();
		blExtPay.setEuPaytype("2");//支付宝退款
		blExtPay.setFlagPay("0");
		//blExtPay.setRefundNum(refundNum);
		//blExtPay.setOutTradeNo(tradeNo);
		//blExtPay.setDetailPay(refundReason);//退款原因
		blExtPay.setAmount(refundAmount);
		blExtPay.setDateAp(new Date());
		pkExtpay = cratePay(blExtPay);
		String payResult = PayProxy.alipayRefund(tradeNo, refundAmount.doubleValue(), refundReason, refundNum);
		if(!"fail".equals(payResult)){
			blExtPay.setFlagPay("1");
			//blExtPay.setPayResult(payResult);
			blExtPay.setDatePay(new Date());
			updatePay(blExtPay);
		}
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("pkExtpay", pkExtpay);
		resultMap.put("flagRefund", "fail".equals(payResult) ? "fail" : "success");
		return resultMap;
	}
	
	
	public Map<String,String> getAliPayQrcode(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		//支付主键
		String pkExtpay = paramMap.get("pkExtpay");
		//业务主键
		String pkBus = paramMap.get("pkBus");
		//业务类型
		String busType = paramMap.get("busType");
		//支付描述
		String descPay = paramMap.get("descPay");
		//商品描述
		String subject = paramMap.get("bodyPay");
		//商品详情描述
		String body = paramMap.get("detailPay");
		//患者主键
		String pkPi = paramMap.get("pkPi");
		//总金额
		BigDecimal totalFee = new BigDecimal(paramMap.get("amount"));
		//回调url
		String notifyUrl = PayProxy.notifyDomain + "/src/main/static/pay/alipay/notify";
		//交易流水号
		String serialNum = NHISUUID.getKeyId();
		
		BlExtPay blExtPay = new BlExtPay();
		if(pkExtpay == null){
			blExtPay.setAmount(totalFee);
			//blExtPay.setBodyPay(subject);
			//blExtPay.setDetailPay(body);
			blExtPay.setDescPay(descPay);
			blExtPay.setEuPaytype("8");
			//blExtPay.setBusType(busType);
			//blExtPay.setPkBus(pkBus);
			blExtPay.setPkPi(pkPi);
			blExtPay.setDateAp(new Date());
			//blExtPay.setSerialNum(serialNum);
			pkExtpay = cratePay(blExtPay);
		}else{
			blExtPay.setPkExtpay(pkExtpay);
			//blExtPay.setSerialNum(serialNum);
			DataBaseHelper.updateBeanByPk(blExtPay, false);
		}
		Map<String,String> resultMap = Maps.newHashMap();
		resultMap.put("pkExtpay", pkExtpay);
		resultMap.put("qrCode", PayProxy.getAlipayQrcode(serialNum, subject, body, totalFee+"", notifyUrl));
		return resultMap;
	}
	
	
	private String cratePay(BlExtPay blExtPay){
		DataBaseHelper.insertBean(blExtPay);
		return blExtPay.getPkExtpay();
	}
	
	
	public int updatePay(BlExtPay blExtPay){
		return DataBaseHelper.updateBeanByPk(blExtPay, false);
	}
	
	
	public BlExtPay savePay(String param, IUser user){
		BlExtPay blExtPay = JsonUtil.readValue(param, BlExtPay.class);
		if(StringUtils.isEmpty(blExtPay.getPkExtpay())){
			throw new BusException("支付主键不能为空！");
		}
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}
	
	/**
	 * 订单列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getOrderList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> resultMap =null;
		User u = (User) user;
		if (Application.isSqlServer()) {
			resultMap = payMapper.getOrderListSqlSer(paramMap);
		} else {
			resultMap = payMapper.getOrderListOracle(paramMap);
		}
		return resultMap;
	}
}
