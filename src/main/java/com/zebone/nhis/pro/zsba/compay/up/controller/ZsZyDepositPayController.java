package com.zebone.nhis.pro.zsba.compay.up.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.compay.pub.vo.ZsbaBlDeposit;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayBlDepositService;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayService;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayUnionpayRecord;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 保存住院预交金
 * 保存银联交易数据、获取微信|支付宝支付二维码、通过商户订单号查询订单状态
 * @author	lipz
 * @date	2020-01-02
 */
@Controller
@RequestMapping("/static/depo")
public class ZsZyDepositPayController {
	
	@Autowired TpPayBlDepositService unionPayService;
	@Autowired TpPayService wxAliPayService;
	
	/*
	 * TODO:默认的机构和工号
	 */
	private String pkOrg = "89ace0e12aba439991c0eb001aaf02f7";
	
	/**
	 * 生成银联交易数据-预下单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/initSandUnionPay")
	public void initSandUnionPay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String amount = paramMap.get("amount");
			String transType = paramMap.get("transtype");		//交易类型：31消费、41撤销、71退货
    		String systemModule = paramMap.get("systemModule");
    		String codeEmp = paramMap.get("codeEmp");
    		String pkPi = paramMap.get("pkPi");
    		String pkPv = paramMap.get("pkPv");
    		
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times)
    				&& StringUtils.isNotEmpty(transType) && StringUtils.isNotEmpty(amount)
    				&& StringUtils.isNotEmpty(systemModule) && StringUtils.isNotEmpty(codeEmp)){
    			if(StringUtils.isEmpty(pkPi) || StringUtils.isEmpty(pkPv) 
      					|| "null".equals(pkPv) || "null".equals(pkPi)){
    				// 查询患者信息
        			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
        			if(map!=null){
        				pkPi = map.get("pkPi").toString();
        	    		pkPv = map.get("pkPv").toString();
        			}
    			}
      			
    			if(StringUtils.isNotEmpty(pkPi) && StringUtils.isNotEmpty(pkPv)){
        			BigDecimal totalFee = new BigDecimal(paramMap.get("amount")).divide(new BigDecimal(100));	// 从分计算为元
        			User emp = wxAliPayService.getByCodeEmp(codeEmp);
        			/*
        			 *  保存银联交易记录
        			 */
        			if(!"31".equals(transType)){
        				totalFee = totalFee.multiply(new BigDecimal(-1));// 转为负数
        			}
        			PayUnionpayRecord pur = new PayUnionpayRecord();
        			pur.setPkExtpay(null);
        			pur.setPkPi(pkPi);
        			pur.setPkPv(pkPv);
        			pur.setAmount(totalFee);
        			pur.setTranstype(transType);
        			pur.setOutTradeNo(NHISUUID.getKeyId());//生成系统商户订单号(时间戳+4位随机数)
        			pur.setSystemModule(systemModule);
        			pur.setTradeState("INIT");
        			pur.setBillStatus("0");//未对账
        			pur.setPkOrg(pkOrg);//当前操作人关联机构
        			pur.setCreator(emp.getPkEmp());//当前操作人关联编码
        			pur.setCreateTime(new Date());
        			pur.setModifier(pur.getCreator());
        			pur.setModityTime(pur.getCreateTime());
        			DataBaseHelper.insertBean(pur);
    				
        			Map<String,String> returnMap = new HashMap<String, String>();
        			returnMap.put("outTradeNo", pur.getOutTradeNo());
        			returnMap.put("pkUnionpayRecord", pur.getPkUnionpayRecord());
        			
