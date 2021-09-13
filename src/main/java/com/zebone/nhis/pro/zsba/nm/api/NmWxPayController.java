package com.zebone.nhis.pro.zsba.nm.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.HttpUtil;
import com.zebone.nhis.pro.zsba.common.support.JsonResult;
import com.zebone.nhis.pro.zsba.common.support.PayCommonUtil;
import com.zebone.nhis.pro.zsba.common.support.StringTools;
import com.zebone.nhis.pro.zsba.common.utils.PayConfig;
import com.zebone.nhis.pro.zsba.compay.up.service.TpPayService;
import com.zebone.nhis.pro.zsba.compay.up.vo.PayWechatRecord;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStDetailsMapper;
import com.zebone.nhis.pro.zsba.nm.dao.NmCiStMapper;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiSt;
import com.zebone.nhis.pro.zsba.nm.vo.NmCiStDetails;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import net.sf.json.JSONObject;

/**
 * 非医疗费用-微信公众号支付
 * @author lipz
 *
 */
@Controller
@RequestMapping("/static/nmpay")
public class NmWxPayController {
	
	private static Logger logger = LoggerFactory.getLogger(NmWxPayController.class);
	
	@Autowired private NmCiStMapper stMapper;
	@Autowired private NmCiStDetailsMapper detailsMapper;
	@Autowired private TpPayService payService; 
	
