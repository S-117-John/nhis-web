package com.zebone.nhis.pro.zsba.rent.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.nhis.pro.zsba.rent.dao.NmRentRecordMapper;
import com.zebone.nhis.pro.zsba.rent.vo.NmRentRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 出租收款、退款
 * @author lipz
 *
 */
@Service
public class NmRentPayService {
	
	private static Logger log = LoggerFactory.getLogger(NmRentPayService.class);
	
	@Autowired NmRentRecordMapper rentRecordMapper;
	
	private static String SYSTEM_MODULES_RENT = "rent";
	
	/**
	 * 收款 022003010033
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("unchecked")
	public void rentPay(String param , IUser user){
    	param = param.replaceAll("\r|\n", "");
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRent = paramMap.get("pkRent");
		if(StringUtils.isEmpty(pkRent)){
			throw new BusException("出租记录主键不能为空！");
		}
		String authCode = paramMap.get("authCode");
		if(StringUtils.isEmpty(authCode)){
			throw new BusException("授权码不能为空！");
		}
		NmRentRecord rent = rentRecordMapper.getById(pkRent);
		if(rent == null){
			throw new BusException("出租记录不存在！");
		}
		/*
		 * 发起交易
		 */
		String pkPv = rent.getCodeIp()+"-"+rent.getTimes();
		Map<String,String> returnMap = new HashMap<String,String>();
		returnMap = tradePay(rent.getPkRent(), pkPv, pkPv, rent.getDepoAmt(), "设备出租押金", authCode);
		if(!"200".equals(returnMap.get("code"))){
			throw new BusException(returnMap.get("msg"));
		}
		