        			jsonData = JsonResult.toJsonObject(new JsonResult(returnMap).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]、[amount]、[transtype]、[codeEmp]、[systemModule]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[initSandUnionPay]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 保存银联交易数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/saveSandUnionPay")
	public void saveSandUnionPay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String codeEmp = paramMap.get("codeEmp");
    		String payData = paramMap.get("payData");
    		String systemModule = paramMap.get("systemModule");
			String pkUnionpayRecord = paramMap.get("pkUnionpayRecord");//银联交易记录主键
			String pkPi = paramMap.get("pkPi");
    		String pkPv = paramMap.get("pkPv");
    		
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) 
    				&& StringUtils.isNotEmpty(codeEmp) && StringUtils.isNotEmpty(payData)
    				&& StringUtils.isNotEmpty(systemModule)){
      			if(StringUtils.isEmpty(pkPi) || StringUtils.isEmpty(pkPv) 
      					|| "null".equals(pkPv) || "null".equals(pkPi)){
    				// 查询患者信息
        			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
        			if(map!=null){
        				pkPi = map.get("pkPi").toString();
        	    		pkPv = map.get("pkPv").toString();
        			}
    			}
    			if(StringUtils.isNotEmpty(pkPi) && StringUtils.isNotEmpty(pkPv)){
    				User emp = wxAliPayService.getByCodeEmp(codeEmp);
        			// 1.初始化交易数据
        			PayUnionpayRecord pur = JsonUtil.readValue(payData, PayUnionpayRecord.class);
        			pur.setPkPi(pkPi);
        			pur.setPkPv(pkPv);
    				pur.setSystemModule(systemModule);
        			pur.setAmount(new BigDecimal(pur.getAmount().doubleValue()).divide(new BigDecimal(100)));//分转元
        			
        			if(StringUtils.isNotEmpty(pkUnionpayRecord)){
        				PayUnionpayRecord initPur = DataBaseHelper.queryForBean("select * from PAY_UNIONPAY_RECORD where PK_UNIONPAY_RECORD=?", 
    							PayUnionpayRecord.class, new Object[]{pkUnionpayRecord});
            			if(initPur!=null){
            				if(initPur.getAmount().compareTo(pur.getAmount())!=0){
            					throw new BusException("交易金额不一致，预交易金额【"+initPur.getAmount().toString()+"】实际交易金额【"+pur.getAmount().toString()+"】！");
            				}
            				pur.setPkUnionpayRecord(initPur.getPkUnionpayRecord());
            			}
        			}
        			
    				// 2.生成支付记录
        			BlExtPay blExtPay = new BlExtPay();
        			blExtPay.setPkPi(pur.getPkPi());
        			blExtPay.setPkPv(pur.getPkPv());
        			if("31".equals(pur.getTranstype())){
        				// 从分计算为元
        				blExtPay.setAmount(pur.getAmount());
        				blExtPay.setDescPay("银联缴费");
        				blExtPay.setTradeNo(pur.getSysrefno()+pur.getTransdate().substring(4,8));
        			}else if("41".equals(pur.getTranstype())){
        				// 从分计算为元，并转为负数
        				blExtPay.setAmount(pur.getAmount().multiply(new BigDecimal(-1)));
        				blExtPay.setDescPay("银联撤销");
        				blExtPay.setTradeNo(pur.getSysrefno());
        			}else if("51".equals(pur.getTranstype())){
        				// 从分计算为元，并转为负数
        				blExtPay.setAmount(pur.getAmount().multiply(new BigDecimal(-1)));
        				blExtPay.setDescPay("银联退货");
        				blExtPay.setTradeNo(pur.getSysrefno());
        			}
        			blExtPay.setEuPaytype("3");
        			blExtPay.setDtBank("0");
        			blExtPay.setFlagPay("1");
        			blExtPay.setDatePay(DateUtils.strToDate(pur.getTransdate() + pur.getTranstime(), "yyyyMMddHHmmss"));
        			blExtPay.setSerialNo(pur.getSysrefno());
        			blExtPay.setTradeNo(pur.getOutTradeNo());
        			blExtPay.setSysname(systemModule);
        			blExtPay.setEuBill("0");
        			blExtPay.setPkOrg(pkOrg);//当前操作人关联机构
        			blExtPay.setCreator(emp.getPkEmp());//当前操作人关联编码
        			blExtPay.setCreateTime(new Date());
        			blExtPay.setModifier(blExtPay.getCreator());
        			blExtPay.setModityTime(blExtPay.getCreateTime());
        			wxAliPayService.addBlExtPay(blExtPay);
        			
        			// 3.关联支付记录主键，并保存交易数据
        			pur.setPkExtpay(blExtPay.getPkExtpay());
        			if(!"31".equals(pur.getTranstype())){
        				//不是消费的，转负数
        				pur.setAmount(pur.getAmount().multiply(new BigDecimal(-1)));
        			}
        			pur.setTradeState("SUCCESS");
        			pur.setBillStatus("0");//未对账
        			pur.setPkOrg(blExtPay.getPkOrg());//当前操作人关联机构
        			pur.setCreator(blExtPay.getCreator());//当前操作人关联编码
        			pur.setCreateTime(blExtPay.getCreateTime());
        			pur.setModifier(pur.getCreator());
        			pur.setModityTime(pur.getCreateTime());
        			if(pur.getPkUnionpayRecord()==null){
        				wxAliPayService.addPayUnionpayRecord(pur);
        			}else{
        				wxAliPayService.updatePayUnionpayRecord(pur);
        			}
    				
    				// 4.生成预交金记录
        			ZsbaBlDeposit depo = new ZsbaBlDeposit();
        			depo.setPkPv(pur.getPkPv());	//就诊主键
        			depo.setPkPi(pur.getPkPi());	//患者主键
        			if(pur.getSystemModule().equals("mz")){
        				depo.setEuPvtype("1");			//就诊类型-门诊
            			depo.setEuDptype("0");			//收付款类型-就诊结算
            			depo.setNote("门诊就诊结算");
        			}else{
        				depo.setEuPvtype("3");			//就诊类型-住院
            			depo.setEuDptype("9");			//收付款类型-住院预交金
            			depo.setNote("住院预交金");
        			}
        			depo.setDtPaymode("3");			//收付款方式-银行卡
        			if("31".equals(pur.getTranstype())){
        				depo.setEuDirect("1");			//收退方向-收
        			}else{
        				depo.setEuDirect("-1");			//收退方向-退
        			}
        			depo.setFlagSettle("0");		//未结算
        			depo.setVoidType("0");			//正常
        			depo.setDtBank("0");//
        			depo.setPayInfo(pur.getSysrefno());
        			depo.setAmount(pur.getAmount());//金额
    				depo.setBankNo(pur.getBankno());//支票号码或银行卡号
        			depo.setPkOrg(pur.getPkOrg());	//所属机构
        			depo.setPkEmpPay(pur.getCreator());//收款人
        			depo.setNameEmpPay(emp.getNameEmp());//收款人姓名
        			depo.setDatePay(pur.getCreateTime());//收付款日期
        			depo.setTs(depo.getDatePay());//时间戳
        			depo.setFlagCc("0");
        			wxAliPayService.addBlDeposit(depo);
        			
    				// 5.更新支付记录关联预交金记录pkDepo
        			blExtPay.setPkDepo(depo.getPkDepo());
        			wxAliPayService.updateBlExtPay(blExtPay);
    				
        			jsonData = JsonResult.toJsonObject(new JsonResult(blExtPay.getPkExtpay()).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[patientId]、[times]、[codeEmp]、[payData]、[systemModule]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[saveSandUnionPay]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 获取支付二维码
	 * （微信、支付宝）
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getQrCode")
	public void getQrCode(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");
    		String times = paramMap.get("times");
    		String payType = paramMap.get("payType");
    		String subject = paramMap.get("subject");
    		String body = paramMap.get("body");
    		String tradeType = paramMap.get("tradeType");
    		String openId = paramMap.get("openId");
    		String amount = paramMap.get("amount");
    		String codeEmp = paramMap.get("codeEmp");
    		String systemModule = paramMap.get("systemModule");
    		String pkPi = paramMap.get("pkPi");
    		String pkPv = paramMap.get("pkPv");
    		
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) 
    				&& StringUtils.isNotEmpty(payType) && StringUtils.isNotEmpty(subject)
    				&& StringUtils.isNotEmpty(body) && StringUtils.isNotEmpty(amount)
    				&& StringUtils.isNotEmpty(codeEmp) && StringUtils.isNotEmpty(systemModule)){
      			if(!StringUtils.isNotEmpty(pkPi) || !StringUtils.isNotEmpty(pkPv)){
    				// 查询患者信息
        			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
        			if(map!=null){
        				pkPi = map.get("pkPi").toString();
        	    		pkPv = map.get("pkPv").toString();
        			}
    			}
    			if(StringUtils.isNotEmpty(pkPi) && StringUtils.isNotEmpty(pkPv)){
        			Map<String, Object> queryMap = new HashMap<String, Object>();
        			queryMap.put("payType", payType);//1微信、2支付宝
        			queryMap.put("subject", subject);//商品描述
        			queryMap.put("body", body);//商品详情
        			queryMap.put("tradeType", tradeType);
        			queryMap.put("openId", openId);
        			queryMap.put("amount", amount);//总金额,元
        			queryMap.put("codeEmp",codeEmp);//操作员工号
        			queryMap.put("systemModule",systemModule);//系统模块
        			queryMap.put("pkPi", pkPi);//患者主键
        			queryMap.put("pkPv", pkPv);//就诊主键
        			
        			String param = JsonUtil.writeValueAsString(queryMap);
        			Map<String,String> result = wxAliPayService.getPayQrcodeByPayType(param, UserContext.getUser());
        			
        			String data = JsonUtil.writeValueAsString(result);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR,
    					"[patientId]、[times]、[payType]、[subject]、[body]、[amount]、[tradeType]、[openId]、[codeEmp]、[systemModule]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getQrCode]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 条码支付
	 * （微信、支付宝）
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/tradePay")
	public void tradePay(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String patientId = paramMap.get("patientId");					
    		String times = paramMap.get("times");						
    		String amount = paramMap.get("amount");							//总金额,元
    		String payType = paramMap.get("payType");						//支付类型、1微信、2支付宝
    		String authCode = paramMap.get("authCode");						//支付授权码
    		String subject = paramMap.get("subject");						//商品描述
    		String body = paramMap.get("body");								//商品详情
    		String systemModule = paramMap.get("systemModule");				//系统模块、zy住院、mz门诊
    		String codeEmp = paramMap.get("codeEmp");						//操作员工号
    		String pkPi = paramMap.get("pkPi");
    		String pkPv = paramMap.get("pkPv");
    		
    		if(StringUtils.isNotEmpty(patientId) && StringUtils.isNotEmpty(times) 
    				&& StringUtils.isNotEmpty(payType) && StringUtils.isNotEmpty(subject)
    				&& StringUtils.isNotEmpty(body) && StringUtils.isNotEmpty(amount)
    				&& StringUtils.isNotEmpty(codeEmp) && StringUtils.isNotEmpty(systemModule)){
    			
    			if(!StringUtils.isNotEmpty(pkPi) || !StringUtils.isNotEmpty(pkPv)){
    				// 查询患者信息
        			Map<String, Object> map = getPkPiAndPkPv(patientId, times);
        			if(map!=null){
        				pkPi = map.get("pkPi").toString();
        	    		pkPv = map.get("pkPv").toString();
        			}
    			}
    			if(StringUtils.isNotEmpty(pkPi) && StringUtils.isNotEmpty(pkPv)){
        			Map<String, Object> queryMap = new HashMap<String, Object>();
        			queryMap.put("pkPi", pkPi);//患者主键
        			queryMap.put("pkPv", pkPv);//就诊主键
        			queryMap.put("payType", payType);//
        			queryMap.put("subject", subject);
        			queryMap.put("body", body);
        			queryMap.put("authCode", authCode);
        			queryMap.put("amount", amount);
        			queryMap.put("pkOrg", pkOrg);
        			queryMap.put("codeEmp",codeEmp);
        			queryMap.put("systemModule",systemModule);
        			
        			String param = JsonUtil.writeValueAsString(queryMap);
        			Map<String,String> result = wxAliPayService.tradePay(param, UserContext.getUser());
        			
        			String data = JsonUtil.writeValueAsString(result);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "未找到患者住院就诊数据：pkPv"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR,
    					"[patientId]、[times]、[payType]、[subject]、[body]、[amount]、[tradeType]、[openId]、[codeEmp]、[systemModule]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getQrCode]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/**
	 * 通过商户订单号查询订单状态
	 * （微信、支付宝）
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getTradeState")
	public void getTradeState(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String outTradeNo = paramMap.get("outTradeNo");//商户订单号
    		String isAlipay = "";//是否支付宝小程序
    		if(paramMap.containsKey("isAlipay")) {
    			isAlipay = paramMap.get("isAlipay");
    		}
    		if(StringUtils.isNotEmpty(outTradeNo)){
    			Map<String, Object> queryMap = new HashMap<String, Object>();
    			queryMap.put("serialNo", outTradeNo);
    			queryMap.put("queryTerminal", "YES");//标志，不为空标识需要生成押金记录
    			queryMap.put("isAlipay", isAlipay);//是否支付宝小程序查询订单
    			
    			String param = JsonUtil.writeValueAsString(queryMap);
    			Map<String,String> result = wxAliPayService.inquiryOrder(param, UserContext.getUser());
    			
    			String data = JsonUtil.writeValueAsString(result);
    			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[outTradeNo]参数不能为空"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getTradeState]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	
	/**
	 * 退费
	 * （微信、支付宝）
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/payRefund")
	public void payRefund(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String refundType = paramMap.get("refundType");//退款类型，1微信、2支付宝、3支付宝小程序
    		String outTradeNo = paramMap.get("outTradeNo");//交易号
    		String refundAmount = paramMap.get("refundAmount");//总金额
    		String refundReason = paramMap.get("refundReason");//退款原因
    		String codeEmp = paramMap.get("codeEmp");//操作员工号
    		
    		if(StringUtils.isNotEmpty(refundType) && StringUtils.isNotEmpty(outTradeNo) 
    				&& StringUtils.isNotEmpty(refundAmount) && StringUtils.isNotEmpty(refundReason)){
    			
        			Map<String, Object> queryMap = new HashMap<String, Object>();
        			queryMap.put("refundType", refundType);//
        			queryMap.put("tradeNo", outTradeNo);
        			queryMap.put("refundAmount", refundAmount);
        			queryMap.put("refundReason", refundReason);
        			queryMap.put("pkOrg", pkOrg);
        			queryMap.put("codeEmp",codeEmp);
        			
        			String param = JsonUtil.writeValueAsString(queryMap);
        			Map<String,String> result = wxAliPayService.payRefund(param, UserContext.getUser());
        			
        			String data = JsonUtil.writeValueAsString(result);
        			jsonData = JsonResult.toJsonObject(new JsonResult(data).success());
    			
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR,
    					"[patientId]、[times]、[payType]、[subject]、[body]、[amount]、[tradeType]、[openId]、[codeEmp]、[systemModule]参数不能为空"));
    		}
		} catch (Exception e) {
			e.printStackTrace();
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[payRefund]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	
	/**
	 * 查询患者住院就诊主键
	 * @param patientId		患者主索引
	 * @param times			住院就诊次数
	 * @return				[pkPi:"患者信息主键",pkPv:"患者就诊主键"]
	 */
	private Map<String, Object> getPkPiAndPkPv(String patientId, String times){
		String mapSql = "select i.PK_PV, n.PK_PI from pv_ip_notice n, PV_ENCOUNTER i  where n.PK_PV_IP=i.PK_PV and n.PK_PV_OP like ? and i.FLAG_CANCEL='0' and i.eu_status<2 ORDER BY n.ts";
		Map<String, Object> data = DataBaseHelper.queryForMap(mapSql, new Object[]{patientId+"-%"});
		if(data==null){
			data = getPiPv(patientId);
		}
		return data;
	}
	
	private Map<String, Object> getPiPv(String patientId){
		StringJoiner sql = new StringJoiner("");
		sql.add(" SELECT pi.PK_PI, pv.PK_PV ");
		sql.add(" FROM PI_MASTER pi INNER JOIN PV_ENCOUNTER pv ON pv.PK_PI=pi.PK_PI");
		sql.add(" WHERE pv.EU_PVTYPE='3' and pv.eu_status<2 and pi.CODE_OP=? ");
		return DataBaseHelper.queryForMap(sql.toString(), new Object[]{patientId});
	}
	
}