	/**
	 * 通过结算主键查询计费明细状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getSettData")
	public void getSettData(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String pkCiSt = paramMap.get("settId");//结算主键
    		if(StringUtils.isNotEmpty(pkCiSt)){
    			NmCiSt sett = stMapper.getById(pkCiSt);
    			if(sett!=null){
    				JSONObject settJo = new JSONObject();
					settJo.put("settId", sett.getPkCiSt());
					settJo.put("codeIp", sett.getCodePv());
					settJo.put("patientName", sett.getNamePi());
					settJo.put("amount", sett.getAmount());
					settJo.put("isPay", sett.getIsPay());
    				
    				String annalSql = " select d.date_annal, i.name_item, d.ci_price, d.num_ord, d.total from nm_ci_st_details d, nm_charge_item i where d.pk_ci=i.pk_ci and d.pk_ci_st=? ";
        			List<Map<String,Object>> annalList = DataBaseHelper.queryForList(annalSql, new Object[]{pkCiSt});
        			
        			JSONObject result = new JSONObject();
					result.put("sett", settJo);
					result.put("items", annalList);
					
        			jsonData = JsonResult.toJsonObject(new JsonResult(result).success());
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "[getSettData]结算记录不存在。"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[settId]参数不能为空。"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getSettData]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}

	/**
	 * 获取预下单数据，下单成功时返回prepayId、codeUrl
	 * @param requestBody
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getWxQrcode")
	public void getWxQrcode(@RequestBody String requestBody, 
			HttpServletRequest request, HttpServletResponse response){
		String jsonData = "";
    	try {
    		Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
    		String openId = paramMap.get("openId");//结算主键
    		String pkCiSt = paramMap.get("settId");//结算主键
    		if(StringUtils.isNotEmpty(pkCiSt) && StringUtils.isNotEmpty(openId)){
    			NmCiSt sett = stMapper.getById(pkCiSt);
    			if(sett!=null){
    				if("0".equals(sett.getIsPay())){
    					Map<String, String> result = initPay(sett, openId);
    					if(result.get("code").equals("200")){
    						jsonData = JsonResult.toJsonObject(new JsonResult(result).success());
    					}else{
    						jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, result.get("msg")));
    					}
    				}else{
    					jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "该结算记录已付款或有退款。"));
    				}
    			}else{
    				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "[getWxQrcode]结算记录不存在。"));
    			}
    		}else{
    			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.PARAMS_ERROR, "[settId]参数不能为空。"));
    		}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "[getWxQrcode]系统异常："+e.getMessage()));
		} finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/*
	 * 微信支付统一下单接口
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> initPay(NmCiSt sett, String openId){
		Map<String, String> returnMap = new HashMap<String, String>();
		
		String outTradeNo = NHISUUID.getKeyId();						//生成系统商户订单号
		String body = "新住院生活用品计费";
		String detail = "NHIS就诊主键：" + sett.getPkPv() + "，操作人工号：" + sett.getNamePi() + "，交易金额："+ String.valueOf(sett.getAmount()) +"元";
		
		//根据生成的订单信息调用《微信统一下单接口》
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));//公众账号ID
		parameters.put("mch_id", ApplicationUtils.getPropertyValue("WECHAT_MCH_ID", ""));//商户号
		parameters.put("device_info", sett.getSettCode());//终端设备号|操作员工号
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());//随机字符串
		//附加数据(在查询API和支付通知中原样返回，可作为自定义参数使用)
		parameters.put("attach", ApplicationUtils.getPropertyValue("PAY_NOTITY_URL", "") + PayConfig.wx_push_notify_url);//中转服务器在微信回调后获取该地址回传后台接口地址
		parameters.put("body", body);//商品描述
		parameters.put("detail", detail);//商品详情
		parameters.put("out_trade_no", outTradeNo);//商户订单号
		String money = String.valueOf(Math.round(sett.getAmount().multiply(new BigDecimal(100)).doubleValue()));
		parameters.put("total_fee", money);//总金额-单位：分
		parameters.put("spbill_create_ip", ApplicationUtils.getCurrIp());//IP
		parameters.put("trade_type", "JSAPI");//类型-原生扫码支付(trade_type非JSAPI方式openid可不传)
		parameters.put("openid", openId);
		//订单生成时间-订单失效时间(5分钟后失效)
		parameters.put("time_start", DateUtils.getDate("yyyyMMddHHmmss"));//交易起始时间
		parameters.put("time_expire", DateUtils.addDate(new Date(), PayConfig.timeout_express, 5, "yyyyMMddHHmmss"));//交易结束时间
		parameters.put("pay_sign_key", ApplicationUtils.getPropertyValue("WECHAT_PAY_SIGN_KEY", ""));//商户支付密钥
		
		//发送http请求通过中转服务器请求微信接口后返回数据处理
		String queryUrl = ApplicationUtils.getPropertyValue("PAY_SERVER_URL", "") + PayConfig.wechat_qrcode_url;
		String result = HttpUtil.httpPost(queryUrl, JsonUtil.writeValueAsString(parameters));
		logger.info("微信预下单返回结果:" + result);
		
		if(StringUtils.isNotEmpty(result)){
			Map<String,String> map = JsonUtil.readValue(result, Map.class);
			// 返回值return_code和result_code都为SUCCESS时下单成功
			if (map.get("return_code").equals("SUCCESS")) {
				if(map.get("result_code").equals("SUCCESS")){
					// 生成 预支付交易订单记录
					PayWechatRecord wr = new PayWechatRecord();
					wr.setPkExtpay(null);//支付主键
					wr.setPkPi(sett.getPkPv());//患者主键
					wr.setPkPv(sett.getPkPv());//就诊主键
					wr.setSystemModule(PayConfig.system_module_nm);
					wr.setProductId(sett.getPkCiSt());
					wr.setAppid(map.get("appid"));
					wr.setMchId(map.get("mch_id"));
					wr.setDeviceInfo(map.get("device_info"));
					wr.setOrderType("pay");//消费
					wr.setBody(body);
					wr.setDetail(detail);
					wr.setOutTradeNo(outTradeNo);
					wr.setTotalAmount(sett.getAmount());
					wr.setTradeState("INIT");//下单
					wr.setInitData(result);
					wr.setBillStatus("0");
					
					wr.setPkOrg(sett.getPkOrg());//当前操作人关联机构
					wr.setCreator(sett.getCreator());//当前操作人关联编码
					wr.setCreateTime(new Date());
					wr.setModifier(wr.getCreator());
					wr.setModityTime(wr.getCreateTime());
					payService.addPayWechatRecord(wr);
					
					//生成预支付交易订单成功,返回二维码地址
					returnMap.put("appId", ApplicationUtils.getPropertyValue("WECHAT_APPID", ""));
					returnMap.put("codeUrl", map.get("code_url"));//二维码地址
					returnMap.put("prepayId", map.get("prepay_id"));//预支付交易会话标识
					returnMap.put("outTradeNo", outTradeNo);
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
		return returnMap;
	}
	
	/**
	 * 微信支付回调
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("payNotify")
	public void payNotify(@RequestBody String requestBody,
			HttpServletRequest request, HttpServletResponse response) {
		String jsonData = "";
		try {
			logger.info("生活缴费：[{}]", requestBody);
			
			Map<String,String> paramMap = StringTools.jsonToMap(requestBody);
			String returnCode = paramMap.get("return_code");
			if ("SUCCESS".equalsIgnoreCase(returnCode)) {
				//通过商户订单号获取微信支付订单
				String outTradeNo = paramMap.get("out_trade_no");
				String wrSql = " select * from PAY_WECHAT_RECORD where OUT_TRADE_NO=? ";
				PayWechatRecord wr = DataBaseHelper.queryForBean(wrSql, PayWechatRecord.class, new Object[]{outTradeNo});
				if(wr!=null){
					//如果订单已经被处理则不继续执行后续流程(如有同一订单多次回调只执行一次)
					String resultCode = paramMap.get("result_code");
					if(StringUtils.isEmpty(wr.getTradeState()) || "INIT".equals(wr.getTradeState()) || !wr.getTradeState().equals(resultCode)){
						updateWxPaySett(wr, requestBody, paramMap);
					}
					jsonData = JsonResult.toJsonObject(new JsonResult().success("支付回调业务处理成功"));
				}else{
					jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_EXIST, "商户订单号["+outTradeNo+"]微信回调成功但商户订单记录找不到!"));
				}
			}else{
				jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.NOT_ALLOWED, "通知数据，return_code："+ returnCode));
			}
		} catch (Exception e) {
			jsonData = JsonResult.toJsonObject(new JsonResult().fail(JsonResult.SYSTEM_ERROR, "异常信息："+ e.getMessage()));
		}finally{
			StringTools.writeResp(response, jsonData);
		}
	}
	
	/*
	 * 根据回调数据处理业务数据
	 */
	private void updateWxPaySett(PayWechatRecord wr, String requestBody, Map<String,String> paramMap){
		wr.setOpenid(paramMap.get("openid"));
		wr.setTradeState(paramMap.get("result_code"));
		wr.setPayData(requestBody);
		wr.setPayTime(paramMap.get("time_end"));
		wr.setModityTime(new Date());
		payService.updatePayWechatRecord(wr);
		
		//交易成功, 更新结算表-计费表的支付标记
		if ("SUCCESS".equalsIgnoreCase(paramMap.get("result_code"))) {
			NmCiSt st = stMapper.getById(wr.getProductId());
			if(st!=null){
				Date currTime = new Date();
				
				st.setIsPay("1");
				st.setPayMethod(PayConfig.PAY_METHOD_WECHAT);
				st.setPkPay(wr.getPkPayWechatRecord());
				st.setChargeTime(currTime);
				st.setModityTime(currTime);
				stMapper.updateCiSt(st);
				
				String annalSql = " select * from nm_ci_st_details where pk_ci_st=? ";
    			List<NmCiStDetails> annalList = DataBaseHelper.queryForList(annalSql, NmCiStDetails.class, new Object[]{wr.getProductId()});
    			for (NmCiStDetails std : annalList) {
					std.setIsPay("1");
					std.setChargeTime(currTime);
					std.setModityTime(currTime);
					detailsMapper.updateCiStd(std);
				}
			}
		}
		
	}
	
	
	
}
