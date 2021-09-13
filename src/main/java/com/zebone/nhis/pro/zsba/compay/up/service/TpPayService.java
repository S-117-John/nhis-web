package com.zebone.nhis.pro.zsba.compay.up.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayAliRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

@Service
public class TpPayService {

	private static Logger log = LoggerFactory.getLogger(TpPayService.class);
	
	/*
	 * TODO : 默认的机构和工号
	 */
	private String pkOrg = "89ace0e12aba439991c0eb001aaf02f7";
	
	/**
	 * 预下单获取支付二维码地址（微信或者支付宝）
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getPayQrcodeByPayType(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,String> returnMap = new HashMap<String,String>();

		String codeEmp = paramMap.get("codeEmp");
		String systemModule = paramMap.get("systemModule");				//系统模块
		
		String pkPi = paramMap.get("pkPi");								//患者主键
		String pkPv = paramMap.get("pkPv");								//就诊主键
		
		String outTradeNo = NHISUUID.getKeyId();						//生成系统商户订单号(时间戳+4位随机数)
		String payType = paramMap.get("payType");						//支付类型,原生扫码支付(trade_type非JSAPI方式openid可不传)
		String tradeType = paramMap.get("tradeType");					//交易类型
		if(StringUtils.isEmpty(tradeType)){
			tradeType = "NATIVE";//原生扫码支付
		}
		String openId = paramMap.get("openId");							//用户标识
		BigDecimal totalFee = new BigDecimal(paramMap.get("amount"));	//总金额,元
		String subject = "新"+paramMap.get("subject");				//商品描述
		String body = paramMap.get("body");								//商品详情
		if(StringUtils.isNotEmpty(body)){
			body = "NHIS就诊主键：" + pkPv + "，" + body;
		}else{
			body = "NHIS就诊主键：" + pkPv + "，操作人工号：" + codeEmp + "，交易金额："+ String.valueOf(totalFee) +"元";
		}
		
		User emp = null;
		// 兼容自助机、公众号不登录调用接口服务时，取不到登录人信息的问题
		if(StringUtils.isNotEmpty(codeEmp) && codeEmp.startsWith("999")){
			emp = getByCodeEmp(codeEmp);
		}else{
			emp = UserContext.getUser();
		}
		
		if(payType.equals("1")){
			/**
			 * 微信支付统一下单接口
			 */
			//根据生成的订单信息调用《微信统一下单接口》
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));//公众账号ID
			parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//商户号
			parameters.put("device_info", codeEmp);//终端设备号|操作员工号
			parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());//随机字符串
			//附加数据(在查询API和支付通知中原样返回，可作为自定义参数使用)
			parameters.put("attach", pkPv);//中转服务器在微信回调后获取该地址回传后台接口地址
			parameters.put("body", subject);//商品描述
			parameters.put("detail", body);//商品详情
			parameters.put("out_trade_no", outTradeNo);//商户订单号
			String money = String.valueOf(Math.round(totalFee.multiply(new BigDecimal(100)).doubleValue()));
			parameters.put("total_fee", money);//总金额-单位：分
			parameters.put("spbill_create_ip", ApplicationUtils.getCurrIp());//IP
			parameters.put("trade_type", tradeType);//类型
			if("JSAPI".equals(tradeType)){
				parameters.put("openid", openId);
			}
			//订单生成时间-订单失效时间(5分钟后失效)
			parameters.put("time_start", DateUtils.getDate("yyyyMMddHHmmss"));//交易起始时间
			parameters.put("time_expire", DateUtils.addDate(new Date(), PayConfig.timeout_express, 5, "yyyyMMddHHmmss"));//交易结束时间
			parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));//商户支付密钥
			
			//发送http请求通过中转服务器请求微信接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_qrcode_url;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			log.info("微信预下单返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				// 返回值return_code和result_code都为SUCCESS时下单成功
				if (map.get("return_code").equals("SUCCESS")) {
					if(map.get("result_code").equals("SUCCESS")){
						// 生成 外部接口-支付 记录
						BlExtPay blExtPay=new BlExtPay();
						blExtPay.setSerialNo(outTradeNo);//商户订单号
						blExtPay.setAmount(totalFee);
						blExtPay.setEuPaytype("7");//二维码
						blExtPay.setDtBank("0");//微信
						blExtPay.setDescPay(subject+"，"+body);
						blExtPay.setSysname(systemModule);
						blExtPay.setDateAp(new Date());//请求时间
						blExtPay.setFlagPay("0");//支付标志 未完成
						blExtPay.setPkPi(pkPi);//患者主键
						blExtPay.setPkPv(pkPv);//就诊主键
						blExtPay.setPkOrg(pkOrg);//当前操作人关联机构
						blExtPay.setCreator(emp.getPkEmp());//当前操作人关联编码
						blExtPay.setCreateTime(new Date());
						blExtPay.setModifier(blExtPay.getCreator());
						blExtPay.setModityTime(blExtPay.getCreateTime());
						DataBaseHelper.insertBean(blExtPay);
						
						// 生成 预支付交易订单记录
						PayWechatRecord wr = new PayWechatRecord();
						wr.setPkExtpay(blExtPay.getPkExtpay());//支付主键
						wr.setPkPi(pkPi);//患者主键
						wr.setPkPv(pkPv);//就诊主键
						wr.setAppid(map.get("appid"));
						wr.setMchId(map.get("mch_id"));
						wr.setDeviceInfo(map.get("device_info"));
						wr.setOrderType("pay");//消费
						wr.setBody(subject);
						wr.setDetail(body);
						wr.setOutTradeNo(outTradeNo);
						wr.setTotalAmount(totalFee);
						wr.setTradeState("INIT");//下单
						wr.setInitData(result);
						wr.setBillStatus("0");
						wr.setSystemModule(systemModule);
						
						wr.setPkOrg(pkOrg);//当前操作人关联机构
						wr.setCreator(blExtPay.getCreator());//当前操作人关联编码
						wr.setCreateTime(new Date());
						wr.setModifier(wr.getCreator());
						wr.setModityTime(wr.getCreateTime());
						DataBaseHelper.insertBean(wr);
						
						//生成预支付交易订单成功,返回二维码地址
						returnMap.put("pkExtpay", blExtPay.getPkExtpay());//主键
						returnMap.put("outTradeNo", outTradeNo);//商户订单号
						returnMap.put("codeUrl", map.get("code_url"));//二维码地址
						returnMap.put("prepayId", map.get("prepay_id"));//预支付交易会话标识
						returnMap.put("code", "200");
						returnMap.put("msg", "微信下单成功");
					}else{
						//失败则直接返回提示信息
						returnMap.put("code", map.get("err_code"));
						returnMap.put("msg", "["+map.get("err_code")+"]:"+map.get("err_code_des"));
					}
				}else{
					//失败则直接返回提示信息
					returnMap.put("code", map.get("return_code"));
					returnMap.put("msg", "["+map.get("return_code")+"]:"+map.get("return_msg"));
				}
			}else{
				returnMap.put("code", "404");
				returnMap.put("msg", "中转接口返回空数据");
			}
		}else if(payType.equals("2")){
			/**
			 * 支付宝支付预下单接口
			 */
			//构造biz_data业务数据JSON
			JSONObject buzData = new JSONObject();
			buzData.put("out_trade_no", outTradeNo);			//商户订单号
			buzData.put("total_amount", totalFee.toString());	//订单总金额
			buzData.put("subject", subject);					//订单标题
			buzData.put("body", body);							//订单描述
			buzData.put("terminal_id", codeEmp);				//商户机具终端编号|操作人工号
			buzData.put("timeout_express", PayConfig.timeout_express+"m");//支付超时时间,分钟
			
			//构造预下单参数MAP
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_GATEWAY", ""));
			String appid = ApplicationUtils.getPropertyValue("APP_ID", "");
			parameters.put("appid", appid);
			parameters.put("private_key", ApplicationUtils.getPropertyValue("PRIVATE_KEY", ""));
			parameters.put("charset","utf-8");
			parameters.put("alipay_public_key", ApplicationUtils.getPropertyValue("ALIPAY_PUBLIC_KEY", ""));
			parameters.put("biz_content", buzData.toString());//业务数据
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_qrcode_url;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			log.info("支付宝扫码支付预下单返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				//解析接口同步返回数据
				if(map!=null && map.get("is_success").equals("true")){
					if(map.get("code").equals("10000")) {
						// 生成 外部接口-支付 记录
						BlExtPay blExtPay=new BlExtPay();
						blExtPay.setSerialNo(outTradeNo);//商户订单号
						blExtPay.setAmount(totalFee);
						blExtPay.setEuPaytype("8");//二维码
						blExtPay.setDtBank("0");//支付宝
						blExtPay.setDescPay(subject+"，"+body);
						blExtPay.setSysname(systemModule);
						blExtPay.setDateAp(new Date());//请求时间
						blExtPay.setFlagPay("0");//支付标志 未完成
						blExtPay.setPkPi(pkPi);//患者主键
						blExtPay.setPkPv(pkPv);//就诊主键
						blExtPay.setPkOrg(pkOrg);//当前操作人关联机构
						blExtPay.setCreator(emp.getPkEmp());//当前操作人关联编码
						blExtPay.setCreateTime(new Date());
						blExtPay.setModifier(blExtPay.getCreator());
						blExtPay.setModityTime(blExtPay.getCreateTime());
						DataBaseHelper.insertBean(blExtPay);
						
						// 生成 预支付交易订单记录
						PayAliRecord ar = new PayAliRecord();
						ar.setPkExtpay(blExtPay.getPkExtpay());//支付主键
						ar.setPkPi(pkPi);//患者主键
						ar.setPkPv(pkPv);//就诊主键
						ar.setAppid(appid);
						ar.setTerminalId(codeEmp);
						ar.setOrderType("pay");//消费
						ar.setBody(subject);
						ar.setDetail(body);
						ar.setOutTradeNo(outTradeNo);
						ar.setTotalAmount(totalFee);
						ar.setTradeState("INIT");//下单
						ar.setInitData(result);
						ar.setBillStatus("0");
						ar.setSystemModule(systemModule);
					
						ar.setPkOrg(pkOrg);//当前操作人关联机构
						ar.setCreator(blExtPay.getCreator());//当前操作人关联编码
						ar.setCreateTime(new Date());
						ar.setModifier(ar.getCreator());
						ar.setModityTime(ar.getCreateTime());
						DataBaseHelper.insertBean(ar);
						
						//生成预支付交易订单成功,返回二维码地址
						returnMap.put("pkExtpay", blExtPay.getPkExtpay());//主键
						returnMap.put("outTradeNo", outTradeNo);//商户订单号
						returnMap.put("codeUrl", map.get("qr_code"));//二维码扫码地址
						returnMap.put("code", "200");
						returnMap.put("msg", "支付宝下单成功");
					}else{
						//失败则直接返回提示信息
						returnMap.put("code", map.get("code"));
						returnMap.put("msg", "["+map.get("code")+"]"+map.get("msg"));
					}
				}else{
					returnMap.put("code", "404");
					returnMap.put("msg", "中转接口返回失败");
				}
			}else{
				returnMap.put("code", "404");
				returnMap.put("msg", "中转接口返回空数据");
			}
		}
		return returnMap;
	}
	
	/**
	 * 条码支付（微信或者支付宝）
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> tradePay(String param,IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String pkOrg = paramMap.get("pkOrg");
		if(StringUtils.isNotEmpty(pkOrg)){
			pkOrg = UserContext.getUser().getPkOrg();
		}
		String codeEmp = paramMap.get("codeEmp");
		User emp = null;
		// 兼容自助机、公众号不登录调用接口服务时，取不到登录人信息的问题
		if(StringUtils.isNotEmpty(codeEmp) && codeEmp.startsWith("999")){
			emp = getByCodeEmp(codeEmp);
		}else{
			emp = UserContext.getUser();
		}
		
		String systemModule = paramMap.get("systemModule");
		
		String pkPi = paramMap.get("pkPi");								//患者主键
		String pkPv = paramMap.get("pkPv");								//就诊主键
		
		String outTradeNo = NHISUUID.getKeyId();						//生成系统商户订单号(时间戳+4位随机数)
		BigDecimal totalFee = new BigDecimal(paramMap.get("amount"));	//总金额
		String payType = paramMap.get("payType");						//支付类型
		String subject = "新"+paramMap.get("subject");				//商品描述
		String authCode = paramMap.get("authCode");						//支付授权码
		
		String body = paramMap.get("body");								//商品详情
		if(StringUtils.isNotEmpty(body)){
			body = "NHIS就诊主键：" + pkPv + "，" + body;
		}else{
			body = "NHIS就诊主键：" + pkPv + "，操作人工号：" + codeEmp + "，交易金额："+ String.valueOf(totalFee) +"元";
		}
		
		if(payType.equals("1")){
			/**
			 * 微信支付：条码支付
			 */
			//根据生成的订单信息调用《提交付款码支付》
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			String appid = ApplicationUtils.getPropertyValue("WECHAT_APPID", "");
			parameters.put("appid", appid);//公众账号ID
			String mchId = ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", "");
			parameters.put("mch_id", mchId);//商户号
			parameters.put("device_info", codeEmp);//终端设备号|操作员工号
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
			
			BlExtPay blExtPay= initExtPay("7", outTradeNo, totalFee, pkPi, pkPv, subject, body, pkOrg, emp.getPkEmp(), systemModule);
			PayWechatRecord wr = initWxPay(outTradeNo, totalFee, pkPi, pkPv, subject, body, appid, mchId, blExtPay.getPkExtpay(), pkOrg, emp.getCodeEmp(), emp.getPkEmp(), systemModule);

			//发送http请求通过中转服务器请求微信接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_barcode_url;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			log.info("微信条码支付返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				returnMap = updateWechatData(result, blExtPay, wr);
			}else{
				returnMap.put("code", "404");
				returnMap.put("msg", "中转接口返回空数据");
			}
		}else{
			/**
			 * 支付宝支付: 条码支付
			 */
			//构造biz_data业务数据JSON《统一收单交易支付接口》
			JSONObject buzData = new JSONObject();
			buzData.put("out_trade_no", outTradeNo);	//商户订单号
			buzData.put("total_amount", totalFee.toString());//订单总金额
			buzData.put("scene", "bar_code");			//支付场景条码支付，取值：bar_code;声波支付，取值：wave_code
			buzData.put("auth_code", authCode);			//支付授权码	
			buzData.put("subject", subject);			//订单标题
			buzData.put("body", body);					//订单描述
			buzData.put("terminal_id", codeEmp);		//商户机具终端编号|操作人工号
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
			
			BlExtPay blExtPay = initExtPay("8", outTradeNo, totalFee, pkPi, pkPv, subject, body, pkOrg, emp.getPkEmp(), systemModule);
			PayAliRecord ar = initAliPay(outTradeNo, totalFee, pkPi, pkPv, subject, body, appid, blExtPay.getPkExtpay(), pkOrg, emp.getCodeEmp(), emp.getPkEmp(), systemModule);
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_barcode_url;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			log.info("支付宝条码支付预下单返回结果:" + result);
			if(StringUtils.isNotEmpty(result)){
				returnMap = updateAliData(result, blExtPay, ar);
			}else{
				returnMap.put("code", "404");
				returnMap.put("msg", "中转接口返回空数据");
			}
		}
		return returnMap;
	}
	
	//外部接口-支付 
	private BlExtPay initExtPay(String payType, String outTradeNo, BigDecimal totalFee, 
			String pkPi, String pkPv, String subject, String body, String pkOrg, String pkEmp, String systemModule){
		BlExtPay blExtPay = new BlExtPay();
		blExtPay.setSerialNo(outTradeNo);//商户订单号
		blExtPay.setAmount(totalFee);
		blExtPay.setEuPaytype(payType);//微信、支付宝
		blExtPay.setDtBank("0");
		blExtPay.setDescPay(subject+"，"+body);
		blExtPay.setSysname(systemModule);
		blExtPay.setDateAp(new Date());//请求时间
		blExtPay.setFlagPay("0");//待支付
		blExtPay.setPkPi(pkPi);//患者主键
		blExtPay.setPkPv(pkPv);//就诊主键
		blExtPay.setPkOrg(pkOrg);//当前操作人关联机构
		blExtPay.setCreator(pkEmp);//当前操作人关联编码
		blExtPay.setCreateTime(new Date());
		blExtPay.setModifier(blExtPay.getCreator());
		blExtPay.setModityTime(blExtPay.getCreateTime());
		DataBaseHelper.insertBean(blExtPay);
		
		return blExtPay;
	}
	
	private PayWechatRecord initWxPay(String outTradeNo, BigDecimal totalFee, String pkPi, String pkPv, 
			String subject, String body, String appid, String mchId, String pkExtpay, String pkOrg, String codeEmp, String pkEmp, String systemModule){
		PayWechatRecord wr = new PayWechatRecord();
		//生成微信支付交易记录
		wr.setPkExtpay(pkExtpay);//支付主键
		wr.setPkPi(pkPi);//患者主键
		wr.setPkPv(pkPv);//就诊主键
		wr.setAppid(appid);
		wr.setMchId(mchId);
		wr.setDeviceInfo(codeEmp);
		wr.setOrderType("pay");//消费
		wr.setBody(subject);
		wr.setDetail(body);
		wr.setOutTradeNo(outTradeNo);
		wr.setTotalAmount(totalFee);
		wr.setTradeState("INIT");//待付款
		wr.setBillStatus("0");
		wr.setSystemModule(systemModule);
		
		wr.setPkOrg(pkOrg);//当前操作人关联机构
		wr.setCreator(pkEmp);//当前操作人关联编码
		wr.setCreateTime(new Date());
		wr.setModifier(wr.getCreator());
		wr.setModityTime(wr.getCreateTime());
		DataBaseHelper.insertBean(wr);
		
		return wr;
	}
	
	private PayAliRecord initAliPay(String outTradeNo, BigDecimal totalFee, String pkPi, String pkPv, 
			String subject, String body, String appid, String pkExtpay, String pkOrg, String codeEmp, String pkEmp, String systemModule){
		//生成微信支付交易记录
		PayAliRecord ar = new PayAliRecord();
		ar.setPkExtpay(pkExtpay);//支付主键
		ar.setPkPi(pkPi);//患者主键
		ar.setPkPv(pkPv);//就诊主键
		ar.setAppid(appid);
		ar.setTerminalId(codeEmp);
		ar.setOrderType("pay");//消费
		ar.setBody(subject);
		ar.setDetail(body);
		ar.setOutTradeNo(outTradeNo);
		ar.setTotalAmount(totalFee);
		ar.setTradeState("INIT");//待付款
		ar.setBillStatus("0");
		ar.setSystemModule(systemModule);
		
		ar.setPkOrg(pkOrg);//当前操作人关联机构
		ar.setCreator(pkEmp);//当前操作人关联编码
		ar.setCreateTime(new Date());
		ar.setModifier(ar.getCreator());
		ar.setModityTime(ar.getCreateTime());
		DataBaseHelper.insertBean(ar);
		
		return ar;
	}
	
	/**
	 * 退费（微信或者支付宝）
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> payRefund(String param,IUser user) throws ParseException{
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,String> returnMap = new HashMap<String,String>();
		
		String pkOrg = paramMap.get("pkOrg");
		if(!StringUtils.isNotEmpty(pkOrg)){
			pkOrg = UserContext.getUser().getPkOrg();
		}
		String codeEmp = paramMap.get("codeEmp");
		User emp = null;
		// 兼容自助机、公众号不登录调用接口服务时，取不到登录人信息的问题
		if(StringUtils.isNotEmpty(codeEmp) && codeEmp.startsWith("999")){
			emp = getByCodeEmp(codeEmp);
		}else{
			emp = UserContext.getUser();
		}
		
		String outRefundNo = NHISUUID.getKeyId();//生成系统商户退款单号(时间戳+4位随机数)
		String refundType = paramMap.get("refundType");
		String outTradeNo = paramMap.get("tradeNo");//交易号
		String refundAmount = paramMap.get("refundAmount");//总金额
		String refundReason = paramMap.get("refundReason");//退款原因
		String isCreateExtDepoRefund = paramMap.containsKey("isCreateExtDepoRefund")?paramMap.get("isCreateExtDepoRefund"):null;//是否生成第三方表、收付款表的退款数据，默认为空则生成
		BigDecimal refundAmt = new BigDecimal(refundAmount).multiply(new BigDecimal(-1));
		
		if(refundType.equals("1")){
			/*
			 * 微信申请退款
			 */
			returnMap = refundWechat(outTradeNo, refundReason, outRefundNo, refundAmount, isCreateExtDepoRefund, refundAmt, emp);
			
		}else if(refundType.equals("2")){
			String sql = "select * from PAY_ALI_RECORD where ORDER_TYPE='pay' and OUT_TRADE_NO = ?";// and REFUND_TRADE_NO is null
			PayAliRecord ar = DataBaseHelper.queryForBean(sql, PayAliRecord.class, new Object[]{outTradeNo});
			boolean flag = true;
			if("mz".equals(ar.getSystemModule()) && "结算失败全额退款".equals(refundReason)){
				String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
				BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkExtpay()});
				if(blExtPay!=null && StringUtils.isNotEmpty(blExtPay.getPkSettle())){
					returnMap.put("code", "999");
					returnMap.put("msg", "结算成功，不予许退款！");
					flag = false;
				}
			}
			if(ar!=null && flag){
				/*
				 * 支付宝申请退款
				 */
				String appletsAppid = ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_APP_ID", "");
				if(StringUtils.isEmpty(ar.getAppid())||!appletsAppid.equals(ar.getAppid())) {
					//支付宝退款
					returnMap = refundAli(ar, outTradeNo, refundReason, outRefundNo, refundAmount, isCreateExtDepoRefund, refundAmt, emp);
				}else {
					//支付宝小程序退款
					returnMap = refundAliApp(ar, outTradeNo, refundReason, outRefundNo, refundAmount, isCreateExtDepoRefund, refundAmt, emp);
				}
			}else{
				if(flag){
					returnMap.put("code", "404");
					returnMap.put("msg", "交易订单不存在");
				}
			}
		}
		return returnMap;
	}

	private Map<String, String> refundWechat(String outTradeNo, String refundReason, String outRefundNo, String refundAmount,
			String isCreateExtDepoRefund, BigDecimal refundAmt, User emp){
		
		Map<String, String> returnMap = new HashMap<String, String>();
		String sql = "select * from PAY_WECHAT_RECORD where ORDER_TYPE='pay' and OUT_TRADE_NO = ?";//and REFUND_TRADE_NO is null 
		PayWechatRecord wr = DataBaseHelper.queryForBean(sql, PayWechatRecord.class, new Object[]{outTradeNo});
		boolean flag = true;
		if("mz".equals(wr.getSystemModule()) && "结算失败全额退款".equals(refundReason)){
			String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
			BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{wr.getPkExtpay()});
			if(blExtPay!=null && StringUtils.isNotEmpty(blExtPay.getPkSettle())){
				returnMap.put("code", "999");
				returnMap.put("msg", "结算成功，不予许退款！");
				flag = false;
			}
		}
		if(wr!=null && flag){
			/**
			 * 微信支付申请退款接口
			 */
			//根据生成的订单信息调用《微信申请退款接口》
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));//公众账号ID
			parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//商户号
			parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
			parameters.put("out_trade_no", outTradeNo);//商户订单号
			parameters.put("out_refund_no", outRefundNo);//商户退款单号
			BigDecimal amount = wr.getTotalAmount().multiply(new BigDecimal(100));//转分
			Integer totalMoney = Integer.parseInt(amount.stripTrailingZeros().toPlainString());
			parameters.put("total_fee", totalMoney);//订单金额
			BigDecimal money = new BigDecimal(refundAmount).multiply(new BigDecimal(100));//转分
			Integer refundMoney = Integer.parseInt(money.stripTrailingZeros().toPlainString());
			parameters.put("refund_fee", refundMoney);//退款金额
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
						
						//外部接口-支付退款信息
						if(StringUtils.isEmpty(isCreateExtDepoRefund)){
							String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
							BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{wr.getPkExtpay()});
							if(blExtPay!=null){
								BlExtPay refundExtPay = new BlExtPay();
								BeanUtils.copyProperties(blExtPay, refundExtPay);
								
								if(StringUtils.isNotEmpty(blExtPay.getPkDepo())){
									String depoSql = "select * from BL_DEPOSIT where PK_DEPO = ?";
									ZsbaBlDeposit depo = DataBaseHelper.queryForBean(depoSql, ZsbaBlDeposit.class, new Object[]{blExtPay.getPkDepo()});
									if(depo!=null){
										ZsbaBlDeposit refundDepo = new ZsbaBlDeposit();
										BeanUtils.copyProperties(depo, refundDepo);
										refundDepo.setPkDepo(null);
										refundDepo.setAmount(refundAmt);
										refundDepo.setNote("微信统一退款");
										refundDepo.setEuDirect("-1");
										refundDepo.setCreateTime(new Date());
										refundDepo.setCreator(emp.getPkEmp());
										refundDepo.setPkEmpPay(emp.getPkEmp());
										refundDepo.setNameEmpPay(emp.getNameEmp());
										refundDepo.setTs(refundDepo.getCreateTime());
										refundDepo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
										refundDepo.setVoidType("1");//1是退费
										refundDepo.setVoidEMP(emp.getNameEmp());//
										refundDepo.setVoidTIME(new Date());
										refundDepo.setFlagCc("0");
										DataBaseHelper.insertBean(refundDepo);
										
										refundExtPay.setPkDepo(refundDepo.getPkDepo());
										
										depo.setVoidType("4");//4是被退费或被作废
										depo.setVoidEMP(emp.getNameEmp());//
										depo.setVoidTIME(new Date());
										DataBaseHelper.updateBeanByPk(depo);
										
									}
								}
								
								refundExtPay.setPkExtpay(null);
								refundExtPay.setAmount(refundAmt);
								refundExtPay.setDescPay("微信统一退款");
								refundExtPay.setResultPay(result);
								refundExtPay.setTradeNo(map.get("refund_id"));
								refundExtPay.setSerialNo(outRefundNo);
								refundExtPay.setRefundNo(blExtPay.getSerialNo());//退了哪一条缴款记录
								refundExtPay.setDateRefund(new Date());//退款支付时间
								refundExtPay.setCreator(emp.getPkEmp());
								refundExtPay.setCreateTime(new Date());
								refundExtPay.setModifier(emp.getPkEmp());
								refundExtPay.setModityTime(new Date());
								DataBaseHelper.insertBean(refundExtPay);
								
								refundWr.setPkExtpay(refundExtPay.getPkExtpay());
								
								// 更新缴款支付记录关联退款记录
								blExtPay.setRefundNo(outRefundNo);//被哪条退费记录退款
								DataBaseHelper.updateBeanByPk(blExtPay);
							}
						} else {
							String extPaySql = "select * from BL_EXT_PAY where PK_PV=? and AMOUNT=?";
							BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{wr.getPkPv(), refundAmt});
							if(blExtPay!=null){
								refundWr.setPkExtpay(blExtPay.getPkExtpay());
							}
						}
						
						refundWr.setPkPayWechatRecord(null);
						refundWr.setInitData(null);
						refundWr.setPayTime(null);
						refundWr.setPayData(null);
						refundWr.setOrderType("refund");//退费
						refundWr.setTotalAmount(refundAmt);//退款金额
						refundWr.setTradeState(resultCode);
						refundWr.setCreateTime(new Date());
						refundWr.setRefundData(result);
						refundWr.setDetail(refundReason);
						refundWr.setWechatTradeNo(map.get("refund_id"));
						refundWr.setOutTradeNo(outRefundNo);
						refundWr.setRefundTradeNo(wr.getOutTradeNo());
						
						refundWr.setPkOrg(pkOrg);//当前操作人关联机构
						refundWr.setCreator(emp.getPkEmp());//当前操作人关联编码
						refundWr.setCreateTime(new Date());
						refundWr.setModifier(refundWr.getCreator());
						refundWr.setModityTime(refundWr.getCreateTime());
						DataBaseHelper.insertBean(refundWr);
						
						// 更新缴款记录关联退款记录
						wr.setRefundTradeNo(outRefundNo);
						DataBaseHelper.updateBeanByPk(wr);
						
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
		}else{
			if(flag){
				returnMap.put("code", "404");
				returnMap.put("msg", "交易订单不存在");
			}
		}
		return returnMap;
	}
	
	private Map<String, String> refundAli(PayAliRecord ar, String outTradeNo, String refundReason, String outRefundNo, String refundAmount,
			String isCreateExtDepoRefund, BigDecimal refundAmt, User emp){
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		//构造申请退款接口的JSON参数
		JSONObject buzData = new JSONObject();
		buzData.put("out_trade_no", outTradeNo);//支付宝交易号
		buzData.put("refund_amount", refundAmount);//退款金额
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
					
					//外部接口-支付退款信息
					if(StringUtils.isEmpty(isCreateExtDepoRefund)){
						String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
						BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkExtpay()});
						if(blExtPay!=null){
							BlExtPay refundExtPay = new BlExtPay();
							BeanUtils.copyProperties(blExtPay, refundExtPay);
							
							if(StringUtils.isNotEmpty(blExtPay.getPkDepo())){
								String depoSql = "select * from BL_DEPOSIT where PK_DEPO = ?";
								ZsbaBlDeposit depo = DataBaseHelper.queryForBean(depoSql, ZsbaBlDeposit.class, new Object[]{blExtPay.getPkDepo()});
								if(depo!=null){
									ZsbaBlDeposit refundDepo = new ZsbaBlDeposit();
									BeanUtils.copyProperties(depo, refundDepo);
									refundDepo.setPkDepo(null);
									refundDepo.setAmount(refundAmt);
									refundDepo.setNote("支付宝统一退款");
									refundDepo.setEuDirect("-1");
									refundDepo.setCreateTime(new Date());
									refundDepo.setCreator(emp.getPkEmp());
									refundDepo.setPkEmpPay(emp.getPkEmp());
									refundDepo.setNameEmpPay(emp.getNameEmp());
									refundDepo.setTs(refundDepo.getCreateTime());
									refundDepo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
									refundDepo.setVoidType("1");//1是退费
									refundDepo.setVoidEMP(emp.getNameEmp());//
									refundDepo.setVoidTIME(new Date());
									refundDepo.setFlagCc("0");
									DataBaseHelper.insertBean(refundDepo);
									
									refundExtPay.setPkDepo(refundDepo.getPkDepo());
									
									depo.setVoidType("4");//4是被退费或被作废
									depo.setVoidEMP(emp.getNameEmp());//
									depo.setVoidTIME(new Date());
									DataBaseHelper.updateBeanByPk(depo);
								}
							}
							
							refundExtPay.setPkExtpay(null);
							refundExtPay.setAmount(refundAmt);
							refundExtPay.setDescPay("支付宝统一退款");
							refundExtPay.setResultPay(result);
							refundExtPay.setSerialNo(outRefundNo);
							refundExtPay.setRefundNo(blExtPay.getSerialNo());//退了哪条缴款记录
							refundExtPay.setDateRefund(new Date());//退款支付时间
							refundExtPay.setCreator(emp.getPkEmp());
							refundExtPay.setCreateTime(new Date());
							refundExtPay.setModifier(emp.getPkEmp());
							refundExtPay.setModityTime(new Date());
							DataBaseHelper.insertBean(refundExtPay);

							refundAr.setPkExtpay(refundExtPay.getPkExtpay());
							
							// 更新缴款支付记录关联退款记录
							blExtPay.setRefundNo(outRefundNo);//被哪条退款记录退款
							DataBaseHelper.updateBeanByPk(blExtPay);
						}
					} else {
						String extPaySql = "select * from BL_EXT_PAY where PK_PV=? and AMOUNT=?";
						BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkPv(), refundAmt});
						if(blExtPay!=null){
							refundAr.setPkExtpay(blExtPay.getPkExtpay());
						}
					}
					
					refundAr.setPkPayAliRecord(null);
					refundAr.setInitData(null);
					refundAr.setPayTime(null);
					refundAr.setPayData(null);
					refundAr.setOrderType("refund");//退费
					refundAr.setTotalAmount(refundAmt);//退款金额
					refundAr.setTradeState("SUCCESS");
					refundAr.setCreateTime(new Date());
					refundAr.setRefundData(result);
					refundAr.setDetail(refundReason);
					refundAr.setOutTradeNo(outRefundNo);
					refundAr.setRefundTradeNo(ar.getOutTradeNo());
					
					refundAr.setPkOrg(pkOrg);//当前操作人关联机构
					refundAr.setCreator(emp.getPkEmp());//当前操作人关联编码
					refundAr.setCreateTime(new Date());
					refundAr.setModifier(refundAr.getCreator());
					refundAr.setModityTime(refundAr.getCreateTime());
					DataBaseHelper.insertBean(refundAr);
					
					// 更新缴款记录关联退款记录
					ar.setRefundTradeNo(outRefundNo);
					DataBaseHelper.updateBeanByPk(ar);
					
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
	
	private Map<String, String> refundAliApp(PayAliRecord ar, String outTradeNo, String refundReason, String outRefundNo, String refundAmount,
			String isCreateExtDepoRefund, BigDecimal refundAmt, User emp){
		
		Map<String, String> returnMap = new HashMap<String, String>();
		//构造申请退款接口的JSON参数
		JSONObject buzData = new JSONObject();
		buzData.put("out_trade_no", outTradeNo);//支付宝交易号
		buzData.put("refund_amount", refundAmount);//退款金额
		buzData.put("out_request_no", outRefundNo);//商户退款请求号
		buzData.put("refund_reason", refundReason);//退款原因
		
		//构造预下单参数MAP
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_GATEWAY", ""));
		parameters.put("appid", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_APP_ID", ""));
		parameters.put("charset", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_CHARSET", ""));
		parameters.put("publicKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PUBLIC_KEY", ""));
		parameters.put("privateKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PRIVATE_KEY", ""));
		parameters.put("bizContent", buzData.toString());//业务数据
		
		//发送http请求通过中转服务器请求支付宝接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_applets_refund_url;
		String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
		log.info("支付宝小程序退款返回结果:" + result);
		
		if(StringUtils.isNotEmpty(result)){
			Map<String,String> map = JsonUtil.readValue(result, Map.class);
			if(map.get("state").equals("true")){
				Map<String,String> returnInfo = JsonUtil.readValue(map.get("return_info"), Map.class);
				if(returnInfo.get("is_success").equals("true")){
					String code = returnInfo.get("code");
					String msg  = returnInfo.get("msg");
					if(code.equals("10000")){
						//生成交易记录表退款记录
						PayAliRecord refundAr = new PayAliRecord();
						BeanUtils.copyProperties(ar, refundAr);
						
						//外部接口-支付退款信息
						if(StringUtils.isEmpty(isCreateExtDepoRefund)){
							String extPaySql = "select * from BL_EXT_PAY where PK_EXTPAY = ?";
							BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkExtpay()});
							if(blExtPay!=null){
								BlExtPay refundExtPay = new BlExtPay();
								BeanUtils.copyProperties(blExtPay, refundExtPay);
								
								if(StringUtils.isNotEmpty(blExtPay.getPkDepo())){
									String depoSql = "select * from BL_DEPOSIT where PK_DEPO = ?";
									ZsbaBlDeposit depo = DataBaseHelper.queryForBean(depoSql, ZsbaBlDeposit.class, new Object[]{blExtPay.getPkDepo()});
									if(depo!=null){
										ZsbaBlDeposit refundDepo = new ZsbaBlDeposit();
										BeanUtils.copyProperties(depo, refundDepo);
										refundDepo.setPkDepo(null);
										refundDepo.setAmount(refundAmt);
										refundDepo.setNote("支付宝统小程序一退款");
										refundDepo.setEuDirect("-1");
										refundDepo.setCreateTime(new Date());
										refundDepo.setCreator(emp.getPkEmp());
										refundDepo.setPkEmpPay(emp.getPkEmp());
										refundDepo.setTs(refundDepo.getCreateTime());
										refundDepo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
										DataBaseHelper.insertBean(refundDepo);
										refundExtPay.setPkDepo(refundDepo.getPkDepo());
									}
								}
								refundExtPay.setPkExtpay(null);
								refundExtPay.setAmount(refundAmt);
								refundExtPay.setDescPay("支付宝小程序统一退款");
								refundExtPay.setResultPay(result);
								refundExtPay.setSerialNo(outRefundNo);
								refundExtPay.setRefundNo(blExtPay.getSerialNo());//退了哪条缴款记录
								refundExtPay.setDateRefund(new Date());//退款支付时间
								refundExtPay.setCreator(emp.getPkEmp());
								refundExtPay.setCreateTime(new Date());
								refundExtPay.setModifier(emp.getPkEmp());
								refundExtPay.setModityTime(new Date());
								DataBaseHelper.insertBean(refundExtPay);
								refundAr.setPkExtpay(refundExtPay.getPkExtpay());
								// 更新缴款支付记录关联退款记录
								blExtPay.setRefundNo(outRefundNo);//被哪条退款记录退款
								DataBaseHelper.updateBeanByPk(blExtPay);
							}
						} else {
							String extPaySql = "select * from BL_EXT_PAY where PK_PV=? and AMOUNT=?";
							BlExtPay blExtPay = DataBaseHelper.queryForBean(extPaySql, BlExtPay.class, new Object[]{ar.getPkPv(), refundAmt});
							if(blExtPay!=null){
								refundAr.setPkExtpay(blExtPay.getPkExtpay());
							}
						}
						
						refundAr.setPkPayAliRecord(null);
						refundAr.setInitData(null);
						refundAr.setPayTime(null);
						refundAr.setPayData(null);
						refundAr.setOrderType("refund");//退费
						refundAr.setTotalAmount(refundAmt);//退款金额
						refundAr.setTradeState("SUCCESS");
						refundAr.setCreateTime(new Date());
						refundAr.setRefundData(result);
						refundAr.setDetail(refundReason);
						refundAr.setOutTradeNo(outRefundNo);
						refundAr.setRefundTradeNo(ar.getOutTradeNo());
						refundAr.setPkOrg(pkOrg);//当前操作人关联机构
						refundAr.setCreator(emp.getPkEmp());//当前操作人关联编码
						refundAr.setCreateTime(new Date());
						refundAr.setModifier(refundAr.getCreator());
						refundAr.setModityTime(refundAr.getCreateTime());
						DataBaseHelper.insertBean(refundAr);
						// 更新缴款记录关联退款记录
						ar.setRefundTradeNo(outRefundNo);
						DataBaseHelper.updateBeanByPk(ar);
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
				returnMap.put("msg", "中转接口返回失败");
			}
		}else{
			returnMap.put("code", "404");
			returnMap.put("msg", "中转接口返回空数据");
		}
		return returnMap;
	}
	
	/**
	 * 微信主动查询订单支付结果
	 * @param outTradeNo	商户订单号
	 * @return
	 */
	private static String activeQueryWechat(String outTradeNo){
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
	private Map<String,String> updateWechatData(String result, BlExtPay blExtPay, PayWechatRecord wr){
		Date currTime = new Date();
		Map<String,String> returnMap = new HashMap<String,String>();
		@SuppressWarnings("unchecked")
		Map<String,String> map = JsonUtil.readValue(result, Map.class);
		
		// 返回值return_code和result_code都为SUCCESS时支付成功
		String return_code = map.get("return_code");
		String return_msg = map.get("return_msg");
		if(return_code.equals("SUCCESS")){
			String result_code = map.get("result_code");
			String trade_state = map.get("trade_state");
			if(result_code.equals("SUCCESS") &&  (StringUtils.isEmpty(trade_state) || "SUCCESS".equals(trade_state))){
				//外部接口-支付 
				blExtPay.setFlagPay("1");//支付成功
				blExtPay.setTradeNo(map.get("transaction_id"));//微信交易号
				blExtPay.setDatePay(currTime);//支付成功时间
                blExtPay.setResultPay(JSONObject.fromObject(map).toString());
				blExtPay.setModityTime(currTime);
				DataBaseHelper.updateBeanByPk(blExtPay);
				
				//生成微信支付交易记录
				wr.setDeviceInfo(map.get("device_info"));
				wr.setTradeState(result_code);//付款标志
				wr.setOpenid(map.get("openid"));
				wr.setWechatTradeNo(map.get("transaction_id"));
				wr.setAttach(map.get("attach"));
				wr.setPayTime(map.get("time_end"));
				wr.setPayData(result);
				wr.setModityTime(currTime);
				DataBaseHelper.updateBeanByPk(wr);
				
				returnMap.put("pkExtpay", blExtPay.getPkExtpay());//主键
				returnMap.put("outTradeNo", wr.getOutTradeNo());//商户订单号
				returnMap.put("code", "200");
				returnMap.put("msg", "微信条码支付成功");
			}else{
				String errCode = map.get("err_code");
				if("PAYERROR".equals(trade_state)){
					returnMap.put("code", "400");
					returnMap.put("msg", "[400]:"+map.get("trade_state_desc"));
				}else{
					if("USERPAYING".equals(errCode) || "SYSTEMERROR".equals(errCode) || "BANKERROR".equals(errCode)){
						/*
						 *  需要调用查询接口获取订单状态
						 */
						result = "";
						result = activeQueryWechat(wr.getOutTradeNo());
						returnMap = updateWechatData(result, blExtPay, wr);
					}else{
						wr.setTradeState(result_code);//付款标志
						wr.setPayData(result);
						wr.setModityTime(currTime);
						DataBaseHelper.updateBeanByPk(wr);
						
						returnMap.put("code", map.get("err_code"));
						returnMap.put("msg", "["+map.get("err_code")+"]:"+map.get("err_code_des"));
					}
				}
			}
		}else{
			returnMap.put("code", return_code);
			returnMap.put("msg", "["+return_code+"]:"+return_msg);
		}
		return returnMap;
	}
	
	/**
	 * 支付宝主动查询订单支付结果
	 * @param outTradeNo	商户订单号
	 * @return
	 */
	private static String alipayTradeQuery(String outTradeNo){
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
			buzData.put("out_trade_no", outTradeNo);	//商户订单号
			
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
	
	private static Map<String,String> aliAppletsOrderQuery(String outTradeNo){
		Map<String,String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "");
		resultMap.put("state", "");
		try {
			//构造biz_data业务数据JSON《统一收单线下交易查询》
			JSONObject buzData = new JSONObject();
			buzData.put("out_trade_no", outTradeNo);	//商户订单号
			
			//构造预下单参数MAP
			SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
			parameters.put("gateway", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_GATEWAY", ""));
			parameters.put("appid", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_APP_ID", ""));
			parameters.put("charset", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_CHARSET", ""));
			parameters.put("publicKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PUBLIC_KEY", ""));
			parameters.put("privateKey", ApplicationUtils.getPropertyValue("ALIPAY_APPLETS_PRIVATE_KEY", ""));
			parameters.put("bizContent", buzData.toString());//业务数据
			
			//发送http请求通过中转服务器请求支付宝接口后返回数据处理
			String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.ali_applets_order_query;
			String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
			log.info("支付宝小程序[主动查询订单]返回结果:" + result);
			
			if(StringUtils.isNotEmpty(result)){
				@SuppressWarnings("unchecked")
				Map<String,String> map = JsonUtil.readValue(result, Map.class);
				if(map.get("state").equals("true")) {
					@SuppressWarnings("unchecked")
					Map<String,String> returnInfo = JsonUtil.readValue(map.get("return_info"), Map.class);
					resultMap.put("result", map.get("return_info"));
					if (returnInfo.get("is_success").equals("true")) {
						/*
						 *  交易状态：
						 *  WAIT_BUYER_PAY（交易创建，等待买家付款）、
						 *  TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
						 *  TRADE_SUCCESS（交易支付成功）、
						 *  TRADE_FINISHED（交易结束，不可退款）
						 */
						if(!"WAIT_BUYER_PAY".equals(returnInfo.get("trade_status"))){
							if("TRADE_SUCCESS".equals(returnInfo.get("trade_status")) || "TRADE_FINISHED".equals(returnInfo.get("trade_status"))){
								resultMap.put("state", "SUCCESS");//交易成功
							}else{
								resultMap.put("state", "FAIL");//交易失败
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("主动查询[支付宝小程序]订单交易结果出现异常："+e.getMessage());
		}
		return resultMap;
	}
	private Map<String,String> updateAliData(String result, BlExtPay blExtPay, PayAliRecord ar){
		Date currTime = new Date();
		Map<String,String> returnMap = new HashMap<String,String>();
		
		@SuppressWarnings("unchecked")
		Map<String,String> map = JsonUtil.readValue(result, Map.class);
		if(map!=null && map.get("is_success").equals("true")){
			if(map.get("code").equals("10000") || map.get("code").equals("10003")) {
				//外部接口-支付 
				blExtPay.setFlagPay("1");//支付成功
				blExtPay.setTradeNo(map.get("trade_no"));//支付宝交易号
				blExtPay.setDatePay(currTime);//支付成功时间
                blExtPay.setResultPay(JSONObject.fromObject(map).toString());
				blExtPay.setModityTime(currTime);
				DataBaseHelper.updateBeanByPk(blExtPay);
				
				//生成微信支付交易记录
				ar.setTradeState("SUCCESS");//付款成功
				ar.setBuyerId(map.get("buyer_id"));
				ar.setAliTradeNo(map.get("trade_no"));
				ar.setPayTime(map.get("time_end"));
				ar.setPayData(result);
				
				ar.setModityTime(currTime);
				DataBaseHelper.updateBeanByPk(ar);
										
				returnMap.put("pkExtpay", blExtPay.getPkExtpay());//主键
				returnMap.put("outTradeNo", ar.getOutTradeNo());//商户订单号
				returnMap.put("code", "200");
				returnMap.put("msg", "支付宝条码支付成功");
			}else{
				
				if(map.get("code").contains("SYSTEM_ERROR")){
					result = "";
					result = alipayTradeQuery(ar.getOutTradeNo());
					returnMap = updateAliData(result, blExtPay, ar);
				}else{
					
					ar.setTradeState("FAIL");//付款失败
					ar.setPayData(result);
					ar.setModityTime(currTime);
					DataBaseHelper.updateBeanByPk(ar);
					
					returnMap.put("code", map.get("code"));
					returnMap.put("msg", "["+map.get("code")+"]:"+map.get("msg"));
				}
			}
		}else{
			returnMap.put("code", map.get("code"));
			returnMap.put("msg", "["+map.get("code")+"]:"+map.get("msg"));
		}
		return returnMap;
	}
	
	/**
	 * 查询订单接口（外部接口-支付）
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,String> inquiryOrder(String param,IUser user){
		@SuppressWarnings("unchecked")
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String,String> returnMap = new HashMap<String,String>();
		//商户订单
		String outTradeNo = paramMap.get("serialNo");
		String isAlipay = paramMap.get("isAlipay");
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where SERIAL_NO = ?", BlExtPay.class, new Object[]{outTradeNo});
		if(blExtPay!=null){
			if(StringUtils.isNotEmpty(isAlipay)&&"1".equals(isAlipay)) {
				//支付宝小程序查询订单
				returnMap = queryAliApplets(blExtPay, outTradeNo, paramMap);
			}else {
				//支付方式：0微信、1支付宝
				if("7".equals(blExtPay.getEuPaytype())){
					returnMap = queryWechat(blExtPay, outTradeNo, paramMap);
				}else if("8".equals(blExtPay.getEuPaytype())){
					returnMap = queryAli(blExtPay, outTradeNo, paramMap);
				}else{
					returnMap = setRespData("404", "交易订单不存在", outTradeNo, "", "");
				}
			}
		}else{
			returnMap = setRespData("404", "交易订单不存在", outTradeNo, "", "");
		}
		return returnMap;
	}
	
	private Map<String, String> queryAliApplets(BlExtPay blExtPay, String outTradeNo, Map<String, String> paramMap) {
		Map<String,String> returnMap = new HashMap<String,String>();
		PayAliRecord ar = DataBaseHelper.queryForBean("select * from PAY_ALI_RECORD where OUT_TRADE_NO = ?", PayAliRecord.class, new Object[]{outTradeNo});
		if(ar!=null){
			returnMap.put("account", String.valueOf(blExtPay.getAmount()));
			if("INIT".equals(ar.getTradeState())){
				
				Map<String,String> resultMap = aliAppletsOrderQuery(outTradeNo);
				String tradeState = resultMap.get("state");
				if(StringUtils.isNotEmpty(tradeState)){
					
					String payData = resultMap.get("result");
					ar.setPayData(payData);
					ar.setTradeState(tradeState);
					
					if("SUCCESS".equals(tradeState)){
						@SuppressWarnings("unchecked")
						Map<String,String> map = JsonUtil.readValue(payData, Map.class);
						
						ar.setAliTradeNo(map.get("tradeNo"));
						ar.setBuyerId(map.get("buyerId"));
		            	ar.setPayTime(map.get("sendPayDate"));
  	            		ar.setModityTime(new Date());
  	            		DataBaseHelper.updateBeanByPk(ar);// 更新交易数据
  	            		
  	            		blExtPay.setFlagPay("1");//支付成功
  	            		blExtPay.setTradeNo(map.get("tradeNo"));//支付宝订单号
  	            		DataBaseHelper.updateBeanByPk(blExtPay);// 更新交易数据
  	            		
	  	            	// 查询终端 - 自助机使用二维码支付时，成功后需要生成押金记录
						String queryTerminal = paramMap.get("queryTerminal");
						if(StringUtils.isNotEmpty(queryTerminal)){
							String pkDepo = createDepoist(blExtPay);
							log.info("支付宝小程序[主动查询订单]生成BlDeposit数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
							
							String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, DATE_PAY=?, PK_DEPO=? where PK_EXTPAY=? ";
	  						DataBaseHelper.update(extpaySql, "1", new Date(), pkDepo, ar.getPkExtpay());
	  						log.info("支付宝小程序[主动查询订单]更新BlExtPay数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
						}
						returnMap = setRespData("200", "该订单已支付", outTradeNo, blExtPay.getPkExtpay(), "1");
					}else{
						blExtPay.setFlagPay("2");//支付失败
						DataBaseHelper.updateBeanByPk(blExtPay);// 更新交易数据
						
						returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
					}
				}else{
					returnMap = setRespData("200", "该订单未支付", outTradeNo, blExtPay.getPkExtpay(), "0");
				}
			}else if("SUCCESS".equals(ar.getTradeState())){
				returnMap = setRespData("200", "该订单已支付", outTradeNo, blExtPay.getPkExtpay(), "1");
			}else{
				returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
			}
		}else{
			returnMap = setRespData("404", "交易订单不存在", outTradeNo, blExtPay.getPkExtpay(), "");
		}
		return returnMap;
	}

	private Map<String,String> queryWechat(BlExtPay blExtPay, String outTradeNo, Map<String,String> paramMap){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		PayWechatRecord wr = DataBaseHelper.queryForBean("select * from PAY_WECHAT_RECORD where OUT_TRADE_NO = ?", PayWechatRecord.class, new Object[]{outTradeNo});
		if(wr!=null){
			returnMap.put("account", String.valueOf(blExtPay.getAmount()));
			if("INIT".equals(wr.getTradeState())){
				
				Map<String,String> resultMap = wechatOrderQuery(outTradeNo);
				String tradeState = resultMap.get("state");
				if(StringUtils.isNotEmpty(tradeState)){
					
					String payData = resultMap.get("result");
					
					if("SUCCESS".equals(tradeState)){
						@SuppressWarnings("unchecked")
						Map<String,String> map = JsonUtil.readValue(payData, Map.class);
						
						String wrSql = " update PAY_WECHAT_RECORD set PAY_DATA=?, TRADE_STATE=?, WECHAT_TRADE_NO=?, OPENID=?, PAY_TIME=?, MODITY_TIME=GETDATE() where PK_PAY_WECHAT_RECORD=? ";
  						DataBaseHelper.update(wrSql, payData, tradeState, map.get("transaction_id"), map.get("openid"), map.get("time_end"), wr.getPkPayWechatRecord());
  						log.info("微信[主动查询订单]更新订单数据完成：{}, {}", outTradeNo, tradeState);

	  	            	// 查询终端 - 自助机使用二维码支付时，成功后需要生成押金记录
						String queryTerminal = paramMap.get("queryTerminal");
						if(StringUtils.isNotEmpty(queryTerminal)){
							String pkDepo = createDepoist(blExtPay);
							log.info("微信[主动查询订单]生成BlDeposit数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
							
							String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, TRADE_NO=?, DATE_PAY=?, PK_DEPO=? where PK_EXTPAY=? ";
	  						DataBaseHelper.update(extpaySql, "1", map.get("transaction_id"), new Date(), pkDepo, wr.getPkExtpay());
	  						log.info("微信[主动查询订单]更新BlExtPay数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
						}else{
							String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, TRADE_NO=?, DATE_PAY=? where PK_EXTPAY=? ";
	  						DataBaseHelper.update(extpaySql, "1", map.get("transaction_id"), new Date(), wr.getPkExtpay());
	  						log.info("微信[主动查询订单]更新BlExtPay数据完成：{}, {}", outTradeNo, tradeState);
						}
						returnMap = setRespData("200", "订单支付成功", outTradeNo, blExtPay.getPkExtpay(), "1");
					}else{

						String wrSql = " update PAY_WECHAT_RECORD set PAY_DATA=?, TRADE_STATE=?, MODITY_TIME=GETDATE() where PK_PAY_WECHAT_RECORD=? ";
  						DataBaseHelper.update(wrSql, payData, tradeState, wr.getPkPayWechatRecord());
  						log.info("微信[主动查询订单]更新订单数据完成：{}, {}", outTradeNo, tradeState);

  						String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, DATE_PAY=? where PK_EXTPAY=? ";
  						DataBaseHelper.update(extpaySql, "2", new Date(), wr.getPkExtpay());
						log.info("微信[主动查询订单]更新BlExtPay数据完成：{}, {}", outTradeNo, tradeState);
						
						returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
					}
				}else{
					returnMap = setRespData("200", "该订单未支付", outTradeNo, blExtPay.getPkExtpay(), "0");
				}
			}else if("SUCCESS".equals(wr.getTradeState())){
				returnMap = setRespData("200", "该订单已支付", outTradeNo, blExtPay.getPkExtpay(), "1");
			}else{
				returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
			}
		}else{
			returnMap = setRespData("404", "交易订单不存在", outTradeNo, "", "");
		}
		return returnMap;
	}

	private Map<String,String> queryAli(BlExtPay blExtPay, String outTradeNo, Map<String,String> paramMap){
		Map<String,String> returnMap = new HashMap<String,String>();
		
		PayAliRecord ar = DataBaseHelper.queryForBean("select * from PAY_ALI_RECORD where OUT_TRADE_NO = ?", PayAliRecord.class, new Object[]{outTradeNo});
		if(ar!=null){
			returnMap.put("account", String.valueOf(blExtPay.getAmount()));
			if("INIT".equals(ar.getTradeState())){
				
				Map<String,String> resultMap = aliOrderQuery(outTradeNo);
				String tradeState = resultMap.get("state");
				if(StringUtils.isNotEmpty(tradeState)){
					
					String payData = resultMap.get("result");
					ar.setPayData(payData);
					ar.setTradeState(tradeState);
					
					if("SUCCESS".equals(tradeState)){
						@SuppressWarnings("unchecked")
						Map<String,String> map = JsonUtil.readValue(payData, Map.class);
						
						ar.setAliTradeNo(map.get("trade_no"));
						ar.setBuyerId(map.get("buyer_id"));
		            	ar.setPayTime(map.get("gmt_payment"));
  	            		ar.setPayTime(map.get("time_end"));
  	            		ar.setModityTime(new Date());
  	            		DataBaseHelper.updateBeanByPk(ar);// 更新交易数据
  	            		
  	            		blExtPay.setFlagPay("1");//支付成功
  	            		blExtPay.setTradeNo(map.get("trade_no"));//支付宝订单号
  	            		DataBaseHelper.updateBeanByPk(blExtPay);// 更新交易数据
  	            		
	  	            	// 查询终端 - 自助机使用二维码支付时，成功后需要生成押金记录
						String queryTerminal = paramMap.get("queryTerminal");
						if(StringUtils.isNotEmpty(queryTerminal)){
							String pkDepo = createDepoist(blExtPay);
							log.info("支付宝[主动查询订单]生成BlDeposit数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
							
							String extpaySql = " update BL_EXT_PAY set FLAG_PAY=?, DATE_PAY=?, PK_DEPO=? where PK_EXTPAY=? ";
	  						DataBaseHelper.update(extpaySql, "1", new Date(), pkDepo, ar.getPkExtpay());
	  						log.info("支付宝[主动查询订单]更新BlExtPay数据完成：{}, {}, {}", outTradeNo, tradeState, pkDepo);
						}
						returnMap = setRespData("200", "该订单已支付", outTradeNo, blExtPay.getPkExtpay(), "1");
					}else{
						blExtPay.setFlagPay("2");//支付失败
						DataBaseHelper.updateBeanByPk(blExtPay);// 更新交易数据
						
						returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
					}
				}else{
					returnMap = setRespData("200", "该订单未支付", outTradeNo, blExtPay.getPkExtpay(), "0");
				}
			}else if("SUCCESS".equals(ar.getTradeState())){
				returnMap = setRespData("200", "该订单已支付", outTradeNo, blExtPay.getPkExtpay(), "1");
			}else{
				returnMap = setRespData("404", "该订单支付失败", outTradeNo, blExtPay.getPkExtpay(), "");
			}
		}else{
			returnMap = setRespData("404", "交易订单不存在", outTradeNo, blExtPay.getPkExtpay(), "");
		}
		return returnMap;
	}
	
	private Map<String,String> setRespData(String code, String msg, String outTradeNo, String pkExtpay, String payFlag){
		Map<String,String> returnMap = new HashMap<String,String>();
		returnMap.put("code", code);
		returnMap.put("outTradeNo", outTradeNo);
		returnMap.put("pkExtpay", pkExtpay);
		returnMap.put("msg", msg);
		if(StringUtils.isNotEmpty(payFlag)){
			returnMap.put("payFlag", payFlag);
		}
		return returnMap;
	}
	
	/**
	 * 生成预交金记录、并支付记录关联预交金
	 * @param blExtPay
	 * @return
	 */
	private String createDepoist(BlExtPay blExtPay){
		// 1.生成预交金记录
		ZsbaBlDeposit depo = new ZsbaBlDeposit();
		depo.setPkPv(blExtPay.getPkPv());	//就诊主键
		depo.setPkPi(blExtPay.getPkPi());	//患者主键
		depo.setPayInfo(blExtPay.getSerialNo());
		depo.setDtBank(blExtPay.getDtBank()!=null?blExtPay.getDtBank():"0");
		if(blExtPay.getSysname().equals("zy")){
			depo.setEuPvtype("3");				//就诊类型-住院
			depo.setEuDptype("9");				//收付款类型-住院预交金
			depo.setNote("住院预交金");
		}else{
			depo.setEuPvtype("1");				//就诊类型-门诊
			depo.setEuDptype("0");				//收付款类型-门诊就诊结算
			depo.setNote("门诊就诊结算");
		}
		depo.setEuDirect("1");				//收退方向-收
		depo.setFlagSettle("0");			//未结算
		depo.setVoidType("0");				//正常
		depo.setAmount(blExtPay.getAmount());//金额
		if("7".equals(blExtPay.getEuPaytype())){
			depo.setDtPaymode("7");				//收付款方式-微信
		}else{
			depo.setDtPaymode("8");				//收付款方式-支付宝
		}
		depo.setPkOrg(blExtPay.getPkOrg());	//所属机构
		depo.setPkEmpPay(blExtPay.getCreator());//收款人
		BdOuEmployee emp = DataBaseHelper.queryForBean("select * from bd_ou_employee where pk_emp=?", BdOuEmployee.class, blExtPay.getCreator());
		depo.setNameEmpPay(emp.getNameEmp());//收款人姓名
		depo.setCreator(depo.getPkEmpPay());//收款人
		depo.setModifier(depo.getPkEmpPay());//收款人
		depo.setCodeDepo(ApplicationUtils.getCode("0606"));//交款编码
		depo.setDatePay(blExtPay.getCreateTime());//收付款日期
		depo.setTs(new Date());//时间戳
		depo.setFlagCc("0");
		DataBaseHelper.insertBean(depo);
		
		return depo.getPkDepo();
	}

	
	public BlExtPay savePay(String param, IUser user){
		BlExtPay blExtPay = JsonUtil.readValue(param, BlExtPay.class);
		if(StringUtils.isEmpty(blExtPay.getPkExtpay())){
			throw new BusException("支付主键不能为空！");
		}
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}
	
	public PayWechatRecord addPayWechatRecord(PayWechatRecord wr){
		DataBaseHelper.insertBean(wr);
		return wr;
	}
	public PayWechatRecord updatePayWechatRecord(PayWechatRecord wr){
		DataBaseHelper.updateBeanByPk(wr);
		return wr;
	}
	
	public BlExtPay addBlExtPay(BlExtPay bep){
		DataBaseHelper.insertBean(bep);
		return bep;
	}
	public BlExtPay updateBlExtPay(BlExtPay bep){
		DataBaseHelper.updateBeanByPk(bep);
		return bep;
	}
	
	public ZsbaBlDeposit addBlDeposit(ZsbaBlDeposit depo){
		DataBaseHelper.insertBean(depo);
		return depo;
	}
	
	public PayUnionpayRecord addPayUnionpayRecord(PayUnionpayRecord pur){
		DataBaseHelper.insertBean(pur);
		return pur;
	}
	public PayUnionpayRecord updatePayUnionpayRecord(PayUnionpayRecord pur){
		DataBaseHelper.updateBeanByPk(pur);
		return pur;
	}
	
	public User getByCodeEmp(String codeEmp){
		String sql = "select emp.PK_ORG, job.PK_DEPT, emp.PK_EMP, emp.NAME_EMP from BD_OU_EMPLOYEE emp INNER JOIN BD_OU_EMPJOB job on job.PK_EMP=emp.PK_EMP where emp.CODE_EMP=?";
		List<Map<String,Object>> data = DataBaseHelper.queryForList(sql, codeEmp);
		if(data.isEmpty()){
			throw new BusException("未找到收费员信息，不能进行结算");
		}
		User emp = new User();
		emp.setPkOrg(data.get(0).get("pkOrg").toString());
		emp.setPkDept(data.get(0).get("pkDept").toString());
		emp.setPkEmp(data.get(0).get("pkEmp").toString());
		emp.setNameEmp(data.get(0).get("nameEmp").toString());
		return emp;
	}
}