		/*
		 *  更新结算数据
		 */
		NmRentRecord newR = new  NmRentRecord();
		newR.setPkRent(pkRent);
		newR.setIsPay("1");
		newR.setPkPay(returnMap.get("pkPay"));
		newR.setPayMethod(returnMap.get("payMethod"));
		newR.setChargeCode(UserContext.getUser().getCodeEmp());
		newR.setChargeName(UserContext.getUser().getNameEmp());
		newR.setChargeTime(new Date());
		newR.setModityTime(newR.getChargeTime());
		rentRecordMapper.updateEntity(newR);
    }
	
	
	private Map<String,String> tradePay(String pkCiSt, String pkPi, String pkPv, BigDecimal totalFee, String subject, String authCode){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		if(PayConfig.PAY_METHOD_WECHAT.equals(PayConfig.getPayMethod(authCode))){
			returnMap = wechatTradePay(pkCiSt, pkPi, pkPv, totalFee, subject, authCode);//微信支付
		}else if(PayConfig.PAY_METHOD_ALI.equals(PayConfig.getPayMethod(authCode))){
			returnMap = aliTradePay(pkCiSt, pkPi, pkPv, totalFee, subject, authCode);//阿里支付
		}
		return returnMap;
	}
	
	/**
	 * 微信支付：条码支付
	 */
	private Map<String,String> wechatTradePay(String pkCiSt, String pkPi, String pkPv, BigDecimal totalFee, String subject, String authCode){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String outTradeNo = NHISUUID.getKeyId();	//生成系统商户订单号(时间戳+4位随机数)
		subject = "新"+subject;					//商品描述
		String body = "NHIS就诊主键：" + pkPv + "，操作人工号：" + UserContext.getUser().getCodeEmp() + "，交易金额："+ String.valueOf(totalFee) +"元";
		
		//根据生成的订单信息调用《提交付款码支付》
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		String appid = ApplicationUtils.getPropertyValue("WECHAT_APPID", "");
		parameters.put("appid", appid);//公众账号ID
		String mchId = ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", "");
		parameters.put("mch_id", mchId);//商户号
		parameters.put("device_info", UserContext.getUser().getCodeEmp());//终端设备号|操作员工号
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());//随机字符串
		parameters.put("body", subject);//商品描述
		parameters.put("detail", body);//商品详情
		parameters.put("attach", "");//附加数据(在查询API和支付通知中原样返回，可作为自定义参数使用)
		parameters.put("out_trade_no", outTradeNo);//商户订单号
		String money = String.valueOf(Math.round(totalFee.multiply(new BigDecimal(100)).doubleValue()));//单位：分
		parameters.put("total_fee", money);//总金额
		parameters.put("spbill_create_ip", ApplicationUtils.getCurrIp());//终端IP
		//订单生成时间-订单失效时间(5分钟后失效)
		parameters.put("time_start", DateUtils.getDate("yyyyMMddHHmmss"));//交易起始时间
		parameters.put("time_expire", DateUtils.addDate(new Date(), PayConfig.timeout_express, 5, "yyyyMMddHHmmss"));//交易结束时间
		parameters.put("auth_code", authCode);//支付授权码
		parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));//商户支付密钥
		String jsonParams = JsonUtil.writeValueAsString(parameters);
		
		// 生成预交易数据
		PayWechatRecord wr = saveWechatPay(pkCiSt, pkPi, pkPv, outTradeNo, totalFee, subject, body, appid, mchId, jsonParams);

		//发送http请求通过中转服务器请求微信接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_barcode_url;
		String result = HttpUtil.httpPost(queryUrl, jsonParams);
		log.info("微信条码支付返回结果:" + result);
		
		if(StringUtils.isNotEmpty(result)){
			returnMap = updateWechatData(wr, result);
		}else{
			returnMap.put("code", "404");
			returnMap.put("msg", "中转接口返回空数据");
		}
		return returnMap;
	}
	/*
	 * 微信主动查询订单支付结果
	 * @param outTradeNo	商户订单号
	 * @return
	 */
	public static String activeQueryWechat(String outTradeNo){
		String result = "";
		for(int i=0; i<PayConfig.queryNum; i++){
			result = "";
			try {
				Thread.sleep(6000);// 睡眠5秒后发起查询
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			Map<String,String> data = wechatOrderQuery(outTradeNo);
			if(StringUtils.isNotEmpty(data.get("state"))){
				result = data.get("result");
				break;
			}
		}
		return result;
	}
	private static Map<String,String> wechatOrderQuery(String outTradeNo){
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "");
		resultMap.put("state", "");
		try {
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));
			parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));
			parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
			parameters.put("out_trade_no", outTradeNo);//商户订单号
			parameters.put("transaction_id", "");//微信订单号
			parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));//商户支付密钥
			
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_order_query;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			resultMap.put("result", result);
			log.info("微信[主动查询订单]返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if (map.get("return_code").equals("SUCCESS")) {
					if(map.get("result_code").equals("SUCCESS")){
						String tradeState = map.get("trade_state");
						if(tradeState.equals("SUCCESS")){
							resultMap.put("state", "SUCCESS");//支付成功-退出
						}else{
							if(!tradeState.equals("USERPAYING") && !tradeState.equals("NOTPAY")){//不是用户支付中的
								resultMap.put("state", "FAIL");//交易失败-退出
							}
						}
					}else{
						if(!map.get("err_code").equals("SYSTEMERROR")){
							resultMap.put("state", "FAIL");//交易失败-退出
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("主动查询[微信]订单交易结果出现异常："+e.getMessage());
		}
		return resultMap;
	}
	private PayWechatRecord saveWechatPay(String pkCiSt, String pkPi, String pkPv, String outTradeNo, BigDecimal totalFee, 
			String subject, String body, String appid, String mchId, String initData){
		PayWechatRecord wr = new PayWechatRecord();
		wr.setPkExtpay(null);//支付主键
		wr.setPkPi(pkPi);//患者主键
		wr.setPkPv(pkPv);//就诊主键
		wr.setAppid(appid);
		wr.setMchId(mchId);
		wr.setDeviceInfo(UserContext.getUser().getCodeEmp());
		wr.setOrderType("pay");//消费
		wr.setSystemModule(SYSTEM_MODULES_RENT);
		wr.setProductId(pkCiSt);
		wr.setBody(subject);
		wr.setDetail(body);
		wr.setOutTradeNo(outTradeNo);
		wr.setTotalAmount(totalFee);
		wr.setTradeState("INIT");//初始化
		wr.setInitData(initData);
		wr.setBillStatus("0");
		
		wr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
		wr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
		wr.setCreateTime(new Date());
		wr.setModifier(wr.getCreator());
		wr.setModityTime(wr.getCreateTime());
		DataBaseHelper.insertBean(wr);
		
		return wr;
	}
	@SuppressWarnings("unchecked")
	private Map<String,String> updateWechatData(PayWechatRecord wr, String result){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		Map<String,String> map = JsonUtil.readValue(result, Map.class);
		// 返回值return_code和result_code都为SUCCESS时支付成功
		String return_code = map.get("return_code");
		String return_msg = map.get("return_msg");
		if(return_code.equals("SUCCESS")){
			String result_code = map.get("result_code");
			if(result_code.equals("SUCCESS")){
				// 更新微信支付交易记录
				wr.setDeviceInfo(map.get("device_info"));
				wr.setTradeState(result_code);//付款成功
				wr.setOpenid(map.get("openid"));
				wr.setWechatTradeNo(map.get("transaction_id"));
				wr.setAttach(map.get("attach"));
				wr.setPayTime(map.get("time_end"));
				wr.setPayData(result);
				wr.setBillStatus("0");
				
				wr.setModityTime(new Date());
				DataBaseHelper.updateBeanByPk(wr);
				
				returnMap.put("pkPay", wr.getPkPayWechatRecord());
				returnMap.put("payMethod", PayConfig.PAY_METHOD_WECHAT);
				returnMap.put("code", "200");
				returnMap.put("msg", "微信条码支付成功");
			}else{
				String errCode = map.get("err_code");
				if("USERPAYING".equals(errCode) || "SYSTEMERROR".equals(errCode) || "BANKERROR".equals(errCode)){
					// 需要调用查询接口获取订单状态
					result = "";
					result = activeQueryWechat(wr.getOutTradeNo());
					returnMap = updateWechatData(wr, result);
				}else{
					returnMap.put("code", map.get("err_code"));
					returnMap.put("msg", "["+map.get("err_code")+"]:"+map.get("err_code_des"));
				}
			}
		}else{
			returnMap.put("code", return_code);
			returnMap.put("msg", "["+return_code+"]:"+return_msg);
		}
		return returnMap;
	}
	
	/**
	 * 支付宝支付: 条码支付
	 */
	private Map<String,String> aliTradePay(String pkCiSt, String pkPi, String pkPv, BigDecimal totalFee, String subject, String authCode){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String outTradeNo = NHISUUID.getKeyId();	//生成系统商户订单号(时间戳+4位随机数)
		subject = "新"+subject;					//商品描述
		String body = "NHIS就诊主键：" + pkPv + "，操作人工号：" + UserContext.getUser().getCodeEmp() + "，交易金额："+ String.valueOf(totalFee) +"元";
		
		//构造biz_data业务数据JSON《统一收单交易支付接口》
		JSONObject buzData = new JSONObject();
		buzData.put("out_trade_no", outTradeNo);	//商户订单号
		buzData.put("total_amount", totalFee.toString());//订单总金额
		buzData.put("scene", "bar_code");			//支付场景条码支付，取值：bar_code;声波支付，取值：wave_code
		buzData.put("auth_code", authCode);			//支付授权码	
		buzData.put("subject", subject);			//订单标题
		buzData.put("body", body);					//订单描述
		buzData.put("terminal_id", UserContext.getUser().getCodeEmp());		//商户机具终端编号|操作人工号
		buzData.put("timeout_express", PayConfig.timeout_express+"m");	//支付超时时间,m分钟
		
		//构造预下单参数MAP
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_GATEWAY", ""));
		String appid = ApplicationUtils.getPropertyValue("APP_ID", "");
		parameters.put("appid", appid);
		parameters.put("private_key", ApplicationUtils.getPropertyValue("PRIVATE_KEY", ""));
		parameters.put("charset","utf-8");
		parameters.put("alipay_public_key", ApplicationUtils.getPropertyValue("ALIPAY_PUBLIC_KEY", ""));
		parameters.put("biz_content", buzData.toString());//业务数据
		String jsonParams = JsonUtil.writeValueAsString(parameters);
		
		//生成预交易数据
		PayAliRecord ar = saveAlipay(pkCiSt, pkPi, pkPv, totalFee, authCode, subject, body, appid, outTradeNo, jsonParams);
		
		//发送http请求通过中转服务器请求支付宝接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_barcode_url;
		String result = HttpUtil.httpPost(queryUrl, jsonParams);
		log.info("支付宝条码支付预下单返回结果:" + result);
		
		if(StringUtils.isNotEmpty(result)){
			returnMap = updateAlipay(ar, result);
		}else{
			returnMap.put("code", "404");
			returnMap.put("msg", "中转接口返回空数据");
		}
		return returnMap;
	}
	/*
	 * 支付宝主动查询订单支付结果
	 * @param outTradeNo	商户订单号
	 * @return
	 */
	public static String alipayTradeQuery(String outTradeNo){
		String result = "";
		for(int i=0; i<PayConfig.queryNum; i++){
			result = "";
			try {
				Thread.sleep(6000);// 睡眠5秒后发起查询
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			Map<String,String> data = aliOrderQuery(outTradeNo);
			if(StringUtils.isNotEmpty(data.get("state"))){
				result = data.get("result");
				break;
			}
		}
		return result;
	}
	private static Map<String,String> aliOrderQuery(String outTradeNo){
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "");
		resultMap.put("state", "");
		try {
			//构造biz_data业务数据JSON《统一收单线下交易查询》
			JSONObject buzData = new JSONObject();
			buzData.put("out_trade_no", outTradeNo);//商户订单号
			
			//构造预下单参数MAP
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_GATEWAY", ""));
			parameters.put("appid",  ApplicationUtils.getPropertyValue("APP_ID", ""));
			parameters.put("charset","utf-8");
			parameters.put("private_key", ApplicationUtils.getPropertyValue("PRIVATE_KEY", ""));
			parameters.put("alipay_public_key", ApplicationUtils.getPropertyValue("ALIPAY_PUBLIC_KEY", ""));
			parameters.put("biz_content", buzData.toString());//业务数据
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_order_query;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			resultMap.put("result", result);
			log.info("支付宝[主动查询订单]返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if (map.get("is_success").equals("true")) {
					/*
					 *  交易状态：
					 *  WAIT_BUYER_PAY（交易创建，等待买家付款）、
					 *  TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
					 *  TRADE_SUCCESS（交易支付成功）、
					 *  TRADE_FINISHED（交易结束，不可退款）
					 */
					if(!"WAIT_BUYER_PAY".equals(map.get("trade_status"))){
						if("TRADE_SUCCESS".equals(map.get("trade_status")) || "TRADE_FINISHED".equals(map.get("trade_status"))){
							resultMap.put("state", "SUCCESS");//交易成功
						}else{
							resultMap.put("state", "FAIL");//交易失败
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("主动查询[支付宝]订单交易结果出现异常："+e.getMessage());
		}
		return resultMap;
	}
	private PayAliRecord saveAlipay(String pkCiSt, String pkPi, String pkPv, BigDecimal totalFee, String authCode, 
			String subject, String body, String appid, String outTradeNo, String initData){
		PayAliRecord ar = new PayAliRecord();
		ar.setPkExtpay(null);//支付主键
		ar.setPkPi(pkPi);//患者主键
		ar.setPkPv(pkPv);//就诊主键
		ar.setAppid(appid);
		ar.setTerminalId(UserContext.getUser().getCodeEmp());
		ar.setOrderType("pay");//消费
		ar.setSystemModule(SYSTEM_MODULES_RENT);
		ar.setProductId(pkCiSt);
		ar.setBody(subject);
		ar.setDetail(body);
		ar.setOutTradeNo(outTradeNo);
		ar.setTotalAmount(totalFee);
		ar.setTradeState("INIT");//初始化
		ar.setInitData(initData);
		ar.setBillStatus("0");
		
		ar.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
		ar.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
		ar.setCreateTime(new Date());
		ar.setModifier(ar.getCreator());
		ar.setModityTime(ar.getCreateTime());
		DataBaseHelper.insertBean(ar);
		return ar;
	}
	private Map<String,String> updateAlipay(PayAliRecord ar, String result){
		Map<String,String> resultMap = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Map<String,String> map = JsonUtil.readValue(result, Map.class);
		if(map!=null && map.get("is_success").equals("true")){
			if(map.get("code").equals("10000") || map.get("code").equals("10003")) {
				
				//生成支付交易记录
				ar.setTradeState("SUCCESS");//付款成功
				ar.setBuyerId(map.get("buyer_id"));
				ar.setAliTradeNo(map.get("trade_no"));
				ar.setPayTime(map.get("time_end"));
				ar.setPayData(result);
				ar.setModityTime(new Date());
				DataBaseHelper.updateBeanByPk(ar);
									
				resultMap.put("pkPay", ar.getPkPayAliRecord());
				resultMap.put("payMethod", PayConfig.PAY_METHOD_ALI);
				resultMap.put("code", "200");
				resultMap.put("msg", "支付宝条码支付成功");
			}else{
				if(map.get("code").contains("SYSTEM_ERROR")){
					result = "";
					result = alipayTradeQuery(ar.getOutTradeNo());
					resultMap = updateAlipay(ar, result);
				}else{
					resultMap.put("code", map.get("code"));
					resultMap.put("msg", "["+map.get("code")+"]:"+map.get("msg"));
				}
			}
		}else{
			if(map.get("code").contains("SYSTEM_ERROR")){
				result = "";
				result = alipayTradeQuery(ar.getOutTradeNo());
				resultMap = updateAlipay(ar, result);
			}else{
				resultMap.put("code", map.get("code"));
				resultMap.put("msg", "["+map.get("code")+"]:"+map.get("msg"));
			}
		}
		return resultMap;
	}
	
	
	
	/**
	 * 退款 022003010034
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void rentRefund(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkRent = paramMap.get("pkRent").toString();
		String refundMoney =paramMap.get("refundMoney").toString();

		if(StringUtils.isEmpty(pkRent)){
			throw new BusException("出租记录主键不能为空！");
		}
		if( StringUtils.isEmpty(refundMoney)){
			throw new BusException("退款金额不能为空！");
		}
		NmRentRecord rent = rentRecordMapper.getById(pkRent);
		if(rent == null){
			throw new BusException("出租记录不存在！");
		}
		if(rent.getIsRefund().equals("1"))
		{
			throw new BusException("押金已退还，请勿重复操作！");
		}
		// 执行退款
		String pkRefund = "";//退款记录主键
		if(rent.getPayMethod().equals(PayConfig.PAY_METHOD_WECHAT)){
			String sql = "select * from PAY_WECHAT_RECORD where PK_PAY_WECHAT_RECORD = ?";
			PayWechatRecord wr = DataBaseHelper.queryForBean(sql, PayWechatRecord.class, new Object[]{rent.getPkPay()});
			Map<String,String> resultMap = refundWx(wr,refundMoney, "退租退押金");
			if("200".equals(resultMap.get("code"))){
				pkRefund = resultMap.get("pkRefund");
			}else{
				throw new BusException(resultMap.get("msg"));
			}
		}else if(rent.getPayMethod().equals(PayConfig.PAY_METHOD_ALI)){
			String sql = "select * from PAY_ALI_RECORD where PK_PAY_ALI_RECORD = ?";
			PayAliRecord ar = DataBaseHelper.queryForBean(sql, PayAliRecord.class, new Object[]{rent.getPkPay()});
			Map<String,String> resultMap = refundAli(ar, refundMoney,"退租退押金");
			if("200".equals(resultMap.get("code"))){
				pkRefund = resultMap.get("pkRefund");
			}else{
				throw new BusException(resultMap.get("msg"));
			}
		}
		/*
		 * 更新退款数据
		 */
		if(StringUtils.isNotEmpty(pkRefund)){			
			NmRentRecord newR = new  NmRentRecord();
			newR.setPkRent(pkRent);
			newR.setIsRefund("1");
			newR.setPkRefund(pkRefund);
			newR.setRefunderCode(UserContext.getUser().getCodeEmp());
			newR.setRefunderName(UserContext.getUser().getNameEmp());
			newR.setRefundTime(new Date());
			newR.setModityTime(rent.getRefundTime());
			rentRecordMapper.updateEntity(newR);
		}
	}
	
	/**
	 * 微信支付申请退款接口
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> refundWx(PayWechatRecord wr,String refundMoney ,String refundReason){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		BigDecimal amount = wr.getTotalAmount().multiply(new BigDecimal(100));//转分
		Integer totalMoney = Integer.parseInt(amount.stripTrailingZeros().toPlainString());
		BigDecimal refund = new BigDecimal(refundMoney).multiply(new BigDecimal(100));//转分
		Integer totalRefund  = Integer.parseInt(refund.stripTrailingZeros().toPlainString());
		
		String outRefundNo = NHISUUID.getKeyId();
		//根据生成的订单信息调用《微信申请退款接口》
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));//公众账号ID
		parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//商户号
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("out_trade_no", wr.getOutTradeNo());//商户订单号
		parameters.put("out_refund_no", outRefundNo);//商户退款单号
		parameters.put("total_fee", totalMoney);//订单金额
		parameters.put("refund_fee", totalRefund);//退款金额
		parameters.put("refund_fee_type", "CNY");//货币种类
		parameters.put("refund_desc", refundReason);//退款原因
		parameters.put("refund_account", "");//退款资金来源
		parameters.put("op_user_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//操作员-默认商户号
		parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));
		
		//发送http请求通过中转服务器请求微信接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_refund_url;
		String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
		log.info("微信退款返回结果:" + result);

		if(StringUtils.isNotEmpty(result)){
			Map<String,String> map = JsonUtil.readValue(result, Map.class);
			
			String returnCode = map.get("return_code");//返回状态码
			String returnMsg = map.get("return_msg");//返回信息
			if(returnCode.equals("SUCCESS")){
				String resultCode = map.get("result_code");//业务结果
				if(resultCode.equals("SUCCESS")){
					//生成交易记录表退款记录
					PayWechatRecord refundWr = new PayWechatRecord();
					BeanUtils.copyProperties(wr, refundWr);
					refundWr.setPkPayWechatRecord(null);
					refundWr.setInitData(null);
					refundWr.setPayTime(null);
					refundWr.setPayData(null);
					refundWr.setPkExtpay(null);
					refundWr.setOrderType("refund");//退费
					refundWr.setTotalAmount(wr.getTotalAmount().multiply(new BigDecimal(-1)));//退款金额
					refundWr.setTradeState(resultCode);
					refundWr.setRefundData(result);
					refundWr.setDetail(refundReason);
					refundWr.setWechatTradeNo(map.get("refund_id"));
					refundWr.setOutTradeNo(outRefundNo);
					refundWr.setRefundTradeNo(wr.getOutTradeNo());
					refundWr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
					refundWr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
					refundWr.setCreateTime(new Date());
					refundWr.setModifier(refundWr.getCreator());
					refundWr.setModityTime(refundWr.getCreateTime());
					DataBaseHelper.insertBean(refundWr);
					
					// 更新缴款记录关联退款记录
					wr.setRefundTradeNo(outRefundNo);
					wr.setModityTime(new Date());
					DataBaseHelper.updateBeanByPk(wr);
					
					returnMap.put("pkRefund", refundWr.getPkPayWechatRecord());
					returnMap.put("code", "200");//状态码
					returnMap.put("msg", "退款成功");//返回信息
				}else{
					returnMap.put("code", map.get("err_code"));
					returnMap.put("msg",  "["+map.get("err_code")+"]:"+map.get("err_code_des"));
				}
			}else{
				returnMap.put("code", returnCode);
				returnMap.put("msg", "["+returnCode+"]:"+returnMsg);
			}
		}else{
			returnMap.put("code", "404");
			returnMap.put("msg", "中转接口返回空数据");
		}
		return returnMap;
	}
	
	/**
	 * 支付宝申请退款
	 */
	@SuppressWarnings("unchecked")
	private Map<String,String> refundAli(PayAliRecord ar,String refundMoney, String refundReason){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String outRefundNo = NHISUUID.getKeyId();
		
		//构造申请退款接口的JSON参数
		JSONObject buzData = new JSONObject();
		buzData.put("out_trade_no", ar.getOutTradeNo());//支付宝交易号
		buzData.put("refund_amount",  refundMoney);//退款金额
		buzData.put("out_request_no", outRefundNo);//商户退款请求号
		buzData.put("refund_reason", refundReason);//退款原因
		
		//构造预下单参数MAP
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_GATEWAY", ""));
		parameters.put("appid",ApplicationUtils.getPropertyValue("APP_ID", ""));
		parameters.put("private_key", ApplicationUtils.getPropertyValue("PRIVATE_KEY", ""));
		parameters.put("charset", "utf-8");
		parameters.put("alipay_public_key", ApplicationUtils.getPropertyValue("ALIPAY_PUBLIC_KEY", ""));
		parameters.put("biz_content", buzData.toString());//业务数据
		
		//发送http请求通过中转服务器请求支付宝接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_refund_url;
		String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
		log.info("支付宝退款返回结果:" + result);
		
		if(StringUtils.isNotEmpty(result)){
			Map<String,String> map = JsonUtil.readValue(result, Map.class);
			if(map.get("is_success").equals("true")){
				String code = map.get("code");
				String msg  = map.get("msg");
				if(code.equals("10000")){
					//生成交易记录表退款记录
					PayAliRecord refundAr = new PayAliRecord();
					BeanUtils.copyProperties(ar, refundAr);
					refundAr.setPkPayAliRecord(null);
					refundAr.setInitData(null);
					refundAr.setPayTime(null);
					refundAr.setPayData(null);
					refundAr.setPkExtpay(null);
					refundAr.setOrderType("refund");//退费
					refundAr.setTotalAmount(ar.getTotalAmount().multiply(new BigDecimal(-1)));//退款金额
					refundAr.setTradeState("SUCCESS");
					refundAr.setRefundData(result);
					refundAr.setDetail(refundReason);
					refundAr.setOutTradeNo(outRefundNo);
					refundAr.setRefundTradeNo(ar.getOutTradeNo());
					refundAr.setPkOrg(UserContext.getUser().getPkOrg());//当前操作人关联机构
					refundAr.setCreator(UserContext.getUser().getCodeEmp());//当前操作人关联编码
					refundAr.setCreateTime(new Date());
					refundAr.setModifier(refundAr.getCreator());
					refundAr.setModityTime(refundAr.getCreateTime());
					DataBaseHelper.insertBean(refundAr);
					
					// 更新缴款记录关联退款记录
					ar.setRefundTradeNo(outRefundNo);
					ar.setModityTime(new Date());
					DataBaseHelper.updateBeanByPk(ar);
					
					returnMap.put("pkRefund", refundAr.getPkPayAliRecord());
					returnMap.put("code", "200");//状态码
					returnMap.put("msg", "退款成功");//返回信息
				}else{
					returnMap.put("code", code);
					returnMap.put("msg", "["+code+"]:"+msg);
				}
			}else{
				returnMap.put("code", "404");
				returnMap.put("msg", "中转接口返回失败");
			}
		}else{
			returnMap.put("code", "404");
			returnMap.put("msg", "中转接口返回空数据");
		}
		return returnMap;
	}
	
	
	
}
